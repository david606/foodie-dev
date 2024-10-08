package com.imooc.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor()
@ToString
public class Users {
    /**
     * 主键id;用户id
     */
    @Id
    private String id;

    /**
     * 用户名;用户名
     */
    private String username;

    /**
     * 密码
     * <br>
     * 敏感信息不显示给前端
     */
    private String password;

    /**
     * 昵称;昵称
     */
    private String nickname;

    /**
     * 真实姓名
     * 敏感信息不显示给前端
     */
    private String realname;

    /**
     * 头像;头像
     */
    private String face;

    /**
     * 手机号;手机号
     */
    private String mobile;

    /**
     * 邮箱地址;邮箱地址
     */
    private String email;

    /**
     * 性别;性别 1:男  0:女  2:保密
     */
    private Integer sex;

    /**
     * 生日;生日
     */
    private Date birthday;

    /**
     * 创建时间;创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间;更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;
}