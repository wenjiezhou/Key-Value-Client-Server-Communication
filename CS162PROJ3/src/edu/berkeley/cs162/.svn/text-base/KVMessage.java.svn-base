/**
 * 
 * XML Parsing library for the key-value store
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This is the object that is used to generate messages the XML based messages
 * for communication between clients and servers. Data is stored in a
 * marshalled String format in this object.
 */
public class KVMessage {
	
	private String msgType = null;
	private String key = null;
	private String value = null;
	private String msg = null;
	private String status = null;
	
	public KVMessage(String msgType, String key, String value) {
		this.msgType = msgType;
		this.key = key;
		this.value = value;
	}
	
	public KVMessage(String msgType, String key, String value, String msg, String status) {
		this.msgType = msgType;
		this.key = key;
		this.value = value;
		this.msg = msg;
		this.status = status;
	}
	
	public KVMessage(String msgType, String msg) {
		this.msgType = msgType;
		this.msg = msg;
		
	}
	
	public String getType() {
		return this.msgType;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setType(String msgType) {
		this.msgType = msgType;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void setMsg(String message) {
		this.msg = message;
	}
	
	public void getStatus(String status) {
		this.status = status;
	}
	
	
	/*
	 * Hack for ensuring XML libraries does not close input stream by default.
	 * Solution from http://weblogs.java.net/blog/kohsuke/archive/2005/07/socket_xml_pitf.html
	 */
	private class NoCloseInputStream extends FilterInputStream {
		
		public NoCloseInputStream(InputStream in) {
			super(in);
		}
		
		public void close() {
		} // ignore close
	}
	
	public KVMessage(InputStream input) throws KVException {
		
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		domfac.setValidating(false);
		KVMessage kvmError = new KVMessage(resp, null, null);
		NoCloseInputStream noIs = new NoCloseInputStream(input);
		
		try {
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			Document doc = dombuilder.parse(noIs);
			Element root = doc.getDocumentElement();
			
			NodeList nodes = root.getChildNodes();
			
			this.msgType = root.getAttribute("type").trim();
			
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					// for (Node node = item.getFirstChild(); node != null; node = node.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						if (node.getNodeName().equals("Key")) {
							if (node.getFirstChild() != null) {
								String key = node.getFirstChild().getNodeValue();
								this.key = key;
							}
							// System.out.println("xml里面的key标签值：" + key);
							continue;
						}
						if (node.getNodeName().equals("Value")) {
							if (node.getFirstChild() != null) {
								String value = node.getFirstChild().getNodeValue();
								this.value = value;
							}
							// System.out.println("xml里面的value标签值：" + value);
							continue;
						}
						if (node.getNodeName().equals("Status")) {
							if (node.getFirstChild() != null) {
								String s = node.getFirstChild().getNodeValue();
								this.status = s;
							}
							// System.out.println("xml里面的key标签值：" + key);
							continue;
						}
						if (node.getNodeName().equals("Message")) {
							if (node.getFirstChild() != null) {
								String s = node.getFirstChild().getNodeValue();
								this.msg = s;
							}
							// System.out.println("xml里面的key标签值：" + key);
							continue;
						}
						// }
					}
					
					//
					// }
				}//
			}
			KVMessage.checksize(this);
		} catch (ParserConfigurationException e) {
			throwKVException(kvmError, "XML Error: Received unparseable message");
		} catch (FileNotFoundException e) {
			throwKVException(kvmError, "XML Error: Received unparseable message");
		} catch (SAXException e) {
			SAXParseException spe = (SAXParseException) e;
			StringBuffer sb = new StringBuffer(spe.toString());
			sb.append("\n Line number: " + spe.getLineNumber());
			sb.append("\nColumn number: " + spe.getColumnNumber());
			sb.append("\n Public ID: " + spe.getPublicId());
			sb.append("\n System ID: " + spe.getSystemId() + "\n");
			System.err.println(sb.toString());
			throwKVException(kvmError, "XML Error: Received unparseable message");
		} catch (IOException e) {
			throwKVException(kvmError, "XML Error: Received unparseable message");
		}
	}
	
	private void throwKVException(KVMessage kvm, String error) throws KVException {
		kvm.setMsg(error);
		throw new KVException(kvm);
	}
	
	/**
	 * Generate the XML representation for this message.
	 * 
	 * @return the XML String
	 */
	public String toXML() {
		String xml;
		xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xml += "<KVMessage type=\"" + this.msgType + "\">";
		if (this.key == null) {
			xml += "<Key>" + "</Key>";
		} else {
			xml += "<Key>" + this.key + "</Key>";
		}
		if (this.value == null) {
			xml += "<Value></Value>";
		} else {
			xml += "<Value>" + this.value + "</Value>";
		}
		if (this.status == null) {
			xml += "<Status></Status>";
		} else {
			xml += "<Status>" + this.status + "</Status>";
		}
		if (this.msg == null) {
			xml += "<Message></Message>";
		} else {
			xml += "<Message>" + this.msg + "</Message>";
		}
		xml += "</KVMessage>";
		
		return xml;
		
	}
	
	/**
	 * 
	 * @param xmlString
	 * @return
	 */
	public static KVMessage xmlToKVMessage(String xmlString) {
		InputStream is = new ByteArrayInputStream(xmlString.getBytes());
		KVMessage k = null;
		try {
			k = new KVMessage(is);
			
		} catch (KVException e) {
			System.err.print("Cannot convert to smlString");
		}
		return k;
	}
	
	/**
	 * 
	 * @param kvmsg
	 * @return
	 */
	public static String KVMessagetoXML(KVMessage kvmsg) {
		return kvmsg.toString();
	}
	
	/** Write the object to a Base64 string. */
	public static String toString(Serializable o) {
		try {
			if (o == null) { return null; }
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			return DatatypeConverter.printBase64Binary(baos.toByteArray());
		} catch (IOException e) {
			System.err.println("Error in converting to serialized string");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object fromString(String s) {
		try {
			if (s == null) { return null; }
			byte[] data = DatatypeConverter.parseBase64Binary(s); // use dataConverter
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (IOException e) {
			System.err.println("Error in converting from serialized string");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Error in converting from serialized string");
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean checksize(KVMessage kvmsg) throws KVException {
		if (kvmsg.key != null) {
			if (kvmsg.key.getBytes().length > 256) {
				String err = "Over sized key";
				KVMessage msg = new KVMessage("resp", err);
				throw new KVException(msg);
			}
		}
		if (kvmsg.value != null) {
			if (kvmsg.value.getBytes().length > 131073) {
				String err = "Over sized value";
				KVMessage msg = new KVMessage("resp", err);
				throw new KVException(msg);
			}
		}
		return true;
		
	}
	
	/** request types. */
	private static String getreq = "getreq";
	private static String putreq = "putreq";
	private static String delreq = "delreq";
	private static String resp = "resp";
	
}
