package com.aierx.user.app;


public enum OperateType {
    REGISTER("register"),
    UPDATE("update");


    private String type;

    OperateType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
