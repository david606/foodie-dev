package com.imooc.service.impl;

import com.imooc.service.IStuService;
import com.imooc.service.ITransactionStuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class TransactionStuServiceImpl implements ITransactionStuService {

    @Resource
    private IStuService stuService;


    @SuppressWarnings("all")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void testPropagation() {
        stuService.saveParent();

        stuService.saveChildren();
        /*try {
            stuService.saveChildren();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}