package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.entity.Sku;

/**
 * SKU Service接口
 */
public interface SkuService {
    IPage<Sku> querySku(int page, int pageSize, String skuCode, String skuName);
    Sku getSkuDetail(Long skuId);
    Sku createSku(Sku sku);
    void updateSku(Long skuId, Sku sku);
    void deleteSku(Long skuId);
}
