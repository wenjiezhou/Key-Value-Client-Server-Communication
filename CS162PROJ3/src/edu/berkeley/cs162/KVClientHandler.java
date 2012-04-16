/**
 * Handle client connections over a socket interface
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

/**
 * This NetworkHandler will asynchronously handle the socket connections.
 * It uses a threadpool to ensure that none of it's methods are blocking.
 * 
 * @param <K> Java Generic type for the Key
 * @param <V> Java Generic type for the Value
 */
public class KVClientHandler<K extends Serializable, V extends Serializable> implements NetworkHandler {
	
	private KeyServer<K, V> keyserver = null;
	private ThreadPool threadpool = null;
	
	public KVClientHandler(KeyServer<K, V> keyserver) {
		initialize(keyserver, 1);
	}
	
	public KVClientHandler(KeyServer<K, V> keyserver, int connections) {
		initialize(keyserver, connections);
	}
	
	private void initialize(KeyServer<K, V> keyserver, int connections) {
		this.keyserver = keyserver;
		threadpool = new ThreadPool(connections);
	}
	
	public void sendResponseToClient(KVMessage kvmsg, Socket client) {
		String Message;
		KVMessage kvM;
		OutputStream os;
		try {
			os = client.getOutputStream();
			PrintWriter out = new PrintWriter(os);
			out.write(kvmsg.toXML());
			os.flush();
			
			// os.close();
		} catch (IOException e) {
			System.err.println("Network Error: Could not send data");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.berkeley.cs162.NetworkHandler#handle(java.net.Socket)
	 */
	@Override
	public void handle(Socket client) throws IOException {
		// implement me
		InputStream clientIfo = client.getInputStream();
		// InetAddress address = client.getInetAddress();
		// int port = client.getPort();
		// Socket sentBack = new Socket(address, port);
		// System.err.println("Address and port are: " + address.toString() + " " + port);
		
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// // Fake code simulating the copy
		// // You can generally do better with nio if you need...
		// // And please, unlike me, do something about the Exceptions :D
		// byte[] buffer = new byte[1024];
		// int len;
		// while ((len = clientIfo.read(buffer)) > -1) {
		// baos.write(buffer, 0, len);
		// }
		// baos.flush();
		//
		// // Open new InputStreams using the recorded bytes
		// // Can be repeated as many times as you wish
		// InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
		// InputStream is2 = new ByteArrayInputStream(baos.toByteArray());
		
		// System.err.println(XMLParser.convertStreamToString(clientIfo));
		// System.exit(1);
		
		try {
			KVMessage kvMessage = new KVMessage(clientIfo);
			
			// fetch information from KVMessage
			K key = (K) kvMessage.fromString(kvMessage.getKey());
			V value = (V) kvMessage.fromString(kvMessage.getValue());
			String Request = kvMessage.getType();
			
			// make the task runnable
			Runnable r = new runnableClient(key, value, Request, client, kvMessage);
			
			// add this task to threadpool
			threadpool.addToQueue(r);
			
		} catch (KVException e1) {
			//System.err.println("err in inputstream conv_1223 line 102");
			if (e1.getMsg() != null) {
				String Message = "Unknow Error: " + e1.getMsg();
				KVMessage kvM = new KVMessage("resp", null, null, Message, null);
				//System.err.println("106");
				this.sendResponseToClient(kvM, client);
				//System.err.println("108");
			}
		} catch (InterruptedException e) {
			//System.err.println("err in inputstream conv line 109");
			if (e.getMessage() != null) {
				String Message = "Unknow Error: " + e.getMessage();
				KVMessage kvM = new KVMessage("resp", null, null, Message, null);
				this.sendResponseToClient(kvM, client);
			}
		}
		
	}
	
	
	public class runnableClient implements Runnable {
		
		K key;
		V value;
		String ClientRequest;
		String Status = "False";
		String Message;
		KVMessage kvM;
		Server server;
		int port;
		Socket client;
		OutputStream os;
		
		public runnableClient(K k, V v, String r) {
			this.key = k;
			this.value = v;
			this.ClientRequest = r;
			
		}
		
		public runnableClient(K key, V value, String r, Socket cl, KVMessage kvmag) {
			this.ClientRequest = r;
			this.client = cl;
			try {
				this.os = client.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.key = key;
			this.value = value;
			this.kvM = kvmag;
			// this.server = cl.;
			// this.port = p;
			
		}
		
		// send response to client
		public void sendResponseToClient(KVMessage kvmsg) {
			// OutputStream os;
			try {
				os = client.getOutputStream();
				// os.flush();
				PrintWriter out = new PrintWriter(os);
				out.write(kvmsg.toXML());
				// System.out.println("XML to be sent back is:\n" + kvmsg.toXML());
				out.flush();
				// os.close();
				client.shutdownOutput();
			} catch (IOException e) {
				// Message = "Network Error: Could not send data";
				// kvM = new KVMessage("resp", null, null, Message, null);
				// this.sendResponseToClient(kvM);
				System.err.println("Network Error: Could not send data");
				e.printStackTrace();
			}
		}
		
		public void run() {
			if (ClientRequest.equals("putreq")) {
				try {
					if (key == null) {
						Message = "Empty key";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else if (value == null) {
						Message = "Empty value";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else if (key.toString().getBytes().length > 256) {
						Message = "Over sized key";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else if (value.toString().getBytes().length > 131073) {
						Message = "Over sized value";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else {
						keyserver.put(key, value);
						this.Status = keyserver.Status;
						Message = keyserver.Message;
						kvM = new KVMessage("resp", null, null, Message, this.Status);
						this.sendResponseToClient(kvM);
					}
				} catch (KVException e) {
					Message = "Unknown Error: put request fail";
					kvM = new KVMessage("resp", null, null, Message, null);
					this.sendResponseToClient(kvM);
				}
			} else if (ClientRequest.equals("getreq")) {
				try {
					if (key == null) {
						Message = "Does not exist";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else if (key.toString().getBytes().length > 256) {
						Message = "Over sized key";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else {
						V vR = keyserver.get(key);
						Message = keyserver.Message;
						kvM = new KVMessage("resp", KVMessage.toString(key), KVMessage.toString(vR), Message, null);
						this.sendResponseToClient(kvM);
						
					}
				} catch (KVException e) {
					Message = "Unknown Error: get request fail";
					kvM = new KVMessage("resp", null, null, Message, null);
					this.sendResponseToClient(kvM);
				}
			} else if (ClientRequest.equals("delreq")) {
				try {
					if (key == null) {
						Message = "Does not exist";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else if (key.toString().getBytes().length > 256) {
						Message = "Over sized key";
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					} else {
						keyserver.del(key);
						Message = keyserver.Message;
						kvM = new KVMessage("resp", null, null, Message, null);
						this.sendResponseToClient(kvM);
					}
				} catch (KVException e) {
					Message = "Unknown Error: del request fail";
					kvM = new KVMessage("resp", null, null, Message, null);
					this.sendResponseToClient(kvM);
				}
			}
		}
	}
}
