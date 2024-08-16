package com.imooc.service;

import com.imooc.bo.UsersBO;
import com.imooc.pojo.Users;

public interface IUserService {
    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean queryUserNameIsExists(String username);

    /**
     * 创建用户
     *
     * @param usersBO 创建参数
     * @return 返回用户信息，方便前端展示信息
     */
    Users createUser(UsersBO usersBO);

    /**
     * 登录
     * <br>
     * 根据用户查询用户信息，校验密码是否匹配
     *
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);
}
