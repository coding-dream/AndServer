package com.yanzhenjie.andserver.sample.model;

/**
 * EventBus事件中心
 */
public class EventWrapper<T> {

    /**
     * reserved data
     */
    private T data;

    /**
     * this code distinguish between different events
     */
    private int eventCode = -1;

    public int arg1;
    public int arg2;

    //请求标识码（根据需求设置）
    private String requestId;

    public EventWrapper(int eventCode) {
        this(eventCode, null, 0, 0);
    }

    public EventWrapper(int eventCode, T data) {
        this(eventCode, data, 0, 0);
    }

    public EventWrapper(int eventCode, T data, int arg1) {
        this(eventCode, data, arg1, 0);
    }

    public EventWrapper(int eventCode, T data, int arg1, int arg2) {
        this.eventCode = eventCode;
        this.data = data;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public EventWrapper(int eventCode, T data, String reqId) {
        this.eventCode = eventCode;
        this.data = data;
        this.requestId = reqId;
    }

    /**
     * get event code
     *
     * @return
     */
    public int getEventCode() {
        return this.eventCode;
    }

    /**
     * get event reserved data
     *
     * @return
     */
    public T getData() {
        return this.data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

