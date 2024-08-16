package com.imooc.service;

import com.imooc.ApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = ApiApplication.class)
public class TransactionStuServiceTests {

    @Resource
    private ITransactionStuService transactionStuService;

    @Test
    public void testPropagation() {
//        transactionStuService.testPropagation();
    }
}
