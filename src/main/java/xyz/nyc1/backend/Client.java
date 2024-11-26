package xyz.nyc1.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.SwingUtilities;

/**
 * 客户端类
 * @author canyie
 */
public class Client extends TransferPoint {
    /**
     * 创建新的客户端
     * @param address 服务器地址
     * @param port 服务器端口号
     * @param callback 当收到新事件时，响应的回调
     * @throws java.net.UnknownHostException 目标主机找不到时抛出
     * @throws IOException 发生 I/O 错误时抛出
     */
    public Client(String address, int port, Callback callback) throws IOException {
        super("Client", callback);
        mSocket = new Socket(address, port);
    }

    /**
     * 服务线程启动时调用
     */
    @Override public void run() {
        final Socket socket = mSocket;
        InetAddress address = socket.getInetAddress();
        System.out.println("Client: Connected to " + socket);
        try (socket;
             DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
            out.writeUTF(HELLO);
            out.flush();
            String response = in.readUTF();
            if (!HELLO.equals(response)) {
                if (GOODBYE.equals(response)) {
                    System.err.println("Client: Shutting down due to server rejection");
                } else {
                    System.err.println("Client: Shutting down due to unexpected hello line " + response);
                }
                return;
            }
            mIn = in;
            mOut = out;
            SwingUtilities.invokeLater(() -> mCallback.onNewConnection(Client.this, address.getHostAddress(), null));
            startPolling();
            handleRequestLoop();
        } catch (InterruptedIOException | InterruptedException e) {
            System.out.println("Client: Shutting down due to interruption");
        } catch (IOException e) {
            System.err.println("Client: Shutting down due to unexpected error");
            e.printStackTrace();
        } finally {
            System.out.println("Client: Lost connection to " + address);
            stopPolling();
            mIn = null;
            mOut = null;
            SwingUtilities.invokeLater(() -> mCallback.onLostConnection(Client.this, address.getHostAddress()));
        }
    }
}
