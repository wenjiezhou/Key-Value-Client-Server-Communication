/**
 /**
 * Slave Server component of a KeyValue store
 * 
 * @author Prashanth Mohan (http://www.cs.berkeley.edu/~prmohan)
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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class defines the salve key value servers. Each individual KeyServer
 * would be a fully functioning Key-Value server. For Project 3, you would
 * implement this class. For Project 4, you will have a Master Key-Value server
 * and multiple of these slave Key-Value servers, each of them catering to a
 * different part of the key namespace.
 * 
 * @param <K> Java Generic Type for the Key
 * @param <V> Java Generic Type for the Value
 */
public class KeyServer<K extends Serializable, V extends Serializable> implements KeyValueInterface<K, V> {
	
	private KVStore<K, V> dataStore = null;
	private KVCache<K, V> dataCache = null;
	Lock loc;
	Condition gCon; // condition variable monitor the get task
	Condition dpCon; // condition variable monitor both delete and put tack
	int AG; // active get requests
	int WG; // wait requests
	int WDP;// wait delete and put requests
	int ADP; // active delete and put requests
	String Message;
	String Status = "False";
	
	// int NDelandPutRequest;
	
	/**
	 * @param cacheSize number of entries in the data Cache.
	 */
	public KeyServer(int cacheSize) {
		// implement me
		// int tableSize = cacheSize*4/3;
		dataStore = new KVStore<K, V>();
		dataCache = new KVCache<K, V>(cacheSize);
		loc = new ReentrantLock();
		gCon = loc.newCondition();
		dpCon = loc.newCondition();
		
	}
	
	@Override
	public boolean put(K key, V value) throws KVException {
		// implement me
		loc.lock();
		while ((AG + ADP) > 0) {
			WDP++;
			try {
				dpCon.await();
			} catch (InterruptedException e) {
				Message = "Unknown Error: throw an Interruptexception when put in keyserver";
			}
			WDP--;
		}
		ADP++;
		loc.unlock();
		
		// handler put section:
		try {
			// loc.unlock();
			if (dataStore.get(key) != null) {
				Status = "True";
				Message = "Success";
				dataStore.put(key, value);
				debug("key " + key.toString()+" put into kvstore");
				return dataCache.put(key, value);
			} else {
				Status = "False";
				Message = "Success";
				dataStore.put(key, value);
				debug("key " + key.toString()+" put into kvstore");
				return dataCache.put(key, value);
			}
		} finally {
			loc.lock();
			ADP--;
			if (WDP > 0) {
				dpCon.notify();
			} else if (WG > 0) {
				gCon.notifyAll();
			}
			loc.unlock();
		}
		
	}
	
	public V get(K key) throws KVException {
		// implement me
		loc.lock();
		while (WDP > 0 || ADP > 0) {
			WG++;
			try {
				gCon.await();
			} catch (InterruptedException e) {
				Message = "Unknown Error: thrown an InterruptedException when get in keyserver";
			}
			WG--;
		}
		AG++;
		loc.unlock();
		
		// handle the get section:
		try {
			V rCacheVal = dataCache.get(key);
			if (rCacheVal == null) {
				V rStoreVal = dataStore.get(key);
				if (rStoreVal == null) {
					Message = "Does not exist";
					return null;
				} else {
					dataCache.put(key, rStoreVal);
					Message = "Success";
					debug("Catch Miss! Get Key " + key.toString() + " from KV Store");
					return rStoreVal;
				}
			} else {
				Message = "Success";
				debug("Catch hit! Get Key " + key.toString());
				return rCacheVal;
			}
		}// end of get handle
		
		finally {
			loc.lock();
			AG--;
			if (AG == 0 && WDP > 0) dpCon.notify();
			loc.unlock();
		}
		
	}
	
	@Override
	public void del(K key) throws KVException {
		// implement me
		loc.lock();
		while ((AG + ADP) > 0) {
			WDP++;
			try {
				dpCon.await();
			} catch (InterruptedException e) {
				Message = "Unknown Error: throw a Interruptexception when del in keyserver";
			}
			WDP--;
		}
		ADP++;
		loc.unlock();
		
		// handle the delete section:
		if (dataCache.get(key) == null && dataStore.get(key) == null) Message = "Does not exist";
		else {
			debug("key " + key.toString()+" DEL from both cache and kvstore");
			dataCache.del(key);
			dataStore.del(key);
			Message = "Success";
		}
		// end of delete handle
		
		loc.lock();
		ADP--;
		if (WDP > 0) {
			dpCon.notify();
		} else if (WG > 0) {
			gCon.notifyAll();
		}
		loc.unlock();
		
	}
	private void debug(String msg) {
		if (todebug) {
			System.out.println(msg);
		}
	}
	
	private static boolean todebug = true;
}
