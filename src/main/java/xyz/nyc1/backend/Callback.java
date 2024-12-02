package xyz.nyc1.backend;

import java.io.File;

/**
 * 回调类
 * @author canyie
 */
public interface Callback {
    /**
     * 有新连接时触发
     * @param transferPoint 传输端点对象
     * @param address 对方地址
     * @param request 若此实例是服务端，在此参数上调用函数以接受或拒绝请求；若是客户端，恒为 null
     */
    void onNewConnection(TransferPoint transferPoint, String address, Request request);

    /**
     * 客户端尝试连接服务端失败时触发
     * @param transferPoint 传输端点对象
     * @param address 对方地址
     * @param e 收到的异常
     */
    void onConnectionFailed(TransferPoint transferPoint, String address, Exception e);

    /**
     * 连接中断时触发，此回调恒在收到新连接后才会触发
     * @param transferPoint 传输端点对象
     * @param address 对方地址
     */
    void onLostConnection(TransferPoint transferPoint, String address);

    /**
     * 收到新文件传输请求时触发
     * @param transferPoint 传输端点对象
     * @param filename 文件名
     * @param request 请求对象 可以同意或拒绝请求
     */
    void onReceiveFile(TransferPoint transferPoint, String filename, String address, Request request);

    /**
     * 传输文件成功时调用
     * @param transferPoint 传输端点对象
     * @param outputFile 若为接收端，输出的文件；若为发送端，恒为 null
     */
    void onTransferSuccess(TransferPoint transferPoint, File outputFile);

    /**
     * 传输文件失败或被对方拒绝时调用
     * @param transferPoint 传输端点对象
     * @param filename 文件名
     */
    void onTransferFailed(TransferPoint transferPoint, String filename);
}
