package com.manager.service.relation;

import com.manager.dao.StudentTeacherRelationDAO;
import com.manager.vo.relation.RelationListVO;
import com.manager.vo.relation.RelationQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationQueryServiceImpl implements RelationQueryService {

    private final StudentTeacherRelationDAO studentTeacherRelationDAO;

    @Autowired
    public RelationQueryServiceImpl(StudentTeacherRelationDAO studentTeacherRelationDAO) {
        this.studentTeacherRelationDAO = studentTeacherRelationDAO;
    }

    /**
     * queryRelation
     * 返回所有的已确立的指导关系信息
     */
    @Override
    public RelationQueryVO queryRelation() {
        RelationQueryVO relationQueryVO = new RelationQueryVO();
        List<RelationListVO> result = new ArrayList<>();
        List<Object[]> studentTeacherRelationList = studentTeacherRelationDAO.queryAllRelation();
        for (Object[] o : studentTeacherRelationList) {
            RelationListVO relationListVO = new RelationListVO(
                    o[0].toString(),
                    o[1].toString(),
                    o[2].toString(),
                    o[3].toString(),
                    o[4].toString(),
                    o[5].toString()
            );
            result.add(relationListVO);
        }
        relationQueryVO.setRelationList(result);
        return relationQueryVO;
    }
}
