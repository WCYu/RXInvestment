package com.rxjy.rxinvestment.entity;

/**
 * Created by hjh on 2018/3/12.
 */

public class MsgNumBean {

    /**
     * StatusCode : 0
     * StatusMsg : null
     * Body : {"count":0}
     */

    private int StatusCode;
    private Object StatusMsg;
    private BodyBean Body;

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int StatusCode) {
        this.StatusCode = StatusCode;
    }

    public Object getStatusMsg() {
        return StatusMsg;
    }

    public void setStatusMsg(Object StatusMsg) {
        this.StatusMsg = StatusMsg;
    }

    public BodyBean getBody() {
        return Body;
    }

    public void setBody(BodyBean Body) {
        this.Body = Body;
    }

    public static class BodyBean {
        /**
         * count : 0
         */

        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
