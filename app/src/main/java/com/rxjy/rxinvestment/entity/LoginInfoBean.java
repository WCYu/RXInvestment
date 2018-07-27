package com.rxjy.rxinvestment.entity;

/**
 * Created by 阿禹 on 2018/7/19.
 */

public class LoginInfoBean {

    /**
     * StatusCode : 0
     * StatusMsg : 登陆成功！
     * Body : {"cardNo":"01011928","account":"13269721027","Token":"1C09E46709345D9AC431DAAB014270F2","appId":"bee75bf6-0e36-45fb-965c-9d88f555762e","is_group":"0"}
     */

    private int StatusCode;
    private String StatusMsg;
    private BodyBean Body;

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getStatusMsg() {
        return StatusMsg;
    }

    public void setStatusMsg(String StatusMsg) {
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
         * cardNo : 01011928
         * account : 13269721027
         * Token : 1C09E46709345D9AC431DAAB014270F2
         * appId : bee75bf6-0e36-45fb-965c-9d88f555762e
         * is_group : 0
         */

        private String cardNo;
        private String account;
        private String Token;
        private String appId;
        private String is_group;

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getIs_group() {
            return is_group;
        }

        public void setIs_group(String is_group) {
            this.is_group = is_group;
        }
    }
}
