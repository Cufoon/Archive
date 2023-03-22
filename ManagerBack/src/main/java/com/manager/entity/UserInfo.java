package com.manager.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    private String id;

    private String password;

    private String name;

    private Integer type;

    private Integer state;

    private String className;

    private String sex;

    private Integer age;

    private String phone;

    private String email;

    private String introduction;

    private String avatar;

    private String periodId;
}
