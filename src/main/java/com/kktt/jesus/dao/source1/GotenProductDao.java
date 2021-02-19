package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.GotenProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface GotenProductDao  extends Mapper<GotenProduct> {

    int insertIgnore(@Param("item") GotenProduct record);

    int batchInsert(@Param("list") List<GotenProduct> list);

    @Update("update goten_product set price = #{price} where sku = #{sku}")
    void updatePrice(@Param("sku") Long sku,@Param("price") BigDecimal price);

    @Update("update goten_product set inventory = #{inventory} where sku = #{sku}")
    void updateQuantity(@Param("sku") Long sku,@Param("inventory") Integer inventory);

    void batchUpdatePrice(@Param("list") List<Map<String, Object>> list);

    void batchUpdateInventory(@Param("list") List<Map<String, Object>> list);

//    @Select("select * from goten_product")
    List<GotenProduct> selectAll();

    List<GotenProduct> selectValidProduct(@Param("category") String category);

    @Select("select * from goten_product where state = 0")
    List<GotenProduct> queryNewProduct();

    @Select("select * from goten_product where state = #{state}")
    List<GotenProduct> queryByState(@Param("state")int state);

    @Update("update goten_product set state = -1 where sku in (#{skus})")
    void updateState(@Param("skus") String skuStr);
}