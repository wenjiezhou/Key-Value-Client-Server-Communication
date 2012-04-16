package edu.berkeley.cs162;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PingPong {
	static KeyServer<String, String> key_server = null;
	static SocketServer server = null;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		System.out.println("Binding Server:");
		key_server = new KeyServer<String, String>(1000);
		server = new SocketServer("localhost", 8081);
		
		NetworkHandler handler = new PingPongClientHandler();
		server.addHandler(handler);		
		server.connect();		
		System.out.println("Starting PingPong Server");
		server.run(); 							// the run method will handle the "pong" response
		
	}

}