package com.manager.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVO {

    private String id;

    private String name;

    private Boolean succeed;
}
