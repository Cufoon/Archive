package com.manager.service.message;

import com.manager.form.message.ReadMessageParam;
import com.manager.form.message.SendMessageParam;
import com.manager.vo.message.AutoVO;
import com.manager.vo.message.MessageListVO;
import com.manager.vo.message.RecvsVO;

public interface MessageService {

    /**
     * 发送消息
     *
     * @param senderId 发送者id
     * @param param    前端参数，消息内容
     */
    void send(String senderId, SendMessageParam param);

    /**
     * 将消息设置为已读
     *
     * @param param 前端参数，包含消息id列表
     */
    void read(ReadMessageParam param);

    /**
     * 查询所有公告
     *
     * @param roleType 用户角色类型
     */
    MessageListVO queryAllNotifications(Integer roleType);

    /**
     * 查看未读/已读信息
     *
     * @param id        用户用户名
     * @param queryType 未读还是已读
     */
    MessageListVO queryAllMessages(String id, Integer queryType);

    /**
     * 自动通知
     *
     * @param studentId 用户id
     */
    AutoVO autoMessage(String studentId);

    /**
     * 指定发送部分用户时获取用户列表
     */
    RecvsVO queryRecvs();
}
