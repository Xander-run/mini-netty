package chapter1.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class BlockingHelloClient {

    public static final int DEFAULT_CLIENT_PORT_NO = 4040;

    private static final String DEFAULT_REQUEST = "hello from client\n";

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting HelloClient");
        Socket socket = new Socket();
        socket.setSoTimeout(1000);
        socket.connect(new InetSocketAddress(BlockingEchoServer.DEFAULT_SERVER_PORT_NO));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String response = null;
        int tryTimes = 0;
        while (tryTimes < 10) {
            tryTimes++;
            out.write(DEFAULT_REQUEST);
            out.flush();
            try {
                response = input.readLine();
            } catch (SocketTimeoutException timeoutException) {
                System.out.println("Read timeout, times:" + tryTimes);
                continue;
            }
            System.out.println(response);
        }

        System.out.println("Closing HelloClient");
        out.close();
        input.close();
        socket.close();

        System.out.println("HelloClient Closed");
    }
}
