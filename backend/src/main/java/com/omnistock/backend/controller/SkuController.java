package com.omnistock.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.entity.Sku;
import com.omnistock.backend.service.SkuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * SKU Controller
 */
@RestController
@RequestMapping("/v1/skus")
@RequiredArgsConstructor
public class SkuController {

    private final SkuService skuService;

    @PostMapping
    public Result<Sku> createSku(@Valid @RequestBody Sku sku) {
        Sku result = skuService.createSku(sku);
        return Result.created(result);
    }

    @GetMapping
    public Result<IPage<Sku>> querySku(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String skuCode,
            @RequestParam(required = false) String skuName) {
        IPage<Sku> result = skuService.querySku(page, pageSize, skuCode, skuName);
        return Result.success(result);
    }

    @GetMapping("/{skuId}")
    public Result<Sku> getSkuDetail(@PathVariable Long skuId) {
        Sku result = skuService.getSkuDetail(skuId);
        return Result.success(result);
    }

    @PutMapping("/{skuId}")
    public Result<Sku> updateSku(@PathVariable Long skuId, @Valid @RequestBody Sku sku) {
        skuService.updateSku(skuId, sku);
        return Result.success(skuService.getSkuDetail(skuId));
    }

    @DeleteMapping("/{skuId}")
    public Result<?> deleteSku(@PathVariable Long skuId) {
        skuService.deleteSku(skuId);
        return Result.success();
    }
}
