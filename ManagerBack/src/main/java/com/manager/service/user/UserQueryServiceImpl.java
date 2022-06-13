package com.manager.service.user;

import com.manager.dao.UserInfoDAO;
import com.manager.entity.UserInfo;
import com.manager.vo.user.UserQueryFormDataVO;
import com.manager.vo.user.UserQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserInfoDAO userInfoDAO;

    @Autowired
    public UserQueryServiceImpl(UserInfoDAO userInfoDAO) {
        this.userInfoDAO = userInfoDAO;
    }

    /**
     * queryUser
     * 根据用户类型返回用户列表
     */
    @Override
    public UserQueryVO queryUser(Integer type) {
        UserQueryVO userQueryVO = new UserQueryVO();
        List<UserInfo> userInfoList = userInfoDAO.findAllByType(type);

        // 检索所有的用户（按类别）
        List<UserQueryFormDataVO> userQueryFormDataVOList = userInfoList.stream().map(
                e -> new UserQueryFormDataVO(
                        e.getId(),
                        e.getType(),
                        e.getState(),
                        e.getName(),
                        e.getAvatar(),
                        e.getSex(),
                        e.getAge(),
                        e.getClassName(),
                        e.getPhone(),
                        e.getEmail()
                )
        ).collect(Collectors.toList());

        userQueryVO.setFormData(userQueryFormDataVOList);
        return userQueryVO;
    }
}
