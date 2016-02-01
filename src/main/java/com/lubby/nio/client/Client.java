package com.lubby.nio.client;

/**
 * Created by liubin on 2016-1-26.
 */
public class Client {
    public static void main(String[] args) {
        int port = 4321;
        String ip = "127.0.0.1";
        ClientTimer timer = new ClientTimer(ip, port);
        new Thread(timer).start();
    }
}
