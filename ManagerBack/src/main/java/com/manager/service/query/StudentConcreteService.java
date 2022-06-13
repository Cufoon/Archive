package com.manager.service.query;

import com.manager.vo.query.QueryStudentVO;

public interface StudentConcreteService {

    /**
     * queryConcreteStudent
     * 根据学生ID返回学生的详细信息
     */
    QueryStudentVO queryConcreteStudent(String studentId);
}
