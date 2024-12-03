package xyz.nyc1.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.SwingUtilities;

/**
 * 客户端类
 * @author canyie
 */
public class Client extends TransferPoint {
    private String address;
    private int port;
    /**
     * 创建新的客户端
     * @param address 服务器地址
     * @param port 服务器端口号
     * @param callback 当收到新事件时，响应的回调
     */
    public Client(String address, int port, Callback callback) {
        super("Client", callback);
        this.address = address;
        this.port = port;
        mSocket = new Socket();
    }

    /**
     * 服务线程启动时调用
     */
    @Override public void run() {
        try {
            System.out.println("Client: Connecting to " + address);
            mSocket.connect(new InetSocketAddress(address, port), 15 * 1000);
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> mCallback.onConnectionFailed(Client.this, address, e));
            return;
        }
        final Socket socket = mSocket;
        System.out.println("Client: Connected to " + socket);
        boolean connected = false;
        Exception exception = null;
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
            connected = true;
            SwingUtilities.invokeLater(() -> mCallback.onNewConnection(Client.this, address, null));
            startPolling();
            handleRequestLoop();
        } catch (InterruptedIOException | InterruptedException e) {
            System.out.println("Client: Shutting down due to interruption");
            exception = e;
        } catch (IOException e) {
            System.err.println("Client: Shutting down due to unexpected error");
            e.printStackTrace();
            exception = e;
        } finally {
            System.out.println("Client: Lost connection to " + address);
            stopPolling();
            mIn = null;
            mOut = null;
            final Exception e = exception;
            if (connected)
                SwingUtilities.invokeLater(() -> mCallback.onLostConnection(Client.this, address));
            else
                SwingUtilities.invokeLater(() -> mCallback.onConnectionFailed(Client.this, address, e));
        }
    }
}
