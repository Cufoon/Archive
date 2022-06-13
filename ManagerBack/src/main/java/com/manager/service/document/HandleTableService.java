package com.manager.service.document;

import com.manager.form.document.HandleTableParam;
import com.manager.vo.document.teacher.HandleTableVO;
import com.sun.istack.NotNull;

public interface HandleTableService {

    /**
     * handleTable
     * 根据前端数据中的学生ID、表单编号、表单月次和校内导师评价保存校内导师对该表的评价，返回保存是否成功
     */
    HandleTableVO handleTable(@NotNull HandleTableParam handleTableParam, int status);
}
