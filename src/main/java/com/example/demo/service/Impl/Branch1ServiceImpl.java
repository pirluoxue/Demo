package com.example.demo.service.Impl;

import com.example.demo.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author chen_bq
 * @description
 * @create: 2019/8/13 16:56
 **/
@Service
public class Branch1ServiceImpl implements BranchService {

    private static final Logger log = LoggerFactory.getLogger(Branch1ServiceImpl.class);

    @Override
    public String getBranch() {
        log.debug("debug test {} {} ", 123, 456);
        log.info("info test {} {} ", 123, 456);
        return "this is brach1";
    }

    @Override
    public String branchParent() {
        return "extends BranchParentService";
    }

}
