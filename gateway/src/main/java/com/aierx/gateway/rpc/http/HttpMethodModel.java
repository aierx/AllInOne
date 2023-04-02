package com.aierx.gateway.rpc.http;

import com.aierx.gateway.model.server.MethodModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.lang.invoke.MethodHandle;
import java.util.List;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
public class HttpMethodModel extends MethodModel {


    private String url;

    private String requestType;

    private List<String> params;
    private List<String> pathParams;

    private boolean requestBody = false;
}
