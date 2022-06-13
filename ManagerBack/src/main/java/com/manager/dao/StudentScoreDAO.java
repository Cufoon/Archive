package com.manager.dao;

import com.manager.entity.StudentScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentScoreDAO extends JpaRepository<StudentScore, String> {

    /**
     * findByStudentId
     * 根据学号查找学生的所有得分，包括材料得分，答辩得分和总分
     */
    StudentScore findByStudentId(String studentId);
}
