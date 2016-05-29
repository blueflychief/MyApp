package com.infinite.myapp.utils.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class DownLoadModel implements Parcelable {
    public int dataId; //!!!!!!databaseId
    public String url;
    public long end;
    public long start;
    public long downed;
    public long total;
    public String saveName;

    public DownLoadModel() {
    }

    public List<DownLoadModel> multiList;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dataId);
        dest.writeString(this.url);
        dest.writeLong(this.end);
        dest.writeLong(this.start);
        dest.writeLong(this.downed);
        dest.writeLong(this.total);
        dest.writeString(this.saveName);
        dest.writeTypedList(multiList);
    }

    protected DownLoadModel(Parcel in) {
        this.dataId = in.readInt();
        this.url = in.readString();
        this.end = in.readLong();
        this.start = in.readLong();
        this.downed = in.readLong();
        this.total = in.readLong();
        this.saveName = in.readString();
        this.multiList = in.createTypedArrayList(DownLoadModel.CREATOR);
    }

    public static final Creator<DownLoadModel> CREATOR = new Creator<DownLoadModel>() {
        @Override
        public DownLoadModel createFromParcel(Parcel source) {
            return new DownLoadModel(source);
        }

        @Override
        public DownLoadModel[] newArray(int size) {
            return new DownLoadModel[size];
        }
    };
}
