package com.manager.controller.common;

import com.manager.constant.SessionFields;
import com.manager.form.message.ReadMessageParam;
import com.manager.form.message.SendMessageParam;
import com.manager.service.message.MessageService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.message.MessageListVO;
import com.manager.vo.message.ReadMessageVO;
import com.manager.vo.message.SendMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/message/send")
    public ResultVO sendMsg(
            @Valid @RequestBody SendMessageParam param,
            HttpServletRequest req
    ) {
        String senderId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (senderId == null) {
            log.error("[sendMsg] session查询用户名失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        messageService.send(senderId, param);
        return ResultWrapper.success("处理成功", new SendMessageVO(true));
    }

    @PostMapping("/message/read")
    public ResultVO readMsg(
            @RequestBody ReadMessageParam param,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[readMsg] session查询用户名失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        messageService.read(param);
        return ResultWrapper.success("处理成功", new ReadMessageVO(true));
    }

    @GetMapping("/message/query/{queryType}")
    public ResultVO queryAll(
            @PathVariable("queryType") Integer queryType,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        Integer roleType = (Integer) req.getSession().getAttribute(SessionFields.ROLE);
        if (id == null || roleType == null) {
            log.error("[queryAll] session查询用户名失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        MessageListVO vo;
        if (queryType == 2) {
            vo = messageService.queryAllNotifications(roleType);
        } else if (queryType == 0 || queryType == 1) {
            vo = messageService.queryAllMessages(id, queryType);
        } else {
            log.error("[queryAll] 参数错误");
            return ResultWrapper.error("参数错误");
        }
        return ResultWrapper.success("查询成功", vo);
    }

    @GetMapping("/message/auto")
    public ResultVO autoMessage(HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[autoMessage] session查询用户名失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return ResultWrapper.success("查询成功", messageService.autoMessage(id));
    }

    @GetMapping("/message/recvs")
    public ResultVO queryRecvs(HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[autoMessage] session查询用户名失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return ResultWrapper.success("查询成功", messageService.queryRecvs());
    }
}