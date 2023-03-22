package com.manager.service.document;

import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.form.document.HandleTableParam;
import com.manager.form.document.TableDataParam;
import com.manager.vo.document.teacher.HandleTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HandleTableServiceImpl implements HandleTableService {

    private TableReportDAO tableReportDAO;

    private TableExamDAO tableExamDAO;

    private TableIdentifyDAO tableIdentifyDAO;

    private TableAppraisalDAO tableAppraisalDAO;

    private TableSummaryDAO tableSummaryDAO;

    private StudentScoreDAO studentScoreDAO;

    @Autowired
    public HandleTableServiceImpl(TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO, StudentScoreDAO studentScoreDAO) {
        this.tableReportDAO = tableReportDAO;
        this.tableExamDAO = tableExamDAO;
        this.tableIdentifyDAO = tableIdentifyDAO;
        this.tableAppraisalDAO = tableAppraisalDAO;
        this.tableSummaryDAO = tableSummaryDAO;
        this.studentScoreDAO = studentScoreDAO;
    }

    /**
     * handleTable
     * 根据前端数据中的学生ID、表单编号、表单月次和校内导师评价保存校内导师对该表的评价，返回保存是否成功
     */
    @Override
    public HandleTableVO handleTable(HandleTableParam handleTableParam, int status) {
        HandleTableVO handleTableVO = new HandleTableVO();
        TableDataParam tableDataParam = handleTableParam.getTableData();
        if (tableDataParam == null) {
            tableDataParam = new TableDataParam();
        }
        Date currentDate = new Date();
        java.sql.Timestamp currentSqlDate = new java.sql.Timestamp(currentDate.getTime());
        StudentScore studentScore = studentScoreDAO.findByStudentId(handleTableParam.getSid());
        // 根据学生的分数情况选择更新还是新建
        if (studentScore == null) {
            studentScore = new StudentScore();
            studentScore.setStudentId(handleTableParam.getSid());
        }

        // 根据五种表格的类别来区分操作
        // case 1: Report
        // case 2: Exam
        // case 3: Identify
        // case 4: Appraisal
        // case 5: Summary
        switch (handleTableParam.getCategory()) {
            case 1:
                TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNum(handleTableParam.getSid(), handleTableParam.getOrder());
                tableReport.setEvaluation(tableDataParam.getEvaluation());
                tableReport.setTeacherSuggestions(tableDataParam.getTeaSuggestions());
                tableReport.setCheckTime(currentSqlDate);
                tableReport.setStatus(status);
                this.tableReportDAO.saveAndFlush(tableReport);
                switch (handleTableParam.getOrder()) {
                    case 1:
                        studentScore.setReportScore1(tableDataParam.getEvaluation());
                        break;
                    case 2:
                        studentScore.setReportScore2(tableDataParam.getEvaluation());
                        break;
                    case 3:
                        studentScore.setReportScore3(tableDataParam.getEvaluation());
                        break;
                }
                break;
            case 2:
                TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNum(handleTableParam.getSid(), handleTableParam.getOrder());
                tableExam.setEvaluation(tableDataParam.getEvaluation());
                tableExam.setTeacherSuggestions(tableDataParam.getTeaSuggestions());
                tableExam.setCheckTime(currentSqlDate);
                tableExam.setStatus(status);
                this.tableExamDAO.saveAndFlush(tableExam);
                switch (handleTableParam.getOrder()) {
                    case 1:
                        studentScore.setExamScore1(tableDataParam.getEvaluation());
                        break;
                    case 2:
                        studentScore.setExamScore2(tableDataParam.getEvaluation());
                        break;
                    case 3:
                        studentScore.setExamScore3(tableDataParam.getEvaluation());
                        break;
                }
                break;
            case 3:
                TableIdentify tableIdentify = tableIdentifyDAO.findByStudentId(handleTableParam.getSid());
                tableIdentify.setEvaluation(tableDataParam.getEvaluation());
                tableIdentify.setTeacherSuggestions(tableDataParam.getTeaSuggestions());
                tableIdentify.setCheckTime(currentSqlDate);
                tableIdentify.setStatus(status);
                this.tableIdentifyDAO.saveAndFlush(tableIdentify);
                studentScore.setIdentifyScore(tableDataParam.getEvaluation());
                break;
            case 4:
                TableAppraisal tableAppraisal = tableAppraisalDAO.findByStudentId(handleTableParam.getSid());
                tableAppraisal.setTeacherEvaluation1(tableDataParam.getTeaEvaluation1());
                tableAppraisal.setTeacherEvaluation2(tableDataParam.getTeaEvaluation2());
                tableAppraisal.setTeacherEvaluation3(tableDataParam.getTeaEvaluation3());
                tableAppraisal.setEvaluation(tableDataParam.getEvaluation());
                tableAppraisal.setTeacherSuggestions(tableDataParam.getTeaSuggestions());
                tableAppraisal.setCheckTime(currentSqlDate);
                tableAppraisal.setStatus(status);
                this.tableAppraisalDAO.saveAndFlush(tableAppraisal);
                studentScore.setAppraisalScore(tableDataParam.getEvaluation());
                break;
            case 5:
                TableSummary tableSummary = tableSummaryDAO.findByStudentId(handleTableParam.getSid());
                tableSummary.setEvaluation(tableDataParam.getEvaluation());
                tableSummary.setTeacherSuggestions(tableDataParam.getTeaSuggestions());
                tableSummary.setCheckTime(currentSqlDate);
                tableSummary.setStatus(status);
                this.tableSummaryDAO.saveAndFlush(tableSummary);
                studentScore.setSummaryScore(tableDataParam.getEvaluation());
                break;
        }
        double sumScore = (double) (
                (studentScore.getReportScore1() == null ? 0 : studentScore.getReportScore1())
                        + (studentScore.getReportScore2() == null ? 0 : studentScore.getReportScore2())
                        + (studentScore.getReportScore3() == null ? 0 : studentScore.getReportScore3())
                        + (studentScore.getExamScore1() == null ? 0 : studentScore.getExamScore1())
                        + (studentScore.getExamScore2() == null ? 0 : studentScore.getExamScore2())
                        + (studentScore.getExamScore3() == null ? 0 : studentScore.getExamScore3())
                        + (studentScore.getIdentifyScore() == null ? 0 : studentScore.getIdentifyScore())
                        + (studentScore.getAppraisalScore() == null ? 0 : studentScore.getAppraisalScore())
                        + (studentScore.getSummaryScore() == null ? 0 : studentScore.getSummaryScore())
                        + (studentScore.getGroupScore() == null ? 0 : studentScore.getGroupScore())
        ) / 10;
        studentScore.setSumScore(sumScore);
        studentScoreDAO.saveAndFlush(studentScore);

        handleTableVO.setHandled(true);
        return handleTableVO;
    }
}
