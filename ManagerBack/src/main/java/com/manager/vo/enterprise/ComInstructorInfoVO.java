package com.manager.vo.enterprise;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComInstructorInfoVO {

    public ComInstructorInfoVO() {
    }

    private String name;

    private String sex;

    private String phone;

    private String email;

    private String city;

    private String comName;

    private String comAddress;
}
