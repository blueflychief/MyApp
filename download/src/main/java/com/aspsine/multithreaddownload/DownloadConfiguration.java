package com.aspsine.multithreaddownload;

/**
 * 下载配置类
 */
public class DownloadConfiguration {

    public static final int DEFAULT_MAX_THREAD_NUMBER = 10;

    public static final int DEFAULT_THREAD_NUMBER = 1;

    //最大线程数
    private int maxThreadNum;

    //启动线程数
    private int threadNum;

    //是否开启Service去下载
    private boolean luanchServiceDownload=false;

    //是否后台下载，仅对Service有效
    private boolean isBackground=false;


    public DownloadConfiguration() {
        maxThreadNum = DEFAULT_MAX_THREAD_NUMBER;
        threadNum = DEFAULT_THREAD_NUMBER;
    }


    public boolean isLuanchServiceDownload() {
        return luanchServiceDownload;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }

    public void setLuanchServiceDownload(boolean luanchServiceDownload) {
        this.luanchServiceDownload = luanchServiceDownload;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
}
