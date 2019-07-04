package com.yanzhenjie.andserver.sample.model;

public interface EventBusMessageId {

    int MSG_LIVEHALL_BASE = 0x1000;
    int MSG_LIVEHALL_GET_BANNER_SUCCESS         = MSG_LIVEHALL_BASE + 1;
    int MSG_LIVEHALL_GET_LIST_SUCCESS           = MSG_LIVEHALL_BASE + 2;
    int MSG_LIVEHALL_GET_MESSAGE           = MSG_LIVEHALL_BASE + 3;
    int MSG_LIVEHALL_UPLOAD_FILE           = MSG_LIVEHALL_BASE + 4;
}
