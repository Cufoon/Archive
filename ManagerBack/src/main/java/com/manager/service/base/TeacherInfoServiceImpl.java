package com.manager.service.base;

import com.manager.dao.UserInfoDAO;
import com.manager.entity.UserInfo;
import com.manager.form.EditTeacherParam;
import com.manager.vo.base.TeacherInfoEditVO;
import com.manager.vo.base.TeacherInfoQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TeacherInfoServiceImpl implements TeacherInfoService {

    private final UserInfoDAO userInfoDAO;

    @Autowired
    public TeacherInfoServiceImpl(UserInfoDAO userInfoDAO) {
        this.userInfoDAO = userInfoDAO;
    }

    /**
     * getTeacherInfo
     * 根据教师ID返回教师的个人信息
     */
    @Override
    public TeacherInfoQueryVO getTeacherInfo(String teacherId) {
        Optional<UserInfo> infoOptional = userInfoDAO.findById(teacherId);
        TeacherInfoQueryVO vo = null;
        if (infoOptional.isPresent()) {
            UserInfo info = infoOptional.get();
            vo = new TeacherInfoQueryVO(
                    teacherId,
                    info.getName(),
                    info.getEmail(),
                    info.getPhone(),
                    info.getIntroduction(),
                    info.getAvatar()
            );
        } else {
            log.error("[GetTeacherInfoService] 用户 id = {} 不存在数据库表种", teacherId);
        }
        return vo;
    }

    /**
     * editTeacherInfo
     * 根据前端数据修改学生的个人信息，返回修改是否成功
     */
    @Override
    public TeacherInfoEditVO editTeacherInfo(EditTeacherParam param) {
        String id;
        Optional<UserInfo> infoOptional = userInfoDAO.findById(id = param.getId());
        if (infoOptional.isPresent()) {
            UserInfo info = infoOptional.get();
            info.setName(param.getName());
            info.setEmail(param.getEmail());
            info.setPhone(param.getPhone());
            info.setIntroduction(param.getIntroduction());
            info.setAvatar(param.getAvatar());
            userInfoDAO.save(info);
            return new TeacherInfoEditVO(true);
        }
        log.error("[EditTeacherInfoService] 用户 id = {} 不存在数据库表中", id);
        return new TeacherInfoEditVO(false);
    }
}
