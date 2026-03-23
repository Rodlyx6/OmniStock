package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.domain.entity.Sku;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.mapper.SkuMapper;
import com.omnistock.backend.service.SkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * SKU Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {

    private final SkuMapper skuMapper;

    @Override
    public IPage<Sku> querySku(int page, int pageSize, String skuCode, String skuName) {
        Page<Sku> pageObj = new Page<>(page, pageSize);
        LambdaQueryWrapper<Sku> wrapper = new LambdaQueryWrapper<>();
        
        if (skuCode != null) {
            wrapper.like(Sku::getSkuCode, skuCode);
        }
        if (skuName != null) {
            wrapper.like(Sku::getSkuName, skuName);
        }
        
        return skuMapper.selectPage(pageObj, wrapper);
    }

    @Override
    public Sku getSkuDetail(Long skuId) {
        Sku sku = skuMapper.selectById(skuId);
        if (sku == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "SKU不存在");
        }
        return sku;
    }

    @Override
    public Sku createSku(Sku sku) {
        sku.setVersion(0);
        sku.setCreatedAt(LocalDateTime.now());
        sku.setUpdatedAt(LocalDateTime.now());
        skuMapper.insert(sku);
        return sku;
    }

    @Override
    public void updateSku(Long skuId, Sku sku) {
        Sku existing = skuMapper.selectById(skuId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "SKU不存在");
        }
        sku.setSkuId(skuId);
        sku.setUpdatedAt(LocalDateTime.now());
        skuMapper.updateById(sku);
    }

    @Override
    public void deleteSku(Long skuId) {
        Sku sku = skuMapper.selectById(skuId);
        if (sku == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "SKU不存在");
        }
        skuMapper.deleteById(skuId);
    }
}
