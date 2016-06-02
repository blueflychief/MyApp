package com.aspsine.multithreaddownload;


import java.io.File;

/**
 * 下载请求信息类
 */
public class DownloadRequestInfo {
    private String mUri;

    private File mSaveDir;

    private CharSequence mFileName;

    private CharSequence mDescription;

    private boolean mScannable;

    private DownloadRequestInfo() {
    }

    private DownloadRequestInfo(String uri, File folder, CharSequence title, CharSequence description, boolean scannable) {
        this.mUri = uri;
        this.mSaveDir = folder;
        this.mFileName = title;
        this.mDescription = description;
        this.mScannable = scannable;
    }

    public String getUri() {
        return mUri;
    }

    public File getSaveDir() {
        return mSaveDir;
    }

    public CharSequence getFileName() {
        return mFileName;
    }

    public CharSequence getDescription() {
        return mDescription;
    }

    public boolean isScannable() {
        return mScannable;
    }

    public static class Builder {

        //下载地址
        private String mUri;

        //下载保存目录
        private File mSaveDir;

        //保存文件名
        private CharSequence mFileName;

        private CharSequence mDescription;

        private boolean mScannable;

        public Builder() {
        }

        public Builder setUri(String uri) {
            this.mUri = uri;
            return this;
        }

        public Builder setSavePath(File folder) {
            this.mSaveDir = folder;
            return this;
        }

        public Builder setFileName(CharSequence title) {
            this.mFileName = title;
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public Builder setScannable(boolean scannable) {
            this.mScannable = scannable;
            return this;
        }

        public DownloadRequestInfo build() {
            DownloadRequestInfo request = new DownloadRequestInfo(mUri, mSaveDir, mFileName, mDescription, mScannable);
            return request;
        }
    }
}
