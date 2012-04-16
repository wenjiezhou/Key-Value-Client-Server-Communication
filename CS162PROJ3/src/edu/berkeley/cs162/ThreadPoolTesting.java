package edu.berkeley.cs162;
  
import java.io.IOException;
  
public class ThreadPoolTesting {
    static SocketServer server = null;
     
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Binding Server:");
        server = new SocketServer("localhost", 8081);
        NetworkHandler handler = new ThreadPoolTestingClientHandler();
        server.addHandler(handler);
        server.connect();
        System.out.println("Starting Server");
        server.run();
        System.out.println("Closing Server");
    }
  
}

