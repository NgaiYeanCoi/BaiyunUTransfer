package xyz.nyc1.backend;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/**
 * 抽象的连接端点类，可以是服务端也可以是客户端
 * @author canyie
 */
public abstract class TransferPoint extends Thread implements Closeable {
    /** 协议：握手 */
    static final String HELLO = "BaiyunUTransferProtocol/1.0 HELLO";
    /** 协议：握手 */
    static final String GOODBYE = "GOODBYE";
    /** 协议：请求头 */
    static final String REQUEST = "REQUEST ";
    /** 协议：响应头 */
    static final String REPLY = "REPLY ";
    /** 协议：文件传输请求头 后续应该以 writeUTF 格式写入文件名 */
    static final String FILE_REQUEST = REQUEST + "TRANSFER FILE";
    /** 协议：响应头 表示同意请求或请求成功完成 */
    static final String REPLY_OK = REPLY + "OKAY";
    /** 协议：响应头 表示拒绝请求 */
    static final String REPLY_DECLINE = REPLY + "DENY";
    /** 协议：响应头 表示请求尚未完成 请对端继续操作 */
    static final String REPLY_CONTINUE = REPLY + "CONT";
    /** 协议：响应头 表示请求失败 */
    static final String REPLY_FAILED = REPLY + "FAIL";

    /** 当前传输点的标签 可以是 {@link Server} 也可以是 {@link Client} */
    private final String mTag;
    /** 事件队列 被 {@link ActionPollingThread} 使用 */
    private final BlockingQueue<Runnable> mActionQueue = new LinkedBlockingQueue<>();
    /** 事件队列 被 {@link ActionPollingThread} 使用 */
    private ActionPollingThread mActionThread;
    /** 处理回应消息的回调 */
    private volatile Consumer<String> mReplyHandler;
    /** 下载到的路径 */
    private Path mBaseDownloadPath;
    /** 处理事件的回调 */
    final Callback mCallback;
    /** 建立的和对端的连接 */
    Socket mSocket;
    /** 和对端的连接的输入流 */
    DataInputStream mIn;
    /** 和对端的连接的输出流 */
    DataOutputStream mOut;

    /**  acceptMD5.equals(sendMD5) 判断接收文件和发送文件是否一致
     *  文件完整:@return true
     *  文件不完整:@return false*/
    private boolean compare;




    /**
     * 创建新的连接端点
     * @param callback 事件回调
     */
    public TransferPoint(String tag, Callback callback){
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

    /**
     * 设置下载到哪个文件夹 此文件夹必须存在
     * @param dir 目标文件夹
     */
    public void setDownloadDir(File dir) {
        try {
            mBaseDownloadPath = dir.toPath().toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid dir: " + dir, e);
        }
    }

    /**
     * 异步发送文件
     * @param filename 文件全路径
     */
    public void sendFile(String filename) {
        mActionQueue.add(() -> {
            DataInputStream in = mIn;
            DataOutputStream out = mOut;
            if (in == null || out == null) {
                System.err.println(mTag + ": Ignore transfer request due to closed connection");
                SwingUtilities.invokeLater(() -> mCallback.onTransferFailed(TransferPoint.this, filename));
                return;
            }
            File file = new File(filename);
            mReplyHandler = response -> completeSendFile(file, mIn, mOut, response);
            try {
                // 请求发送文件
                mOut.writeUTF(FILE_REQUEST);
                mOut.writeUTF(file.getName());
                mOut.flush();
            } catch (IOException e) {
                mReplyHandler = null;
                if (e instanceof InterruptedIOException) {
                    System.err.println(mTag + ": Request aborted due to interruption");
                    Thread.currentThread().interrupt();
                } else {
                    System.err.println(mTag + ": Request failed with I/O error");
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> mCallback.onTransferFailed(TransferPoint.this, filename));
            }
        });
    }

    /**
     * 处理发送文件请求的响应，并完成剩余步骤
     * @param file 传输的文件
     * @param in 连接输入流
     * @param out 连接输出流
     * @param response 对端响应行
     */
    private void completeSendFile(File file, DataInputStream in, DataOutputStream out, String response) {

        boolean done = false;
        if (REPLY_OK.equals(response)) {
            try (FileInputStream fis = new FileInputStream(file)) {
                // 创建 MD5 校验和 Send
                MessageDigest mDigestSend = MessageDigest.getInstance("MD5");
                byte[] buf = new byte[8192];
                for (;;) {
                    int len = fis.read(buf);
                    if (len < 0) {
                        // 文件已经读完
                        out.writeInt(0);
                        out.flush();
                        break;
                    } else if (len == 0) {
                        // 读取失败，直接重试，不告知对端
                        continue;
                    } else {
                        out.writeInt(len);
                        out.write(buf, 0, len);
                        out.flush();
                    }
                    response = in.readUTF();
                    if (!response.equals(REPLY_CONTINUE)) {
                        System.err.println(mTag + ": Request interrupted by responding " + response);
                        return;
                    }
                    // 计算校验
                    mDigestSend.update(buf, 0, len);
                }

                // 计算最终的散列值
                String sendMD5 = bytesToHex(mDigestSend.digest());

                // 发送校验和
                out.writeUTF(sendMD5);
                out.flush();


                response = in.readUTF();
                System.err.println(mTag + ": Request completed with " + response);
                done = REPLY_OK.equals(response);
            } catch (InterruptedIOException e) {
                System.err.println(mTag + ": Request aborted due to interruption");
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                System.err.println(mTag + ": Request failed with I/O error");
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.err.println(mTag + ": Request rejected by responding " + response);
        }
        Runnable runnable = done
                ? () -> mCallback.onTransferSuccess(TransferPoint.this, null)
                : () -> mCallback.onTransferFailed(TransferPoint.this, file.getName());
        SwingUtilities.invokeLater(runnable);
    }

    /**
     * 进入事件处理主循环 等待对端发送消息 仅当连接被关闭时返回
     * @throws InterruptedException 如线程被中断
     * @throws IOException 若发生 I/O 错误
     */
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
                    // 检查是否会发生严重的安全违规，如果发生，立即终止
                    if (invalidFilename(filename)) {
                        // EventLog.writeEvent(0x534e4554, "", -1, filename);
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
        } catch (SocketException e) {
            if (e.toString().contains("Connection reset"))
                System.err.println(mTag + ": Socket closed by opposite");
            else
                throw e;
        }
    }

    /**
     * 同意接收文件
     * @param filename 文件名
     * @throws IOException 若发生 I/O 错误
     */
    private void acceptFile(String filename) throws IOException{
        mOut.writeUTF(REPLY_OK);
        mOut.flush();


        File outputFile = generateOutputFile(filename);
        byte[] buf = new byte[8192];
        boolean done = false;
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            // 创建 MessageDigest 对象 用于获取接收后的MD5
            MessageDigest mDigestAccept = MessageDigest.getInstance("MD5");
            for (;;) {
                // 读取本次传输的长度
                int length = mIn.readInt();
                if (length == 0) {
                    // 文件已经读完
                    done = true;
                    break;
                } else if (length < 0 || length > buf.length) {
                    // 非法请求，拒绝
                    System.err.println("Server: Rejecting transfer request due to bad length " + length);
                    break;
                }
                mIn.readFully(buf, 0, length);
                fos.write(buf, 0, length);

                // 计算校验和
                mDigestAccept.update(buf, 0, length);

                mOut.writeUTF(REPLY_CONTINUE);
                mOut.flush();
            }

            // 计算接收文件的MD5
            String acceptMD5 = bytesToHex(mDigestAccept.digest());
            String sendMD5 = mIn.readUTF();

            compare = CompareMD5.compare(acceptMD5, sendMD5);

        } catch (InterruptedIOException e) {
            System.err.println(mTag + ": Transfer aborted due to interruption");
            throw e;
        } catch (IOException e) {
            System.err.println(mTag + ": Transfer failed due to I/O error");
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(mTag + ": Transfer completed");
            if (done) {
                mOut.writeUTF(REPLY_OK);
                mOut.flush();
                SwingUtilities.invokeLater(() -> mCallback.onTransferSuccess(TransferPoint.this, outputFile));
            } else {
                mOut.writeUTF(REPLY_FAILED);
                mOut.flush();
                outputFile.delete();
                SwingUtilities.invokeLater(() -> mCallback.onTransferFailed(TransferPoint.this, filename));
            }
        }
    }

    private boolean invalidFilename(String filename) {
        if (mBaseDownloadPath == null)
            throw new NullPointerException("for security reasons, set a base download dir");
        // 阻止可能的路径穿越攻击
        if (filename.isEmpty() || filename.contains("/") || filename.contains("\\"))
            return true;
        // 额外的路径穿越检查
        Path path = mBaseDownloadPath.resolve(filename).normalize();
        return !path.startsWith(mBaseDownloadPath);
    }

    private File generateOutputFile(String filename) throws IOException {
        int dot = filename.lastIndexOf(".");
        for (int i = 0;i != -1;i++) {
            String generated;
            if (i == 0) {
                generated = filename;
            } else {
                if (dot == -1)
                    generated = filename + "(" + i + ")";
                else
                    generated = filename.substring(0, dot) + "(" + i + ")" + filename.substring(dot);
            }
            Path path = mBaseDownloadPath.resolve(generated).normalize();
            // 尝试创建文件以检查文件是否存在
            // 使用 NIO 的 createFile 是为了检测 path 指向一个存在的符号链接但是链接指向的文件不存在的情况以阻止意外写入其他文件
            try {
                Files.createFile(path);
                return path.toFile();
            } catch (FileAlreadyExistsException ignored) {
                // 文件存在，尝试下一个
            }
        }
        throw new IllegalStateException("No available filename for " + filename);
    }

    /**
     * 启动消息循环子线程
     */
    public void startPolling() {
        mActionThread = new ActionPollingThread();
        mActionThread.start();
    }

    /**
     * 终止消息循环子线程
     */
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

    /**
     * 事件处理循环子线程 循环执行 {@link TransferPoint#mActionQueue} 中的所有任务
     */
    private class ActionPollingThread extends Thread {
        ActionPollingThread() {
            super(mTag + "-ActionPollingThread");
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

    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /** 获取文件完整性
     * @return 文件完整性
     * */
    public boolean getCompare() {
        return compare;
    }
}
