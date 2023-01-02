package chapter1.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingEchoServer {

    public static final int DEFAULT_SERVER_PORT_NO = 8080;

    public static void main(String[] args) {
        System.out.println("Starting echo server");

        try (final ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(DEFAULT_SERVER_PORT_NO));

            while (true) {
                Socket acceptedSocket = serverSocket.accept();
                System.out.println("Accept connection from client");
                new Thread(() -> {
                    try {
                        PrintWriter out = new PrintWriter(acceptedSocket.getOutputStream());
                        BufferedReader input = new BufferedReader(new InputStreamReader(acceptedSocket.getInputStream()));
                        String request;
                        while((request = input.readLine()) != null) {
                            System.out.println("Received request from client: " + request);
                            out.write("echo: " + request + "\n");
                            out.flush();
                        }
                        acceptedSocket.close();
                        System.out.println("Close connection");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

            }
        } catch (Exception e) {
            System.out.println("Echo server failed to start!!!");
            e.printStackTrace();
        }
    }
}
