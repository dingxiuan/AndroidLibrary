package com.dxa.utils.thread;

import java.util.concurrent.TimeUnit;

public class ThreadHelper {

    private ThreadHelper() {
        throw new IllegalAccessError("Unsupport create object !");
    }

    /**
     * 获取当前线程
     */
    public synchronized static Thread getCurrentThread() {
        return Thread.currentThread();
    }

    /**
     * 获取当前线程的名字
     */
    public static String getName() {
        return getCurrentThread().getName();
    }

    /**
     * 获取当前线程的ID
     */
    public static long getId() {
        return getCurrentThread().getId();
    }

    /**
     * 获取当前线程的优先级
     */
    public static int getPriority() {
        return getCurrentThread().getPriority();
    }

    public static int getActiveCount(){
        ThreadGroup group = getCurrentThread().getThreadGroup();
        return notNull(group) ? group.activeCount() : 0;
    }

    /**
     * 检查当前线程是否被打断
     */
    public static boolean isInterrupted() {
        return getCurrentThread().isInterrupted();
    }
    
    /**
     * 检查当前线程是否被打断
     */
    public static boolean isInterrupted(Thread thread){
    	return notNull(thread) && thread.isInterrupted();
    }

    /**
     * 检查当前线程是否存活
     */
    public static boolean isAlive() {
        return getCurrentThread().isAlive();
    }

    /**
     * 检查当前线程线程是否为守护线程(即后台线程)
     */
    public static boolean isDaemon() {
        return getCurrentThread().isDaemon();
    }


    /**
     * 是否正在阻塞中
     * @param thread 线程
     * @return boolean类型的值
     */
    public static boolean isBlocking(Thread thread) {
        return notNull(thread) && thread.getState() == Thread.State.BLOCKED;
    }


    /**
     * 是否处于就绪状态
     * @param thread 线程
     * @return
     */
    public static boolean isWaiting(Thread thread){
        return notNull(thread) && thread.getState() == Thread.State.WAITING;
    }
    
    /**
     * 是否处于就绪状态
     * @param thread 线程
     * @return
     */
    public static boolean isRunnable(Thread thread){
        return notNull(thread) && thread.getState() == Thread.State.RUNNABLE;
    }
    
    /**
     * 获取当前线程的上下文对象
     */
    public static ClassLoader getContextClassLoader() {
        return getCurrentThread().getContextClassLoader();
    }

    /**
     * 获取当前线程的堆栈跟踪数组
     */
    public static StackTraceElement[] getStackTrace() {
        return getCurrentThread().getStackTrace();
    }

    /**
     * 获取当前线程所在的线程组
     */
    public static ThreadGroup getThreadGroup() {
        return getCurrentThread().getThreadGroup();
    }


    /**************** 睡眠 ********************************************************************/
    /**
     * 睡眠
     *
     * @param unit    失眠单位
     * @param timeout 时长
     * @throws InterruptedException
     */
    public static void sleep(TimeUnit unit, long timeout) throws InterruptedException {
        unit.sleep(timeout);
    }

    /**
     * 睡眠
     *
     * @param timeout 时长
     */
    public static void sleep(long timeout){
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 睡眠时间: 纳秒
     *
     * @param timeout 时长
     * @throws InterruptedException
     */
    public static void sleepNanoSeconds(long timeout) throws InterruptedException {
        sleep(TimeUnit.NANOSECONDS, timeout);
    }

    /**
     * 睡眠时间: 微秒
     *
     * @param timeout 时长
     * @throws InterruptedException
     */
    public static void sleepMicroSeconds(long timeout) throws InterruptedException {
        sleep(TimeUnit.MICROSECONDS, timeout);
    }

    /**
     * 睡眠时间: 毫秒
     *
     * @param timeout 时长
     * @throws InterruptedException
     */
    public static void sleepMilliSeconds(long timeout) throws InterruptedException {
        sleep(TimeUnit.MILLISECONDS, timeout);
    }

    /**
     * 睡眠时间: 秒
     *
     * @param timeout 时长
     * @throws InterruptedException
     */
    public static void sleepSeconds(long timeout) throws InterruptedException {
        sleep(TimeUnit.SECONDS, timeout);
    }

    /**
     * 睡眠时间: 分钟
     *
     * @param timeout 时长
     * @throws InterruptedException
     */
    public static void sleepMinutes(long timeout) throws InterruptedException {
        sleep(TimeUnit.MINUTES, timeout);
    }

    /**
     * 睡眠时间: 小时
     *
     * @param timeout 时长
     * @throws InterruptedException
     */
    public static void sleepHours(long timeout) throws InterruptedException {
        sleep(TimeUnit.HOURS, timeout);
    }

    private static boolean notNull(Object o) {
        return o != null;
    }
}
