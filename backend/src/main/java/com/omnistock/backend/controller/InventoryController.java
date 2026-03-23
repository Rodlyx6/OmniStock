package com.omnistock.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.dto.DeductInventoryDTO;
import com.omnistock.backend.domain.dto.IncreaseInventoryDTO;
import com.omnistock.backend.domain.vo.InventoryFlowVO;
import com.omnistock.backend.domain.vo.InventoryOperationVO;
import com.omnistock.backend.domain.vo.InventoryVO;
import com.omnistock.backend.service.InventoryFlowService;
import com.omnistock.backend.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 库存Controller
 * 对应接口文档: 03_INVENTORY_API.md
 */
@RestController
@RequestMapping("/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryFlowService inventoryFlowService;

    /** 3.1 查询库存列表 */
    @GetMapping
    public Result<IPage<InventoryVO>> queryInventories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Long warehouseId) {
        return Result.success(inventoryService.queryInventories(page, pageSize, skuId, warehouseId));
    }

    /** 3.2 查询库存详情 */
    @GetMapping("/{inventoryId}")
    public Result<InventoryVO> getInventoryDetail(@PathVariable Long inventoryId) {
        return Result.success(inventoryService.getInventoryDetail(inventoryId));
    }

    /** 3.3 查询SKU总库存（跨库位） */
    @GetMapping("/sku/{skuId}/total")
    public Result<Map<String, Object>> getSkuTotalInventory(@PathVariable Long skuId) {
        return Result.success(inventoryService.getSkuTotalInventory(skuId));
    }

    /** 3.4 库存扣减（防超卖） */
    @PostMapping("/deduct")
    public Result<InventoryOperationVO> deductInventory(@Valid @RequestBody DeductInventoryDTO dto) {
        return Result.success(inventoryService.deductInventory(dto));
    }

    /** 3.5 库存增加（入库） */
    @PostMapping("/increase")
    public Result<InventoryOperationVO> increaseInventory(@Valid @RequestBody IncreaseInventoryDTO dto) {
        return Result.success(inventoryService.increaseInventory(dto));
    }

    /** 3.6 查询库存流水列表 */
    @GetMapping("/flows")
    public Result<IPage<InventoryFlowVO>> queryFlows(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Long inventoryId,
            @RequestParam(required = false) String flowType,
            @RequestParam(required = false) String referenceNo,
            @RequestParam(required = false) Long operatorId) {
        return Result.success(inventoryFlowService.queryFlows(
                page, pageSize, skuId, inventoryId, flowType, referenceNo, operatorId));
    }

    /** 3.7 查询库存流水详情 */
    @GetMapping("/flows/{flowId}")
    public Result<InventoryFlowVO> getFlowDetail(@PathVariable Long flowId) {
        return Result.success(inventoryFlowService.getFlowDetail(flowId));
    }
}
