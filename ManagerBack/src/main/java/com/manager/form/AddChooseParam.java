package com.manager.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddChooseParam {

    private String id;

    // 以下来自前端
    @Length(max = 12)
    private String tid;
}
