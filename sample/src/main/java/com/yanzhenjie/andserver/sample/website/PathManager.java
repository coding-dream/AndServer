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
    }

    public String getWebDir() {
        return new File(mRootDir, "1111web").getAbsolutePath();
    }

    public File getUploadFileFolder() {
        return new File(PathManager.getInstance().getWebDir(), "files");
    }
}