package com.qlm.llw.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/11/23.
 */

public class OperationBean implements Serializable {

    private String type;
    private String operation;
   private String qlmcmd;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getQlmcmd() {
        return qlmcmd;
    }

    public void setQlmcmd(String qlmcmd) {
        this.qlmcmd = qlmcmd;
    }
}
