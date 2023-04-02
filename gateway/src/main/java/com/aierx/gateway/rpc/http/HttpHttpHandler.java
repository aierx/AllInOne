package com.aierx.gateway.rpc.http;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.aierx.gateway.model.client.ApiRequest;
import com.aierx.gateway.model.server.MethodModel;
import com.aierx.gateway.rpc.RpcInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HttpHttpHandler implements RpcInterface {

    ObjectMapper json = new ObjectMapper();

    @SneakyThrows
    @Override
    public Object invoke(String ip, int port, MethodModel httpMethod, ApiRequest apiRequest) {
        HttpMethodModel model = (HttpMethodModel) httpMethod;
        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < model.getParams().size(); i++) {
            map.put(model.getParams().get(i), apiRequest.getParamValues().get(i));
        }
        String url = model.getUrl().split(";")[0];
        // path参数
        for (int i = 0; i < apiRequest.getPathParams().size(); i++) {
            url = url.replaceFirst("\\{[\\s\\S]*?\\}", apiRequest.getParamValues().get(i));
        }
        String completeUrl = String.format("http://%s:%s%s", ip, port, url);
        String s;
        if (model.getRequestType().equals("post") && model.isRequestBody()) {
            s = HttpUtil.post(completeUrl, apiRequest.getParamValues().get(0));
        } else {
            s = HttpUtil.get(completeUrl, map);
        }
        return s;
    }
}
