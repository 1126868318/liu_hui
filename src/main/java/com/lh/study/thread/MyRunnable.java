package com.lh.study.thread;

import java.util.Date;

/**
 * @program: liu_hui
 * @description: 这是一个简单的Runnable类，需要大约5秒钟来执行其任务
 * @author: Mr.Lhui
 * @create: 2020-07-22 20:52
 **/
public class MyRunnable implements  Runnable{
    private String command;

    public MyRunnable(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
//        processCommand();
//        System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
    }


    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.command;
    }


}

