package com.lubby.nio.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by liubin on 2016-1-27.
 */
public class SocketClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 4321);

        OutputStreamWriter write = new OutputStreamWriter(socket.getOutputStream());
        write.write("hello world");
        write.flush();
        TimeUnit.HOURS.sleep(1);
        socket.close();
    }
}
