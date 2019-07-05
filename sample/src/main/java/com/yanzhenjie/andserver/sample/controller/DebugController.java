package com.yanzhenjie.andserver.sample.controller;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.ResponseBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.RequestBody;
import com.yanzhenjie.andserver.http.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by wl on 2019/7/4.
 */
@Controller
@RequestMapping(path = "/test")
public class DebugController {

    private static final String TAG = DebugController.class.getSimpleName();

    /**
     * 无返回值示例
     * url: /test/info
     * @param request
     * @param response
     */
    @GetMapping("/info")
    public void info(HttpRequest request, HttpResponse response) {
        Log.d(TAG, "request:" + request);
    }

    /**
     * 参数
     * url: /test/info
     * key=value，规定了某key必须等于某value，例如：param = "name=123"。
     * key!=value，规定了某key必须不等于某value，例如：param = "name!=123"。
     * key，规定了参数中必须有某key，且值不能为空，例如：param = "name"。
     * !key，规定了参数中必须不能由某key，例如：param = "!name"。
     *
     * 注意：客户端必须提交account和parssword参数，否则将会抛出ParamMissingException异常。或：
     * 我们也可以让某参数不是必填的，同时也可以选择给它一个默认值。
     */
    @GetMapping(path = "/info", params = {"name!=123", "password"})
    public void info(@RequestParam(value = "name", required = false, defaultValue = "jack") String name, @RequestParam("password") String password) {
        Log.d(TAG, "request:" + name + " " + password);
    }

    /**
     * 接收文件实例
     */
    @GetMapping(path = "/info", params = {"name!=123", "password"})
    public String upload(@RequestParam(value = "image") MultipartFile file) throws IOException {
        File localFile = new File(Environment.getExternalStorageDirectory(), "11upload");
        file.transferTo(localFile); // Transfer the file to the target location.
        return localFile.getAbsolutePath();
    }

    /**
     * 无需注解的参数(可选参数)
     */
    @GetMapping("/project/get")
    public void get(Context context, RequestBody requestBody, HttpRequest request, HttpResponse response) {

    }

    /**
     * consumes示例：Consume单词的字面意思是消耗、消费，在转换到程序中来就是说：你能消费什么？你能处理什么？因此它适合用于校验客户端的Content-Type头，Contnet-Type的意思是内容类型，因此consume的含义就是能消费什么内容了。
     * consume的语法有两种：
     *
     * 1. application/json，规定了客户端提交的Content-Type须是JSON格式。
     * 2. !application/xml，规定了客户端提交的Content-Type不能是XML格式。
     *
     * 如果客户端的请求违反约束，则会抛出ContentNotSupportedException异常，异常处理请参考ExceptionResolver。
     */
    @PostMapping(path = "/info", consumes = "application/json")
    public void info() {

    }

    /**
     * produces 示例
     *
     * produces的语法和consumes完全一致，只是它用来规定客户端的Accept头，而consume用来规定客户端的Content-Type头。
     * 与consume不同的是，它在服务端不支持*，但是它支持客户端的*。因为对于Content-Type来说，客户端上行或者服务端下行时内容类型都是明确的，因此服务端校验Content-Type时可以用非明确的值去做包含。
     * 而对于Accept，因为不知道服务端下发的Content-Type，所以客户端可以用非明确的值做包含，因此客户端的Accept值有可能是*\/*
     *
     * 下述示例中，如果客户端的Accept是*\/*或者application/json就可以校验通过。
     *
     * 特别注意，produce的值会作为服务端响应消息的Content-Type发送到客户端。
     */
    @PostMapping(path = "/info", produces = "application/json")
    public void testProduce() {

    }

    /**
     * 直接使用返回值作为响应:
     * {"data":"Hello","errorCode":200,"isSuccess":true}
     * 注意：如果开发者使用了MessageConverter，那么有ResponseBody注解的返回值还会经过MessageConverter转换。
     */
    @ResponseBody
    @GetMapping("/testResponseBody")
    public String testResponseBody() {
        return "Hello";
    }
}
