package com.manager.service.relation;

import com.manager.vo.relation.ShowStudentApplyVO;

public interface ShowStudentApplyService {

    /**
     * showStudentApply
     * 根据校内导师ID返回学生的申请列表
     */
    ShowStudentApplyVO showStudentApply(String teacherId);
}
