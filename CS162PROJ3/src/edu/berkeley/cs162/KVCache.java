/**
 * Implementation of an LRU Cache (copied from the Internet)
 * 
 * Copyright (c) 2011, University of California at Berkeley
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of University of California, Berkeley nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *    
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL PRASHANTH MOHAN BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.berkeley.cs162;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.*;


/**
 * An LRU cache which has a fixed maximum number of elements (cacheSize).
 * If the cache is full and another entry is added, the LRU (least recently used) entry is dropped.
 */
public class KVCache<K extends Serializable, V extends Serializable> implements KeyValueInterface<K, V>{
	private static final float hashTableLoadFactor = 0.75f;
	private int cacheSize;
	LinkedHashMap LRUCache;
	Lock loc; //added

	/**
	 * Creates a new LRU cache.
	 * @param cacheSize the maximum number of entries that will be kept in this cache.
	 */
	public KVCache (int cacheSize) {
		// implement me
		this.cacheSize = cacheSize; 
		loc = new ReentrantLock(); // added
		int hashTableCapacity = (int)Math.ceil(cacheSize / hashTableLoadFactor) + 1;
		LRUCache = new LinkedHashMap<K,V>(hashTableCapacity, hashTableLoadFactor, true){
		      // (an anonymous inner class)
		      private static final long serialVersionUID = 1;
		      @Override 
		      protected boolean removeEldestEntry (Map.Entry<K,V> eldest) {
		         return size() > KVCache.this.cacheSize;}};
	}
	
	


	/**
	 * Retrieves an entry from the cache.
	 * The retrieved entry becomes the MRU (most recently used) entry.
	 * @param key the key whose associated value is to be returned.
	 * @return the value associated to this key, or null if no value with this key exists in the cache.
	 */
	public V get (K key) {
		// implement me
		loc.lock();
		V value = (V)LRUCache.get(key);
		if(value != null)
			{	
				//debug("Catch hit! Get Key " + key.toString());
				LRUCache.remove(key);
				LRUCache.put(key,value);
			}
		loc.unlock();
		return value;
	}

	/**
	 * Adds an entry to this cache.
	 * The new entry becomes the MRU (most recently used) entry.
	 * If an entry with the specified key already exists in the cache, it is replaced by the new entry.
	 * If the cache is full, the LRU (least recently used) entry is removed from the cache.
	 * @param key    the key with which the specified value is to be associated.
	 * @param value  a value to be associated with the specified key.
	 * @return 
	 */
	public boolean put (K key, V value) {
		// implement me
		loc.lock();
		V temp = (V)LRUCache.get(key);
		
		if(temp != null)
		{
			debug("put with key "+ key.toString() + " already in Cache, and repalced" );
			LRUCache.remove(key);
			LRUCache.put(key, value);
		}
		else
		{
			debug("put with key " + key.toString() + " not in Cache yet, write into catche");
			LRUCache.put(key, value);
		}
		loc.unlock();
		return true;
	}

	/**
	 * Removes an entry to this cache.
	 * @param key the key with which the specified value is to be associated.
	 */
	public void del (K key) {
		//loc.lock();
		debug("Del "+key.toString()+" from catche");
		LRUCache.remove(key);
		//loc.unlock();
	}
	private void debug(String msg) {
		if (todebug) {
			System.out.println(msg);
		}
	}
	
	private static boolean todebug = true;
} // end class LRUCache