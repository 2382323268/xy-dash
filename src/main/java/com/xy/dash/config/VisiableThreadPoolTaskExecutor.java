package com.xy.data.config.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 创建ThreadPoolTaskExecutor子类 打印线程池使用情况
 **/
@Slf4j
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    //创建方法打印线程池使用情况
    private void showThreadPoolInfo(String prefix) {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        //判断
        if (threadPoolExecutor == null) {
            return;
        }
        //打印线程池使用日志
        log.info("{}, {}, 任务总数 taskCount [{}], 已完成任务数 completedTaskCount [{}], 活跃线程数 activeCount [{}], 队列大小 queueSize [{}]",
                this.getThreadNamePrefix(),
                prefix,
                threadPoolExecutor.getTaskCount(),    //任务总数
                threadPoolExecutor.getCompletedTaskCount(),  //已完成任务数
                threadPoolExecutor.getActiveCount(),   //活跃线程数
                threadPoolExecutor.getQueue().size()   //队列大小
        );
    }


    /**
     * @Description 重写ThreadPoolTaskExecutor的以下六个方法
     */
    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("1. do execute");
        super.execute(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("线程名称: [{}] 异常信息: [{}]", Thread.currentThread().getName(), e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo("1. do submitListenable");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo("2. do submitListenable");
        return super.submitListenable(task);
    }
}
