package com.manager.service.document;

import com.manager.vo.document.student.DocumentQueryVO;
import com.sun.istack.NotNull;

public interface QueryAllService {

    /**
     * queryAll
     * 根据学生ID返回学生所有表格信息
     */
    DocumentQueryVO queryAll(@NotNull String studentId);
}
