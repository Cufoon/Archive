package com.manager.service.relation;

import com.manager.form.HandleStudentApplyParam;
import com.manager.vo.relation.HandletStudentApplyVO;

public interface HandleStudentApplyService {

    /**
     * handleStudentApply
     * 根据前端数据中学生列表和处理方式（同意或拒绝）处理学生的指导关系申请，返回处理是否成功
     */
    HandletStudentApplyVO handleStudentApply(HandleStudentApplyParam param, Integer state);
}
