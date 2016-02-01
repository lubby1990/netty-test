package com.lubby.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by liubin on 2016-1-28.
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsynServerHandler attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsynServerHandler attachment) {
        attachment.countDownLatch.countDown();
    }

}
