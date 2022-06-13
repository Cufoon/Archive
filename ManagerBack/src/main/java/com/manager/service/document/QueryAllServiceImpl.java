package com.manager.service.document;

import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.vo.document.student.DocumentQueryVO;
import com.manager.vo.document.student.TableVO;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class QueryAllServiceImpl implements QueryAllService {

    private TableReportDAO tableReportDAO;

    private TableExamDAO tableExamDAO;

    private TableIdentifyDAO tableIdentifyDAO;

    private TableAppraisalDAO tableAppraisalDAO;

    private TableSummaryDAO tableSummaryDAO;

    private StudentEnterpriseDAO studentEnterpriseDAO;

    private DeadlineDAO deadlineDAO;

    @Autowired
    public QueryAllServiceImpl(TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO, StudentEnterpriseDAO studentEnterpriseDAO, DeadlineDAO deadlineDAO) {
        this.tableReportDAO = tableReportDAO;
        this.tableExamDAO = tableExamDAO;
        this.tableIdentifyDAO = tableIdentifyDAO;
        this.tableAppraisalDAO = tableAppraisalDAO;
        this.tableSummaryDAO = tableSummaryDAO;
        this.studentEnterpriseDAO = studentEnterpriseDAO;
        this.deadlineDAO = deadlineDAO;
    }

    /**
     * queryAll
     * 根据学生ID返回学生所有表格信息
     */
    @Override
    public DocumentQueryVO queryAll(@NotNull String studentId) {
        DocumentQueryVO documentQueryVO = new DocumentQueryVO();
        List<TableVO> tableVOList = new ArrayList<>();
        TableVO tableVO;
//        检索出该学生实习时间、每一份表格的截止时间
        Deadline deadline = deadlineDAO.findByStudentId(studentId);
        if (deadline == null) {
            deadline = new Deadline();
        }

//        检索出该学生所有表格信息
//        根据以上所有表格按表不同类别、同一类别不同月份提交区分
//        以下为三份 Report 表
        Date currentDate = new Date();
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i <= 3; i++) {
            tableVO = new TableVO();
            TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNum(studentId, i);
            if (tableReport == null) {
                tableReport = new TableReport();
            }
            tableVO.setStudentId(studentId);
            tableVO.setFileName("湖南大学学生校外实践情况阶段汇报表");
            tableVO.setCategory(1);
            tableVO.setOrder(i);
            switch (i) {
                case 1:
                    tableVO.setDeadline(calDay(currentDate, deadline.getReportDeadline1()));
                    tableVO.setDue(currentDate.after(deadline.getReportDeadline1()));
                    break;
                case 2:
                    tableVO.setDeadline(calDay(currentDate, deadline.getReportDeadline2()));
                    tableVO.setDue(currentDate.after(deadline.getReportDeadline2()));
                    break;
                case 3:
                    tableVO.setDeadline(calDay(currentDate, deadline.getReportDeadline3()));
                    tableVO.setDue(currentDate.after(deadline.getReportDeadline3()));
                    break;
            }
            if (tableReport.getCheckTime() == null) {
                tableVO.setCheckTime("");
            } else {
                try {
                    if (null != tableReport.getCheckTime()) {
                        tableVO.setCheckTime(simpleDateFormat.format(tableReport.getCheckTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            if (tableReport.getSubmitTime() == null) {
                tableVO.setSubmitTime("");
            } else {
                try {
                    if (null != tableReport.getSubmitTime()) {
                        tableVO.setSubmitTime(simpleDateFormat.format(tableReport.getSubmitTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            tableVO.setEvaluation(tableReport.getEvaluation());
            tableVO.setStatus(tableReport.getStatus() == null ? 0 : tableReport.getStatus());
            tableVOList.add(tableVO);
        }

        //        以下为三份 Exam 表
        for (int i = 1; i <= 3; i++) {
            tableVO = new TableVO();
            TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNum(studentId, i);
            if (tableExam == null) {
                tableExam = new TableExam();
            }
            tableVO.setStudentId(studentId);
            tableVO.setFileName("湖南大学学生校外实践阶段检查表");
            tableVO.setCategory(2);
            tableVO.setOrder(i);
            switch (i) {
                case 1:
                    tableVO.setDeadline(calDay(currentDate, deadline.getExamDeadline1()));
                    tableVO.setDue(currentDate.after(deadline.getExamDeadline1()));
                    break;
                case 2:
                    tableVO.setDeadline(calDay(currentDate, deadline.getExamDeadline2()));
                    tableVO.setDue(currentDate.after(deadline.getExamDeadline2()));
                    break;
                case 3:
                    tableVO.setDeadline(calDay(currentDate, deadline.getExamDeadline3()));
                    tableVO.setDue(currentDate.after(deadline.getExamDeadline3()));
                    break;
            }
            if (tableExam.getCheckTime() == null) {
                tableVO.setCheckTime("");
            } else {
                try {
                    if (null != tableExam.getCheckTime()) {
                        tableVO.setCheckTime(simpleDateFormat.format(tableExam.getCheckTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            if (tableExam.getSubmitTime() == null) {
                tableVO.setSubmitTime("");
            } else {
                try {
                    if (null != tableExam.getSubmitTime()) {
                        tableVO.setSubmitTime(simpleDateFormat.format(tableExam.getSubmitTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            tableVO.setEvaluation(tableExam.getEvaluation());
            tableVO.setStatus(tableExam.getStatus() == null ? 0 : tableExam.getStatus());
            tableVOList.add(tableVO);
        }

//        以下为 Identify 表
        TableIdentify tableIdentify = tableIdentifyDAO.findByStudentId(studentId);
        if (tableIdentify == null) {
            tableIdentify = new TableIdentify();
        }
        tableVO = new TableVO();
        tableVO.setStudentId(tableIdentify.getStudentId());
        tableVO.setFileName("湖南大学校外实践鉴定表");
        tableVO.setCategory(3);
        tableVO.setOrder(1);
        tableVO.setDeadline(calDay(currentDate, deadline.getIdentifyDeadline()));
        tableVO.setDue(currentDate.after(deadline.getIdentifyDeadline()));
        if (tableIdentify.getCheckTime() == null) {
            tableVO.setCheckTime("");
        } else {
            try {
                if (null != tableIdentify.getCheckTime()) {
                    tableVO.setCheckTime(simpleDateFormat.format(tableIdentify.getCheckTime()));
                }
            } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
            }
        }
        if (tableIdentify.getSubmitTime() == null) {
            tableVO.setSubmitTime("");
        } else {
            try {
                if (null != tableIdentify.getSubmitTime()) {
                    tableVO.setSubmitTime(simpleDateFormat.format(tableIdentify.getSubmitTime()));
                }
            } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
            }
        }
        tableVO.setEvaluation(tableIdentify.getEvaluation());
        tableVO.setStatus(tableIdentify.getStatus() == null ? 0 : tableIdentify.getStatus());
        tableVOList.add(tableVO);

//        以下为 Appraisal 表
        TableAppraisal tableAppraisal = tableAppraisalDAO.findByStudentId(studentId);
        if (tableAppraisal == null) {
            tableAppraisal = new TableAppraisal();
        }
        tableVO = new TableVO();
        tableVO.setStudentId(tableAppraisal.getStudentId());
        tableVO.setFileName("湖南大学学生校外实践企业导师评价表");
        tableVO.setCategory(4);
        tableVO.setOrder(1);
        tableVO.setDeadline(calDay(currentDate, deadline.getAppraisalDeadline()));
        tableVO.setDue(currentDate.after(deadline.getAppraisalDeadline()));
        if (tableAppraisal.getCheckTime() == null) {
            tableVO.setCheckTime("");
        } else {
            try {
                if (null != tableAppraisal.getCheckTime()) {
                    tableVO.setCheckTime(simpleDateFormat.format(tableAppraisal.getCheckTime()));
                }
            } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
            }
        }
        if (tableAppraisal.getSubmitTime() == null) {
            tableVO.setSubmitTime("");
        } else {
            try {
                if (null != tableAppraisal.getSubmitTime()) {
                    tableVO.setSubmitTime(simpleDateFormat.format(tableAppraisal.getSubmitTime()));
                }
            } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
            }
        }
        tableVO.setEvaluation(tableAppraisal.getEvaluation());
        tableVO.setStatus(tableAppraisal.getStatus() == null ? 0 : tableAppraisal.getStatus());
        tableVOList.add(tableVO);

//        以下为 Summary 表
        TableSummary tableSummary = tableSummaryDAO.findByStudentId(studentId);
        if (tableSummary == null) {
            tableSummary = new TableSummary();
        }
        tableVO = new TableVO();
        tableVO.setStudentId(tableSummary.getStudentId());
        tableVO.setFileName("湖南大学学生实习总结报告");
        tableVO.setCategory(5);
        tableVO.setOrder(1);
        tableVO.setDeadline(calDay(currentDate, deadline.getSummaryDeadline()));
        tableVO.setDue(currentDate.after(deadline.getSummaryDeadline()));
        if (tableSummary.getCheckTime() == null) {
            tableVO.setCheckTime("");
        } else {
            try {
                if (null != tableSummary.getCheckTime()) {
                    tableVO.setCheckTime(simpleDateFormat.format(tableSummary.getCheckTime()));
                }
            } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
            }
        }
        if (tableSummary.getSubmitTime() == null) {
            tableVO.setSubmitTime("");
        } else {
            try {
                if (null != tableSummary.getSubmitTime()) {
                    tableVO.setSubmitTime(simpleDateFormat.format(tableSummary.getSubmitTime()));
                }
            } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
            }
        }
        tableVO.setEvaluation(tableSummary.getEvaluation());
        tableVO.setStatus(tableSummary.getStatus() == null ? 0 : tableSummary.getStatus());
        tableVOList.add(tableVO);

//        根据以上对所有表格的检索信息按照既定顺序进行排序
        tableVOList.sort((t1, t2) -> {
            if (!t1.getCategory().equals(t2.getCategory())) {
                return t1.getCategory().compareTo(t2.getCategory());
            } else {
                return t1.getOrder().compareTo(t2.getOrder());
            }
        });

        documentQueryVO.setForm(tableVOList);
        return documentQueryVO;
    }

    /**
     * calDay
     * 计算两个日期相差的天数
     */
    private String calDay(Date current, Date destination) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        long time1 = calendar.getTimeInMillis();
        calendar.setTime(destination);
        long time2 = calendar.getTimeInMillis();
        long between = (time2 - time1) / (1000 * 3600 * 24);
        return String.valueOf(between);
    }
}
