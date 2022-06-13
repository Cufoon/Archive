package com.manager.vo.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageListVO {

    List<MessageVO> messageList;
}
