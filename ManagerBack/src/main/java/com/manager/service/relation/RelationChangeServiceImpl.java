package com.manager.service.relation;

import com.manager.dao.StudentTeacherRelationDAO;
import com.manager.entity.StudentTeacherRelation;
import com.manager.vo.relation.RelationChangeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RelationChangeServiceImpl implements RelationChangeService {

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    @Autowired
    public RelationChangeServiceImpl(StudentTeacherRelationDAO studentTeacherRelationDAO) {
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
    }

    /**
     * addRelation
     * 根据学生ID与校内导师ID保存指导关系，返回保存是否成功
     */
    @Override
    public RelationChangeVO addRelation(String teaID, String stuID) {
        RelationChangeVO relationChangeVO = new RelationChangeVO();
        relationChangeVO.setHandle(false);
        StudentTeacherRelation studentTeacherRelationList = studentTeacherRelationDAO.findByStudentIdAndState(stuID, 1);
        if (studentTeacherRelationList == null) {
            StudentTeacherRelation studentTeacherRelation = new StudentTeacherRelation();
            studentTeacherRelation.setTeacherId(teaID);
            studentTeacherRelation.setStudentId(stuID);
            studentTeacherRelation.setState(1);
            studentTeacherRelation.setSendTime(new Date());
            studentTeacherRelation.setDealTime(new Date());
            studentTeacherRelationDAO.saveAndFlush(studentTeacherRelation);
            relationChangeVO.setHandle(true);
        }
        return relationChangeVO;
    }

    /**
     * deleteRelation
     * 根据学生ID与校内导师ID删除指导关系，返回删除是否成功
     */
    @Override
    public RelationChangeVO deleteRelation(String teaID, String stuID) {
        RelationChangeVO relationChangeVO = new RelationChangeVO();
        relationChangeVO.setHandle(false);
        Optional<StudentTeacherRelation> studentTeacherRelationResult = studentTeacherRelationDAO.findByStudentIdAndTeacherIdAndState(stuID, teaID, 1);
        if (studentTeacherRelationResult.isPresent()) {
            StudentTeacherRelation studentTeacherRelation = studentTeacherRelationResult.get();
            studentTeacherRelationDAO.delete(studentTeacherRelation);
            studentTeacherRelationDAO.flush();
            relationChangeVO.setHandle(true);
        }
        return relationChangeVO;
    }
}
