package com.example.demo.util.common;

import lombok.Data;
import org.springframework.retry.backoff.Sleeper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

/**
 * @author chen_bq
 * @description 控制台进度条工具
 * @create: 2019/10/10 11:23
 **/
@Data
public class ConsoleProgressBar implements Runnable{

    private final static String ONE_EIGHTH = "▏";
    private final static String ONE_FORTH = "▎";
    private final static String THIRD_EIGHTH = "▍";
    private final static String A_HALF = "▌";
    private final static String FIFTH_EIGHTH = "▋";
    private final static String THIRD_FORTH = "▊";
    private final static String SEVENTH_EIGHTH = "▉";
    // 总格数
    public static int COMPLETE_NUMBER = 250;
    // 百分比
    public final static int PERCENT = 100;
    // 刷新时长
    private final long threadSleep;
    // 计数点
    private CountDownLatch latch;
    // 全部任务数
    private final int totalTask;
    // 完成任务数
    private int completeTask = 0;

    public ConsoleProgressBar(int totalTask, CountDownLatch latch){
        this.totalTask = totalTask;
        this.latch = latch;
        threadSleep = 1000L;
    }

    public ConsoleProgressBar(int totalTask, CountDownLatch latch, long threadSleep){
        this.totalTask = totalTask;
        this.latch = latch;
        this.threadSleep = threadSleep;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ConsoleProgressBar progressBar = new ConsoleProgressBar(1000, countDownLatch);
        Thread thread = new Thread(progressBar);
        thread.start();
        Thread.sleep(1000);
        for (int i = 0 ; i < 1000 ; i ++){
            progressBar.setCompleteTask(i + 1);
        }
        countDownLatch.await();
    }

    /**
     * @Author chen_bq
     * @Description 简化打印进度条
     * @Date 2019/10/10 15:51
     * @Param [totalTask, completeTask]
     * @return void
     */
    private void printProgressBar(){
        BigDecimal percent = BigDecimal.valueOf(completeTask).divide(BigDecimal.valueOf(totalTask));
        if (completeTask < totalTask){
            int currentProgress = percent.multiply(BigDecimal.valueOf(COMPLETE_NUMBER)).setScale(BigDecimal.ROUND_HALF_UP).intValue();
            int surplusProgress = totalTask - currentProgress;
            while (currentProgress -- > 0){
                System.out.print(ONE_EIGHTH);
            }
            // 保留2位小数，四舍五入 向上取整
            System.out.println("  " + percent.multiply(BigDecimal.valueOf(PERCENT)).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        }else {
            System.out.println("Complete Success!");
        }
    }


    @Override
    public void run() {
        while (this.totalTask > 0){
            printProgressBar();
            if (completeTask == totalTask){
                break;
            }
            try {
                Thread.sleep(threadSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        latch.countDown();
        Thread.interrupted();
    }
}
