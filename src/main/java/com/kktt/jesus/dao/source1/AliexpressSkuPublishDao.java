
package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.schedule.AmzFlatFileListingBatchCreateScheduler;
import com.kktt.jesus.utils.DaoUtil;
import com.kktt.jesus.utils.MapBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AliexpressSkuPublishDao {

    @Resource
    @Qualifier("jdbcDropShippingTemplate")
    private JdbcTemplate jdbcDropShippingTemplate;

    @Resource(name = "namedParameterJdbcDropShippingTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcDropShippingTemplate;

    public List<AliexpressSkuPublishEntity> query(Integer state){
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish where state = ? limit 0,1000");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),state);
    }

    public List<AliexpressSkuPublishEntity> query(Integer state,Integer startIndex){
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish where state = ? order by id limit ?,5000");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),state,startIndex);
    }

    public List<AliexpressSkuPublishEntity> queryRetryTask(){
        Integer state = AliexpressSkuPublishEntity.STATE.FAILURE;
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish where state = ? and count < 100");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),state);
    }

    public List<AliexpressSkuPublishEntity> queryAll(){
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish ");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class));
    }

    public List<AliexpressSkuPublishEntity> queryShopSku(Integer amazonMarketplaceId,Integer state) {
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish where amazon_marketplace_id = ? and state = ? ");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),amazonMarketplaceId,state);
    }

    public List<AliexpressSkuPublishEntity> querySyncImage(Long productId){
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish where product_id = ? and state = 1 ");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),productId);
    }

    public List<AliexpressSkuPublishEntity> queryTask(Long productId,Integer amazonMarketplaceId){
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish where product_id = ? and amazon_marketplace_id = ? and state = 2 ");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),productId,amazonMarketplaceId);
    }

    public List<AliexpressSkuPublishEntity> queryProductIdByState(Integer state) {
        StringBuilder sql = new StringBuilder("SELECT * FROM aliexpress_sku_publish where state = ? GROUP BY product_id ");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),state);
    }

    public List<AliexpressSkuPublishEntity> querySuccessfulProductId() {
        StringBuilder sql = new StringBuilder("SELECT * FROM aliexpress_sku_publish where state = 2 GROUP BY product_id,amazon_marketplace_id ");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class));
    }

    public List<AliexpressSkuPublishEntity> queryBySubmissionId(String submissionId) {
        StringBuilder sql = new StringBuilder("select * from aliexpress_sku_publish where submission_id = ? ");
        return jdbcDropShippingTemplate.query(sql.toString(),new BeanPropertyRowMapper<>(AliexpressSkuPublishEntity.class),submissionId);
    }

    public void update(List<AliexpressSkuPublishEntity> aliexpressSkuPublishEntities){
        DaoUtil.update(namedParameterJdbcDropShippingTemplate, "aliexpress_sku_publish", aliexpressSkuPublishEntities, new String[]{"id","amazon_marketplace_id"});
    }

    public int updateState(List<String> idList, Integer state) {
        StringBuilder sql = new StringBuilder("update aliexpress_sku_publish set state = :state,COUNT = COUNT+1  where id in (:idList) ");
        Map<String, Object> paramMap = MapBuilder.of("idList", (Object) idList).build();
        paramMap.put("state",state);
        return namedParameterJdbcDropShippingTemplate.update(sql.toString(),paramMap);
    }

    public int updateUndoStateByProductId(List<Long> productId, Integer state) {
        StringBuilder sql = new StringBuilder("update aliexpress_sku_publish set state = :state where product_id in (:productId) and state = 0");
        Map<String, Object> paramMap = MapBuilder.of("productId", (Object) productId).build();
        paramMap.put("state",state);
        return namedParameterJdbcDropShippingTemplate.update(sql.toString(),paramMap);
    }

    public int[] update(AliexpressSkuPublishEntity entity) {
        return DaoUtil.update(namedParameterJdbcDropShippingTemplate, "aliexpress_sku_publish", Collections.singletonList(entity), new String[]{"id","amazon_marketplace_id"});
    }

    public void updateSubmission(String shopId, String submissionId, List<Long> productIdList) {
        String sql = "update aliexpress_sku_publish set submission_id = :submissionId where amazon_marketplace_id = :shopId and product_id in(:productIdList) ";
        Map<String,Object> param = new HashMap<>();
        param.put("submissionId",submissionId);
        param.put("shopId",shopId);
        param.put("productIdList",productIdList);
        namedParameterJdbcDropShippingTemplate.update(sql,param);
    }

    public void updateBySubmissionId(String submissionId) {
        Integer success = AliexpressSkuPublishEntity.STATE.SUCCESS;
        StringBuilder sql = new StringBuilder("update aliexpress_sku_publish set state = :state where submission_id = :submissionId ");
        Map<String,Object> param = new HashMap<>();
        param.put("submissionId",submissionId);
        param.put("state",success);
        namedParameterJdbcDropShippingTemplate.update(sql.toString(),param);
    }

    public void updateUpc(Integer amazonMarketplaceId, Long skuId, String upc) {
        StringBuilder sql = new StringBuilder("update aliexpress_sku_publish set upc = :upc where amazon_marketplace_id = :amazonMarketplaceId and sku_id = :skuId ");
        Map<String,Object> param = new HashMap<>();
        param.put("amazonMarketplaceId",amazonMarketplaceId);
        param.put("skuId",skuId);
        param.put("upc",upc);
        namedParameterJdbcDropShippingTemplate.update(sql.toString(),param);
    }

    public void updatePrefix(String shopId, List<Long> skuIdList, String prefix) {
        String sql = "update aliexpress_sku_publish set prefix = :prefix  where amazon_marketplace_id = :shopId and sku_id in(:skuIdList) and prefix = '' ";
        Map<String,Object> param = new HashMap<>();
        param.put("prefix", AmzFlatFileListingBatchCreateScheduler.SKU_PREFIX+prefix);
        param.put("shopId",shopId);
        param.put("skuIdList",skuIdList);
        namedParameterJdbcDropShippingTemplate.update(sql,param);
    }

}
