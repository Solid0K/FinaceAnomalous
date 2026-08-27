package com.krishu.finaceanomoly.Controller;

import com.krishu.finaceanomoly.Service.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }
}
