package com.manager.form.document;

import lombok.Data;

@Data
public class EditDocParam {

    //用户 id
    private String id;

    // 以下信息来自前端
    private Integer category;

    private Integer order;
}
