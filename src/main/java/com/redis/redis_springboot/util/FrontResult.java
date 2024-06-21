
package com.redis.redis_springboot.util;

import com.redis.redis_springboot.enums.EnumClass;


public class FrontResult<T> {

    /**
     * 结果状态码
     */
    private Integer code;
    /**
     * 响应结果描述
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;


    public FrontResult(T data) {
        this.data = data;
        this.code = EnumClass.ResultEnum.SUCCESS.getCode();
        this.message = "操作成功";
    }

       /**
         * 获取一个异常结果
         *
         * @param code    错误码
         * @param message 自定义异常信息
         * @return FrontResult
         */
        public static FrontResult getExceptionResult(Integer code, String message) {
        FrontResult result = new FrontResult();
        result.code = (code == null) ? EnumClass.ResultEnum.CODE_EXCEPTION.getCode() : code;
        result.message = message.isEmpty() ? EnumClass.ResultEnum.CODE_EXCEPTION.getMsg() : message;
        return result;
          }

    /**
     * 静态方法，返回前端实体结果
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @return 前端实体结果
     */
    public static FrontResult build(Integer code, String message, Object data) {
        return new FrontResult(code, message, data);
    }

    /**
     * 返回成功的结果实体
     *
     * @param message 消息
     * @param data    数据
     * @return 实体
     */
    public static FrontResult getSuccessResult(String message, Object data) {
        FrontResult result = new FrontResult();
        result.code = EnumClass.ResultEnum.SUCCESS.getCode();
        result.message = message;
        result.data = data;
        return result;
    }

    /**
     * 返回无需data的成功结果实体
     *
     * @param message 消息内容
     * @return 返回结果
     */
    public static FrontResult getSuccessResultOnlyMessage(String message) {
        FrontResult result = new FrontResult();
        result.code = EnumClass.ResultEnum.SUCCESS.getCode();
        result.message = message;
        result.data = null;
        return result;
    }


    /****************************************************************/
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public FrontResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public FrontResult() {
    }

    @Override
    public String toString() {
        return "FrontResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}