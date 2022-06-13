package com.manager.service.user;

import com.manager.dao.UserInfoDAO;
import com.manager.entity.UserInfo;
import com.manager.form.UserIncreaseParam;
import com.manager.vo.user.UserIncreaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserIncreaseServiceImpl implements UserIncreaseService {

    private final UserInfoDAO userInfoDAO;

    @Autowired
    public UserIncreaseServiceImpl(UserInfoDAO userInfoDAO) {
        this.userInfoDAO = userInfoDAO;
    }

    /**
     * increaseUser
     * 根据前端数据中的用户信息创建用户，返回创建是否成功
     */
    @Override
    public UserIncreaseVO increaseUser(UserIncreaseParam userIncreaseParam) {
        UserIncreaseVO userIncreaseVO = new UserIncreaseVO();
        if (userInfoDAO.findById(userIncreaseParam.getUserId()).isPresent()) {
            userIncreaseVO.setIncreased(false);
        } else {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userIncreaseParam.getUserId());
            if (userIncreaseParam.getPassword() == null || userIncreaseParam.getPassword().equals("")) {
                userInfo.setPassword(userIncreaseParam.getUserId());
            } else {
                userInfo.setPassword(userIncreaseParam.getPassword());
            }
            userInfo.setType(userIncreaseParam.getType());
            userInfo.setState(userIncreaseParam.getState());
            userInfo.setName(userIncreaseParam.getName());
            userInfo.setAvatar(userIncreaseParam.getAvatar());
            userInfo.setSex(userIncreaseParam.getSex());
            userInfo.setAge(userIncreaseParam.getAge());
            userInfo.setClassName(userIncreaseParam.getClassName());
            userInfo.setPhone(userIncreaseParam.getPhone());
            userInfo.setEmail(userIncreaseParam.getEmail());
            userInfo.setIntroduction(userIncreaseParam.getIntroduction());
            if (userInfo.getType() == 1) {
                userInfo.setPeriodId(userInfo.getId().substring(0, 4));
            }
            userInfoDAO.saveAndFlush(userInfo);
            userIncreaseVO.setIncreased(true);
        }
        return userIncreaseVO;
    }
}
