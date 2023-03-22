package com.manager.vo.relation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherQueryVO {

    public TeacherQueryVO(boolean succeed) {
        this.succeed = succeed;
    }

    private boolean succeed;

    private String name;

    private String sex;

    private String phone;

    private String email;
}
