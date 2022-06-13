package com.manager.dao;

import com.manager.entity.StudentTeacherRelation;
import com.manager.vo.query.MyStudentVO;
import com.manager.vo.relation.StudentApplyInfoVO;
import com.sun.istack.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentTeacherRelationDAO
        extends JpaRepository<StudentTeacherRelation, Integer> {

    /**
     * findByState
     * 根据状态查找一个指导关系列表，状态包括：申请中，已同意，已拒绝
     */
    List<StudentTeacherRelation> findByState(Integer state);

    /**
     * findByStudentIdAndState
     * 根据学号和状态查找一个指导关系
     */
    StudentTeacherRelation findByStudentIdAndState(String studentId, Integer state);

    /**
     * findByStudentIdAndStateLessThan
     * 根据学号和状态查找申请中或已确立的指导关系
     */
    List<StudentTeacherRelation> findByStudentIdAndStateLessThan(String studentId, Integer state);

    /**
     * findByStudentIdAndTeacherId
     * 根据学号和工号查找一个指导关系
     */
    Optional<StudentTeacherRelation> findByStudentIdAndTeacherId(String stuID, String teaID);

    /**
     * findByTeacherIdAndStateLessThan
     * 根据工号和状态查找申请中的指导关系
     */
    @Nullable
    @Query(value = "select new com.manager.vo.relation.StudentApplyInfoVO(u, str) " +
            "from UserInfo as u, StudentTeacherRelation as str " +
            "where u.id = str.studentId " +
            "and str.teacherId = ?1 " +
            "and str.state = ?2 ")
    List<StudentApplyInfoVO> findByTeacherIdAndStateLessThan(String teacherId, Integer state);

    /**
     * findAllByTeacherId
     * 根据工号和状态查找该状态下的所有学生信息
     */
    @Nullable
    @Query(value = "select new com.manager.vo.query.MyStudentVO(u, se) " +
            "from StudentTeacherRelation as str, UserInfo as u, StudentEnterprise as se " +
            "where str.studentId = u.id " +
            "and str.studentId = se.studentId " +
            "and str.teacherId = ?1 " +
            "and str.state = ?2 ")
    List<MyStudentVO> findAllByTeacherId(String teacherId, Integer state);

    /**
     * 查询所有的指导关系
     */
    @Nullable
    @Query(value = "select u.id as tid, u.name as tname, uu.id as sid, uu.name as sname, s.send_time as sendTime, s.deal_time as dealTime " +
            "from student_teacher_relation as s, user_info as u, user_info as uu " +
            "where s.state = 1 and s.teacher_id = u.id and s.student_id = uu.id",
            nativeQuery = true
    )
    List<Object[]> queryAllRelation();

    /**
     * 查询学生和校内导师的那条记录
     */
    Optional<StudentTeacherRelation> findByStudentIdAndTeacherIdAndState(String studentId, String teacherId, Integer state);
}
