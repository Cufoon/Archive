package com.manager.controller.admin;

import com.manager.constant.SessionFields;
import com.manager.form.ScoreEnterParam;
import com.manager.form.ScoreQueryParam;
import com.manager.service.score.ScoreEnterService;
import com.manager.service.score.ScoreQueryService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin")
public class ScoreManageController {

    private final ScoreEnterService scoreEnterService;

    private final ScoreQueryService scoreQueryService;

    @Autowired
    public ScoreManageController(ScoreEnterService scoreEnterService, ScoreQueryService scoreQueryService) {
        this.scoreEnterService = scoreEnterService;
        this.scoreQueryService = scoreQueryService;
    }

    /**
     * enterScore
     * 录入分数
     */
    @PostMapping("/score/enter")
    public ResultVO enterScore(
            @Valid @RequestBody ScoreEnterParam param,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        if (id == null) {
            log.error("[EnterScoreController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            resultVO = ResultWrapper.success("成功", scoreEnterService.scoreEnter(param));
        }
        return resultVO;
    }

    /**
     * queryScore
     * 查询分数
     */
    @PostMapping("/score/query")
    public ResultVO queryScore(
            @Valid @RequestBody ScoreQueryParam param,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        if (id == null) {
            log.error("[QueryScoreController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            resultVO = ResultWrapper.success("成功", scoreQueryService.scoreQuery(param));
        }
        return resultVO;
    }
}
