package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.AmazonMarketplace;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AmazonMarketplaceDao {

    @Resource
    @Qualifier("jdbcDropShippingTemplate")
    private JdbcTemplate jdbcDropShippingTemplate;

    public int update(String sellerId, String marketplaceId, int status) {
        return jdbcDropShippingTemplate.update("UPDATE amazon_marketplaces SET status = ? WHERE seller_id = ? AND marketplace_id = ?", status, sellerId, marketplaceId); // ID: 16204 India testing amazon store.
    }

    public AmazonMarketplace findBySellerIdAndMarketplaceId(String sellerId, String marketplaceId) {
        String sql = "SELECT * FROM amazon_marketplaces WHERE seller_id = ? and marketplace_id = ?";
        List<AmazonMarketplace> list = jdbcDropShippingTemplate.query(sql, new BeanPropertyRowMapper<>(AmazonMarketplace.class), sellerId, marketplaceId);
        return list.size() > 0 ? list.get(0) : null;
    }

    public AmazonMarketplace findById(Integer id) {
        String sql = "SELECT * FROM amazon_marketplaces WHERE id = ?";
        List<AmazonMarketplace> list = jdbcDropShippingTemplate.query(sql, new BeanPropertyRowMapper<>(AmazonMarketplace.class), id);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<Integer> findActiveIds() {
        String sql = "SELECT id FROM amazon_marketplaces WHERE status = 1";
        return jdbcDropShippingTemplate.queryForList(sql, Integer.class);
    }

    public List<AmazonMarketplace> findWaiting() {
        String sql = "SELECT * FROM amazon_marketplaces WHERE status IN (0, -1)";
        return jdbcDropShippingTemplate.query(sql, new BeanPropertyRowMapper<>(AmazonMarketplace.class));
    }
}