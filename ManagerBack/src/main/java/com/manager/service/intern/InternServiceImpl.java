package com.manager.service.intern;

import com.manager.dao.InternDAO;
import com.manager.dao.StudentEnterpriseDAO;
import com.manager.dao.StudentTeacherRelationDAO;
import com.manager.dao.UserInfoDAO;
import com.manager.entity.Intern;
import com.manager.entity.StudentEnterprise;
import com.manager.entity.StudentTeacherRelation;
import com.manager.entity.UserInfo;
import com.manager.form.EditInternParam;
import com.manager.vo.intern.IsPeriodVO;
import com.manager.vo.intern.PeriodListVO;
import com.manager.vo.intern.PeriodVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InternServiceImpl implements InternService {

    private final InternDAO internDAO;

    private final UserInfoDAO userInfoDAO;

    private final StudentEnterpriseDAO studentEnterpriseDAO;

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    @Autowired
    public InternServiceImpl(InternDAO internDAO, UserInfoDAO userInfoDAO, StudentEnterpriseDAO studentEnterpriseDAO, StudentTeacherRelationDAO studentTeacherRelationDAO) {
        this.internDAO = internDAO;
        this.userInfoDAO = userInfoDAO;
        this.studentEnterpriseDAO = studentEnterpriseDAO;
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
    }

    @Override
    public boolean editPeriod(EditInternParam param) {
        Calendar calendar = Calendar.getInstance();
        String currentYear = Integer.toString(calendar.get(Calendar.YEAR) - 3);
        Optional<Intern> byId = internDAO.findById(currentYear);
        if (byId.isPresent()) {
            Intern intern = byId.get();
            intern.setFromDate(param.getFromDate());
            intern.setEndDate(param.getEndDate());
            intern.setPeriodName(param.getPeriodName());
            internDAO.saveAndFlush(intern);
        } else {
            internDAO.save(new Intern(currentYear, param.getFromDate(), param.getEndDate(), param.getPeriodName()));
        }
        return true;
    }

    @Override
    public IsPeriodVO isPeriod(String studentId) {
        int flag;
        String date = null;
        UserInfo userInfo = userInfoDAO.findAllById(studentId);
        Optional<Intern> intern = internDAO.findById(userInfo.getPeriodId());
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(userInfo.getId());
        StudentTeacherRelation studentTeacherRelation = studentTeacherRelationDAO.findByStudentIdAndState(userInfo.getId(), 1);
        if (intern.isPresent()) {
            Intern intern1 = intern.get();
            Date currentDate = new Date();
            if (currentDate.before(intern1.getFromDate())) {
                flag = 1; // 还未到实习阶段
                date = intern1.getFromDate().toString().substring(0, 10);
//            } else if (currentDate.after(intern1.getEndDate())) {
//                flag = 2; // 已超过实习阶段
//                date = intern1.getEndDate().toString().substring(0, 10);
            } else {
                flag = 3;
                if (studentEnterprise == null) {
                    flag += 1;
                }
                if (studentTeacherRelation == null) {
                    flag += 2;
                }
            }
        } else {
            flag = 0; // 管理员未开启实习
        }
        return new IsPeriodVO(flag, date);
    }

    @Override
    public PeriodListVO getPeriodList() {
        List<Intern> periodList = internDAO.getByOrderByPeriodIdDesc();
        if (periodList.isEmpty()) {
            return new PeriodListVO(false, new ArrayList<>());
        } else {
            Calendar calendar = Calendar.getInstance();
            String currentYear = Integer.toString(calendar.get(Calendar.YEAR) - 3);
            boolean isNowCreated = periodList.get(0).getPeriodId().equals(currentYear);
            List<PeriodVO> periodVOList = periodList
                    .stream()
                    .map(item -> new PeriodVO(
                            item.getPeriodName(),
                            item.getFromDate().toString().substring(0, 10),
                            item.getEndDate().toString().substring(0, 10))
                    )
                    .collect(Collectors.toList());
            return new PeriodListVO(isNowCreated, periodVOList);
        }
    }
}
