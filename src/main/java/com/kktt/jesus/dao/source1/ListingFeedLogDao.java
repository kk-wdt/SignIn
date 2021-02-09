
package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.ListingFeedLogEntity;
import com.kktt.jesus.utils.DaoUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ListingFeedLogDao {

    @Resource
    @Qualifier("jdbcDropShippingTemplate")
    private JdbcTemplate jdbcDropShippingTemplate;

    @Resource(name = "namedParameterJdbcDropShippingTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcDropShippingTemplate;

    public List<ListingFeedLogEntity> queryByStatus(Integer status) {
        List<ListingFeedLogEntity> entities = jdbcDropShippingTemplate.query("SELECT * FROM listing_feed_log WHERE status = ? ", new BeanPropertyRowMapper<>(ListingFeedLogEntity.class), status);
        return entities;
    }

    /**
     * 查询一个subimission包含的非忽略状态的商品信息
     * @param submissionId
     * @return
     */
    public List<ListingFeedLogEntity> query(String submissionId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM listing_feed_log WHERE submission_id = ? and report_status is NULL ");
        List<ListingFeedLogEntity> entities = jdbcDropShippingTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(ListingFeedLogEntity.class), submissionId);
        return entities;
    }

    public List<ListingFeedLogEntity> query(List<String> uuidList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM listing_feed_log WHERE uuid IN (:uuidList)");
        Map<String, Object> condition = new HashMap<>();
        condition.put("uuidList", uuidList);
        return namedParameterJdbcDropShippingTemplate.query(sql.toString(), condition, new BeanPropertyRowMapper<>(ListingFeedLogEntity.class));
    }

    public void updateStatus(List<String> submissionIds,Integer status) {
        String sql = "update listing_feed_log set status = :status  WHERE submission_id IN (:submissionIds)";
        Map<String, Object> params = new HashMap<>();
        params.put("status",status);
        params.put("submissionIds", submissionIds);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

    public void updateReportStatus(List<String> submissionIds,Integer status) {
        String sql = "update listing_feed_log set report_status = :status  WHERE submission_id IN (:submissionIds)";
        Map<String, Object> params = new HashMap<>();
        params.put("status",status);
        params.put("submissionIds", submissionIds);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

    public void updateReportStatusByUuid(List<String> uuidList,Integer status) {
        String sql = "update listing_feed_log set report_status = :status  WHERE uuid IN (:uuidList)";
        Map<String, Object> params = new HashMap<>();
        params.put("status",status);
        params.put("uuidList", uuidList);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

    public int insertOne(ListingFeedLogEntity pricingLogEntity) {
        return DaoUtil.insert(namedParameterJdbcDropShippingTemplate, "listing_feed_log", pricingLogEntity);
    }

    public int[] insert(List<ListingFeedLogEntity> pricingLogEntityList) {
        return DaoUtil.insert(namedParameterJdbcDropShippingTemplate, "listing_feed_log", pricingLogEntityList);
    }

    public void updateErrorInfo(String submissionId, String errorInfo) {
        String sql = "update listing_feed_log set report_status = :reportStatus,error_info = :errorInfo  WHERE submission_id  = :submissionId ";
        Map<String, Object> params = new HashMap<>();
        params.put("reportStatus", ListingFeedLogEntity.REPORT_STATUS.FAILED);
        params.put("errorInfo", errorInfo);
        params.put("submissionId", submissionId);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

    public void updateErrorInfoByUuid(String uuid, String errorInfo) {
        String sql = "update listing_feed_log set report_status = :reportStatus,error_info = :errorInfo  WHERE uuid  = :uuid ";
        Map<String, Object> params = new HashMap<>();
        params.put("reportStatus", ListingFeedLogEntity.REPORT_STATUS.FAILED);
        params.put("errorInfo", errorInfo);
        params.put("uuid", uuid);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

}
