package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.dto.DeductInventoryDTO;
import com.omnistock.backend.domain.dto.IncreaseInventoryDTO;
import com.omnistock.backend.domain.vo.InventoryOperationVO;
import com.omnistock.backend.domain.vo.InventoryVO;

import java.util.Map;

/**
 * 库存Service接口
 */
public interface InventoryService {

    /** 查询库存列表 */
    IPage<InventoryVO> queryInventories(int page, int pageSize, Long skuId, Long warehouseId);

    /** 查询库存详情 */
    InventoryVO getInventoryDetail(Long inventoryId);

    /** 查询SKU总库存（跨库位） */
    Map<String, Object> getSkuTotalInventory(Long skuId);

    /** 库存扣减（防超卖） */
    InventoryOperationVO deductInventory(DeductInventoryDTO dto);

    /** 库存增加 */
    InventoryOperationVO increaseInventory(IncreaseInventoryDTO dto);
}
