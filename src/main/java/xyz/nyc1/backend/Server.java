package xyz.nyc1.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;

/**
 * 服务器类
 * @author canyie
 */
public class Server extends TransferPoint {
    private final ServerSocket mServerSocket;

    /**
     * 创建新服务器
     * @param port 监听的端口号
     * @param callback 当服务器收到新事件时，调用的回调函数
     * @throws IOException 发生 I/O 错误如对应端口被占用时触发
     */
    public Server(int port, Callback callback) throws IOException {
        super(callback);
        mServerSocket = new ServerSocket(port);
    }

    /**
     * 服务线程启动时调用
     */
    @Override public void run() {
        try (mServerSocket) {
            for (;;) {
                mSocket = mServerSocket.accept();
                handleConnection(mSocket);
                mSocket = null;
            }
        } catch (InterruptedIOException|InterruptedException e) {
            System.out.println("Server: Shutting down due to interruption");
        } catch (IOException e) {
            System.err.println("Server: Shutting down due to unexpected error");
            e.printStackTrace();
        }
    }

    /**
     * 处理新连接
     * @param socket 连接
     * @throws InterruptedException 当服务线程被中断时抛出
     */
    private void handleConnection(final Socket socket) throws InterruptedException {
        boolean valid = false;
        String address = socket.getInetAddress().getHostAddress();
        try (socket;
             DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
            System.out.println("Server: " + address + " connected");
            String response = in.readUTF();
            if (!HELLO.equals(response)) {
                System.err.println("Server: Rejecting connection due to unexpected hello line " + response);
                return;
            }
            Request request = new Request();
            SwingUtilities.invokeLater(() -> mCallback.onNewConnection(address, request));
            if (!request.await()) {
                System.err.println("Server: Reject connection due to user interaction");
                out.writeUTF(GOODBYE);
                out.flush();
                return;
            }
            valid = true;
            out.writeUTF(HELLO);
            out.flush();
            for (;;) {
                response = in.readUTF();
                if (response.equals(GOODBYE)) {
                    System.out.println("Server: Closing connection due to client request");
                    return;
                } else {
                    System.err.println("Server: Closing connection due to unexpected message " + response);
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Server: Encounter errors while communicating with client");
            e.printStackTrace();
        } finally {
            System.out.println("Server: Lost connection to address " + address);
            if (valid)
                mCallback.onLostConnection(address);
        }
    }
}
