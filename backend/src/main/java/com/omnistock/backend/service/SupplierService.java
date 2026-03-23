package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.entity.Supplier;

/**
 * 供应商Service接口
 */
public interface SupplierService {
    IPage<Supplier> querySupplier(int page, int pageSize);
    Supplier getSupplierDetail(Long supplierId);
    Supplier createSupplier(Supplier supplier);
    void updateSupplier(Long supplierId, Supplier supplier);
    void deleteSupplier(Long supplierId);
}
