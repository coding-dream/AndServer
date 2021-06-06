package com.yanzhenjie.andserver.sample.controller;

import android.text.TextUtils;

import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.ResponseBody;
import com.yanzhenjie.andserver.framework.body.FileBody;
import com.yanzhenjie.andserver.framework.body.StringBody;
import com.yanzhenjie.andserver.http.multipart.MultipartFile;
import com.yanzhenjie.andserver.sample.GlobalValueMananger;
import com.yanzhenjie.andserver.sample.model.EventBusMessageId;
import com.yanzhenjie.andserver.sample.model.EventWrapper;
import com.yanzhenjie.andserver.sample.util.FileUtils;
import com.yanzhenjie.andserver.sample.util.Logger;
import com.yanzhenjie.andserver.sample.website.PathManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

/**
 * Created by wl on 2019/7/4.
 */
@Controller
public class TxController {

    @PostMapping(path = "/upload")
    String upload(@RequestParam(name = "uploadFile") MultipartFile file) throws IOException {
        File folder = PathManager.getInstance().getUploadFileFolder();
        if (!folder.exists()) {
            boolean flag = folder.mkdirs();
            Logger.d("服务器目录创建结果: " + flag);
        }
        File localFile = FileUtils.createWebSiteFile(file);
        file.transferTo(localFile);
        Logger.d("Path: " + localFile.getAbsolutePath());
        EventBus.getDefault().post(new EventWrapper<>(EventBusMessageId.MSG_LIVEHALL_UPLOAD_FILE, localFile.getAbsolutePath()));
        return "redirect:/index.html";
    }

    @GetMapping(path = "/chat")
    String chat(@RequestParam(name = "message") String message) {
        Logger.i("message: " + message);
        EventBus.getDefault().post(new EventWrapper<>(EventBusMessageId.MSG_LIVEHALL_GET_MESSAGE, message));
        return "redirect:/index.html";
    }

    @GetMapping(path = "/chat_get")
    @ResponseBody
    StringBody chatGet() {
        if (TextUtils.isEmpty(GlobalValueMananger.getInstance().tempMessage)) {
            return new StringBody("暂无信息");
        }
        return new StringBody(GlobalValueMananger.getInstance().tempMessage);
    }

    /**
     * 直接返回XXBody，省略了AppMessageConverter转换的过程
     * @return
     */
    @ResponseBody
    @GetMapping(path = "/testNoConverter")
    StringBody testNoConverter() {
        Logger.d("testNoConverter");
        return new StringBody("testNoConverter");
    }

    @ResponseBody
    @GetMapping(path = "/download")
    public FileBody download(){
        Logger.d("download: ");
        File file = new File(PathManager.getInstance().getWebDir(), "1.mp4");
        return new FileBody(file);
    }

    @GetMapping(path = "/go_home")
    String goHome() {
        return "redirect:/index.html";
    }
}
