package com.lubby.nio.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liubin on 2016-1-26.
 */
public class MultiTimerServer implements Runnable {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean stop;

    public MultiTimerServer(int port) {
        try {
            //创建Selector
            selector = Selector.open();
            //创建ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            //设置channel为非阻塞
            serverSocketChannel.configureBlocking(false);
            //绑定端口，这只backlog为1024
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            //将ServerSocketChannel注册到Selector上，监听OP_ACCEPT
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server is starting....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (!stop) {
            try {
                //无论有无读写时间都每隔1s唤醒一次
                selector.select(1000);
//                selector.select();
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    public void handleInput(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverChannel.accept();  //相当于完成了TCP三次握手
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }

        if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(readBuffer);

            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, "utf-8");
                System.out.println("receive data : " + body);
                String result = "My name is Jack";
                doWrite(socketChannel, result);
            } else if (readBytes < 0) {
                key.cancel();
                socketChannel.close();
            }


        }

    }

    public void doWrite(SocketChannel channel, String result) throws IOException {
        byte[] bytes = result.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer);

        System.out.println("has remain :   " + writeBuffer.hasRemaining());

    }

    public void stop() {
        stop = false;
    }
}
