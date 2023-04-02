package com.aierx.user.app.http;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin

public class AdminController {

    @GetMapping("/heartbeat")
    public boolean heartbeat(){
        return true;
    }
}
