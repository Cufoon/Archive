package com.manager.service.user;

import com.manager.vo.user.UserBanOrStartVO;

public interface UserBanOrStartService {

    UserBanOrStartVO banUser(String studentId, Integer banOrStart);
}
