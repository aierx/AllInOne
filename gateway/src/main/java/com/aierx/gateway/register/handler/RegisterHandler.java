package com.aierx.gateway.register.handler;

import com.aierx.gateway.model.server.AppModel;
import com.aierx.gateway.model.server.MachineModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RegisterHandler extends ChannelInboundHandlerAdapter {

    static public List<AppModel> appModels = new ArrayList<>();

    ObjectMapper json = new ObjectMapper();

    ConcurrentHashMap<String,List<String>> hashMap = new ConcurrentHashMap();

    /**
     * channelRead
     * 读写数据核心方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1. 读取客户端的数据(缓存中去取并打印到控制台)
        ByteBuf buf = (ByteBuf) msg;
        byte[] request = new byte[buf.readableBytes()];
        buf.readBytes(request);
        String requestBody = new String(request, StandardCharsets.UTF_8);
        String key = requestBody.substring(0, 13);
        String data = requestBody.substring(13);
        if (requestBody.endsWith("[end]")){
            data = data.substring(0,data.length()-5);
            if (hashMap.get(key)!=null){
               data = hashMap.get(key).stream().collect(Collectors.joining())+data;
               hashMap.remove(key);
            }
            //2. 返回响应数据
            AppModel curApp = json.readValue(data, AppModel.class);
            MachineModel curMachine = curApp.getMachineModels().get(0);
            curMachine.setIp(((InetSocketAddress) ctx.channel().remoteAddress()).getHostString());
            for (AppModel app : appModels) {
                if (app.getAppkey().equals(curApp.getAppkey())) {
                    for (MachineModel machine : app.getMachineModels()) {
                        if (machine.getIp().equals(curMachine.getIp())
                                && machine.getSlimline().equals(curApp.getMachineModels().get(0).getSlimline())
                                && machine.getPort() == curMachine.getPort()) {
                            machine.setServiceModelList(curMachine.getServiceModelList());
                            curMachine.setServiceModelList(null);
                        }
                    }
                    if (curMachine.getServiceModelList() != null) {
                        app.getMachineModels().add(curMachine);
                    }
                    curApp = null;
                    break;
                }
            }
            if (curApp != null) {
                appModels.add(curApp);
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("registered".getBytes()));
        }else {
            if (hashMap.get(key)!=null){
                hashMap.get(key).add(data);
            }else {
                List<String> list = new ArrayList<>();
                list.add(data);
                hashMap.put(key,list);
            }
        }
    }
}
