package com.omnistock.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.omnistock.backend.domain.entity.Inventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 库存Mapper
 */
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 库存扣减（乐观锁）
     * 
     * 防超卖SQL：
     * UPDATE inventory 
     * SET quantity = quantity - ?, 
     *     available_quantity = available_quantity - ?,
     *     version = version + 1
     * WHERE sku_id = ? AND version = ?
     */
    @Update("UPDATE inventory SET quantity = quantity - #{quantity}, " +
            "available_quantity = available_quantity - #{quantity}, " +
            "version = version + 1 " +
            "WHERE sku_id = #{skuId} AND version = #{version}")
    int deductInventory(@Param("skuId") Long skuId, 
                        @Param("quantity") Integer quantity,
                        @Param("version") Integer version);

    /**
     * 库存增加
     */
    @Update("UPDATE inventory SET quantity = quantity + #{quantity}, " +
            "available_quantity = available_quantity + #{quantity}, " +
            "version = version + 1 " +
            "WHERE sku_id = #{skuId} AND location_id = #{locationId}")
    int increaseInventory(@Param("skuId") Long skuId,
                          @Param("locationId") Long locationId,
                          @Param("quantity") Integer quantity);
}
