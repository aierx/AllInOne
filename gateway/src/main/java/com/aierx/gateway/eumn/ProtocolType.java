package com.aierx.gateway.eumn;

public enum ProtocolType {
    GRPC("grpc"),
    DUBBO("dubbo"),
    HTTP("http"),
    THRIFT("thrift");

    private String name;

    ProtocolType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
