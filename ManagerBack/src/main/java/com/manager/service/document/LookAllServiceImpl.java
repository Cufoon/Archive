package com.manager.service.document;

import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.vo.document.student.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LookAllServiceImpl implements LookAllService {

    private TableReportDAO tableReportDAO;

    private TableExamDAO tableExamDAO;

    private TableIdentifyDAO tableIdentifyDAO;

    private TableAppraisalDAO tableAppraisalDAO;

    private TableSummaryDAO tableSummaryDAO;

    private UserInfoDAO userInfoDAO;

    private StudentTeacherRelationDAO studentTeacherRelationDAO;

    private StudentEnterpriseDAO studentEnterpriseDAO;

    @Autowired
    public LookAllServiceImpl(TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO, UserInfoDAO userInfoDAO, StudentTeacherRelationDAO studentTeacherRelationDAO, StudentEnterpriseDAO studentEnterpriseDAO) {
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
     * lookAll
     * 根据学生ID、表单编号和表单月次返回获取对应表单的详细信息
     */
    @Override
    public Object lookAll(String studentId, Integer category, Integer order) {
        Object table = new Object();
        UserInfo stuInfo = userInfoDAO.findById(studentId).get();
        StudentTeacherRelation strInfo = studentTeacherRelationDAO.findByStudentIdAndState(studentId, 1);
        UserInfo teaInfo = userInfoDAO.findById(strInfo.getTeacherId()).get();
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(studentId);
        if (studentEnterprise == null) {
            studentEnterprise = new StudentEnterprise();
        }

        // 根据五种表格的类别来区分操作
        // case 1: Report
        // case 2: Exam
        // case 3: Identify
        // case 4: Appraisal
        // case 5: Summary
        switch (category) {
            case 1:
                LookTableReportVO lookTableReportVO = new LookTableReportVO();
                lookTableReportVO.setClassName(stuInfo.getClassName());
                lookTableReportVO.setName(stuInfo.getName());
                lookTableReportVO.setStudentId(studentId);
                lookTableReportVO.setTeacherName(teaInfo.getName());
                lookTableReportVO.setEpName(studentEnterprise.getEnterpriseName());
                lookTableReportVO.setEpCity(studentEnterprise.getEnterpriseCity());
                lookTableReportVO.setInstructorName(studentEnterprise.getInstructorName());
                lookTableReportVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                lookTableReportVO.setInstructorEmail(studentEnterprise.getInstructorEmail());
                TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNum(studentId, order);
                if (tableReport != null) {
                    lookTableReportVO.setStartDate(tableReport.getStartDate().toString());
                    lookTableReportVO.setEndDate(tableReport.getEndDate().toString());
                    lookTableReportVO.setProject(tableReport.getProject());
                    lookTableReportVO.setLivingAddress(tableReport.getLivingAddress());
                    lookTableReportVO.setLivingPhone(tableReport.getLivingPhone());
                    lookTableReportVO.setLivingCondition(tableReport.getLivingCondition());
                    lookTableReportVO.setJobContent(tableReport.getJobContent());
                    lookTableReportVO.setRequirements(tableReport.getRequirements());
                    if (tableReport.getStatus() == 3) {
                        lookTableReportVO.setEvaluation(tableReport.getEvaluation());
                        lookTableReportVO.setTeacherSuggestion(tableReport.getTeacherSuggestions());
                    }
                }
                table = lookTableReportVO;
                break;
            case 2:
                LookTableExamVO lookTableExamVO = new LookTableExamVO();
                lookTableExamVO.setClassName(stuInfo.getClassName());
                lookTableExamVO.setName(stuInfo.getName());
                lookTableExamVO.setStudentId(studentId);
                lookTableExamVO.setTeacherName(teaInfo.getName());
                lookTableExamVO.setInstructorName(studentEnterprise.getInstructorName());
                lookTableExamVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                lookTableExamVO.setInstructorEmail(studentEnterprise.getInstructorEmail());
                lookTableExamVO.setEpName(studentEnterprise.getEnterpriseName());
                lookTableExamVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNum(studentId, order);
                if (tableExam != null) {
                    lookTableExamVO.setStartDate(tableExam.getStartDate().toString());
                    lookTableExamVO.setEndDate(tableExam.getEndDate().toString());
                    lookTableExamVO.setProject(tableExam.getProject());
                    lookTableExamVO.setAttendance(tableExam.getAttendance());
                    lookTableExamVO.setEptSuggestions(tableExam.getEptSuggestions());
                    if (tableExam.getStatus() == 3) {
                        lookTableExamVO.setEvaluation(tableExam.getEvaluation());
                        lookTableExamVO.setTeacherSuggestion(tableExam.getTeacherSuggestions());
                    }
                }
                table = lookTableExamVO;
                break;
            case 3:
                LookTableIdentifyVO lookTableIdentifyVO = new LookTableIdentifyVO();
                lookTableIdentifyVO.setClassName(stuInfo.getClassName());
                lookTableIdentifyVO.setName(stuInfo.getName());
                lookTableIdentifyVO.setSex(stuInfo.getSex());
                lookTableIdentifyVO.setStudentId(studentId);
                lookTableIdentifyVO.setEpName(studentEnterprise.getEnterpriseName());
                lookTableIdentifyVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableIdentify tableIdentify = tableIdentifyDAO.findByStudentId(studentId);
                if (tableIdentify != null) {
                    lookTableIdentifyVO.setStartDate(tableIdentify.getStartDate().toString());
                    lookTableIdentifyVO.setEndDate(tableIdentify.getEndDate().toString());
                    lookTableIdentifyVO.setProject(tableIdentify.getProject());
                    lookTableIdentifyVO.setPersonalSummary(tableIdentify.getPersonalSummary());
                    lookTableIdentifyVO.setAttendance(tableIdentify.getAttendance());
                    lookTableIdentifyVO.setEptSuggestions(tableIdentify.getEptSuggestions());
                    lookTableIdentifyVO.setEpSuggestions(tableIdentify.getEpSuggestions());
                    if (tableIdentify.getStatus() == 3) {
                        lookTableIdentifyVO.setEvaluation(tableIdentify.getEvaluation());
                        lookTableIdentifyVO.setTeacherSuggestion(tableIdentify.getTeacherSuggestions());
                    }
                }
                table = lookTableIdentifyVO;
                break;
            case 4:
                LookTableAppraisalVO lookTableAppraisalVO = new LookTableAppraisalVO();
                lookTableAppraisalVO.setName(stuInfo.getName());
                lookTableAppraisalVO.setStudentId(studentId);
                lookTableAppraisalVO.setInstructorName(studentEnterprise.getInstructorName());
                lookTableAppraisalVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                lookTableAppraisalVO.setEpName(studentEnterprise.getEnterpriseName());
                lookTableAppraisalVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableAppraisal tableAppraisal = tableAppraisalDAO.findByStudentId(studentId);
                if (tableAppraisal != null) {
                    lookTableAppraisalVO.setStartDate(tableAppraisal.getStartDate().toString());
                    lookTableAppraisalVO.setEndDate(tableAppraisal.getEndDate().toString());
                    lookTableAppraisalVO.setProject(tableAppraisal.getProject());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation1(tableAppraisal.getEnterpriseTeacherEvaluation1());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation2(tableAppraisal.getEnterpriseTeacherEvaluation2());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation3(tableAppraisal.getEnterpriseTeacherEvaluation3());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation4(tableAppraisal.getEnterpriseTeacherEvaluation4());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation5(tableAppraisal.getEnterpriseTeacherEvaluation5());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation6(tableAppraisal.getEnterpriseTeacherEvaluation6());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation7(tableAppraisal.getEnterpriseTeacherEvaluation7());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation8(tableAppraisal.getEnterpriseTeacherEvaluation8());
                    lookTableAppraisalVO.setEnterpriseTeacherEvaluation9(tableAppraisal.getEnterpriseTeacherEvaluation9());
                    lookTableAppraisalVO.setEnterpriseTeacherSuggestions(tableAppraisal.getEnterpriseTeacherSuggestions());
                    if (tableAppraisal.getStatus() == 3) {
                        lookTableAppraisalVO.setEvaluation(tableAppraisal.getEvaluation());
                        lookTableAppraisalVO.setTeacherSuggestion(tableAppraisal.getTeacherSuggestions());
                        lookTableAppraisalVO.setTeacherEvaluation1(tableAppraisal.getTeacherEvaluation1());
                        lookTableAppraisalVO.setTeacherEvaluation2(tableAppraisal.getTeacherEvaluation2());
                        lookTableAppraisalVO.setTeacherEvaluation3(tableAppraisal.getTeacherEvaluation3());
                    }
                }
                table = lookTableAppraisalVO;
                break;
            case 5:
                LookTableSummaryVO lookTableSummaryVO = new LookTableSummaryVO();
                lookTableSummaryVO.setClassName(stuInfo.getClassName());
                lookTableSummaryVO.setStudentId(studentId);
                lookTableSummaryVO.setName(stuInfo.getName());
                lookTableSummaryVO.setPhone(stuInfo.getPhone());
                TableSummary tableSummary = tableSummaryDAO.findByStudentId(studentId);
                if (tableSummary != null) {
                    lookTableSummaryVO.setSummaryReport(tableSummary.getSummaryReport());
                    if (tableSummary.getStatus() == 3) {
                        lookTableSummaryVO.setEvaluation(tableSummary.getEvaluation());
                        lookTableSummaryVO.setTeacherSuggestion(tableSummary.getTeacherSuggestions());
                    }
                }
                table = lookTableSummaryVO;
                break;
        }
        return table;
    }
}
