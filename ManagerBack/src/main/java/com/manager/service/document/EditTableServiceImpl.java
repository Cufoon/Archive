package com.manager.service.document;

import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.form.document.EditTableParam;
import com.manager.form.document.FormDataParam;
import com.manager.vo.document.student.EditTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EditTableServiceImpl implements EditTableService {

    private TableReportDAO tableReportDAO;

    private TableExamDAO tableExamDAO;

    private TableIdentifyDAO tableIdentifyDAO;

    private TableAppraisalDAO tableAppraisalDAO;

    private TableSummaryDAO tableSummaryDAO;

    private StudentTeacherRelationDAO studentTeacherRelationDAO;

    @Autowired
    public EditTableServiceImpl(TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO, StudentTeacherRelationDAO studentTeacherRelationDAO) {
        this.tableReportDAO = tableReportDAO;
        this.tableExamDAO = tableExamDAO;
        this.tableIdentifyDAO = tableIdentifyDAO;
        this.tableAppraisalDAO = tableAppraisalDAO;
        this.tableSummaryDAO = tableSummaryDAO;
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
    }

    /**
     * editTable
     * 根据学生ID、表单编号、表单月次和表单数据保存学生所填写的表单内容，返回保存是否成功
     */
    @Override
    public EditTableVO editTable(EditTableParam editTableParam, int status) {
        EditTableVO editTableVO = new EditTableVO();
        FormDataParam formDataParam = editTableParam.getFormData();
        StudentTeacherRelation strInfo = studentTeacherRelationDAO.findByStudentIdAndState(editTableParam.getId(), 1);
        Date currentDate = new Date();
        java.sql.Timestamp currentSqlDate = new java.sql.Timestamp(currentDate.getTime());
        java.sql.Date sqlStartDate = null;
        java.sql.Date sqlEndDate = null;
        if (formDataParam.getStartDate() != null) {
            try {
                java.util.Date javaStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(formDataParam.getStartDate());
                sqlStartDate = new java.sql.Date(javaStartDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (formDataParam.getEndDate() != null) {
            try {
                java.util.Date javaEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(formDataParam.getEndDate());
                sqlEndDate = new java.sql.Date(javaEndDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        根据五种表格的类别来区分操作
//        case 1: Report
//        case 2: Exam
//        case 3: Identify
//        case 4: Appraisal
//        case 5: Summary
        switch (editTableParam.getCategory()) {
            case 1:
                TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNum(editTableParam.getId(), editTableParam.getOrder());
                if (tableReport == null) {
                    tableReport = new TableReport();
                }
                tableReport.setStudentId(editTableParam.getId());
                tableReport.setTeacherId(strInfo.getTeacherId());
                tableReport.setOrderNum(editTableParam.getOrder());
                tableReport.setStartDate(sqlStartDate);
                tableReport.setEndDate(sqlEndDate);
                tableReport.setProject(formDataParam.getProjectName());
                tableReport.setLivingPhone(formDataParam.getLivingPhone());
                tableReport.setLivingAddress(formDataParam.getLivingAddress());
                tableReport.setLivingCondition(formDataParam.getLivingCondition());
                tableReport.setJobContent(formDataParam.getJobContent());
                tableReport.setRequirements(formDataParam.getRequirements());
                if (status == 1) {
                    tableReport.setSubmitTime(currentSqlDate);
                }
                tableReport.setCheckTime(null);
                tableReport.setStatus(status);
                this.tableReportDAO.saveAndFlush(tableReport);
                break;
            case 2:
                TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNum(editTableParam.getId(), editTableParam.getOrder());
                if (tableExam == null) {
                    tableExam = new TableExam();
                }
                tableExam.setStudentId(editTableParam.getId());
                tableExam.setTeacherId(strInfo.getTeacherId());
                tableExam.setOrderNum(editTableParam.getOrder());
                tableExam.setStartDate(sqlStartDate);
                tableExam.setEndDate(sqlEndDate);
                tableExam.setProject(formDataParam.getProjectName());
                tableExam.setAttendance(formDataParam.getAttendance());
                tableExam.setEptSuggestions(formDataParam.getEptSuggestions());
                if (status == 1) {
                    tableExam.setSubmitTime(currentSqlDate);
                }
                tableExam.setCheckTime(null);
                tableExam.setStatus(status);
                this.tableExamDAO.saveAndFlush(tableExam);
                break;
            case 3:
                TableIdentify tableIdentify = tableIdentifyDAO.findByStudentId(editTableParam.getId());
                if (tableIdentify == null) {
                    tableIdentify = new TableIdentify();
                }
                tableIdentify.setStudentId(editTableParam.getId());
                tableIdentify.setTeacherId(strInfo.getTeacherId());
                tableIdentify.setStartDate(sqlStartDate);
                tableIdentify.setEndDate(sqlEndDate);
                tableIdentify.setProject(formDataParam.getProjectName());
                tableIdentify.setPersonalSummary(formDataParam.getPersonalSummary());
                tableIdentify.setAttendance(formDataParam.getAttendance());
                tableIdentify.setEptSuggestions(formDataParam.getEptSuggestions());
                tableIdentify.setEpSuggestions(formDataParam.getEpSuggestions());
                if (status == 1) {
                    tableIdentify.setSubmitTime(currentSqlDate);
                }
                tableIdentify.setCheckTime(null);
                tableIdentify.setStatus(status);
                this.tableIdentifyDAO.saveAndFlush(tableIdentify);
                break;
            case 4:
                TableAppraisal tableAppraisal = tableAppraisalDAO.findByStudentId(editTableParam.getId());
                if (tableAppraisal == null) {
                    tableAppraisal = new TableAppraisal();
                }
                tableAppraisal.setStudentId(editTableParam.getId());
                tableAppraisal.setTeacherId(strInfo.getTeacherId());
                tableAppraisal.setStartDate(sqlStartDate);
                tableAppraisal.setEndDate(sqlEndDate);
                tableAppraisal.setProject(formDataParam.getProjectName());
                tableAppraisal.setEnterpriseTeacherEvaluation1(formDataParam.getEptEvaluation1());
                tableAppraisal.setEnterpriseTeacherEvaluation2(formDataParam.getEptEvaluation2());
                tableAppraisal.setEnterpriseTeacherEvaluation3(formDataParam.getEptEvaluation3());
                tableAppraisal.setEnterpriseTeacherEvaluation4(formDataParam.getEptEvaluation4());
                tableAppraisal.setEnterpriseTeacherEvaluation5(formDataParam.getEptEvaluation5());
                tableAppraisal.setEnterpriseTeacherEvaluation6(formDataParam.getEptEvaluation6());
                tableAppraisal.setEnterpriseTeacherEvaluation7(formDataParam.getEptEvaluation7());
                tableAppraisal.setEnterpriseTeacherEvaluation8(formDataParam.getEptEvaluation8());
                tableAppraisal.setEnterpriseTeacherEvaluation9(formDataParam.getEptEvaluation9());
                tableAppraisal.setEnterpriseTeacherSuggestions(formDataParam.getEptSuggestions());
                if (status == 1) {
                    tableAppraisal.setSubmitTime(currentSqlDate);
                }
                tableAppraisal.setCheckTime(null);
                tableAppraisal.setStatus(status);
                this.tableAppraisalDAO.saveAndFlush(tableAppraisal);
                break;
            case 5:
                TableSummary tableSummary = tableSummaryDAO.findByStudentId(editTableParam.getId());
                if (tableSummary == null) {
                    tableSummary = new TableSummary();
                }
                tableSummary.setStudentId(editTableParam.getId());
                tableSummary.setTeacherId(strInfo.getTeacherId());
                tableSummary.setSummaryReport(formDataParam.getSummaryReport());
                if (status == 1) {
                    tableSummary.setSubmitTime(currentSqlDate);
                }
                tableSummary.setCheckTime(null);
                tableSummary.setStatus(status);
                this.tableSummaryDAO.saveAndFlush(tableSummary);
                break;
        }
        editTableVO.setEdited(true);
        return editTableVO;
    }
}
