package com.example.demo.service.Impl;

import com.example.demo.service.BranchService;
import org.springframework.stereotype.Service;

/**
 * @author chen_bq
 * @description
 * @create: 2019/8/13 16:56
 **/
@Service
public class Branch1ServiceImpl implements BranchService {

    @Override
    public String getBranch() {
        return "this is brach1";
    }

    @Override
    public String branchParent() {
        return "extends BranchParentService";
    }

}
