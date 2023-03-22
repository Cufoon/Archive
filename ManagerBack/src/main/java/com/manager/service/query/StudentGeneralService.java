package com.manager.service.query;

import com.manager.vo.query.QueryAllStudentVO;

public interface StudentGeneralService {

    /**
     * queryAllStudent
     * 根据校内导师ID和申请状态返回该校内导师所有处于该状态下的指导关系信息
     */
    QueryAllStudentVO queryAllStudent(String teacherId, Integer state);
}
