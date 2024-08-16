package com.imooc.service.impl;

import com.imooc.bo.UsersBO;
import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.IUserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private Sid sid;

    private static final String FACE_IMAGE = "https://img.alicdn.com/imgextra/i2/O1CN01tTGNxM25zwAC9ZSAA_!!6000000007598-2-tps-400-400.png";
    /**
     * 默认生日日期，默认为1990-01-01(行业默认 )
     */
    private static final String DEFAULT_BIRTHDAY = "1990-01-01";

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUserNameIsExists(String userName) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", userName);
        Users result = usersMapper.selectOneByExample(example);

        return result != null;
    }

    @Override
    public Users createUser(UsersBO usersBO) {
        Users user = new Users();
        user.setUsername(usersBO.getUsername());

        try {
            user.setPassword(MD5Utils.getMD5Str(usersBO.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("密码生成错误！");
        }

        String userId = sid.nextShort();
        user.setId(userId);

        user.setNickname(usersBO.getUsername());
        user.setFace(FACE_IMAGE);
        user.setBirthday(DateUtil.stringToDate(DEFAULT_BIRTHDAY));
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);

        return usersMapper.selectOneByExample(example);
    }
}
