package com.kktt.jesus.utils;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaoUtil {

    private static final int BATCH_SIZE = 1000;

	public static <T> int insert(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, T entity) {
		if (StringUtil.isEmpty(tableName)) throw new IllegalArgumentException("表名不能为空");
		if (entity == null) throw new IllegalArgumentException("实体对象不能为空");

		String sql = EntityUtil.genInsertSql(tableName, entity.getClass());
		return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public static <T> int[] insert(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, List<T> entities) {
		if(entities == null || entities.size() == 0) return null;
		String sql = EntityUtil.genInsertSql(tableName, entities.get(0).getClass());
		return processBatch(namedParameterJdbcTemplate, sql, entities);
	}

	public static <T> int update(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, T entity, String keyColumn) {
		if (entity == null) throw new IllegalArgumentException("实体对象不能为空");
		if (StringUtil.isEmpty(tableName)) throw new IllegalArgumentException("表名不能为空");
		if (StringUtil.isEmpty(keyColumn)) throw new IllegalArgumentException("关联字段名不能为空");
		
	    String sql = EntityUtil.genUpdateSql(tableName, entity.getClass(), keyColumn);
	    return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(entity));
	}
	
	public static <T> int[] update(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, List<T> entities, String keyColumn) {
		if(entities == null || entities.size() == 0) return null;
		String sql = EntityUtil.genUpdateSql(tableName, entities.get(0).getClass(), keyColumn);
        return processBatch(namedParameterJdbcTemplate, sql, entities);
	}

	public static <T> int[] update(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, List<T> entities, String[] keyColumns) {
		if(entities == null || entities.size() == 0) return null;
		String sql = EntityUtil.genUpdateSql(tableName, entities.get(0).getClass(), keyColumns);
		return processBatch(namedParameterJdbcTemplate, sql, entities);
	}

	public static <T> int[] processBatch(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, List<T> entities) {
	    if (entities.isEmpty()) return null;

        int times = entities.size() / BATCH_SIZE;
        int lefts = entities.size() % BATCH_SIZE;

        List<Integer> results = new ArrayList<>(entities.size());
        for(int i=0; i<times; i++) {
            List<T> subList = entities.subList(BATCH_SIZE * i, BATCH_SIZE * i + BATCH_SIZE);
            int[] res = namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(subList.toArray()));
            Arrays.stream(res).forEach(r -> results.add(r));
        }

        if (lefts != 0) {
            List<T> subList = entities.subList(entities.size() - lefts, entities.size());
            int[] res = namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(subList.toArray()));
            if (res != null) Arrays.stream(res).forEach(r -> results.add(r));
        }

        return results.stream().mapToInt(Integer::valueOf).toArray();
    }

}
