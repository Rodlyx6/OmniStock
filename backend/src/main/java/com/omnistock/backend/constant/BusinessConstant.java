package com.omnistock.backend.constant;

/**
 * 业务常量
 */
public class BusinessConstant {
    // 库存流水类型
    public static final String FLOW_INBOUND = "INBOUND";
    public static final String FLOW_OUTBOUND = "OUTBOUND";
    
    // 出库单状态
    public static final String OUTBOUND_PENDING = "PENDING";
    public static final String OUTBOUND_COMPLETED = "COMPLETED";
    public static final String OUTBOUND_CANCELLED = "CANCELLED";
    
    // 入库单状态
    public static final String INBOUND_STATUS_PENDING = "PENDING";
    public static final String INBOUND_STATUS_RECEIVING = "RECEIVING";
    public static final String INBOUND_STATUS_COMPLETED = "COMPLETED";
    public static final String INBOUND_STATUS_CANCELLED = "CANCELLED";
    
    // 入库明细状态
    public static final String INBOUND_ITEM_STATUS_PENDING = "PENDING";
    public static final String INBOUND_ITEM_STATUS_RECEIVED = "RECEIVED";
    public static final String INBOUND_ITEM_STATUS_PARTIAL = "PARTIAL";
}
