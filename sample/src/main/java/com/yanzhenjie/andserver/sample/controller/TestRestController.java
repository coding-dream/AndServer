package com.yanzhenjie.andserver.sample.controller;

import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RestController;

/**
 * Created by wl on 2019/7/4.
 */
@RestController
public class TestRestController {

    /**
     * 返回：{"data":"testRestController","errorCode":200,"isSuccess":true}
     *
     * @return
     */
    @GetMapping(path = "/testRestController")
    public String testRestController() {
        return "testRestController";
    }
}