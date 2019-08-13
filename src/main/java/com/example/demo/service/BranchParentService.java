package com.example.demo.service;

public interface BranchParentService {

    default String branchParent() {
        return "origin BranchParentService";
    }

}
