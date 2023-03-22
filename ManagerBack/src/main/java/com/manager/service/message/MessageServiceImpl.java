package com.manager.service.message;

import com.manager.constant.MessageConstants;
import com.manager.constant.UserName;
import com.manager.dao.*;
import com.manager.entity.*;
import com.manager.form.message.ReadMessageParam;
import com.manager.form.message.SendMessageParam;
import com.manager.vo.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageDAO messageDAO;

    private final UserInfoDAO userInfoDAO;

    private final DeadlineDAO deadlineDAO;

    private final TableReportDAO tableReportDAO;

    private final TableExamDAO tableExamDAO;

    private final TableIdentifyDAO tableIdentifyDAO;

    private final TableAppraisalDAO tableAppraisalDAO;

    private final TableSummaryDAO tableSummaryDAO;

    @Autowired
    public MessageServiceImpl(MessageDAO messageDAO, UserInfoDAO userInfoDAO, DeadlineDAO deadlineDAO, TableReportDAO tableReportDAO, TableExamDAO tableExamDAO, TableIdentifyDAO tableIdentifyDAO, TableAppraisalDAO tableAppraisalDAO, TableSummaryDAO tableSummaryDAO) {
        this.messageDAO = messageDAO;
        this.userInfoDAO = userInfoDAO;
        this.deadlineDAO = deadlineDAO;
        this.tableReportDAO = tableReportDAO;
        this.tableExamDAO = tableExamDAO;
        this.tableIdentifyDAO = tableIdentifyDAO;
        this.tableAppraisalDAO = tableAppraisalDAO;
        this.tableSummaryDAO = tableSummaryDAO;
    }

    /**
     * 将消息存储到数据库中，公告类型没有接受者列表，消息类型需要使用接受者列表
     *
     * @param senderId 发送者的id
     * @param param    前端参数
     */
    @Override
    public void send(String senderId, SendMessageParam param) {
        String msgTitle = param.getMsgTitle();
        String msgContent = param.getMsgContent();
        String msgType = param.getMsgType();
        String[] recvIds = param.getMsgRecvIds();
        Date now = new Date();
        if (msgType.equals(MessageConstants.TYPE_SPECIFIC)) { // 指定部分用户
            List<Message> list = new LinkedList<>();
            for (String recvId : recvIds) {
                list.add(new Message(
                        senderId,
                        recvId,
                        now,
                        msgTitle,
                        msgContent,
                        MessageConstants.STATE_UNREAD,
                        msgType
                ));
            }
            messageDAO.saveAll(list);
        } else { // 群体公告
            Message message = new Message();
            message.setSenderId(senderId);
            message.setTitle(param.getMsgTitle());
            message.setContent(param.getMsgContent());
            message.setType(param.getMsgType());
            message.setSendTime(now);
            message.setState(MessageConstants.STATE_UNREAD);

            messageDAO.save(message);
        }
    }

    @Override
    public void read(ReadMessageParam param) {
        Integer[] idList = param.getMessageList();
        List<Message> allById = messageDAO.findAllById(Arrays.asList(idList.clone()));
        allById.forEach(message -> { // 所有消息设置为已读
            message.setState(MessageConstants.STATE_READ);
        });
        messageDAO.saveAll(allById);
    }

    @Override
    public MessageListVO queryAllNotifications(Integer roleType) {
        Collection<String> accessList = new ArrayList<>();
        accessList.add(MessageConstants.TYPE_ALL);
        if (roleType == 1) {
            accessList.add(MessageConstants.TYPE_STUDENT);
        } else if (roleType == 2) {
            accessList.add(MessageConstants.TYPE_TEACHER);
        } else {
            accessList.add(MessageConstants.TYPE_STUDENT);
            accessList.add(MessageConstants.TYPE_TEACHER);
        }
        List<MessageVO> list = messageDAO.findAllByRecipientIdIsNullAndTypeIsIn(accessList)
                .stream()
                .map(m -> new MessageVO(
                                m.getMessageId(),
                                m.getType(),
                                m.getTitle(),
                                m.getContent(),
                                getReceiver(m.getType(), m.getRecipientId()),
                                true,
                                getSummary(m.getContent()),
                                getSender(m.getType(), m.getSenderId()),
                                m.getSendTime().toString()
                        )
                )
                .collect(Collectors.toList());
        return new MessageListVO(list);
    }

    @Override
    public MessageListVO queryAllMessages(String id, Integer queryType) {
        List<MessageVO> list = messageDAO.findAllByRecipientId(id)
                .stream()
                .filter(m -> m.getState().equals(queryType))
                .map(m -> new MessageVO(
                        m.getMessageId(),
                        m.getType(),
                        m.getTitle(),
                        m.getContent(),
                        getReceiver(m.getType(), m.getRecipientId()),
                        true,
                        getSummary(m.getContent()),
                        getSender(m.getType(), m.getSenderId()),
                        m.getSendTime().toString())
                )
                .collect(Collectors.toList());
        return new MessageListVO(list);
    }

    @Override
    public AutoVO autoMessage(String studentId) {
        Deadline deadline = deadlineDAO.findByStudentId(studentId);
        Date currentDate = new Date();
        List<AutoContentVO> autoContentVOList = new ArrayList<>();
        if (deadline != null) {

            for (int i = 1; i <= 3; i++) {
                switch (i) {
                    case 1:
                        if (isDeadline(calDay(currentDate, deadline.getReportDeadline1()))) {
                            TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNumAndStatus(studentId, i, 3);
                            if (tableReport == null) {
                                autoContentVOList.add(new AutoContentVO(
                                        "表单提交过期提醒",
                                        "湖南大学校外实践情况阶段汇报表1还有" +
                                                calDay(currentDate, deadline.getReportDeadline1())
                                                + "天到期"
                                ));
                            }
                        }
                        break;
                    case 2:
                        if (isDeadline(calDay(currentDate, deadline.getReportDeadline2()))) {
                            TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNumAndStatus(studentId, i, 3);
                            if (tableReport == null) {
                                autoContentVOList.add(new AutoContentVO(
                                        "表单提交过期提醒",
                                        "湖南大学校外实践情况阶段汇报表2还有" +
                                                calDay(currentDate, deadline.getReportDeadline2())
                                                + "天到期"
                                ));
                            }
                        }
                        break;
                    case 3:
                        if (isDeadline(calDay(currentDate, deadline.getReportDeadline3()))) {
                            TableReport tableReport = tableReportDAO.findByStudentIdAndOrderNumAndStatus(studentId, i, 3);
                            if (tableReport == null) {
                                autoContentVOList.add(new AutoContentVO(
                                        "表单提交过期提醒",
                                        "湖南大学校外实践情况阶段汇报表3还有" +
                                                calDay(currentDate, deadline.getReportDeadline3())
                                                + "天到期"
                                ));
                            }
                        }
                        break;
                }
            }

            for (int i = 1; i <= 3; i++) {
                switch (i) {
                    case 1:
                        if (isDeadline(calDay(currentDate, deadline.getExamDeadline1()))) {
                            TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNumAndStatus(studentId, i, 3);
                            if (tableExam == null) {
                                autoContentVOList.add(new AutoContentVO(
                                        "表单提交过期提醒",
                                        "湖南大学校外实践阶段检查表1还有" +
                                                calDay(currentDate, deadline.getExamDeadline1())
                                                + "天到期"
                                ));
                            }
                        }
                        break;
                    case 2:
                        if (isDeadline(calDay(currentDate, deadline.getExamDeadline2()))) {
                            TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNumAndStatus(studentId, i, 3);
                            if (tableExam == null) {
                                autoContentVOList.add(new AutoContentVO(
                                        "表单提交过期提醒",
                                        "湖南大学校外实践阶段检查表2还有" +
                                                calDay(currentDate, deadline.getExamDeadline2())
                                                + "天到期"
                                ));
                            }
                        }
                        break;
                    case 3:
                        if (isDeadline(calDay(currentDate, deadline.getExamDeadline3()))) {
                            TableExam tableExam = tableExamDAO.findByStudentIdAndOrderNumAndStatus(studentId, i, 3);
                            if (tableExam == null) {
                                autoContentVOList.add(new AutoContentVO(
                                        "表单提交过期提醒",
                                        "湖南大学校外实践阶段检查表3还有" +
                                                calDay(currentDate, deadline.getExamDeadline3())
                                                + "天到期"
                                ));
                            }
                        }
                        break;
                }
            }

            if (isDeadline(calDay(currentDate, deadline.getIdentifyDeadline()))) {
                TableIdentify tableIdentify = tableIdentifyDAO.findByStudentIdAndStatus(studentId, 3);
                if (tableIdentify == null) {
                    autoContentVOList.add(new AutoContentVO(
                            "表单提交过期提醒",
                            "湖南大学校外实践鉴定表还有" +
                                    calDay(currentDate, deadline.getIdentifyDeadline())
                                    + "天到期"
                    ));
                }
            }

            if (isDeadline(calDay(currentDate, deadline.getAppraisalDeadline()))) {
                TableAppraisal tableAppraisal = tableAppraisalDAO.findByStudentIdAndStatus(studentId, 3);
                if (tableAppraisal == null) {
                    autoContentVOList.add(new AutoContentVO(
                            "表单提交过期提醒",
                            "湖南大学校外校外实践企业导师评价表还有" +
                                    calDay(currentDate, deadline.getAppraisalDeadline())
                                    + "天到期"
                    ));
                }
            }

            if (isDeadline(calDay(currentDate, deadline.getSummaryDeadline()))) {
                TableSummary tableSummary = tableSummaryDAO.findByStudentIdAndStatus(studentId, 3);
                if (tableSummary == null) {
                    autoContentVOList.add(new AutoContentVO(
                            "表单提交过期提醒",
                            "湖南大学校外实践总结报告还有" +
                                    calDay(currentDate, deadline.getSummaryDeadline())
                                    + "天到期"
                    ));
                }
            }
        }

        return new AutoVO(autoContentVOList);
    }

    @Override
    public RecvsVO queryRecvs() {
        List<Recv> list = userInfoDAO.findAll()
                .stream()
                .map(e -> new Recv(e.getId(), e.getName()))
                .filter(e -> !e.getId().equals(UserName.ADMIN))
                .collect(Collectors.toList());
        return new RecvsVO(list);
    }

    /**
     * 根据消息的类型获取接收者
     * 公告的接收者为范围群体
     * 消息的接收者为精确的名字
     */
    private String getReceiver(String type, String id) {
        switch (type) {
            case MessageConstants.TYPE_ALL:
                return MessageConstants.ALL_TAG;
            case MessageConstants.TYPE_STUDENT:
                return MessageConstants.STUDENT_TAG;
            case MessageConstants.TYPE_TEACHER:
                return MessageConstants.TEACHER_TAG;
            default:
                Optional<UserInfo> byId = userInfoDAO.findById(id);
                return byId.get().getName();
        }
    }

    private String getSender(String type, String id) {
        switch (type) {
            case MessageConstants.TYPE_ALL:
            case MessageConstants.TYPE_STUDENT:
            case MessageConstants.TYPE_TEACHER:
                return MessageConstants.ADMIN_NAME;
            default:
                Optional<UserInfo> byId = userInfoDAO.findById(id);
                return byId.get().getName();
        }
    }

    private String getSummary(String content) {
        if (content == null || content.length() < 8) {
            return content;
        }
        return content.substring(0, 8);
    }

    /**
     * calDay
     * 计算两个日期相差的天数是否小于5天
     */
    private long calDay(Date current, Date destination) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        long time1 = calendar.getTimeInMillis();
        calendar.setTime(destination);
        long time2 = calendar.getTimeInMillis();
        return (time2 - time1) / (1000 * 3600 * 24);
    }

    /**
     * IsDeadline
     * 计算两个日期相差的天数是否小于5天
     */
    private Boolean isDeadline(long between) {
        return between <= 5 && between >= 0;
    }
}
