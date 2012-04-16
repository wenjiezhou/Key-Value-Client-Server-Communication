package edu.berkeley.cs162;

public class ClientTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientTest test = new ClientTest();
		test.testPut1();
		test.testPut2();
		test.testGet1();
		test.testGet2();
		test.testDel1();
		test.testDel2();
		test.testGet1();
		// bad key
		String key = "<>/>";
		int value = 10;
		
		test.testPut(key, value);
		test.testPut(key, value + 10);
		test.testGet(key);
		test.testDel(key);
		test.testGet(key);
		test.testDel(key);
		
		String keyover = "lakdsjflksajfl;ksadjfl;ksajfl;ksadjfl;kasdjflaskjflkasdhjflk;ashflkajsfhklasjfhlkjashfajklshfkljashfkljasfhkjlsahfkjlasfhsakjldsakjfsadlfkj;aklsdfjals;kjf;laskjfl;kasjflkasdkjflk;askjfl;kaskjflksajfl;kasdjflk;dsajfkl;sajfkl;asdjflksajflksajfj";
		String valueover = "nishabi";
		for (int i = 0; i < 1000; i++)
			valueover += keyover;
		test.testPut(keyover, value);
		test.testPut(key, value);
		test.testPut(key, valueover);
		test.testGet(key);
		
		test.testPut(null, 100);
		test.testPut(key, null);
		test.testGet(null);
		test.testDel(null);
	}
	
	private void testPut(String key, int value) {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		debug("Input key is " + key + " and value is " + value);
		try {
			System.out.println("First input output is: " + client.put(key, value));
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
	}
	
	private void testPut(String key, String value) {
		KVClient<String, String> client = new KVClient<String, String>(server2, port);
		debug("Input key is " + key + " and value is " + "oversized value");
		try {
			System.out.println("First input output is: " + client.put(key, value));
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
	}
	
	private int testGet(String key) {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		debug("Get key is " + key);
		int value = -9999;
		try {
			value = client.get(key);
			debug("First get value is: " + value);
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
		return value;
	}
	
	private void testDel(String key) {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		debug("del key is " + key);
		try {
			client.del(key);
			debug("Successfully deleted key");
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
	}
	
	private void testPut1() {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		String key = "a";
		int value = 1;
		debug("Input key is " + key + " and value is " + value);
		try {
			System.out.println("First input output is: " + client.put(key, value));
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
	}
	
	private void testPut2() {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		String key = "a";
		int value = 2;
		debug("Input key is " + key + " and value is " + value);
		try {
			System.out.println("First input output is: " + client.put(key, value));
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
	}
	
	private int testGet1() {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		String key = "a";
		int value = -99999;
		debug("Get key is " + key);
		try {
			value = client.get(key);
			debug("First get value is: " + value);
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
		return value;
	}
	
	/**
	 * Get an non-existing key
	 */
	private void testGet2() {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		String key = "non-exsiting";
		int value = -99999;
		debug("Get key is " + key);
		try {
			value = client.get(key);
			debug("First get value is: " + value);
		} catch (KVException e) {
			System.err.flush();
			System.err.println(e.getMsg().getMsg());
			System.err.flush();
		}
	}
	
	/**
	 * delete an exising key
	 */
	private void testDel1() {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		String key = "a";
		int value = -99999;
		debug("del key is " + key);
		try {
			client.del(key);
			debug("Successfully deleted key");
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
	}
	
	/**
	 * delete an non-exising key
	 */
	private void testDel2() {
		KVClient<String, Integer> client = new KVClient<String, Integer>(server2, port);
		String key = "a";
		int value = -99999;
		debug("del key is " + key);
		try {
			client.del(key);
			debug("Successfully deleted key");
		} catch (KVException e) {
			System.err.println(e.getMsg().getMsg());
		}
	}
	
	public void debug(String s) {
		System.out.flush();
		System.out.println(s);
		System.out.flush();
	}
	
	private static String server = "ec2-23-20-176-40.compute-1.amazonaws.com";
	private static String server3 = "ec2-23-22-16-236.compute-1.amazonaws.com";
	private static String server2 = "localhost";
	private int port = 8080;
}
