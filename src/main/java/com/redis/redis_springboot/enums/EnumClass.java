package com.redis.redis_springboot.enums;


public interface EnumClass {


     enum ResultEnum {

        //ResultEnum.java

        /**
         * 成功
         */
        SUCCESS(200, "操作成功"),

        /**
         * 系统异常
         */
        CODE_EXCEPTION(500, "系统异常");

        /**
         * 状态码
         */
        private Integer code;

         /**
          * 描述
          */
         private String msg;

         public Integer getCode() {
             return code;
         }

         public void setCode(Integer code) {
             this.code = code;
         }

         public String getMsg() {
             return msg;
         }

         public void setMsg(String msg) {
             this.msg = msg;
         }

         ResultEnum(Integer code, String msg) {
             this.code = code;
             this.msg = msg;
         }


     }

}
