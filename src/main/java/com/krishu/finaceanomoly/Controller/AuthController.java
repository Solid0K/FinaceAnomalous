package com.krishu.finaceanomoly.Controller;

import com.krishu.finaceanomoly.DTO.LoginRequest;
import com.krishu.finaceanomoly.DTO.RegisterRequest;
import com.krishu.finaceanomoly.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody RegisterRequest request){
        authService.clientSignUp(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.clientSignIn(request));
    }
}
