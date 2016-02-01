package com.lubby.aio.server;

/**
 * Created by liubin on 2016-1-28.
 */
public class Server {
    public static void main(String[] args) {
        int port = 4321;

        new Thread(new AsynServerHandler(port)).start();

    }
}
