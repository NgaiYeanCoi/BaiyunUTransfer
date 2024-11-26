package xyz.nyc1.backend;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/**
 * 抽象的连接端点类，可以是服务端也可以是客户端
 * @author canyie
 */
public abstract class TransferPoint extends Thread implements Closeable {
    static final String HELLO = "BaiyunUTransferProtocol/1.0 HELLO";
    static final String GOODBYE = "GOODBYE";
    static final String REQUEST = "REQUEST ";
    static final String REPLY = "REPLY ";
    static final String FILE_REQUEST = REQUEST + "TRANSFER FILE";
    static final String REPLY_OK = REPLY + "OKAY";
    static final String REPLY_DECLINE = REPLY + "DENY";
    static final String REPLY_CONTINUE = REPLY + "CONT";
    static final String REPLY_FAILED = REPLY + "FAIL";

    private final BlockingQueue<Runnable> mActionQueue = new LinkedBlockingQueue<>();
    private final String mTag;
    final Callback mCallback;
    private ActionPollingThread mActionThread;
    private volatile Consumer<String> mReplyHandler;
    Socket mSocket;
    DataInputStream mIn;
    DataOutputStream mOut;

    /**
     * 创建新的连接端点
     * @param callback 事件回调
     */
    public TransferPoint(String tag, Callback callback) {
        super(tag + "-Thread");
        setPriority(Thread.MAX_PRIORITY);
        mTag = tag;
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
        mActionQueue.add(() -> {
            DataInputStream in = mIn;
            DataOutputStream out = mOut;
            if (in == null || out == null) {
                System.err.println(mTag + ": Ignore transfer request due to closed connection");
                SwingUtilities.invokeLater(() -> mCallback.onTransferFailed(TransferPoint.this, filename));
                return;
            }
            mReplyHandler = response -> completeSendFile(filename, mIn, mOut, response);
            try {
                mOut.writeUTF(FILE_REQUEST);
                mOut.writeUTF(new File(filename).getName());
                mOut.flush();
            } catch (IOException e) {
                mReplyHandler = null;
                System.err.println(mTag + ": Request failed with I/O error");
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> mCallback.onTransferFailed(TransferPoint.this, filename));
            }
        });
    }

    private void completeSendFile(String filename, DataInputStream in, DataOutputStream out, String response) {
        boolean done = false;
        if (REPLY_OK.equals(response)) {
            try (FileInputStream fis = new FileInputStream(filename)) {
                byte[] buf = new byte[8192];
                for (;;) {
                    int len = fis.read(buf);
                    if (len < 0) {
                        mOut.writeInt(0);
                        mOut.flush();
                        break;
                    } else if (len == 0) {
                        continue;
                    } else {
                        mOut.writeInt(len);
                        mOut.write(buf, 0, len);
                        mOut.flush();
                    }
                    response = mIn.readUTF();
                    if (!response.equals(REPLY_CONTINUE)) {
                        System.err.println(mTag + ": Request interrupted by responding " + response);
                        return;
                    }
                }
                response = mIn.readUTF();
                System.err.println(mTag + ": Request completed with " + response);
                done = REPLY_OK.equals(response);
            } catch (IOException e) {
                System.err.println(mTag + ": Request failed with I/O error");
                e.printStackTrace();
            }
        } else {
            System.err.println(mTag + ": Request rejected by responding " + response);
        }
        Runnable runnable = done
                ? () -> mCallback.onTransferSuccess(TransferPoint.this, null)
                : () -> mCallback.onTransferFailed(TransferPoint.this, filename);
        SwingUtilities.invokeLater(runnable);
    }

    public void handleRequestLoop() throws InterruptedException, IOException {
        // Listen for client requests
        try {
            for (; ; ) {
                String request = mIn.readUTF();
                if (GOODBYE.equals(request)) {
                    System.out.println(mTag + ": Closing connection due to client request");
                    return;
                } else if (request.startsWith(REPLY) && mReplyHandler != null) {
                    mReplyHandler.accept(request);
                    mReplyHandler = null;
                } else if (FILE_REQUEST.equals(request)) {
                    System.out.println(mTag + ": New incoming file transfer request");
                    String filename = mIn.readUTF();
                    if (filename == null || filename.isEmpty()
                            || filename.contains("/") || filename.contains("\\")) {
                        System.err.println(mTag + ": Reject request due to invalid file name " + filename);
                        mOut.writeUTF(REPLY_DECLINE);
                        mOut.flush();
                    } else {
                        Request req = new Request();
                        SwingUtilities.invokeLater(() -> mCallback.onReceiveFile(TransferPoint.this, filename, req));
                        if (req.await()) {
                            System.out.println(mTag + ": Accepted file transfer request");
                            acceptFile(filename);
                        } else {
                            System.err.println(mTag + ": Reject request due to user interaction");
                            mOut.writeUTF(REPLY_DECLINE);
                            mOut.flush();
                        }
                    }
                } else {
                    System.err.println(mTag + ": Closing connection due to unexpected message " + request);
                    return;
                }
            }
        } catch (EOFException e) {
            System.err.println(mTag + ": Socket closed by opposite");
        }
    }

    private void acceptFile(String filename) throws IOException {
        mOut.writeUTF(REPLY_OK);
        mOut.flush();
        byte[] buf = new byte[8192];
        boolean done = false;
        File outputFile = new File(filename);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            for (;;) {
                int length = mIn.readInt();
                if (length == 0) {
                    done = true;
                    break;
                } else if (length < 0 || length > buf.length) {
                    System.err.println("Server: Rejecting transfer request due to bad length " + length);
                    break;
                }
                mIn.readFully(buf, 0, length);
                fos.write(buf, 0, length);
                mOut.writeUTF(REPLY_CONTINUE);
                mOut.flush();
            }
        } catch (IOException e) {
            System.err.println("Server: Transfer failed due to I/O error");
            e.printStackTrace();
        } finally {
            if (done) {
                mOut.writeUTF(REPLY_OK);
                mOut.flush();
                SwingUtilities.invokeLater(() -> mCallback.onTransferSuccess(TransferPoint.this, outputFile));
            } else {
                mOut.writeUTF(REPLY_FAILED);
                mOut.flush();
                SwingUtilities.invokeLater(() -> mCallback.onTransferFailed(TransferPoint.this, filename));
            }
        }
    }

    public void startPolling() {
        mActionThread = new ActionPollingThread();
        mActionThread.start();
    }

    public void stopPolling() {
        ActionPollingThread thread = mActionThread;
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mActionThread = null;
        }
    }

    /**
     * 关闭此端点并等待所有操作完成
     */
    @Override public void close() {
        interrupt();
        stopPolling();
        try {
            join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private class ActionPollingThread extends Thread {
        ActionPollingThread() {
            super(mTag + "-ActionPollingThread");
            System.out.println(getName());
            setPriority(MAX_PRIORITY);
        }

        @Override public void run() {
            System.out.println(mTag + ": Start polling");
            try {
                for (;;) {
                    Runnable action = mActionQueue.take();
                    action.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(mTag + ": End polling");
            mActionQueue.clear();
        }
    }
}
