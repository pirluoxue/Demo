package com.example.demo.util.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chen_bq
 * @description 使用ReentrantLock实现线程锁和唤醒机制
 * @create: 2020/4/17 17:55
 **/
public class LockThread {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isFinish = false;

    public void signalAllThread() {
        lock.lock();
        try {
            isFinish = true;
            System.out.println(Thread.currentThread().getName() + " 实际唤醒所有线程 ...");
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 是否有服务已经完成
     */
    public boolean hasServerFinish() {
        return isFinish;
    }

    public void lockThread(long timeout, TimeUnit unit){
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 进入锁定等待中 ...");
            while (!hasServerFinish()) {
                if (nanos <= 0L) {
                    /* 超时 */
                    System.out.println(Thread.currentThread().getName() + " 唤醒超时 ... ");
                    System.out.println("已超时： " + nanos + " " + unit.name());
                    return;
                }
                nanos = condition.awaitNanos(nanos);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(){
            @Override
            public void run() {
                LockThread thread = new LockThread();
                thread.lockThread(10, TimeUnit.SECONDS);
            }
        };
        Thread thread2 = new Thread(){
            @Override
            public void run() {
                LockThread thread = new LockThread();
                thread.lockThread(10, TimeUnit.SECONDS);
            }
        };
        thread1.start();
        thread2.start();
        Thread.sleep(3000);

    }

}
