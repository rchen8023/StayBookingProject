package com.ruichen.staybooking.controller;

import com.ruichen.staybooking.model.User;
import com.ruichen.staybooking.model.UserRole;
import com.ruichen.staybooking.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//all method in RestCotroller contains response body (REST API) --> convert response in JSON and return to front end
@RestController
public class RegisterController {
    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    //PostMapping --> map RequestMethod in POST request
    //RequestBody --> get JSON string from request, use this annotation to convert JSON to User Object (Deserialize)
    @PostMapping("/register/guest")
    public void addGuest(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_GUEST);
    }

    @PostMapping("/register/host")
    public void addHost(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_HOST);
    }
}
