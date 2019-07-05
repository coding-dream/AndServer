package com.yanzhenjie.andserver.sample.controller;

import android.util.Log;

import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.ResponseBody;

/**
 * Created by wl on 2019/7/4.
 */
@Controller
public class TestUnRestController {
    /**
     * 对比TestRestController，方法可以处理，但是无响应。
     * @return
     */
    @GetMapping(path = "/testUnRestController1")
    public String testUnRestController1() {
        Log.d("TestUnRestController", "testUnRestController1");
        return "testUnRestController";
    }

    /**
     * 等价于 {@link TestRestController#testRestController()}
     * @return
     */
    @ResponseBody
    @GetMapping(path = "/testUnRestController2")
    public String testUnRestController2() {
        Log.d("TestUnRestController", "testUnRestController2");
        return "testUnRestController";
    }
}