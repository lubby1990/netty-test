package com.lubby.nio.server;

/**
 * Created by liubin on 2016-1-25.
 */
public class Server {

    public static void main(String[] args) {
        int port = 4321;
        MultiTimerServer server = new MultiTimerServer(port);

        new Thread(server).start();

    }
}
