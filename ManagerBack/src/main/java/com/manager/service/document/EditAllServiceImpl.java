package com.manager.service.document;

import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.form.document.EditDocParam;
import com.manager.vo.document.student.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditAllServiceImpl implements EditAllService {

    private final TableReportDAO tableReportDAO;

    private final TableExamDAO tableExamDAO;

    private final TableIdentifyDAO tableIdentifyDAO;

    private final TableAppraisalDAO tableAppraisalDAO;

    private final TableSummaryDAO tableSummaryDAO;

    private final UserInfoDAO userInfoDAO;

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    private final StudentEnterpriseDAO studentEnterpriseDAO;

    @Autowired
    public EditAllServiceImpl(TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO, UserInfoDAO userInfoDAO, StudentTeacherRelationDAO studentTeacherRelationDAO, StudentEnterpriseDAO studentEnterpriseDAO) {
        this.tableReportDAO = tableReportDAO;

        this.tableExamDAO = tableExamDAO;

        this.tableIdentifyDAO = tableIdentifyDAO;

        this.tableAppraisalDAO = tableAppraisalDAO;

        this.tableSummaryDAO = tableSummaryDAO;

        this.userInfoDAO = userInfoDAO;

        this.studentTeacherRelationDAO = studentTeacherRelationDAO;

        this.studentEnterpriseDAO = studentEnterpriseDAO;
    }

    /**
     * editAll
     * 根据学生ID，表单编号，表单月次返回对应的表单的基础信息
     */
    @Override
    public Object editAll(EditDocParam editDocParam) {
        Object table = new Object();
        String studentId = editDocParam.getId();
        UserInfo stuInfo = userInfoDAO.findById(studentId).get();
        StudentTeacherRelation strInfo = studentTeacherRelationDAO.findByStudentIdAndState(studentId, 1);
        UserInfo teaInfo = userInfoDAO.findById(strInfo.getTeacherId()).get();
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(studentId);
        if (studentEnterprise == null) {
            studentEnterprise = new StudentEnterprise();
        }

//        根据五种表格的类别来区分操作
//        case 1: Report
//        case 2: Exam
//        case 3: Identify
//        case 4: Appraisal
//        case 5: Summary
        switch (editDocParam.getCategory()) {
            case 1:
                TableReportVO tableReportVO = new TableReportVO();
                tableReportVO.setClassName(stuInfo.getClassName());
                tableReportVO.setName(stuInfo.getName());
                tableReportVO.setStudentId(editDocParam.getId());
                tableReportVO.setTeacherName(teaInfo.getName());
                tableReportVO.setEpName(studentEnterprise.getEnterpriseName());
                tableReportVO.setEpCity(studentEnterprise.getEnterpriseCity());
                tableReportVO.setInstructorName(studentEnterprise.getInstructorName());
                tableReportVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                tableReportVO.setInstructorEmail(studentEnterprise.getInstructorEmail());
                TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNum(studentId, editDocParam.getOrder());
                if (tableReport != null) {
                    BeanUtils.copyProperties(tableReport, tableReportVO);
                    tableReportVO.setStartDate(tableReport.getStartDate() == null ? "" : tableReport.getStartDate().toString());
                    tableReportVO.setEndDate(tableReport.getEndDate() == null ? "" : tableReport.getEndDate().toString());
                }
                table = tableReportVO;
                break;
            case 2:
                TableExamVO tableExamVO = new TableExamVO();
                tableExamVO.setClassName(stuInfo.getClassName());
                tableExamVO.setName(stuInfo.getName());
                tableExamVO.setStudentId(editDocParam.getId());
                tableExamVO.setTeacherName(teaInfo.getName());
                tableExamVO.setInstructorName(studentEnterprise.getInstructorName());
                tableExamVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                tableExamVO.setInstructorEmail(studentEnterprise.getInstructorEmail());
                tableExamVO.setEpName(studentEnterprise.getEnterpriseName());
                tableExamVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNum(studentId, editDocParam.getOrder());
                if (tableExam != null) {
                    BeanUtils.copyProperties(tableExam, tableExamVO);
                    tableExamVO.setStartDate(tableExam.getStartDate() == null ? "" : tableExam.getStartDate().toString());
                    tableExamVO.setEndDate(tableExam.getEndDate() == null ? "" : tableExam.getEndDate().toString());
                }
                table = tableExamVO;
                break;
            case 3:
                TableIdentifyVO tableIdentifyVO = new TableIdentifyVO();
                tableIdentifyVO.setClassName(stuInfo.getClassName());
                tableIdentifyVO.setName(stuInfo.getName());
                tableIdentifyVO.setSex(stuInfo.getSex());
                tableIdentifyVO.setStudentId(editDocParam.getId());
                tableIdentifyVO.setEpName(studentEnterprise.getEnterpriseName());
                tableIdentifyVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableIdentify tableIdentify = tableIdentifyDAO.findByStudentId(studentId);
                if (tableIdentify != null) {
                    BeanUtils.copyProperties(tableIdentify, tableIdentifyVO);
                    tableIdentifyVO.setStartDate(tableIdentify.getStartDate() == null ? "" : tableIdentify.getStartDate().toString());
                    tableIdentifyVO.setEndDate(tableIdentify.getStartDate() == null ? "" : tableIdentify.getEndDate().toString());
                }
                table = tableIdentifyVO;
                break;
            case 4:
                TableAppraisalVO tableAppraisalVO = new TableAppraisalVO();
                tableAppraisalVO.setName(stuInfo.getName());
                tableAppraisalVO.setStudentId(editDocParam.getId());
                tableAppraisalVO.setInstructorName(studentEnterprise.getInstructorName());
                tableAppraisalVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                tableAppraisalVO.setEpName(studentEnterprise.getEnterpriseName());
                tableAppraisalVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableAppraisal tableAppraisal = tableAppraisalDAO.findByStudentId(studentId);
                if (tableAppraisal != null) {
                    BeanUtils.copyProperties(tableAppraisal, tableAppraisalVO);
                    tableAppraisalVO.setStartDate(tableAppraisal.getStartDate() == null ? "" : tableAppraisal.getStartDate().toString());
                    tableAppraisalVO.setEndDate(tableAppraisal.getStartDate() == null ? "" : tableAppraisal.getEndDate().toString());
                }
                table = tableAppraisalVO;
                break;
            case 5:
                TableSummaryVO tableSummaryVO = new TableSummaryVO();
                tableSummaryVO.setClassName(stuInfo.getClassName());
                tableSummaryVO.setStudentId(editDocParam.getId());
                tableSummaryVO.setName(stuInfo.getName());
                tableSummaryVO.setPhone(stuInfo.getPhone());
                TableSummary tableSummary = tableSummaryDAO.findByStudentId(studentId);
                if (tableSummary != null) {
                    BeanUtils.copyProperties(tableSummary, tableSummaryVO);
                }
                table = tableSummaryVO;
                break;
        }
        return table;
    }
}
