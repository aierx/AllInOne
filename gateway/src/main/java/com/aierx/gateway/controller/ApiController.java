package com.aierx.gateway.controller;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.aierx.gateway.model.client.ApiRequest;
import com.aierx.gateway.model.common.CommonResponse;
import com.aierx.gateway.model.server.AppModel;
import com.aierx.gateway.model.server.MachineModel;
import com.aierx.gateway.model.server.MethodModel;
import com.aierx.gateway.model.server.ServiceModel;
import com.aierx.gateway.register.handler.RegisterHandler;
import com.aierx.gateway.rpc.http.HttpHttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ApiController {

    ObjectMapper json = new JsonMapper();

    @Autowired
    SnowflakeGenerator snowflakeGenerator;


    @Autowired
    HttpHttpHandler adapter;

    @SneakyThrows
    @PostMapping("/api")
    @CrossOrigin
    public CommonResponse invoke(@RequestBody ApiRequest apiRequest) {
        String methodName = apiRequest.getMethodName();
        log.info(json.writeValueAsString(apiRequest));
        Long id = snowflakeGenerator.next();
        for (AppModel e : RegisterHandler.appModels) {
            if (e.getAppkey().equals(apiRequest.getAppkey()) ) {
                MachineModel machineModel = e.getMachineModels().get(0);
                for (ServiceModel model : machineModel.getServiceModelList()) {
                    if (model.getClassName().equals(apiRequest.getClassName())
                            && machineModel.getSlimline().equals(apiRequest.getSlimline())) {
                        for (MethodModel method : model.getMethodModels()) {
                            if (method.getMethodName().equals(methodName)) {
                                boolean match = paramEqual(apiRequest.getParamTypes(), method.getParamTypes());
                                if (match) {
                                    Object data = adapter.invoke
                                            (machineModel.getIp(), machineModel.getPort(), method, apiRequest);
                                    CommonResponse res = new CommonResponse(200,id ,"success", data);
                                    return res;
                                }
                            }
                        }
                    }
                }
            }
        }
        return new CommonResponse(400,id, "no method provided", null);
    }

    private boolean paramEqual(List<String> paramTypes, List<Object> existParamTypes) {

        for (int i = 0; i < paramTypes.size(); i++) {
            if (!paramTypes.get(i).equals(existParamTypes.get(i).toString())) {
                return false;
            }
        }
        return true;

    }
}
