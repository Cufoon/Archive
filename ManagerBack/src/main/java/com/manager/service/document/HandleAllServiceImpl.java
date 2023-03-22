package com.manager.service.document;

import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.vo.document.teacher.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandleAllServiceImpl implements HandleAllService {

    private TableReportDAO tableReportDAO;

    private TableExamDAO tableExamDAO;

    private TableIdentifyDAO tableIdentifyDAO;

    private TableAppraisalDAO tableAppraisalDAO;

    private TableSummaryDAO tableSummaryDAO;

    private UserInfoDAO userInfoDAO;

    private StudentEnterpriseDAO studentEnterpriseDAO;

    @Autowired
    public HandleAllServiceImpl(TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO, UserInfoDAO userInfoDAO, StudentEnterpriseDAO studentEnterpriseDAO) {
        this.tableReportDAO = tableReportDAO;
        this.tableExamDAO = tableExamDAO;
        this.tableIdentifyDAO = tableIdentifyDAO;
        this.tableAppraisalDAO = tableAppraisalDAO;
        this.tableSummaryDAO = tableSummaryDAO;
        this.userInfoDAO = userInfoDAO;
        this.studentEnterpriseDAO = studentEnterpriseDAO;
    }

    /**
     * handleAll
     * 根据校内导师ID、学生ID、表单编号和表单月次返回获取对应表单的详细信息
     */
    @Override
    public Object handleAll(String teacherId, String studentId, Integer category, Integer order) {
        Object table = new Object();
        UserInfo stuInfo = userInfoDAO.findById(studentId).get();
        UserInfo teaInfo = userInfoDAO.findById(teacherId).get();
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(studentId);

        // 根据五种表格的类别来区分操作
        // case 1: Report
        // case 2: Exam
        // case 3: Identify
        // case 4: Appraisal
        // case 5: Summary
        switch (category) {
            case 1:
                TeacherTableReportVO teacherTableReportVO = new TeacherTableReportVO();
                teacherTableReportVO.setClassName(stuInfo.getClassName());
                teacherTableReportVO.setName(stuInfo.getName());
                teacherTableReportVO.setStudentId(studentId);
                teacherTableReportVO.setTeacherName(teaInfo.getName());
                teacherTableReportVO.setEpName(studentEnterprise.getEnterpriseName());
                teacherTableReportVO.setEpCity(studentEnterprise.getEnterpriseCity());
                teacherTableReportVO.setInstructorName(studentEnterprise.getInstructorName());
                teacherTableReportVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                teacherTableReportVO.setInstructorEmail(studentEnterprise.getInstructorEmail());
                TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNum(studentId, order);
                if (tableReport != null) {
                    teacherTableReportVO.setStartDate(tableReport.getStartDate().toString());
                    teacherTableReportVO.setEndDate(tableReport.getEndDate().toString());
                    teacherTableReportVO.setProject(tableReport.getProject());
                    teacherTableReportVO.setLivingAddress(tableReport.getLivingAddress());
                    teacherTableReportVO.setLivingPhone(tableReport.getLivingPhone());
                    teacherTableReportVO.setLivingCondition(tableReport.getLivingCondition());
                    teacherTableReportVO.setJobContent(tableReport.getJobContent());
                    teacherTableReportVO.setRequirements(tableReport.getRequirements());
                }
                table = teacherTableReportVO;
                break;
            case 2:
                TeacherTableExamVO teacherTableExamVO = new TeacherTableExamVO();
                teacherTableExamVO.setClassName(stuInfo.getClassName());
                teacherTableExamVO.setName(stuInfo.getName());
                teacherTableExamVO.setStudentId(studentId);
                teacherTableExamVO.setTeacherName(teaInfo.getName());
                teacherTableExamVO.setInstructorName(studentEnterprise.getInstructorName());
                teacherTableExamVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                teacherTableExamVO.setInstructorEmail(studentEnterprise.getInstructorEmail());
                teacherTableExamVO.setEpName(studentEnterprise.getEnterpriseName());
                teacherTableExamVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNum(studentId, order);
                if (tableExam != null) {
                    teacherTableExamVO.setStartDate(tableExam.getStartDate().toString());
                    teacherTableExamVO.setEndDate(tableExam.getEndDate().toString());
                    teacherTableExamVO.setProject(tableExam.getProject());
                    teacherTableExamVO.setAttendance(tableExam.getAttendance());
                    teacherTableExamVO.setEptSuggestions(tableExam.getEptSuggestions());
                }
                table = teacherTableExamVO;
                break;
            case 3:
                TeacherTableIdentifyVO teacherTableIdentifyVO = new TeacherTableIdentifyVO();
                teacherTableIdentifyVO.setClassName(stuInfo.getClassName());
                teacherTableIdentifyVO.setName(stuInfo.getName());
                teacherTableIdentifyVO.setSex(stuInfo.getSex());
                teacherTableIdentifyVO.setStudentId(studentId);
                teacherTableIdentifyVO.setEpName(studentEnterprise.getEnterpriseName());
                teacherTableIdentifyVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableIdentify tableIdentify = tableIdentifyDAO.findByStudentId(studentId);
                if (tableIdentify != null) {
                    teacherTableIdentifyVO.setStartDate(tableIdentify.getStartDate().toString());
                    teacherTableIdentifyVO.setEndDate(tableIdentify.getEndDate().toString());
                    teacherTableIdentifyVO.setProject(tableIdentify.getProject());
                    teacherTableIdentifyVO.setPersonalSummary(tableIdentify.getPersonalSummary());
                    teacherTableIdentifyVO.setAttendance(tableIdentify.getAttendance());
                    teacherTableIdentifyVO.setEptSuggestions(tableIdentify.getEptSuggestions());
                    teacherTableIdentifyVO.setEpSuggestions(tableIdentify.getEpSuggestions());
                }
                table = teacherTableIdentifyVO;
                break;
            case 4:
                TeacherTableAppraisalVO teacherTableAppraisalVO = new TeacherTableAppraisalVO();
                teacherTableAppraisalVO.setName(stuInfo.getName());
                teacherTableAppraisalVO.setStudentId(studentId);
                teacherTableAppraisalVO.setInstructorName(studentEnterprise.getInstructorName());
                teacherTableAppraisalVO.setInstructorPhone(studentEnterprise.getInstructorPhone());
                teacherTableAppraisalVO.setEpName(studentEnterprise.getEnterpriseName());
                teacherTableAppraisalVO.setEpCity(studentEnterprise.getEnterpriseCity());
                TableAppraisal tableAppraisal = tableAppraisalDAO.findByStudentId(studentId);
                if (tableAppraisal != null) {
                    teacherTableAppraisalVO.setStartDate(tableAppraisal.getStartDate().toString());
                    teacherTableAppraisalVO.setEndDate(tableAppraisal.getEndDate().toString());
                    teacherTableAppraisalVO.setProject(tableAppraisal.getProject());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation1(tableAppraisal.getEnterpriseTeacherEvaluation1());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation2(tableAppraisal.getEnterpriseTeacherEvaluation2());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation3(tableAppraisal.getEnterpriseTeacherEvaluation3());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation4(tableAppraisal.getEnterpriseTeacherEvaluation4());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation5(tableAppraisal.getEnterpriseTeacherEvaluation5());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation6(tableAppraisal.getEnterpriseTeacherEvaluation6());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation7(tableAppraisal.getEnterpriseTeacherEvaluation7());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation8(tableAppraisal.getEnterpriseTeacherEvaluation8());
                    teacherTableAppraisalVO.setEnterpriseTeacherEvaluation9(tableAppraisal.getEnterpriseTeacherEvaluation9());
                    teacherTableAppraisalVO.setEnterpriseTeacherSuggestions(tableAppraisal.getEnterpriseTeacherSuggestions());
                }
                table = teacherTableAppraisalVO;
                break;
            case 5:
                TeacherTableSummaryVO teacherTableSummaryVO = new TeacherTableSummaryVO();
                teacherTableSummaryVO.setClassName(stuInfo.getClassName());
                teacherTableSummaryVO.setStudentId(studentId);
                teacherTableSummaryVO.setName(stuInfo.getName());
                teacherTableSummaryVO.setPhone(stuInfo.getPhone());
                TableSummary tableSummary = tableSummaryDAO.findByStudentId(studentId);
                if (tableSummary != null) {
                    teacherTableSummaryVO.setSummaryReport(tableSummary.getSummaryReport());
                }
                table = teacherTableSummaryVO;
                break;
        }
        return table;
    }
}
