package com.manager.service.query;

import com.manager.dao.StudentTeacherRelationDAO;
import com.manager.vo.query.MyStudentVO;
import com.manager.vo.query.QueryAllStudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StudentGeneralServiceImpl implements StudentGeneralService {

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    @Autowired
    public StudentGeneralServiceImpl(StudentTeacherRelationDAO studentTeacherRelationDAO) {
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
    }

    /**
     * queryAllStudent
     * 根据校内导师ID和申请状态返回该校内导师所有处于该状态下的指导关系信息
     */
    @Override
    public QueryAllStudentVO queryAllStudent(String teacherId, Integer state) {
        List<MyStudentVO> myStudentVOList = studentTeacherRelationDAO.findAllByTeacherId(teacherId, state);
        if (myStudentVOList == null) {
            myStudentVOList = new ArrayList<>();
        }
        return new QueryAllStudentVO(myStudentVOList);
    }
}
