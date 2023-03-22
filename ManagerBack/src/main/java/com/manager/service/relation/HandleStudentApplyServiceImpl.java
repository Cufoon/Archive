package com.manager.service.relation;

import com.manager.dao.StudentTeacherRelationDAO;
import com.manager.entity.StudentTeacherRelation;
import com.manager.form.HandleStudentApplyParam;
import com.manager.vo.relation.HandletStudentApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class HandleStudentApplyServiceImpl implements HandleStudentApplyService {

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    @Autowired
    public HandleStudentApplyServiceImpl(StudentTeacherRelationDAO studentTeacherRelationDAO) {
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
    }

    /**
     * handleStudentApply
     * 根据前端数据中学生列表和处理方式（同意或拒绝）处理学生的指导关系申请，返回处理是否成功
     */
    @Override
    public HandletStudentApplyVO handleStudentApply(HandleStudentApplyParam param, Integer state) {
        int size = param.getHandleApplyList().size();
        for (int i = 0; i < size; i++) {
            String studentId = param.getHandleApplyList().get(i);
            // 数据库中最多存在一条学生“正在申请”的记录
            StudentTeacherRelation relations = studentTeacherRelationDAO.findByStudentIdAndState(studentId, 0);
            if (relations == null) {
                log.error("[HandleStudentApplyService] 找不到学号为" + studentId + "同学的申请");
                return new HandletStudentApplyVO(false);
            } else {
                relations.setState(state);
                relations.setDealTime(new Date());
                studentTeacherRelationDAO.save(relations);
            }
        }
        return new HandletStudentApplyVO(true);
    }
}
