package com.krishu.finaceanomoly.Controller;

import com.krishu.finaceanomoly.DTO.*;
import com.krishu.finaceanomoly.RuleType;
import com.krishu.finaceanomoly.Service.AdminService;
import com.krishu.finaceanomoly.Service.PolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final PolicyService policyService;

    public AdminController(AdminService adminService, PolicyService policyService) {
        this.adminService = adminService;
        this.policyService = policyService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request){
        return ResponseEntity.ok(adminService.createUser(request));
    }

    @PostMapping("/amountPolicy")
    public ResponseEntity<PolicyRuleResponse> createAmountPolicy(@RequestBody AmountPolicyRequest request){
        return ResponseEntity.ok(policyService.createAmountPolicy(request));
    }

    @PostMapping("/categoryLimitPolicy")
    public ResponseEntity<PolicyRuleResponse> createCategoryLimitPolicy(@RequestBody CategoryLimitPolicyRequest request){
        return ResponseEntity.ok(policyService.createCategoryLimitPolicy(request));
    }

    @PostMapping("duplicateWindowPolicy")
    public ResponseEntity<PolicyRuleResponse> createDuplicateWindowPolicy(@RequestBody DuplicateWindowPolicyRequest request){
        return ResponseEntity.ok(policyService.createDuplicateWindowPolicy(request));
    }

    @PatchMapping("/update/amountLimitPolicy/{policyId}")
    public ResponseEntity<PolicyRuleResponse> updateAmountPolicyByCategory(@RequestBody UpdateAmountLimitRequest request,@PathVariable UUID policyId){
        return ResponseEntity.ok(policyService.updateAmountPolicyByCategory(request,policyId));
    }

    @PatchMapping("/update/categoryLimit/{policyId}")
    public ResponseEntity<PolicyRuleResponse> updateCategoryLimitPolicy(@RequestBody UpdateCategoryLimitRequest request,@PathVariable UUID policyId){
        return ResponseEntity.ok(policyService.updateCategoryLimit(request,policyId));
    }

    @PatchMapping("/update/DuplicatePolicyWindow")
    public ResponseEntity<PolicyRuleResponse> updateDuplicateWindow(@RequestBody UpdateDuplicateWindowRequest request,UUID policyId){
        return ResponseEntity.ok(policyService.updateDuplicateWindow(request,policyId));
    }

    @PostMapping("/customPolicy")
    public ResponseEntity<PolicyRuleResponse> createCustomPolicy(@RequestBody CustomPolicyRequest request){
        return ResponseEntity.ok(policyService.createCustomPolicy(request));
    }

    @PatchMapping("/disableCustom/{policyId}")
    public ResponseEntity<PolicyRuleResponse> disableCustomPolicy(@PathVariable UUID policyId){
        return ResponseEntity.ok(policyService.disableCustomPolicy(policyId));
    }
    @GetMapping("/getPolicies")
    public ResponseEntity<List<PolicyRuleResponse>> getPolicies(){
        return ResponseEntity.ok(policyService.getPolicies());
    }

    @GetMapping("/policiesByStatus")
    public ResponseEntity<List<PolicyRuleResponse>> getByStatus(@RequestParam("status") Boolean status){
        return ResponseEntity.ok(policyService.getPoliciesByStatus(status));
    }

    @GetMapping("/policiesByType")
    public ResponseEntity<List<PolicyRuleResponse>> getByPolicyType(@RequestParam("type") RuleType type){
        return ResponseEntity.ok(policyService.getPoliciesByType(type));
    }
}
