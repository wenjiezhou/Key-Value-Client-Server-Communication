/**
 * Client component for generating load for the KeyValue store. 
 * This is also used by the Master server to reach the slave nodes.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class is used to communicate with (appropriately marshalling and unmarshalling)
 * objects implementing the {@link KeyValueInterface}.
 * 
 * @param <K> Java Generic type for the Key
 * @param <V> Java Generic type for the Value
 */
public class KVClient<K extends Serializable, V extends Serializable> implements KeyValueInterface<K, V> {
	
	private String server = null;
	private int port = 0;
	
	/**
	 * @param server is the DNS reference to the Key-Value server
	 * @param port is the port on which the Key-Value server is listening
	 */
	public KVClient(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean put(K key, V value) throws KVException {
		KVMessage sendmsg = new KVMessage(putreq, KVMessage.toString(key), KVMessage.toString(value));
		// System.out.println("XML to be sent is:\n" + sendmsg.toXML());
		// System.out.println("Key is\n" + (K) KVMessage.fromString(sendmsg.getKey()));
		// System.out.println("Value is\n" + (V) KVMessage.fromString(sendmsg.getValue()));
		
		if (key == null) {
			String err = "Empty key";
			KVMessage msg = new KVMessage("resp", err);
			throw new KVException(msg);
		}
		if (value == null) {
			String err = "Empty value";
			KVMessage msg = new KVMessage("resp", err);
			throw new KVException(msg);
		}
		KVMessage receivemsg = sendKV(sendmsg);
		if (receivemsg == null || receivemsg.getMsg() == null) {
			String err = "Unknown Error: Cannot put Key and Value";
			KVMessage msg = new KVMessage("resp", err);
			throw new KVException(msg);
		} else if (!receivemsg.getMsg().equals(success)) { throw new KVException(receivemsg); }
		return strToBoolean(receivemsg.getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws KVException {
		KVMessage sendmsg = new KVMessage(getreq, KVMessage.toString(key), null);
		KVMessage receivemsg = sendKV(sendmsg);
		if (receivemsg == null || receivemsg.getMsg() == null) {
			String err = "Unknown Error: Cannot get Key and Value";
			KVMessage msg = new KVMessage("resp", err);
			throw new KVException(msg);
		} else if (!receivemsg.getMsg().equals(success)) { throw new KVException(receivemsg); }
		return (V) KVMessage.fromString(receivemsg.getValue());
	}
	
	@Override
	public void del(K key) throws KVException {
		KVMessage sendmsg = new KVMessage(delreq, KVMessage.toString(key), null);
		KVMessage receivemsg = sendKV(sendmsg);
		if (receivemsg == null || receivemsg.getMsg() == null) {
			String err = "Unknown Error: Cannot delete Key";
			KVMessage msg = new KVMessage("resp", err);
			throw new KVException(msg);
		} else if (!receivemsg.getMsg().equals(success)) { throw new KVException(receivemsg); }
	}
	
	/**
	 * Send a KV message
	 * 
	 * @param kvmsg
	 * @return KV message received
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws KVException
	 */
	private KVMessage sendKV(KVMessage kvmsg) {
		KVMessage kvmError = new KVMessage(resp, null, null);
		try {
			KVMessage.checksize(kvmsg);
			Socket sendSocket = null;
			OutputStream outputStream = null;
			InputStream inputStream = null;
			try {
				sendSocket = new Socket(server, port);
			} catch (UnknownHostException e) {
				throwKVException(kvmError, "Network Error: Could not create socket");
			} catch (IOException e) {
				throwKVException(kvmError, "Network Error: Could not connect");
			}
			
			try {
				sendSocket.setSoTimeout(timeout);
			} catch (SocketException e2) {
				close(sendSocket, kvmError, "Network Error: Could not send data");
			}
			
			try {
				outputStream = sendSocket.getOutputStream();
			} catch (IOException e) {
				close(sendSocket, kvmError, "Network Error: Could not send data");
			}
			PrintWriter out = new PrintWriter(outputStream);
			
			String xml = kvmsg.toXML();
			out.write(xml);
			out.flush();
			// out.close();
			// outputStream.close();
			try {
				sendSocket.shutdownOutput();
			} catch (IOException e1) {
				close(sendSocket, kvmError, "Unknown Error: Could not shutdown socket");
			}
			
			// OutputStream os = sendSocket.getOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(os);
			// oos.writeObject(kvmsg.toXML());
			// oos.close();
			// os.close();
			
			try {
				inputStream = sendSocket.getInputStream();
				// System.out.println(XMLParser.convertStreamToString(inputStream));
				// System.exit(2);
			} catch (IOException e) {
				close(sendSocket, kvmError, "Network Error: Could not receive data");
			}
			
			// int a = 1;
			// while (a == 1) {
			// System.out.println(a);
			// }
			
			KVMessage response = new KVMessage(inputStream);
			try {
				sendSocket.close();
			} catch (IOException e) {
				throwKVException(kvmError, "Unknown Error: Could not close socket");
			}
			return response;
		} catch (KVException e) {
			return e.getMsg();
		}
	}
	
	private boolean strToBoolean(String s) throws KVException {
		if (s.trim().equals("True")) return true;
		else if (s.trim().equals("False")) return false;
		else throw new KVException(null);
	}
	
	private void throwKVException(KVMessage kvm, String error) throws KVException {
		kvm.setMsg(error);
		throw new KVException(kvm);
	}
	
	private void close(Socket socket, KVMessage kvm, String error) throws KVException {
		try {
			socket.close();
			throwKVException(kvm, error);
		} catch (IOException e) {
			kvm.setMsg("IO Error");
			throw new KVException(kvm);
		}
	}
	
	/** request types. */
	private static String getreq = "getreq";
	private static String putreq = "putreq";
	private static String delreq = "delreq";
	private static String resp = "resp";
	private static String success = "Success";
	
	/** time out time. */
	private static int timeout = 12 * 1000;
}
