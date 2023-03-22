package com.manager.service.document;

import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.vo.document.teacher.DocumentBrowseVO;
import com.manager.vo.document.teacher.TeacherTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BrowseAllServiceImpl implements BrowseAllService {

    private final TableReportDAO tableReportDAO;

    private final TableExamDAO tableExamDAO;

    private final TableIdentifyDAO tableIdentifyDAO;

    private final TableAppraisalDAO tableAppraisalDAO;

    private final TableSummaryDAO tableSummaryDAO;

    private final UserInfoDAO userInfoDAO;

    private final DeadlineDAO deadlineDAO;

    @Autowired
    public BrowseAllServiceImpl(TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO, UserInfoDAO userInfoDAO, DeadlineDAO deadlineDAO) {

        this.tableReportDAO = tableReportDAO;

        this.tableExamDAO = tableExamDAO;

        this.tableIdentifyDAO = tableIdentifyDAO;

        this.tableAppraisalDAO = tableAppraisalDAO;

        this.tableSummaryDAO = tableSummaryDAO;

        this.userInfoDAO = userInfoDAO;

        this.deadlineDAO = deadlineDAO;
    }

    /**
     * browseAll
     * 根据校内导师ID返回所有学生填写过的表单的基础信息
     */
    @Override
    public DocumentBrowseVO browseAll(String teacherId) {
        DocumentBrowseVO documentBrowseVO = new DocumentBrowseVO();
        List<TableReport> tableReportList = tableReportDAO.findByTeacherId(teacherId);
        List<TableExam> tableExamList = tableExamDAO.findByTeacherId(teacherId);
        List<TableIdentify> tableIdentifyList = tableIdentifyDAO.findByTeacherId(teacherId);
        List<TableAppraisal> tableAppraisalList = tableAppraisalDAO.findByTeacherId(teacherId);
        List<TableSummary> tableSummaryList = tableSummaryDAO.findByTeacherId(teacherId);
        List<TeacherTableVO> teacherTableVOList = new ArrayList<>();
        Date currentDate = new Date();
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 检索出该学生所有表格信息
        // 根据以上所有表格按表不同类别、同一类别不同月份提交区分
        for (TableReport tableReport : tableReportList) {
            if (tableReport.getStatus() != 1 && tableReport.getStatus() != 3) {
                continue;
            }
            TeacherTableVO teacherTableVO = new TeacherTableVO();
            teacherTableVO.setFName("湖南大学学生校外实践情况阶段汇报表");
            teacherTableVO.setStudentId(tableReport.getStudentId());
            teacherTableVO.setCategory(1);
            teacherTableVO.setOrder(tableReport.getOrderNum());
            Deadline deadline = deadlineDAO.findByStudentId(tableReport.getStudentId());
            if (deadline == null) {
                deadline = new Deadline();
            }
            switch (tableReport.getOrderNum()) {
                case 1:
                    teacherTableVO.setDeadline(deadline.getReportDeadline1().toString());
                    teacherTableVO.setDue(currentDate.after(deadline.getReportDeadline1()));
                    break;
                case 2:
                    teacherTableVO.setDeadline(deadline.getReportDeadline2().toString());
                    teacherTableVO.setDue(currentDate.after(deadline.getReportDeadline2()));
                    break;
                case 3:
                    teacherTableVO.setDeadline(deadline.getReportDeadline3().toString());
                    teacherTableVO.setDue(currentDate.after(deadline.getReportDeadline3()));
                    break;
            }
            if (tableReport.getCheckTime() == null) {
                teacherTableVO.setCheckTime("");
            } else {
                try {
                    if (null != tableReport.getCheckTime()) {
                        teacherTableVO.setCheckTime(simpleDateFormat.format(tableReport.getCheckTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            if (tableReport.getSubmitTime() == null) {
                teacherTableVO.setSubmitTime("");
            } else {
                try {
                    if (null != tableReport.getSubmitTime()) {
                        teacherTableVO.setSubmitTime(simpleDateFormat.format(tableReport.getSubmitTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            UserInfo stuInfo = userInfoDAO.findById(tableReport.getStudentId()).get();
            teacherTableVO.setStudentName(stuInfo.getName());
            teacherTableVO.setEvaluation(tableReport.getEvaluation());
            teacherTableVO.setStatus(tableReport.getStatus());
            teacherTableVOList.add(teacherTableVO);
        }

        for (TableExam tableExam : tableExamList) {
            if (tableExam.getStatus() != 1 && tableExam.getStatus() != 3) {
                continue;
            }
            TeacherTableVO teacherTableVO = new TeacherTableVO();
            teacherTableVO.setFName("湖南大学学生校外实践阶段检查表");
            teacherTableVO.setStudentId(tableExam.getStudentId());
            teacherTableVO.setCategory(2);
            teacherTableVO.setOrder(tableExam.getOrderNum());
            Deadline deadline = deadlineDAO.findByStudentId(tableExam.getStudentId());
            switch (tableExam.getOrderNum()) {
                case 1:
                    teacherTableVO.setDeadline(deadline.getExamDeadline1().toString());
                    teacherTableVO.setDue(currentDate.after(deadline.getExamDeadline1()));
                    break;
                case 2:
                    teacherTableVO.setDeadline(deadline.getExamDeadline2().toString());
                    teacherTableVO.setDue(currentDate.after(deadline.getExamDeadline2()));
                    break;
                case 3:
                    teacherTableVO.setDeadline(deadline.getExamDeadline3().toString());
                    teacherTableVO.setDue(currentDate.after(deadline.getExamDeadline3()));
                    break;
            }
            if (tableExam.getCheckTime() == null) {
                teacherTableVO.setCheckTime("");
            } else {
                try {
                    if (null != tableExam.getCheckTime()) {
                        teacherTableVO.setCheckTime(simpleDateFormat.format(tableExam.getCheckTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            if (tableExam.getSubmitTime() == null) {
                teacherTableVO.setSubmitTime("");
            } else {
                try {
                    if (null != tableExam.getSubmitTime()) {
                        teacherTableVO.setSubmitTime(simpleDateFormat.format(tableExam.getSubmitTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            UserInfo stuInfo = userInfoDAO.findById(tableExam.getStudentId()).get();
            teacherTableVO.setStudentName(stuInfo.getName());
            teacherTableVO.setEvaluation(tableExam.getEvaluation());
            teacherTableVO.setStatus(tableExam.getStatus());
            teacherTableVOList.add(teacherTableVO);
        }

        for (TableIdentify tableIdentify : tableIdentifyList) {
            if (tableIdentify.getStatus() != 1 && tableIdentify.getStatus() != 3) {
                continue;
            }
            TeacherTableVO teacherTableVO = new TeacherTableVO();
            teacherTableVO.setFName("湖南大学校外实践鉴定表");
            teacherTableVO.setStudentId(tableIdentify.getStudentId());
            teacherTableVO.setCategory(3);
            teacherTableVO.setOrder(1);
            Deadline deadline = deadlineDAO.findByStudentId(tableIdentify.getStudentId());
            teacherTableVO.setDeadline(deadline.getIdentifyDeadline().toString());
            teacherTableVO.setDue(currentDate.after(deadline.getIdentifyDeadline()));
            if (tableIdentify.getCheckTime() == null) {
                teacherTableVO.setCheckTime("");
            } else {
                try {
                    if (null != tableIdentify.getCheckTime()) {
                        teacherTableVO.setCheckTime(simpleDateFormat.format(tableIdentify.getCheckTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            if (tableIdentify.getSubmitTime() == null) {
                teacherTableVO.setSubmitTime("");
            } else {
                try {
                    if (null != tableIdentify.getSubmitTime()) {
                        teacherTableVO.setSubmitTime(simpleDateFormat.format(tableIdentify.getSubmitTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            UserInfo stuInfo = userInfoDAO.findById(tableIdentify.getStudentId()).get();
            teacherTableVO.setStudentName(stuInfo.getName());
            teacherTableVO.setEvaluation(tableIdentify.getEvaluation());
            teacherTableVO.setStatus(tableIdentify.getStatus());
            teacherTableVOList.add(teacherTableVO);
        }
        for (TableAppraisal tableAppraisal : tableAppraisalList) {
            if (tableAppraisal.getStatus() != 1 && tableAppraisal.getStatus() != 3) {
                continue;
            }
            TeacherTableVO teacherTableVO = new TeacherTableVO();
            teacherTableVO.setFName("湖南大学学生校外实践企业导师评价表");
            teacherTableVO.setStudentId(tableAppraisal.getStudentId());
            teacherTableVO.setCategory(4);
            teacherTableVO.setOrder(1);
            Deadline deadline = deadlineDAO.findByStudentId(tableAppraisal.getStudentId());
            teacherTableVO.setDeadline(deadline.getAppraisalDeadline().toString());
            teacherTableVO.setDue(currentDate.after(deadline.getAppraisalDeadline()));
            if (tableAppraisal.getCheckTime() == null) {
                teacherTableVO.setCheckTime("");
            } else {
                try {
                    if (null != tableAppraisal.getCheckTime()) {
                        teacherTableVO.setCheckTime(simpleDateFormat.format(tableAppraisal.getCheckTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            if (tableAppraisal.getSubmitTime() == null) {
                teacherTableVO.setSubmitTime("");
            } else {
                try {
                    if (null != tableAppraisal.getSubmitTime()) {
                        teacherTableVO.setSubmitTime(simpleDateFormat.format(tableAppraisal.getSubmitTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            UserInfo stuInfo = userInfoDAO.findById(tableAppraisal.getStudentId()).get();
            teacherTableVO.setStudentName(stuInfo.getName());
            teacherTableVO.setEvaluation(tableAppraisal.getEvaluation());
            teacherTableVO.setStatus(tableAppraisal.getStatus());
            teacherTableVOList.add(teacherTableVO);
        }

        for (TableSummary tableSummary : tableSummaryList) {
            if (tableSummary.getStatus() != 1 && tableSummary.getStatus() != 3) {
                continue;
            }
            TeacherTableVO teacherTableVO = new TeacherTableVO();
            teacherTableVO.setFName("湖南大学学生实习总结报告");
            teacherTableVO.setStudentId(tableSummary.getStudentId());
            teacherTableVO.setCategory(5);
            teacherTableVO.setOrder(1);
            Deadline deadline = deadlineDAO.findByStudentId(tableSummary.getStudentId());
            teacherTableVO.setDeadline(deadline.getSummaryDeadline().toString());
            teacherTableVO.setDue(currentDate.after(deadline.getSummaryDeadline()));
            if (tableSummary.getCheckTime() == null) {
                teacherTableVO.setCheckTime("");
            } else {
                try {
                    if (null != tableSummary.getCheckTime()) {
                        teacherTableVO.setCheckTime(simpleDateFormat.format(tableSummary.getCheckTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            if (tableSummary.getSubmitTime() == null) {
                teacherTableVO.setSubmitTime("");
            } else {
                try {
                    if (null != tableSummary.getSubmitTime()) {
                        teacherTableVO.setSubmitTime(simpleDateFormat.format(tableSummary.getSubmitTime()));
                    }
                } catch (Exception e) {
//                    logger.warn("TimeStamp转换String格式出错", e);
                }
            }
            UserInfo stuInfo = userInfoDAO.findById(tableSummary.getStudentId()).get();
            teacherTableVO.setStudentName(stuInfo.getName());
            teacherTableVO.setEvaluation(tableSummary.getEvaluation());
            teacherTableVO.setStatus(tableSummary.getStatus());
            teacherTableVOList.add(teacherTableVO);
        }
        documentBrowseVO.setForm(teacherTableVOList);
        return documentBrowseVO;
    }
}
