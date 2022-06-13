package com.manager.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserQueryFormDataVO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("state")
    private Integer state;

    @JsonProperty("name")
    private String name;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("className")
    private String className;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;
}
