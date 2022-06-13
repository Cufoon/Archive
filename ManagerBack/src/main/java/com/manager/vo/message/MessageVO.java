package com.manager.vo.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageVO {

    private Integer msgId;

    private String type;

    private String msgTitle;

    private String msgContent;

    private String receiver;

    private Boolean isRead;

    private String summary; // 消息概要

    private String sender;

    private String sentTime;
}
