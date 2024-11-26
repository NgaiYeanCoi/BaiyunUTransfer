package xyz.nyc1.backend;

import java.io.File;

/**
 * 回调类
 * @author canyie
 */
public interface Callback {
    /**
     * 有新连接时触发
     * @param address 对方地址
     * @param request 若此实例是服务端，在此参数上调用函数以接受或拒绝请求；若是客户端，恒为 null
     */
    void onNewConnection(String address, Request request);

    /**
     * 连接中断时触发，若此实例是服务端，此回调恒在收到新连接后才会触发；若为客户端，请求连接后先收到此回调而非新连接回调表示服务端拒绝连接
     * @param address 对方地址
     */
    void onLostConnection(String address);
    void onReceiveFile(String filename, Request request);
    void onTransferSuccess(File outputFile);
    void onTransferFailed(String filename);
}
