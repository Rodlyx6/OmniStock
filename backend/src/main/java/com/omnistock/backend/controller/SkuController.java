package com.omnistock.backend.controller;

import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.dto.SkuUpsertRequest;
import com.omnistock.backend.domain.vo.SkuVO;
import com.omnistock.backend.service.SkuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sku")
@RequiredArgsConstructor
public class SkuController {

    private final SkuService skuService;

    @PostMapping
    public Result<Long> upsert(@Valid @RequestBody SkuUpsertRequest request) {
        return Result.success(skuService.upsert(request));
    }

    @GetMapping
    public Result<List<SkuVO>> listAll() {
        return Result.success(skuService.listAll());
    }
}
