package com.kktt.jesus.controller.viewobject;

public class WXSession {
//    {"session_key":"RnsiROIRLxEHpxbau2wuBg==","openid":"oprLr4q2GvqkBe5Idg_wONPbyz0g"}
    private String session_key;

    private String openid;

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
