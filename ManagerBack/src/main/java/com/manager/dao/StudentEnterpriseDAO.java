package com.manager.dao;

import com.manager.entity.StudentEnterprise;
import com.manager.vo.query.QueryStudentVO;
import com.sun.istack.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentEnterpriseDAO extends JpaRepository<StudentEnterprise, Integer> {

    /**
     * findByStudentId
     * 根据学生的学号查询学生的实习信息
     */
    StudentEnterprise findByStudentId(String studentId);

    /**
     * findStudentConcreteById
     * 根据学号查找学生的详细信息
     */
    @Nullable
    @Query(value = "select new com.manager.vo.query.QueryStudentVO(u, se) " +
            "from UserInfo as u, StudentEnterprise as se " +
            "where u.id = se.studentId " +
            "and u.id = ?1 ")
    QueryStudentVO findStudentConcreteById(String studentId);
}
