package com.manager.service.relation;

import com.manager.dao.StudentTeacherRelationDAO;
import com.manager.vo.relation.ShowStudentApplyVO;
import com.manager.vo.relation.StudentApplyInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowStudentApplyServiceImpl implements ShowStudentApplyService {

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    @Autowired
    public ShowStudentApplyServiceImpl(StudentTeacherRelationDAO studentTeacherRelationDAO) {
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
    }

    /**
     * showStudentApply
     * 根据校内导师ID返回学生的申请列表
     */
    @Override
    public ShowStudentApplyVO showStudentApply(String teacherId) {

        List<StudentApplyInfoVO> studentApplyList = studentTeacherRelationDAO.findByTeacherIdAndStateLessThan(teacherId, 0);
        if (studentApplyList == null) {
            studentApplyList = new ArrayList<>();
        }
        ShowStudentApplyVO showStudentApplyVO = new ShowStudentApplyVO();
        showStudentApplyVO.setStudentApplyList(studentApplyList);
        return showStudentApplyVO;
    }
}
