
package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.ListingCreateRecordsEntity;
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
public class ListingCreateRecordsDao {

    @Resource
    @Qualifier("jdbcDropShippingTemplate")
    private JdbcTemplate jdbcDropShippingTemplate;

    @Resource(name = "namedParameterJdbcDropShippingTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcDropShippingTemplate;

    public int insertOne(ListingCreateRecordsEntity createRecordsEntity) {
        return DaoUtil.insert(namedParameterJdbcDropShippingTemplate, "listing_create_records", createRecordsEntity);
    }

    public int[] insert(List<ListingCreateRecordsEntity> createRecordsEntity) {
        return DaoUtil.insert(namedParameterJdbcDropShippingTemplate, "listing_create_records", createRecordsEntity);
    }

    public List<ListingCreateRecordsEntity> queryAll() {
        return jdbcDropShippingTemplate.query("SELECT * FROM listing_create_records ", new BeanPropertyRowMapper<>(ListingCreateRecordsEntity.class));
    }

    public List<ListingCreateRecordsEntity> queryByStatus(Integer status, Byte type) {
        return jdbcDropShippingTemplate.query("SELECT * FROM listing_create_records WHERE status = ? and type = ? and submission_id != '' ", new BeanPropertyRowMapper<>(ListingCreateRecordsEntity.class), status,type);
    }

    public ListingCreateRecordsEntity find(String submissionId) {
        String sql = "select * from listing_create_records where submission_id = ? ";
        List<ListingCreateRecordsEntity> list = jdbcDropShippingTemplate.query(sql,new BeanPropertyRowMapper<>(ListingCreateRecordsEntity.class),submissionId);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void updateStatus(List<String> submissionList, Integer status) {
        String sql = "update listing_create_records set status = :status  WHERE submission_id IN (:submissionIds)";
        Map<String, Object> params = new HashMap<>();
        params.put("status",status);
        params.put("submissionIds", submissionList);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

    public void updateErrorInfo(String submissionId, String errorInfo) {
        String sql = "update listing_create_records set report_status = :reportStatus,error_info = :errorInfo  WHERE submission_id  = :submissionId ";
        Map<String, Object> params = new HashMap<>();
        params.put("reportStatus", ListingCreateRecordsEntity.REPORT_STATUS.FAILED);
        params.put("errorInfo", errorInfo);
        params.put("submissionId", submissionId);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

    public void updateReportStatus(String submissionId,Integer status,String content) {
        String sql = "update listing_create_records set report_status = :status,error_info = :content  WHERE submission_id = :submissionId ";
        Map<String, Object> params = new HashMap<>();
        params.put("status",status);
        params.put("content",content);
        params.put("submissionId", submissionId);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }

    public void update(String submissionId, String uuid, Byte type) {
        String sql = "update listing_create_records set submission_id = :submissionId WHERE uuid  = :uuid and type= :type ";
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("uuid", uuid);
        params.put("submissionId", submissionId);
        namedParameterJdbcDropShippingTemplate.update(sql,params);
    }
}
