package com.aierx.user.app.http;

import com.aierx.user.app.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin

public class BookController {

    @GetMapping("/book")
    public List<User> queryAll(){
        return Collections.emptyList();
    }
}
