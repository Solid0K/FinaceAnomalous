package com.krishu.finaceanomoly.Controller;

import com.krishu.finaceanomoly.DTO.CreateUserRequest;
import com.krishu.finaceanomoly.DTO.UserResponse;
import com.krishu.finaceanomoly.Service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request){
        return ResponseEntity.ok(adminService.createUser(request));
    }
}
