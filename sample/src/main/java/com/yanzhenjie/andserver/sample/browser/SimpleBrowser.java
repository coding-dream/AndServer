package com.yanzhenjie.andserver.sample.browser;

import com.yanzhenjie.andserver.annotation.Website;
import com.yanzhenjie.andserver.framework.website.FileBrowser;
import com.yanzhenjie.andserver.sample.website.PathManager;

/**
 * Created by wl on 2019/7/4.
 *
 * FileBrowser比较简单，它可以以列表的形式展示开发者指定目录的文件和目录，并可以点击下载列表中的文件和查看列表中的目录的内容。
 */
@Website
public class SimpleBrowser extends FileBrowser {

    public SimpleBrowser() {
        super(PathManager.getInstance().getWebDir());
    }
}