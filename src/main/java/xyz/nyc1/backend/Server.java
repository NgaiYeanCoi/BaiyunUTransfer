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
    /** 服务连接 */
    private final ServerSocket mServerSocket;

    /**
     * 创建新服务器
     * @param port 监听的端口号
     * @param callback 当服务器收到新事件时，调用的回调函数
     * @throws IOException 发生 I/O 错误如对应端口被占用时触发
     */
    public Server(int port, String downloadDir, Callback callback) throws IOException {
        super("Server", downloadDir, callback);
        mServerSocket = new ServerSocket(port);
    }

    /**
     * 服务线程启动时调用
     */
    @Override public void run() {
        try (mServerSocket) {
            for (;;) {
                System.out.println("Server: Waiting for connection");
                mSocket = mServerSocket.accept();
                System.out.println("Server: New incoming connection");
                startPolling();
                handleConnection(mSocket);
                System.out.println("Server: Connection closed");
                mSocket = null;
            }
        } catch (InterruptedIOException|InterruptedException e) {
            System.out.println("Server: Shutting down due to interruption");
        } catch (IOException e) {
            String msg = e.toString();
            if (msg.contains("Socket closed") || msg.contains("Socket is closed")) {
                System.out.println("Server: Shutting down due to server closed");
            } else {
                System.err.println("Server: Shutting down due to unexpected error");
                e.printStackTrace();
            }
        } finally {
            stopPolling();
        }
    }

    /**
     * 处理新连接
     * @param socket 连接
     * @throws InterruptedException 当服务线程被中断时抛出
     */
    private void handleConnection(final Socket socket) throws InterruptedException, InterruptedIOException {
        boolean valid = false;
        String address = socket.getInetAddress().getHostAddress();
        try (socket;
             DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
            System.out.println("Server: " + address + " connected");
            mIn = in;
            mOut = out;
            valid = checkConnection(address);
            if (valid) {
                handleRequestLoop();
            }
        } catch (InterruptedIOException e) {
            // 此线程已经被中断，重新向上抛出以终止整个服务器
            System.out.println("Server: Interrupted while communicating with client, rethrowing");
            throw e;
        } catch (IOException e) {
            String msg = e.toString();
            if (msg.contains("Socket closed") || msg.contains("Socket is closed")) {
                System.out.println("Server: Connection closed by client while communicating with it");
            } else {
                System.err.println("Server: Encounter errors while communicating with client");
                e.printStackTrace();
            }
        } finally {
            System.out.println("Server: Lost connection to address " + address);
            mIn = null;
            mOut = null;
            if (valid)
                SwingUtilities.invokeLater(() -> mCallback.onLostConnection(Server.this, address));
        }
    }

    /**
     * 有新连接时调用此方法进行基础握手 以检查客户端是否正确
     * @param address 对端地址
     * @return 此连接是否有效 返回 false 会导致此连接被抛弃
     * @throws InterruptedException 若线程被中断
     * @throws IOException 若发生 I/O 错误
     */
    private boolean checkConnection(String address) throws InterruptedException, IOException {
        String response = mIn.readUTF();
        if (!HELLO.equals(response)) {
            System.err.println("Server: Reject connection due to unexpected hello line " + response);
            return false;
        }
        Request request = new Request();
        SwingUtilities.invokeLater(() -> mCallback.onNewConnection(Server.this, address, request));
        if (!request.await()) {
            System.err.println("Server: Reject connection due to user interaction");
            mOut.writeUTF(GOODBYE);
            mOut.flush();
            return false;
        }
        mOut.writeUTF(HELLO);
        mOut.flush();
        return true;
    }

    @Override public void close() {
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                System.err.println("Service: Failed to close server socket");
                e.printStackTrace();
            }
        }
        super.close();
    }
}
