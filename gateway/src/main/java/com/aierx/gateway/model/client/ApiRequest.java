package com.aierx.gateway.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequest {

    private String methodName;

    private String className;

    private String appkey;

    private String slimline;

    private List<String> paramTypes;

    private List<String> paramValues;

    private List<String> pathParams;

}
