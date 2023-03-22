package com.manager.form;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HandleStudentApplyParam {

    // 来自session
    String id;

    // 以下内容来自前端
    List<String> handleApplyList = new ArrayList<>();
}
