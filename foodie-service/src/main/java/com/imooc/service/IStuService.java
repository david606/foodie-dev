package com.imooc.service;

import com.imooc.pojo.Stu;

public interface IStuService {
    public Stu getStu(int id);

    public void saveStu(Stu stu);

    public void updateStu(Stu stu);

    public void delStu(int id);

    public void saveParent();

    public void saveChildren();
}
