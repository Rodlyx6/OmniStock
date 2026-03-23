package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.domain.entity.Supplier;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.mapper.SupplierMapper;
import com.omnistock.backend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 供应商Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierMapper supplierMapper;

    @Override
    public IPage<Supplier> querySupplier(int page, int pageSize) {
        Page<Supplier> pageObj = new Page<>(page, pageSize);
        return supplierMapper.selectPage(pageObj, null);
    }

    @Override
    public Supplier getSupplierDetail(Long supplierId) {
        Supplier supplier = supplierMapper.selectById(supplierId);
        if (supplier == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "供应商不存在");
        }
        return supplier;
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        supplier.setStatus(1);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierMapper.insert(supplier);
        return supplier;
    }

    @Override
    public void updateSupplier(Long supplierId, Supplier supplier) {
        if (supplierMapper.selectById(supplierId) == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "供应商不存在");
        }
        supplier.setSupplierId(supplierId);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierMapper.updateById(supplier);
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        if (supplierMapper.selectById(supplierId) == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "供应商不存在");
        }
        supplierMapper.deleteById(supplierId);
    }
}
