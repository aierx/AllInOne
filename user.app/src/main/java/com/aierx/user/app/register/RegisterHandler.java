package com.aierx.user.app.register;


import com.aierx.gateway.model.server.AppModel;
import com.aierx.gateway.model.server.MachineModel;
import com.aierx.gateway.model.server.MethodModel;
import com.aierx.gateway.model.server.ServiceModel;
import com.aierx.gateway.rpc.http.HttpMethodModel;
import com.aierx.user.app.http.AdminController;
import com.aierx.user.app.http.BookController;
import com.aierx.user.app.http.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class RegisterHandler implements InitializingBean {

    static List<ServiceModel> serviceModelArrayList = new ArrayList<>();

    static {
        List<Class> classes = Arrays.asList(UserController.class, AdminController.class, BookController.class);
        for (Class e : classes) {
            ServiceModel serviceModel = new ServiceModel();
            serviceModel.setProtocolType("http");
            List<MethodModel> list = new ArrayList<>();
            RestController annotation = AnnotationUtils.findAnnotation(e, RestController.class);
            if (annotation == null) {
                continue;
            }
            String prefixUrl = annotation.value().startsWith("/") ? annotation.value() : "/" + annotation.value();
            prefixUrl = prefixUrl.endsWith("/") ? prefixUrl.substring(0, prefixUrl.length() - 1) : prefixUrl;
            for (Method method : e.getDeclaredMethods()) {
                serviceModel.setMethodModels(list);
                serviceModel.setClassName(e.getName());
                Annotation[] annotations = method.getDeclaredAnnotations();
                HttpMethodModel model = new HttpMethodModel();
                model.setType("http");
                model.setMethodName(e.getName() + "." + method.getName());
                for (Annotation methodAnnon : annotations) {
                    if (methodAnnon.annotationType() == GetMapping.class) {
                        GetMapping getMapping = (GetMapping) methodAnnon;
                        List<String> params = new ArrayList<>();
                        String url = "";
                        for (String s : getMapping.value()) {
                            url = url + prefixUrl + (s.startsWith("/") ? s : "/" + s) + ";";
                        }
                        model.setUrl(url);
                        model.setRequestType("get");
                        List<String> pathParams = new ArrayList<>();

                        for (Annotation[] parameterAnnotation : method.getParameterAnnotations()) {
                            for (Annotation paramAnnon : parameterAnnotation) {
                                if (paramAnnon.annotationType() == RequestParam.class) {
                                    RequestParam requestParam = (RequestParam) paramAnnon;
                                    params.add(requestParam.value());
                                }
                                if (paramAnnon.annotationType() == PathVariable.class) {
                                    PathVariable pathVariable = (PathVariable) paramAnnon;
                                    pathParams.add(pathVariable.value());
                                }
                            }
                        }
                        model.setParams(params);
                        model.setPathParams(pathParams);
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        model.setParamTypes(Arrays.asList(parameterTypes));
                        list.add(model);
                        break;

                    } else if (methodAnnon.annotationType() == PostMapping.class) {
                        PostMapping postMapping = (PostMapping) methodAnnon;
                        String url = "";
                        for (String s : postMapping.value()) {
                            url = url + prefixUrl + (s.startsWith("/") ? s : "/" + s) + ";";
                        }
                        model.setUrl(url);
                        model.setRequestType("post");
                        List<String> params = new ArrayList<>();
                        List<String> pathParams = new ArrayList<>();
                        for (Annotation[] parameterAnnotation : method.getParameterAnnotations()) {
                            for (Annotation paramAnnon : parameterAnnotation) {
                                if (paramAnnon.annotationType() == RequestParam.class) {
                                    RequestParam requestParam = (RequestParam) paramAnnon;
                                    params.add(requestParam.value());
                                }
                                if (paramAnnon.annotationType() == RequestBody.class) {
                                    model.setRequestBody(true);
                                }
                                if (paramAnnon.annotationType() == PathVariable.class) {
                                    PathVariable pathVariable = (PathVariable) paramAnnon;
                                    pathParams.add(pathVariable.value());
                                }
                            }
                        }
                        model.setParams(params);
                        model.setPathParams(pathParams);
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        model.setParamTypes(Arrays.asList(parameterTypes));
                        list.add(model);
                    }
                }
            }
            serviceModelArrayList.add(serviceModel);
        }
    }

    @Autowired
    UserController userController;
    ObjectMapper json = new ObjectMapper();
    @Value("${server.port}")
    private int port;


    @Override
    public void afterPropertiesSet() throws Exception {
        Properties properties = readConfig();
        String appkey = properties.getProperty("appkey");
        String slimline = properties.getProperty("slimline");
        new Thread(() -> {
            while (true) {
                try {
                    AppModel appModel = new AppModel();
                    appModel.setAppkey(appkey);
                    MachineModel machineModel = new MachineModel();
                    machineModel.setPort(port);
                    machineModel.setServiceModelList(serviceModelArrayList);
                    machineModel.setSlimline(slimline);
                    appModel.setMachineModels(Arrays.asList(machineModel));
                    Socket socket = new Socket("127.0.0.1", 4200);
                    String s = json.writeValueAsString(appModel);
                    String data = "";
                    String key = String.valueOf(System.currentTimeMillis());
                    while (s.length()>1006){
                        data = key + s.substring(0,1011);
                        s = s.substring(Math.min(s.length(), 1011));
                    }
                    data=data+key+s+"[end]";
                    socket.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
                    byte[] bytes = new byte[1024];
                    int read = socket.getInputStream().read(bytes);
                    log.info(new String(bytes, 0, read));
                } catch (Exception e) {
                    log.error("connect fail");
                }
                try {
                    Thread.sleep(10000);
                }catch (Exception e){
                }
            }
        }).start();
    }

    private Properties readConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("appenv"));
        return properties;
    }
}
