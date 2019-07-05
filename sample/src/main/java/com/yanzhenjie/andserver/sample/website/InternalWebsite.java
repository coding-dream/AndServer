package com.yanzhenjie.andserver.sample.website;

import com.yanzhenjie.andserver.annotation.Website;
import com.yanzhenjie.andserver.framework.website.StorageWebsite;

/**
 * Created by wl on 2019/7/4.
 */
@Website
public class InternalWebsite extends StorageWebsite {

    public InternalWebsite() {
        // 可以指定根目录和每个文件的默认首页叫什么名字：
        super(PathManager.getInstance().getWebDir(), "index.html");
    }
}