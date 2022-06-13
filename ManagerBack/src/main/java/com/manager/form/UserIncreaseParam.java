package com.manager.form;

import lombok.Data;

@Data
public class UserIncreaseParam {

    String id;

    //以下数据来自前端
    String userId;

    String password;

    Integer type;

    Integer state;

    String name;

    String avatar;

    String sex;

    Integer age;

    String className;

    String phone;

    String email;

    String introduction;
}
