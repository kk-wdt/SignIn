
package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.UpcEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UpcDao {

    @Resource
    @Qualifier("jdbcDropShippingTemplate")
    private JdbcTemplate jdbcDropShippingTemplate;

    @Resource(name = "namedParameterJdbcDropShippingTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcDropShippingTemplate;

    public void insert(List<String> upcList){
        StringBuilder sql = new StringBuilder("insert into upc values ");
        for (String upc : upcList) {
            sql.append("(").append(upc).append(",0),");
        }
        String rs = sql.substring(0,sql.length() - 1);
        jdbcDropShippingTemplate.update(rs);
    }

    public List<UpcEntity> fetch(int count) {
        String sql = "select * from upc where used = 0 limit 0,? ";
        return jdbcDropShippingTemplate.query(sql,new BeanPropertyRowMapper<>(UpcEntity.class),count);
    }

    public void used(String upc){
        String sql = "update upc set used = 1 where upc = ?  ";
        jdbcDropShippingTemplate.update(sql,upc);
    }

}
