package com.yanzhenjie.andserver.sample;

/**
 * Created by wl on 2020/2/13.
 */
public class GlobalValueMananger {

    public String tempMessage;

    private static GlobalValueMananger sInstance;

    public static GlobalValueMananger getInstance() {
        if(sInstance == null) {
            synchronized (GlobalValueMananger.class) {
                if(sInstance == null) {
                    sInstance = new GlobalValueMananger();
                }
            }
        }
        return sInstance;
    }
}
