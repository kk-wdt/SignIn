package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.AmazonsCategoryTemplateParamEntity;
import com.tapcash.tool4seller.library.sellercentral.MwsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AmazonCategoryTemplateParamDao {
    @Resource
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public AmazonsCategoryTemplateParamEntity findVariationTheme(String marketplaceId, String nodeId) {
        List<AmazonsCategoryTemplateParamEntity> list = jdbcTemplate.query(String.format("select * from %s where node_id = ? and field_name = 'variation_theme'", getTableName(marketplaceId)), new BeanPropertyRowMapper<>(AmazonsCategoryTemplateParamEntity.class), nodeId);
        return CollectionUtils.isEmpty(list)?null: list.get(0);
    }

    private String getTableName(String marketplaceId){
        if(marketplaceId.equals("ATVPDKIKX0DER")){
            return "amazons_category_template_param";
        }
        return "amazons_category_template_param_"+MwsUtil.getMarketplace(marketplaceId).getCountryCode().toLowerCase();
    }
}