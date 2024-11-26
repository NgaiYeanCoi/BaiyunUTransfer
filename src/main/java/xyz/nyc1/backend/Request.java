package xyz.nyc1.backend;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象的请求类，调用 accept 接受请求，decline 拒绝请求
 * @author canyie
 */
public class Request {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final AtomicBoolean result = new AtomicBoolean();

    /**
     * 等待请求被响应
     * @return 请求结果
     * @throws InterruptedException 线程被中断
     */
    public boolean await() throws InterruptedException {
        countDownLatch.await();
        return result.get();
    }

    /**
     * 接受请求
     */
    public void accept() {
        result.set(true);
        countDownLatch.countDown();
    }

    /**
     * 拒绝请求
     */
    public void decline() {
        result.set(false);
        countDownLatch.countDown();
    }
}
