package com.infinite.myapp.utils.networkutil.download;


import com.infinite.myapp.utils.databaseutil.DownLoadModel;
import com.infinite.myapp.utils.networkutil.ApiService;
import com.infinite.myapp.utils.networkutil.MyNetWorkRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class DownLoadTask implements Runnable {

    private  final String TAG = DownLoadTask.class.getSimpleName();

    private int mTaskId;

    private String mSaveFileName;

    private DownLoadTaskListener mDownLoadTaskListener;

    private ApiService mApiService = MyNetWorkRequest.getInstance().getApiService();

    private Call<ResponseBody> mResponseCall;

    private long mFileSizeDownloaded;

    private DownLoadModel mDownLoadModel;

    private long mNeedDownSize;

    private final long Call_Back_Length = 1024 * 1024;

    DownLoadTask(int taskId, DownLoadModel downLoadModel, DownLoadTaskListener downLoadTaskListener) {
        this.mDownLoadModel = downLoadModel;
        this.mDownLoadTaskListener = downLoadTaskListener;
        this.mSaveFileName = downLoadModel.saveName;
        this.mTaskId = taskId;
        this.mNeedDownSize = downLoadModel.end - (downLoadModel.start + downLoadModel.downed);
    }

    @Override
    public void run() {
        if (mDownLoadModel.downed != 0) {
            mResponseCall = mApiService.downloadFile(mDownLoadModel.url, "bytes=" + (mDownLoadModel.downed + mDownLoadModel.start) + "-" + mDownLoadModel.end);
//            sLoggerd("haha", "bytes=" + (mDownLoadModel.downed + mDownLoadModel.start) + "-" + mDownLoadModel.end);
        } else {
            mResponseCall = mApiService.downloadFile(mDownLoadModel.url, "bytes=" + mDownLoadModel.start + "-" + mDownLoadModel.end);
//            sLoggerd("haha", "bytes=" + mDownLoadModel.start + "-" + mDownLoadModel.end);
        }
        ResponseBody result = null;
        try {
            Response response = mResponseCall.execute();
            onStart();
            result = (ResponseBody) response.body();
            if (response.isSuccessful()) {
                if (writeToFile(result, mDownLoadModel.start, mDownLoadModel.downed)) {
                    onCompleted();
                }
            } else {
                onError(new Throwable(response.message()));
            }
        } catch (IOException e) {
            onError(new Throwable(e.getMessage()));
        } finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private boolean writeToFile(ResponseBody body, long startSet, long mDownedSet) {
        try {
            File futureStudioIconFile = new File(mSaveFileName);

            if (!futureStudioIconFile.exists()) {
                futureStudioIconFile.createNewFile();
            }

            RandomAccessFile oSavedFile = new RandomAccessFile(futureStudioIconFile, "rw");

            oSavedFile.seek(startSet + mDownedSet);

            InputStream inputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                mFileSizeDownloaded = 0;

                inputStream = body.byteStream();

                while (mFileSizeDownloaded < mNeedDownSize) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    oSavedFile.write(fileReader, 0, read);

                    mFileSizeDownloaded += read;

                    mDownLoadModel.downed += read;

                    if (mFileSizeDownloaded >= Call_Back_Length) {
                        onDownLoading(mFileSizeDownloaded);
                        mNeedDownSize -= mFileSizeDownloaded;
                        mFileSizeDownloaded = 0;
                    } else {
                        if (mNeedDownSize < Call_Back_Length) {
                            if (mFileSizeDownloaded >= mNeedDownSize) {
                                onDownLoading(mFileSizeDownloaded);
                                mNeedDownSize = 0;
                                mFileSizeDownloaded = 0;
                                return true;
                            }
                        }
                    }
//                    sLoggerd(TAG, "thread " + Thread.currentThread().getId() + " taskID " + mTaskId + " file download: " + mFileSizeDownloaded + " of " + fileSize);
                }
                return true;
            } finally {
                if (oSavedFile != null) {
                    oSavedFile.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
                if (body != null) {
                    body.close();
                }
            }
        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                onCancel();
            } else {
                if (mFileSizeDownloaded > 0) {
                    onDownLoading(mFileSizeDownloaded);
                }
                onError(e);
            }
            return false;
        }
    }

    private void onStart() {
        mDownLoadTaskListener.onStart(mDownLoadModel);
    }

    private void onCancel() {
        mResponseCall.cancel();
        mResponseCall = null;
        mDownLoadTaskListener.onCancel(mDownLoadModel);
    }

    private void onCompleted() {
        mResponseCall = null;
        mDownLoadTaskListener.onCompleted(mDownLoadModel);
    }

    private void onDownLoading(long downSize) {
        mDownLoadTaskListener.onDownLoading(downSize);
//        sLoggerd(TAG, "onDownLoading mTaskId" + mTaskId + " downed " + (mDownLoadModel.downed) + " count " + (mDownLoadModel.total));
    }

    private void onError(Throwable throwable) {
        mDownLoadTaskListener.onError(mDownLoadModel, throwable);
    }

    public static final class Builder {
        private DownLoadModel mDownEntity;
        private int mTaskId;

        private DownLoadTaskListener mDownLoadTaskListener;


        public Builder downLoadModel(DownLoadModel downEntity) {
            mDownEntity = downEntity;
            return this;
        }

        public Builder downLoadTaskListener(DownLoadTaskListener downLoadTaskListener) {
            mDownLoadTaskListener = downLoadTaskListener;
            return this;
        }


        public Builder taskId(int id) {
            this.mTaskId = id;
            return this;
        }

        public DownLoadTask build() {
            if (mDownEntity.url.isEmpty()) {
                throw new IllegalStateException("DownLoad URL required.");
            }

            if (mDownLoadTaskListener == null) {
                throw new IllegalStateException("DownLoadTaskListener required.");
            }

            if (mDownEntity.end == 0) {
                throw new IllegalStateException("SetCount required.");
            }

            if (mTaskId == 0) {
                throw new IllegalStateException("TaskId required.");
            }
            return new DownLoadTask(mTaskId, mDownEntity, mDownLoadTaskListener);
        }

    }

    public int getTaskId() {
        return mTaskId;
    }
//
//    public static final class DownLoadStatus {
//        public static final int DownLoading = 1;
//        public static final int Cancel = 2;
//        public static final int Completed = 3;
//        public static final int Error = 4;
//    }
}
