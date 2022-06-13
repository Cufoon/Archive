package com.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "message")
public class Message {

    public Message() {
    }

    public Message(String senderId, String recipientId, Date sendTime, String title, String content, Integer state, String type) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.sendTime = sendTime;
        this.title = title;
        this.content = content;
        this.state = state;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    private String senderId;

    private String recipientId;

    private Date sendTime;

    private String title;

    private String content;

    private Integer state;

    private String type;
}
