package com.yanzhenjie.andserver.sample.website;

import android.os.Environment;

import com.yanzhenjie.andserver.sample.App;
import com.yanzhenjie.andserver.sample.util.FileUtils;
import com.yanzhenjie.andserver.util.IOUtils;

import java.io.File;

/**
 * Created by wl on 2019/7/4.
 */
public class PathManager {

    private static PathManager sInstance;

    public static PathManager getInstance() {
        if(sInstance == null) {
            synchronized (PathManager.class) {
                if(sInstance == null) {
                    sInstance = new PathManager();
                }
            }
        }
        return sInstance;
    }

    private File mRootDir;

    private PathManager() {
        if (FileUtils.storageAvailable()) {
            mRootDir = Environment.getExternalStorageDirectory();
        } else {
            mRootDir = App.getInstance().getFilesDir();
        }
        mRootDir = new File(mRootDir, "1111WEB");
        IOUtils.createFolder(mRootDir);
    }

    public String getWebDir() {
        return mRootDir.getAbsolutePath();
    }
}