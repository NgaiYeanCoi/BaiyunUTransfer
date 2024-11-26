package xyz.nyc1.backend;

import java.io.Closeable;
import java.net.Socket;

/**
 * 抽象的连接端点类，可以是服务端也可以是客户端
 * @author canyie
 */
public abstract class TransferPoint extends Thread implements Closeable {
    static final String HELLO = "BaiyunUTransferProtocol/1.0 HELLO";
    static final String GOODBYE = "GOODBYE";

    final Callback mCallback;
    Socket mSocket;

    /**
     * 创建新的连接端点
     * @param callback 事件回调
     */
    public TransferPoint(Callback callback) {
        mCallback = callback;
    }

    /**
     * 返回连接是否有效
     * @return 如果已连接到对方，返回 true，否则 false
     */
    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    public void sendFile(String filename) {

    }

    /**
     * 关闭此端点并等待所有操作完成
     */
    @Override public void close() {
        interrupt();
        try {
            join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
