package com.bora.device.test;

import com.alibaba.fastjson.JSONObject;

/***
 *Ticket: 
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-14 14:30:59
 *****/
public class ExtTest {

    private String username;

    private Integer age;

    private JSONObject jsonObject;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        return "ExtTest{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", jsonObject=" + jsonObject +
                '}';
    }
}
