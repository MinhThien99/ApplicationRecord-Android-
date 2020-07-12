package com.example.thien_record;



import java.io.Serializable;

public class RecordingItems implements Serializable {


    private String mName; // file name
    private String mFilePath; //file path
    private long mLength; // length of recording in seconds
    private long mTime; // date/time of the recording

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public long getmLength() {
        return mLength;
    }

    public void setmLength(long mLength) {
        this.mLength = mLength;
    }

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public RecordingItems() {
    }

    public RecordingItems(String mName, String mFilePath,  long mLength, long mTime) {
        this.mName = mName;
        this.mFilePath = mFilePath;
        this.mLength = mLength;
        this.mTime = mTime;
    }




}
