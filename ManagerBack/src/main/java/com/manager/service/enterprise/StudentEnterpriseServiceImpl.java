package com.manager.service.enterprise;

import com.manager.dao.CityDAO;
import com.manager.dao.DeadlineDAO;
import com.manager.dao.ProvinceDAO;
import com.manager.dao.StudentEnterpriseDAO;
import com.manager.entity.City;
import com.manager.entity.Deadline;
import com.manager.entity.Province;
import com.manager.entity.StudentEnterprise;
import com.manager.form.CreateInternshipParam;
import com.manager.vo.enterprise.InternshipCreateVO;
import com.manager.vo.enterprise.InternshipVO;
import com.manager.vo.enterprise.UploadArchiveVO;
import com.manager.vo.enterprise.UploadedArchiveVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

@Service
@Slf4j
public class StudentEnterpriseServiceImpl implements StudentEnterpriseService {

    private final StudentEnterpriseDAO studentEnterpriseDAO;

    private final DeadlineDAO deadlineDAO;

    private final CityDAO cityDAO;

    private final ProvinceDAO provinceDAO;

    @Autowired
    public StudentEnterpriseServiceImpl(StudentEnterpriseDAO studentEnterpriseDAO, DeadlineDAO deadlineDAO, CityDAO cityDAO, ProvinceDAO provinceDAO) {

        this.studentEnterpriseDAO = studentEnterpriseDAO;

        this.deadlineDAO = deadlineDAO;

        this.cityDAO = cityDAO;

        this.provinceDAO = provinceDAO;
    }

    /**
     * queryInternship
     * 根据学生ID返回对应学生的企业实习信息
     */
    @Override
    public InternshipVO queryInternship(String username) {
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(username);
        InternshipVO internshipVO = new InternshipVO();
        if (studentEnterprise == null) {
            internshipVO.setState("0");
        } else {
            internshipVO.setState("1");
            internshipVO.setComCity(studentEnterprise.getEnterpriseCity());
            internshipVO.setComName(studentEnterprise.getEnterpriseName());
            internshipVO.setComAddress(studentEnterprise.getEnterpriseAddress());
            internshipVO.setComContact(studentEnterprise.getContactName());
            internshipVO.setSex(studentEnterprise.getContactSex());
            internshipVO.setComContactEmail(studentEnterprise.getContactEmail());
            internshipVO.setComContactPhone(studentEnterprise.getContactPhone());
            internshipVO.setComInstructorName(studentEnterprise.getInstructorName());
            internshipVO.setComInstructorSex(studentEnterprise.getInstructorSex());
            internshipVO.setComInstructorEmail(studentEnterprise.getInstructorEmail());
            internshipVO.setComInstructorPhone(studentEnterprise.getInstructorPhone());
            internshipVO.setOfferImg(studentEnterprise.getOfferImg());
        }
        return internshipVO;
    }

    /**
     * createInternship
     * 根据前端数据中的学生ID和实习相关信息保存对应学生的实习信息，返回保存是否成功
     */
    @Override
    public InternshipCreateVO createInternship(CreateInternshipParam createInternshipParam) {
        StudentEnterprise studentEnterprise = new StudentEnterprise();

        studentEnterprise.setStudentId(createInternshipParam.getId());
        studentEnterprise.setEnterpriseName(createInternshipParam.getComName());
        studentEnterprise.setEnterpriseCity(createInternshipParam.getComCity());
        studentEnterprise.setEnterpriseAddress(createInternshipParam.getComAddress());
        studentEnterprise.setContactName(createInternshipParam.getComContactName());
        studentEnterprise.setContactSex(createInternshipParam.getComContactSex());
        studentEnterprise.setContactEmail(createInternshipParam.getComContactEmail());
        studentEnterprise.setContactPhone(createInternshipParam.getComContactPhone());
        studentEnterprise.setInstructorName(createInternshipParam.getComInstructorName());
        studentEnterprise.setInstructorSex(createInternshipParam.getComInstructorSex());
        studentEnterprise.setInstructorEmail(createInternshipParam.getComInstructorEmail());
        studentEnterprise.setInstructorPhone(createInternshipParam.getComInstructorPhone());
        studentEnterprise.setOfferImg(createInternshipParam.getOfferImg());

        Deadline deadline = new Deadline();
        deadline.setStudentId(createInternshipParam.getId());
        Date currentDate = new Date();
        deadline.setStartDate(currentDate);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, 1);
        deadline.setReportDeadline1(calendar.getTime());
        deadline.setExamDeadline1(calendar.getTime());
        calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 2);
        deadline.setReportDeadline2(calendar.getTime());
        deadline.setExamDeadline2(calendar.getTime());
        calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 3);
        deadline.setReportDeadline3(calendar.getTime());
        deadline.setExamDeadline3(calendar.getTime());
        deadline.setIdentifyDeadline(calendar.getTime());
        deadline.setAppraisalDeadline(calendar.getTime());
        deadline.setSummaryDeadline(calendar.getTime());
        studentEnterpriseDAO.saveAndFlush(studentEnterprise);
        deadlineDAO.saveAndFlush(deadline);

        Optional<City> city = cityDAO.findById(createInternshipParam.getComCityCode());
        if (city.isPresent()) {
            City realCity = city.get();
            realCity.setPeople(realCity.getPeople() + 1);
            cityDAO.saveAndFlush(realCity);
            Province province = provinceDAO.findByProvinceId(realCity.getProvinceId());
            province.setPeople(province.getPeople() + 1);
            provinceDAO.saveAndFlush(province);
        }
        return new InternshipCreateVO(true);
    }

    @Override
    public UploadArchiveVO uploadArchive(String studentId, String archive) {
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(studentId);
        if (studentEnterprise == null) {
            return new UploadArchiveVO(false);
//            studentEnterprise = new StudentEnterprise();
        }
//        studentEnterprise.setStudentId(studentID);
        studentEnterprise.setArchive(archive);
        studentEnterpriseDAO.saveAndFlush(studentEnterprise);
        return new UploadArchiveVO(true);
    }

    @Override
    public UploadedArchiveVO downloadArchive(String studentId) {
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(studentId);
        if (studentEnterprise == null) {
            return new UploadedArchiveVO(null);
        }
        return new UploadedArchiveVO(studentEnterprise.getArchive());
    }
}
