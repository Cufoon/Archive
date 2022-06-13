package com.manager.service.relation;

import com.manager.vo.relation.RelationChangeVO;

public interface RelationChangeService {

    /**
     * addRelation
     * 根据学生ID与校内导师ID保存指导关系，返回保存是否成功
     */
    RelationChangeVO addRelation(String teaID, String stuID);

    /**
     * deleteRelation
     * 根据学生ID与校内导师ID删除指导关系，返回删除是否成功
     */
    RelationChangeVO deleteRelation(String teaID, String stuID);
}
