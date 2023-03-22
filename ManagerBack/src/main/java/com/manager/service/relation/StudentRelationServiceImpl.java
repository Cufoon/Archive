package com.manager.service.relation;

import com.manager.dao.StudentEnterpriseDAO;
import com.manager.dao.StudentTeacherRelationDAO;
import com.manager.dao.UserInfoDAO;
import com.manager.entity.StudentEnterprise;
import com.manager.entity.StudentTeacherRelation;
import com.manager.entity.UserInfo;
import com.manager.form.EditComParam;
import com.manager.vo.enterprise.ComInstructorInfoVO;
import com.manager.vo.enterprise.ComInstructorQueryVO;
import com.manager.vo.relation.ChooseQueryVO;
import com.manager.vo.relation.TeacherQueryVO;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class StudentRelationServiceImpl implements StudentRelationService {

    private static final int NONE = 0, EXIST = 1;

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    private final UserInfoDAO userInfoDAO;

    private final StudentEnterpriseDAO studentEnterpriseDAO;

    @Autowired
    public StudentRelationServiceImpl(
            StudentTeacherRelationDAO studentTeacherRelationDAO,
            UserInfoDAO userInfoDAO,
            StudentEnterpriseDAO studentEnterpriseDAO
    ) {
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
        this.userInfoDAO = userInfoDAO;
        this.studentEnterpriseDAO = studentEnterpriseDAO;
    }

    /**
     * queryTeacher
     * 根据学生ID返回对应校内导师的信息
     */
    @Override
    public TeacherQueryVO queryTeacher(@NotNull String studentId) {
        StudentTeacherRelation relation = studentTeacherRelationDAO.findByStudentIdAndState(studentId, 1);
        if (relation == null) {
            log.error("[QueryTeacherService] studentId = {}, 数据库没有查询到指导关系", studentId);
            return new TeacherQueryVO(false);
        }
        Optional<UserInfo> infoOptional = userInfoDAO.findById(relation.getTeacherId());
        TeacherQueryVO queryVO;
        if (infoOptional.isPresent()) {
            UserInfo teacherInfo = infoOptional.get();
            queryVO = new TeacherQueryVO(
                    true,
                    teacherInfo.getName(),
                    teacherInfo.getSex(),
                    teacherInfo.getPhone(),
                    teacherInfo.getEmail()
            );
        } else {
            log.error("[QueryTeacherService] studentId = {}, 关系存在，导师不存在", studentId);
            queryVO = new TeacherQueryVO(false);
        }
        return queryVO;
    }

    /**
     * queryComInstructor
     * 根据学生ID返回对应企业导师的信息
     */
    @Override
    public ComInstructorQueryVO queryComInstructor(String studentId) {
        StudentEnterprise eInfo = studentEnterpriseDAO.findByStudentId(studentId);
        ComInstructorQueryVO queryVO = new ComInstructorQueryVO();
        if (eInfo == null) {
            log.warn("[QueryComInstructorService] studentId = {}, 没有填写过企业信息数据", studentId);
            queryVO.setState(NONE);
            queryVO.setInfo(new ComInstructorInfoVO());
        } else {
            queryVO.setState(EXIST);
            queryVO.setInfo(new ComInstructorInfoVO(
                    eInfo.getInstructorName(),
                    eInfo.getInstructorSex(),
                    eInfo.getInstructorPhone(),
                    eInfo.getInstructorEmail(),
                    eInfo.getEnterpriseCity(),
                    eInfo.getEnterpriseName(),
                    eInfo.getEnterpriseAddress()
            ));
        }
        return queryVO;
    }

    /**
     * queryChooseGet
     * 根据学生ID返回对应的指导关系信息
     */
    @Override
    public ChooseQueryVO queryChooseGet(String studentId) {
        StudentTeacherRelation rInfo = studentTeacherRelationDAO.findByStudentIdAndState(studentId, 1);
        ChooseQueryVO queryVO = new ChooseQueryVO();
        if (rInfo == null) {
            log.warn("[QueryChooseGetService] studentId = {}, 还没有确定指导关系", studentId);
            rInfo = studentTeacherRelationDAO.findByStudentIdAndState(studentId, 0);
            if (rInfo == null) {
                log.warn("[QueryChooseGetService] studentId = {}, 还没有申请中的指导关系", studentId);
                queryVO.setState(1);
            } else {
                queryVO.setState(2);
            }
        } else {
            queryVO.setState(3);
        }
        queryVO.setTeachers(getTeacher(queryVO.getState(), rInfo));
        return queryVO;
    }

    /**
     * queryChoosePost
     * 根据学生ID和校内导师ID添加指导关系，返回添加是否成功
     */
    @Override
    public boolean queryChoosePost(String studentId, String teacherId) {
        List<StudentTeacherRelation> rInfo = studentTeacherRelationDAO.findByStudentIdAndStateLessThan(studentId, 2);
        if (rInfo.size() == 0) {
            StudentTeacherRelation str = new StudentTeacherRelation();
            str.setStudentId(studentId);
            str.setTeacherId(teacherId);
            str.setState(0);
            str.setSendTime(new Date());
            studentTeacherRelationDAO.save(str);
            return true;
        } else {
            log.warn("[QueryChoosePostService] studentId = {}, 存在申请中或者已确立的指导关系", studentId);
            return false;
        }
    }

    private List<Map<String, String>> getTeacher(Integer state, StudentTeacherRelation rInfo) {
        List<Map<String, String>> teachers = new ArrayList<>();
        switch (state) {
            case 1:
                List<UserInfo> teacherList = userInfoDAO.findAllByType(2);
                for (UserInfo userInfo : teacherList) {
                    Map<String, String> teacher = new HashMap<>();
                    teacher.put("name", userInfo.getName());
                    teacher.put("tid", userInfo.getId());
                    teachers.add(teacher);
                }
                break;
            case 2:
            case 3:
                Map<String, String> teacher = new HashMap<>();
                teacher.put("name", userInfoDAO.findById(rInfo.getTeacherId()).get().getName());
                teacher.put("tid", userInfoDAO.findById(rInfo.getTeacherId()).get().getId());
                teachers.add(teacher);
                break;
        }
        return teachers;
    }

    /**
     * editComInstructor
     * 根据前端数据中的学生ID和企业导师数据修改对应学生的企业导师信息，返回修改是否成功
     */
    @Override
    public boolean editComInstructor(@NotNull EditComParam paramVO) {
        String id;
        StudentEnterprise se = studentEnterpriseDAO.findByStudentId(id = paramVO.getId());
        if (se == null) {
            se = new StudentEnterprise();
        }
        se.setStudentId(id);
        se.setInstructorName(paramVO.getName());
        se.setInstructorSex(paramVO.getSex());
        se.setInstructorPhone(paramVO.getPhone());
        se.setInstructorEmail(paramVO.getEmail());
        se.setEnterpriseCity(paramVO.getCity());
        se.setEnterpriseName(paramVO.getComName());
        se.setEnterpriseAddress(paramVO.getComAddress());
        studentEnterpriseDAO.save(se);
        return true;
    }
}
