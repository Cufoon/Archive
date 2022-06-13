package com.manager.service.query;

import com.manager.dao.StudentEnterpriseDAO;
import com.manager.dao.UserInfoDAO;
import com.manager.entity.UserInfo;
import com.manager.vo.query.QueryStudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StudentConcreteServiceImpl implements StudentConcreteService {

    private final StudentEnterpriseDAO studentEnterpriseDAO;

    private final UserInfoDAO userInfoDAO;

    @Autowired
    public StudentConcreteServiceImpl(StudentEnterpriseDAO studentEnterpriseDAO, UserInfoDAO userInfoDAO) {
        this.studentEnterpriseDAO = studentEnterpriseDAO;
        this.userInfoDAO = userInfoDAO;
    }

    /**
     * queryConcreteStudent
     * 根据学生ID返回学生的详细信息
     */
    @Override
    public QueryStudentVO queryConcreteStudent(String studentId) {
        QueryStudentVO concrete = studentEnterpriseDAO.findStudentConcreteById(studentId);
        Optional<UserInfo> userInfo = userInfoDAO.findById(studentId);
        if (userInfo.isPresent()) {
            UserInfo uInfo = userInfo.get();
            concrete.setAvatar(uInfo.getAvatar());
        }
        if (concrete == null) {
            log.error("[QueryConcreteStudentService] id = {} 联表查询学生、企业详情，未找到", studentId);
        }
        return concrete;
    }
}
