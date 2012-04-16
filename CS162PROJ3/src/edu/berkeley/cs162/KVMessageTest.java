/**
 * Test Class for KVMessage
 */
package edu.berkeley.cs162;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @author Hang Wen
 * 
 */
public class KVMessageTest implements Serializable {
	
	private static final long serialVersionUID = -3172181550857750545L;
	
	/**
	 * @param args
	 * @throws KVException
	 */
	public static void main(String[] args) {
		KVMessageTest testClass = new KVMessageTest();
		testClass.keySizeCheck();
		testClass.TypeConventionTest();
		try {
			testClass.ToXMLTest();
		} catch (KVException e) {
			System.out.println("ToXMLTest Error!");
		}
		testClass.InputStreamTest();
		
	}
	
	private boolean ToXMLTest() throws KVException {
		System.out.println(breaker + " toXMLTest Begins" + breaker);
		KVMessage msg = new KVMessage("This is Message Type", "This is Key", "This is Type");
		KVMessage.checksize(msg);
		System.out.println("ToXMLTest Begins.");
		System.out.println(msg.toXML());
		return true;
	}
	
	private boolean InputStreamTest() {
		System.out.println(breaker + " InputStreamTest Begins" + breaker);
		String in = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<KVMessage type=\"This is Message Type\">\n"
				+ "<Key>This is Key</Key>\n" + "<Value>This is Type</Value>\n" + "<Status>null</Status>\n"
				+ "<Message>null</Message>\n" + "</KVMessage>\n";
		System.out.println("The xml input is:\n" + in);
		InputStream is = new ByteArrayInputStream(in.getBytes());
		try {
			KVMessage msg = new KVMessage(is);
			KVMessage.checksize(msg);
			System.out.println("The xml out is:\n");
			System.out.println(msg.toXML());
		} catch (KVException e) {
			System.out.println("InputStreamTest Error!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean TypeConventionTest() {
		System.out.println(breaker + " TypeConventionTest Begins" + breaker);
		People t = new People("Tony", 21, "male");
		String s = KVMessage.toString(t);
		People t2 = (People) KVMessage.fromString(s);
		if (t.equals(t2)) {
			System.out.println("Type Convention Test Sucess!");
			return true;
		} else {
			System.out.println("Type Convention Test Failed!");
			return false;
		}
	}
	
	private void keySizeCheck() {
		System.out.println(breaker + " InputStreamTest Begins" + breaker);
		String key = "kljasflkk;lasfkjkkksfjksfjasfjfjfjfjfjfjksfjfkjfkjkjklsfjkls";
		String in = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<KVMessage type=\"This is Message Type\">\n"
				+ "<Key>" + key + "</Key>\n" + "<Value>This is Type</Value>\n" + "<Status>null</Status>\n"
				+ "<Message>null</Message>\n" + "</KVMessage>\n";
		System.out.println("The xml input is:\n" + in);
		InputStream is = new ByteArrayInputStream(in.getBytes());
		try {
			KVMessage msg = new KVMessage(is);
			KVMessage.checksize(msg);
			System.out.println("The xml out is:\n");
			System.out.println(msg.toXML());
		} catch (KVException e) {
			System.out.println(e.getMsg().getMsg());
		}
	}
	
	
	private class People implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8838777812746359431L;
		private String name;
		private int age;
		private String sex;
		
		People(String name, int age, String sex) {
			this.name = name;
			this.age = age;
			this.sex = sex;
		}
		
		public boolean equals(People p) {
			if (p.age == this.age && p.name.equals(this.name) && p.sex.equals(this.sex)) { return true; }
			return false;
		}
		
		public int getAge() {
			return age;
		}
		
		public void setAge(int age) {
			this.age = age;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getSex() {
			return sex;
		}
		
		public void setSex(String sex) {
			this.sex = sex;
		}
		
	}
	
	private String breaker = "%%%%%%%%%%%%";
}
