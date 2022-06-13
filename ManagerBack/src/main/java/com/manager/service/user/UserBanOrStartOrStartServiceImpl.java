package com.manager.service.user;

import com.manager.dao.UserInfoDAO;
import com.manager.entity.UserInfo;
import com.manager.vo.user.UserBanOrStartVO;
import org.springframework.stereotype.Service;

@Service
public class UserBanOrStartOrStartServiceImpl implements UserBanOrStartService {

    private final UserInfoDAO userInfoDAO;

    public UserBanOrStartOrStartServiceImpl(UserInfoDAO userInfoDAO) {

        this.userInfoDAO = userInfoDAO;
    }

    @Override
    public UserBanOrStartVO banUser(String studentId, Integer banOrStart) {
        UserBanOrStartVO userBanOrStartVO = new UserBanOrStartVO();
        UserInfo userInfo = userInfoDAO.findAllById(studentId);
        if (userInfo == null) {
            userBanOrStartVO.setBan(false);
            return userBanOrStartVO;
        }
        userInfo.setState(banOrStart);
        userBanOrStartVO.setBan(true);
        userInfoDAO.saveAndFlush(userInfo);
        return userBanOrStartVO;
    }
}
