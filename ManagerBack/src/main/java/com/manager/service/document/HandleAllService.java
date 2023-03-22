package com.manager.service.document;

import com.sun.istack.NotNull;

public interface HandleAllService {

    /**
     * handleAll
     * 根据校内导师ID、学生ID、表单编号和表单月次返回获取对应表单的详细信息
     */
    Object handleAll(@NotNull String tid, @NotNull String sid, @NotNull Integer category, @NotNull Integer order);
}
