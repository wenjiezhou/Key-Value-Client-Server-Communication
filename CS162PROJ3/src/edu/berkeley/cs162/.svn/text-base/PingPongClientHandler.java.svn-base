 
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
  
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
  
/**
 * This NetworkHandler will asynchronously handle the socket connections. 
 * It uses a threadpool to ensure that none of it's methods are blocking.
 *
 * @param <K> Java Generic type for the Key
 * @param <V> Java Generic type for the Value
 */
public class PingPongClientHandler implements NetworkHandler {
    private ThreadPool threadPool = null;
     
    public PingPongClientHandler() {
        initialize(3);
    }
  
    public PingPongClientHandler(int connections) {
        initialize(connections);
    }
  
    private void initialize(int connections) {
        threadPool = new ThreadPool(connections);   
    }
     
    /* (non-Javadoc)
     * @see edu.berkeley.cs162.NetworkHandler#handle(java.net.Socket)
     */
    @Override
    public void handle(Socket acceptedSocket) throws IOException {
        RunnableJob job = new RunnableJob(acceptedSocket);
         
        try {
            threadPool.addToQueue(job);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
     
    private static class RunnableJob implements Runnable {
        protected Socket acceptedSocket = null;
         
        public RunnableJob(Socket acceptedSocket) {
            this.acceptedSocket = acceptedSocket;
        }
         
        public void run() {
            try {
                /* kk: this block is only for debugging
                BufferedReader clientTextReader = new BufferedReader(new InputStreamReader(acceptedSocket.getInputStream()));
                String clientText = "";
                 
                try {
                    while (clientTextReader.ready() == false) {
                        Thread.sleep(1000);
                    }
                    while (clientTextReader.ready() == true) {
                        clientText += (char)clientTextReader.read();  
                    }
                } catch (Exception e) {
                    System.err.print(e);
                }
                 
                System.out.println("Received: \n" + clientText + "\n");
                //*/
     
                DataOutputStream outputStreamToClient = new DataOutputStream(acceptedSocket.getOutputStream());
                outputStreamToClient.writeBytes("pong");
                acceptedSocket.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
  
}

