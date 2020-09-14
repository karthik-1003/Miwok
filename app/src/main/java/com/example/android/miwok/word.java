package com.example.android.miwok;

public class word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int mImage = NO_IMAGE;
    private static final int NO_IMAGE=-1;
    private int maudio;


    public word(String defaultTranslation, String miwokTranslation, int audio){
        mDefaultTranslation=defaultTranslation;
        mMiwokTranslation=miwokTranslation;
        maudio=audio;
    }

    public word(String defaultTranslation,String miwokTranslation,int image,int audio){
        mDefaultTranslation=defaultTranslation;
        mMiwokTranslation=miwokTranslation;
        mImage=image;
        maudio=audio;
    }
    public String getDefaultTranslation(){
        return mDefaultTranslation;
    }
    public String getMiwokTranslation(){
        return mMiwokTranslation;
    }

    public int getmImage() {
        return mImage;
    }
    public boolean hasimage(){
        return mImage != NO_IMAGE;
    }

    public int getMaudio() {
        return maudio;
    }
}
