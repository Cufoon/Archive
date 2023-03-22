package com.manager.service.base;

import com.manager.dao.UserInfoDAO;
import com.manager.entity.UserInfo;
import com.manager.form.EditStudentParam;
import com.manager.vo.base.StudentInfoEditVO;
import com.manager.vo.base.StudentInfoQueryVO;
import com.manager.vo.base.StudentInfoVO;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j // 用于打印日志
public class StudentInfoServiceImpl implements StudentInfoService {

    private static final int NONE = 0, EXIST = 1;

    private final UserInfoDAO userInfoDAO;

    @Autowired
    public StudentInfoServiceImpl(UserInfoDAO userInfoDAO) {
        this.userInfoDAO = userInfoDAO;
    }

    /**
     * getStudentInfo
     * 根据学生ID返回学生的个人信息
     */
    @Override
    public StudentInfoQueryVO getStudentInfo(@NotNull String studentId) {
        Optional<UserInfo> optionalUserInfo = userInfoDAO.findById(studentId);
        StudentInfoQueryVO vo = new StudentInfoQueryVO();
        if (optionalUserInfo.isPresent()) { // 用户数据存在
            UserInfo info = optionalUserInfo.get();
            String name = info.getName();
            String sex = info.getSex();
            String className = info.getClassName();
            String phone = info.getPhone();
            String email = info.getEmail();
            String introduction = info.getIntroduction();
            String avatar = info.getAvatar();
            if (name != null || sex != null || className != null || phone != null ||
                    email != null || introduction != null || avatar != null) {
                // 用户之前填写过信息
                vo.setState(EXIST);
                vo.setInfo(new StudentInfoVO(name, sex, className, phone, email, introduction, avatar, studentId));
            } else {
                vo.setState(NONE);
                vo.setInfo(new StudentInfoVO());
            }
        } else { // 用户数据不存在数据库表中
            log.error("[GetStudentInfoService] 用户 id = {} 不存在数据库表中", studentId);
            vo.setState(NONE);
            vo.setInfo(null);
        }
        return vo;
    }

    /**
     * editStudentInfo
     * 根据前端数据修改学生的个人信息，返回修改是否成功
     */
    @Override
    public StudentInfoEditVO editStudentInfo(@NotNull EditStudentParam param) {
        String id;
        Optional<UserInfo> optionalUserInfo = userInfoDAO.findById(id = param.getId());
        UserInfo info;
        if (optionalUserInfo.isPresent()) {
            info = optionalUserInfo.get();
            info.setId(param.getId());
            info.setName(param.getStudentName());
            info.setSex(param.getSex());
            info.setClassName(param.getClassName());
            info.setPhone(param.getPhoneNumber());
            info.setEmail(param.getEmail());
            info.setIntroduction(param.getIntroduction());
            info.setAvatar(param.getAvatar());
            userInfoDAO.save(info);
            return new StudentInfoEditVO(true);
        }
        log.error("[EditStudentInfoService] 用户 id = {} 不存在数据库表中", id);
        return new StudentInfoEditVO(false);
    }
}
