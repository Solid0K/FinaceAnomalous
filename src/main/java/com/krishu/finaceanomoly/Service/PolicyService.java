package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.CustomException.NotFoundException;
import com.krishu.finaceanomoly.CustomException.PolicyConflictException;
import com.krishu.finaceanomoly.CustomException.PolicyTypeConflictException;
import com.krishu.finaceanomoly.DTO.*;
import com.krishu.finaceanomoly.Model.PolicyRule;
import com.krishu.finaceanomoly.Repository.PolicyRuleRepo;
import com.krishu.finaceanomoly.RuleType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PolicyService {

    private final PolicyRuleRepo policyRepo;

    public PolicyService(PolicyRuleRepo policyRepo) {
        this.policyRepo = policyRepo;
    }

    public PolicyRuleResponse createAmountPolicy(AmountPolicyRequest request) {
        if(policyRepo.existsByTypeAndCategoryAndActive(RuleType.AMOUNT_LIMIT,request.category(),true)){
            throw new PolicyConflictException("Policy for this category already exists");
        }
        PolicyRule policy=new PolicyRule();
        policy.setAmountThreshold(request.amountThreshold());
        policy.setCategory(request.category());
        policy.setActive(true);
        policy.setType(RuleType.AMOUNT_LIMIT);
        PolicyRule savedPolicy=policyRepo.save(policy);
        return PolicyRuleResponse.from(savedPolicy);
    }

    public PolicyRuleResponse createCategoryLimitPolicy(CategoryLimitPolicyRequest request) {
        if(policyRepo.existsByTypeAndCategoryAndActive(RuleType.CATEGORY_LIMIT,request.category(),true)){
            throw new PolicyConflictException("Policy for this category already exists");
        }
        PolicyRule policy=new PolicyRule();
        policy.setType(RuleType.CATEGORY_LIMIT);
        policy.setCategoryThreshold(request.categoryLimit());
        policy.setPeriod(request.period());
        policy.setCategory(request.category());
        policy.setActive(true);
        PolicyRule savedPolicy=policyRepo.save(policy);
        return PolicyRuleResponse.from(savedPolicy);
    }

    public PolicyRuleResponse createDuplicateWindowPolicy(DuplicateWindowPolicyRequest request) {
        PolicyRule policy=new PolicyRule();
        policy.setType(RuleType.DUPLICATE_CHECK);
        policy.setDuplicateWindowDay(request.duplicateWindowDay());
        policy.setActive(true);
        PolicyRule savedPolicy=policyRepo.save(policy);
        return PolicyRuleResponse.from(savedPolicy);
    }

    public PolicyRuleResponse updateAmountPolicyByCategory(UpdateAmountLimitRequest request, UUID policyId) {
        PolicyRule policy=policyRepo.findById(policyId).orElseThrow(()->new NotFoundException("Policy Not Found"));
        if(!policy.getType().equals(RuleType.AMOUNT_LIMIT)){
            throw new PolicyTypeConflictException("Policy Type Conflict");
        }
        policy.setAmountThreshold(request.newAmountThreshold());
        PolicyRule savedPolicy=policyRepo.save(policy);
        return PolicyRuleResponse.from(savedPolicy);
    }

    public PolicyRuleResponse updateCategoryLimit(UpdateCategoryLimitRequest request, UUID policyId) {
        PolicyRule policy=policyRepo.findById(policyId).orElseThrow(()->new NotFoundException("Policy not found"));
        if(!policy.getType().equals(RuleType.CATEGORY_LIMIT)){
            throw new PolicyTypeConflictException("Policy Type Conflict");
        }
        policy.setCategoryThreshold(request.newCategoryLimit());
        policy.setPeriod(request.updatePeriod());
        PolicyRule savedPolicy=policyRepo.save(policy);
        return PolicyRuleResponse.from(savedPolicy);
    }

    public PolicyRuleResponse updateDuplicateWindow(UpdateDuplicateWindowRequest request, UUID policyId) {
        PolicyRule policy=policyRepo.findById(policyId).orElseThrow(()->new NotFoundException("Policy not found"));
        if(!policy.getType().equals(RuleType.DUPLICATE_CHECK)){
            throw new PolicyTypeConflictException("Policy Type Conflict");
        }
        policy.setDuplicateWindowDay(request.updatedDuplicateWindow());
        PolicyRule savedPolicy=policyRepo.save(policy);
        return PolicyRuleResponse.from(savedPolicy);
    }

    public PolicyRuleResponse createCustomPolicy(CustomPolicyRequest request) {
        PolicyRule customPolicy=new PolicyRule();
        customPolicy.setType(RuleType.CUSTOM);
        customPolicy.setRuleText(request.ruleText());
        customPolicy.setActive(true);
        PolicyRule savedCustomPolicy=policyRepo.save(customPolicy);
        return PolicyRuleResponse.from(savedCustomPolicy);
    }

    public PolicyRuleResponse disableCustomPolicy(UUID policyId) {
        PolicyRule policy=policyRepo.findById(policyId).orElseThrow(()->new NotFoundException("Policy Not Found"));
        if(!policy.getType().equals(RuleType.CUSTOM)){
            throw new PolicyTypeConflictException("Policy Type Conflict");
        }
        policy.setActive(false);
        PolicyRule savedPolicy=policyRepo.save(policy);
        return PolicyRuleResponse.from(savedPolicy);
    }

    public List<PolicyRuleResponse> getPolicies() {
        List<PolicyRule> policies=policyRepo.findAll();
        return policies.stream().map(PolicyRuleResponse::from).toList();
    }

    public List<PolicyRuleResponse> getPoliciesByStatus(Boolean status) {
        List<PolicyRule> policies=policyRepo.findByActive(status);
        return policies.stream().map(PolicyRuleResponse::from).toList();
    }

    public List<PolicyRuleResponse> getPoliciesByType(RuleType type) {
        List<PolicyRule> policies=policyRepo.findByType(type);
        return policies.stream().map(PolicyRuleResponse::from).toList();
    }
}
