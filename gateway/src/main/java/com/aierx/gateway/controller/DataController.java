package com.aierx.gateway.controller;

import com.aierx.gateway.model.server.AppModel;
import com.aierx.gateway.register.handler.RegisterHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    @GetMapping("/services.json")
    @CrossOrigin
    private List<AppModel> queryServices(){
        return RegisterHandler.appModels;
    }


}
