package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.omnistock.backend.domain.dto.SkuUpsertRequest;
import com.omnistock.backend.domain.entity.Sku;
import com.omnistock.backend.domain.vo.SkuVO;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.exception.ErrorCode;
import com.omnistock.backend.mapper.SkuMapper;
import com.omnistock.backend.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {

    private final SkuMapper skuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long upsert(SkuUpsertRequest request) {
        if (request.getId() == null) {
            long count = skuMapper.selectCount(
                    new LambdaQueryWrapper<Sku>().eq(Sku::getCode, request.getCode()));
            if (count > 0) {
                throw new BusinessException(ErrorCode.SKU_CODE_DUPLICATE, "SKU编码已存在");
            }
            Sku sku = new Sku();
            sku.setCode(request.getCode());
            sku.setName(request.getName());
            sku.setCategory(request.getCategory());
            sku.setUnit(request.getUnit());
            sku.setWeight(request.getWeight());
            sku.setVolume(request.getVolume());
            skuMapper.insert(sku);
            return sku.getId();
        }

        Sku exist = skuMapper.selectById(request.getId());
        if (exist == null) {
            throw new BusinessException(ErrorCode.SKU_NOT_FOUND, "SKU不存在");
        }
        exist.setName(request.getName());
        exist.setCategory(request.getCategory());
        exist.setUnit(request.getUnit());
        exist.setWeight(request.getWeight());
        exist.setVolume(request.getVolume());
        skuMapper.updateById(exist);
        return exist.getId();
    }

    @Override
    public List<SkuVO> listAll() {
        return skuMapper.selectList(
                new LambdaQueryWrapper<Sku>().orderByDesc(Sku::getUpdatedTime))
                .stream().map(this::toVO).collect(Collectors.toList());
    }

    private SkuVO toVO(Sku sku) {
        SkuVO vo = new SkuVO();
        vo.setId(sku.getId());
        vo.setCode(sku.getCode());
        vo.setName(sku.getName());
        vo.setCategory(sku.getCategory());
        vo.setUnit(sku.getUnit());
        vo.setWeight(sku.getWeight());
        vo.setVolume(sku.getVolume());
        vo.setCreatedTime(sku.getCreatedTime());
        vo.setUpdatedTime(sku.getUpdatedTime());
        return vo;
    }
}
