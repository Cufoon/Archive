package com.manager.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherInfoQueryVO {

    private String id;

    private String name;

    private String email;

    private String phone;

    private String introduction;

    private String avatar;
}
