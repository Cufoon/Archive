package com.manager.service.relation;

import com.manager.vo.relation.RelationQueryVO;

public interface RelationQueryService {

    /**
     * queryRelation
     * 返回所有的已确立的指导关系信息
     */
    RelationQueryVO queryRelation();
}
