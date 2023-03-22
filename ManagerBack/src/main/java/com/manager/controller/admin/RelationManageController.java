package com.manager.controller.admin;

import com.manager.constant.SessionFields;
import com.manager.form.RelationParam;
import com.manager.service.relation.RelationChangeService;
import com.manager.service.relation.RelationQueryService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin")
public class RelationManageController {

    private final RelationQueryService relationQueryService;

    private final RelationChangeService relationChangeService;

    @Autowired
    public RelationManageController(RelationQueryService relationQueryService, RelationChangeService relationChangeService) {
        this.relationQueryService = relationQueryService;
        this.relationChangeService = relationChangeService;
    }

    /**
     * queryRelation
     * 查询当前系统中存在的指导关系列表
     */
    @GetMapping("/relation/query")
    public ResultVO queryRelation(HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[QueryRelationController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return ResultWrapper.success("成功", relationQueryService.queryRelation());
    }

    /**
     * addRelation
     * 添加指导关系
     */
    @PostMapping("/relation/add")
    public ResultVO addRelation(@NonNull @RequestBody RelationParam RP) {
        String teaID = RP.getTeacherID();
        String stuID = RP.getStudentID();
        return ResultWrapper.success("成功", relationChangeService.addRelation(teaID, stuID));
    }

    /**
     * deleteRelation
     * 删除指导关系
     */
    @PostMapping("/relation/delete")
    public ResultVO deleteRelation(@NonNull @RequestBody RelationParam RP) {
        String teaID = RP.getTeacherID();
        String stuID = RP.getStudentID();
        return ResultWrapper.success("成功", relationChangeService.deleteRelation(teaID, stuID));
    }
}
