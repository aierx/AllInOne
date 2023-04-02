package com.aierx.user.app.http;

import com.aierx.user.app.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    List<User> users = new ArrayList<>();


    @PostMapping("/save")
    public boolean saveUser(@RequestBody User user){

        return users.add(user);
    }

    @GetMapping("/qeury")
    public List<User> queryUser(@RequestParam("id")int id,@RequestParam("name")String name){
        System.out.println(id);
        return users;
    }

    @GetMapping("/qeury/{id}/{name}")
    public List<User> queryUserById(@PathVariable("id")int id,@PathVariable("name")String name){
        System.out.println(id);
        System.out.println(name);
        return users;
    }
}
