package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.AmazonsCategoryTemplateEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AmazonCategoryTemplateDao  {
    @Resource
    @Qualifier("jdbcDropShippingTemplate")
    private JdbcTemplate jdbcDropShippingTemplate;

    public AmazonsCategoryTemplateEntity find(String nodeId) {
        String sql  = "SELECT * FROM amazon_category_templates WHERE node_id = ?";
        List<AmazonsCategoryTemplateEntity> list = jdbcDropShippingTemplate.query(sql, new BeanPropertyRowMapper<>(AmazonsCategoryTemplateEntity.class), nodeId);
        return list.size() > 0 ? list.get(0) : null;
    }
}