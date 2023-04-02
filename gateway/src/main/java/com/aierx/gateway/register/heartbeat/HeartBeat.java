package com.aierx.gateway.register.heartbeat;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.aierx.gateway.model.server.AppModel;
import com.aierx.gateway.model.server.MachineModel;
import com.aierx.gateway.register.handler.RegisterHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component("ignore-heartBeat")
@Slf4j
public class HeartBeat implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    if (RegisterHandler.appModels.size()>0){
                        for (AppModel appModel : RegisterHandler.appModels) {
                            Iterator<MachineModel> iterator = appModel.getMachineModels().iterator();
                            while (iterator.hasNext()){
                                MachineModel model = iterator.next();
                                String url = String.format("http://%s:%s/heartbeat",model.getIp(),model.getPort());
                                try {
                                    HttpUtil.get(url);
                                }catch (HttpException e){
                                    log.error("{}:{} not online." ,model.getIp(),model.getPort());
                                    iterator.remove();
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
