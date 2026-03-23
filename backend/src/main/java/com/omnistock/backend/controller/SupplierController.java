package com.omnistock.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.entity.Supplier;
import com.omnistock.backend.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商Controller
 */
@RestController
@RequestMapping("/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public Result<Supplier> createSupplier(@Valid @RequestBody Supplier supplier) {
        return Result.created(supplierService.createSupplier(supplier));
    }

    @GetMapping
    public Result<IPage<Supplier>> querySupplier(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(supplierService.querySupplier(page, pageSize));
    }

    @GetMapping("/{supplierId}")
    public Result<Supplier> getSupplierDetail(@PathVariable Long supplierId) {
        return Result.success(supplierService.getSupplierDetail(supplierId));
    }

    @PutMapping("/{supplierId}")
    public Result<Supplier> updateSupplier(
            @PathVariable Long supplierId,
            @Valid @RequestBody Supplier supplier) {
        supplierService.updateSupplier(supplierId, supplier);
        return Result.success(supplierService.getSupplierDetail(supplierId));
    }

    @DeleteMapping("/{supplierId}")
    public Result<?> deleteSupplier(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return Result.success();
    }
}
