package com.aierx.gateway.rpc;

import com.aierx.gateway.model.client.ApiRequest;
import com.aierx.gateway.model.server.MethodModel;

import java.util.List;

public interface RpcInterface {

    Object invoke(String ip, int port, MethodModel method, ApiRequest apiRequest);
}
