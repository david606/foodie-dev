package com.imooc.controller;


import com.google.common.base.Strings;
import com.imooc.bo.UsersBO;
import com.imooc.pojo.Users;
import com.imooc.service.IUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Api(value = "用户注册登录", tags = "注册登录相关接口")
@RestController
@RequestMapping("/passport")
public class PassportController {

    @Resource
    private IUserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "判断传入的用户名在数据库中是否存在", httpMethod = "GET")
    @GetMapping(value = "/usernameIsExists")
    public JSONResult usernameIsExists(@RequestParam("username") String username) {
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }

        boolean isExists = userService.queryUserNameIsExists(username);
        if (isExists) {
            return JSONResult.errorMsg("用户名已存在");
        }

        return JSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "校验用户名密码后注册用户信息", httpMethod = "POST")
    @PostMapping(value = "/register")
    public JSONResult register(@RequestBody UsersBO usersBO) {
        // 1. 判断用户名和密码不能为空
        if (StringUtils.isBlank(usersBO.getUsername()) || StringUtils.isBlank(usersBO.getPassword())) {
            return JSONResult.errorMsg("用户名或 密码不能为空");
        }
        // 2. 查询用户名是否存在
        if (userService.queryUserNameIsExists(usersBO.getUsername())) {
            return JSONResult.errorMsg("用户名已存在");
        }
        // 3. 密码长度不能少于6位
        if (usersBO.getPassword().length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于6位");
        }
        // 4. 判断确认密码是否一致
        if (!usersBO.getPassword().equals(usersBO.getConfirmPassword())) {
            return JSONResult.errorMsg("确认密码不一致");
        }

        // 5. 注册用户
        Users user = userService.createUser(usersBO);

        return JSONResult.ok(user);
    }


    /**
     * 用户登录接口
     * <p>
     * 该接口通过POST请求接收用户名和密码，完成用户登录流程。主要步骤包括验证用户名和密码，
     * 从数据库中查询用户信息，处理用户信息的安全性，以及在登录成功后更新cookie信息。
     * </p>
     *
     * @param usersBO  封装了用户登录信息（如用户名和密码）的对象
     * @param request  用于获取请求信息
     * @param response 用于设置响应信息
     * @return 返回登录结果，包括可能的错误信息或成功的用户信息
     * @throws Exception 可能抛出异常，例如数据库查询异常
     */
    @ApiOperation(value = "用户登录", notes = "根据用户名密码登录", httpMethod = "POST")
    @PostMapping(value = "/login")
    public JSONResult login(@RequestBody UsersBO usersBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 判断用户名和密码是否为空，因为这是登录所必需的
        if (StringUtils.isBlank(usersBO.getUsername()) || StringUtils.isBlank(usersBO.getPassword())) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        // 对用户输入的密码进行MD5加密，以匹配数据库中存储的加密密码
        String password = MD5Utils.getMD5Str(usersBO.getPassword());

        // 根据用户名和加密后的密码查询用户信息
        Users user = userService.queryUserForLogin(usersBO.getUsername(), password);

        // 判断查询到的用户是否存在
        if (user == null) {
            return JSONResult.errorMsg("用户名或密码不正确");
        }

        // 登录成功后，去掉用户信息中的敏感内容
        setNullProperty(user);

        // 将用户信息写入cookie，以便在后续的请求中识别用户
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);

        // 返回登录成功的消息，携带用户信息
        return JSONResult.ok(user);
    }

    private void setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
    }

    @ApiOperation(value = "用户退出 ", notes = "用户退出,清除相应用户数据", httpMethod = "POST")
    @PostMapping(value = "/logout")
    public JSONResult logout(@RequestParam("userId") String userId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        CookieUtils.deleteCookie(request, response, "user");
        return JSONResult.ok();
    }

}
