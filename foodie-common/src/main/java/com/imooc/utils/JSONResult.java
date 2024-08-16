package com.imooc.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 自定义响应数据结构
 * 本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * <pre>
 * 前端接收到此类数据（json object)后，可自行根据业务去实现相关功能
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 * 501：Bean验证错误，校验多个错误（不管多少个错误）都以map形式返回
 * 502：拦截器拦截到用户token出错
 * 555：异常抛出信息
 * 556: 用户qq校验异常
 * @author David
 * @version V1.0
 */
@Data
public class JSONResult implements Serializable {

    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应中的数据
     */
    private Object data;

    /**
     * 附加信息（暂未使用 ）
     */
    @JsonIgnore
    private String ok;

    /**
     * 构建并返回一个JSONResult对象
     * 此方法提供了一种创建JSONResult对象的标准化方式，通过指定状态码、消息和数据，
     * 来生成一个JSONResult实例，这对于API响应的格式化非常有用
     *
     * @param status 状态码，用于标识操作的状态，例如成功或失败等
     * @param msg    消息字符串，提供状态的详细信息
     * @param data   任意类型的数据对象，通常用于存储和传输业务数据
     * @return 返回一个构建好的JSONResult对象，包含传入的状态码、消息和数据
     */
    public static JSONResult build(Integer status, String msg, Object data) {
        return new JSONResult(status, msg, data);
    }

    /**
     * 构建并返回一个JSONResult对象
     * 此方法提供灵活性，允许开发者根据需要自定义返回的JSONResult对象
     *
     * @param status 状态码，用于标识操作是否成功，例如200表示成功
     * @param msg    消息字符串，用于进一步描述状态或操作结果
     * @param data   数据对象，可以是任何类型的对象，用于返回具体的操作结果数据
     * @param ok     一个额外的字段，用于指定成功时的附加信息或操作
     * @return 返回构建的JSONResult对象，该对象包含提供的状态码、消息、数据和成功信息
     */
    public static JSONResult build(Integer status, String msg, Object data, String ok) {
        return new JSONResult(status, msg, data, ok);
    }


    public static JSONResult ok(Object data) {
        return new JSONResult(data);
    }

    public static JSONResult ok() {
        return new JSONResult(null);
    }

    /**
     * 创建一个包含错误信息的JSONResult对象
     * 此方法用于生成一个表示错误的JSONResult对象，通常用于向前端反馈错误信息
     *
     * @param msg 错误信息，用于详细描述发生了什么错误
     * @return 返回一个JSONResult对象，其中包含错误码（500）、错误信息（msg）和空的数据部分（null）
     */
    public static JSONResult errorMsg(String msg) {
        return new JSONResult(500, msg, null);
    }


    /**
     * 构造一个包含错误信息的JSON结果对象
     * 该方法用于创建一个包含自定义错误数据的JSON结果对象，通常用于返回操作失败的响应
     *
     * @param data 错误数据，可以是错误消息或其他错误信息
     * @return 返回一个构造好的JSON结果对象，其中包含了错误代码、错误信息和错误数据(Map)
     */
    public static JSONResult errorMap(Object data) {
        return new JSONResult(501, "error", data);
    }


    public static JSONResult errorTokenMsg(String msg) {
        return new JSONResult(502, msg, null);
    }

    public static JSONResult errorException(String msg) {
        return new JSONResult(555, msg, null);
    }

    public static JSONResult errorUserQQ(String msg) {
        return new JSONResult(556, msg, null);
    }

    public JSONResult() {

    }

    public JSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JSONResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public JSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }
}
