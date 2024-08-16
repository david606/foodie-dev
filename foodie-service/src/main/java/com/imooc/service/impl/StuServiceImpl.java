package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import com.imooc.service.IStuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class StuServiceImpl implements IStuService {

    @Resource
    private StuMapper stuMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStu(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveStu(Stu stu) {
        stuMapper.insert(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStu(Stu stu) {
        stuMapper.updateByPrimaryKey(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delStu(int id) {
        stuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void saveParent() {
        stuMapper.insert(Stu.builder().name("Parent").age(29).build());
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void saveChildren() {
        saveChild1();
        int exp = 1 / 0;
        saveChild2();
    }

    private void saveChild1() {
        stuMapper.insert(Stu.builder().name("Child-01").age(19).build());
    }

    private void saveChild2() {
        stuMapper.insert(Stu.builder().name("Child-02").age(18).build());
    }
}
