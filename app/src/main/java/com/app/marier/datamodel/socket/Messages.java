package com.app.marier.datamodel.socket;

import android.net.Uri;

public class Messages {

    public static final  int TYPE_MESSAGE = 0;
    public static final  int TYPE_MESSAGE_REMOTE =1;
    public static final  int TYPE_ACTION=2;
    public static final  int TYPE_IMAGE=3;


    private int mType;
    private String mMessage;
    private String mTime;
    private String fromMess;
    private String toMess;
    private String createdOn;
    private String userimage;
    private Uri imageuri;
    private String recordaudiofile;
    private String pdffile;

    public String getPdffile() {
        return pdffile;
    }

    public void setPdffile(String pdffile) {
        this.pdffile = pdffile;
    }

    public String getRecordaudiofile() {
        return recordaudiofile;
    }

    public void setRecordaudiofile(String recordaudiofile) {
        this.recordaudiofile = recordaudiofile;
    }

    public Uri getImageuri() {
        return imageuri;
    }

    public void setImageuri(Uri imageuri) {
        this.imageuri = imageuri;
    }

    public Messages() {
    }

    public static int getTypeMessage() {
        return TYPE_MESSAGE;
    }

    public static int getTypeMessageRemote() {
        return TYPE_MESSAGE_REMOTE;
    }

    public static int getTypeAction() {
        return TYPE_ACTION;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getFromMess() {
        return fromMess;
    }

    public void setFromMess(String fromMess) {
        this.fromMess = fromMess;
    }

    public String getToMess() {
        return toMess;
    }

    public void setToMess(String toMess) {
        this.toMess = toMess;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUserimage() {
        return userimage;
    }



    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public static class Builder{
        private final int mType;
        private String mMessage;
        private String mTime;
        private Uri suri;
        private String mrecordaudiofile;
        private String pdffile;

        public Builder(int mType) {
            this.mType = mType;
        }

        public Builder time(String time){
            mTime=time;
            return this;
        }

        public Builder uri(Uri uri){
            suri = uri;
            return this;
        }
        public Builder mrecordaudo(String recordfile){
            mrecordaudiofile = recordfile;
            return this;
        }

        public Builder pdffile(String pdfifle){
            pdffile = pdfifle;
            return this;
        }
        public Builder message(String message)
        {
            mMessage=message;
            return this;
        }
        public Messages build()
        {
            Messages message=new Messages();
            message.mType=mType;
            message.mMessage=mMessage;
            message.mTime =mTime;
            message.imageuri = suri;
            message.recordaudiofile = mrecordaudiofile;
            message.pdffile = pdffile;
            return message;
        }
    }
}
