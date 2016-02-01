package com.lubby.aio.client;

/**
 * Created by liubin on 2016-1-28.
 */
public class Client {
    public static void main(String[] args) {
        int port = 4321;
        String ip = "127.0.0.1";
        new Thread(new AsynClientHandler(ip, port)).start();
    }
}
