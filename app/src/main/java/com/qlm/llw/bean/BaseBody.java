package com.qlm.llw.bean;

import java.io.Serializable;

/**
 * Created by c4542 on 2020/11/22.
 */

public class BaseBody implements Serializable {

    /**
     * display_type : custom
     * body : {"custom":"14"}
     * msg_id : umcimgx160604885120011
     */

    private String display_type;
    private BodyBean body;
    private String msg_id;
    private OperationBean extra;

    public OperationBean getExtra() {
        return extra;
    }

    public void setExtra(OperationBean extra) {
        this.extra = extra;
    }

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public static class BodyBean {

    }
}
