package com.omnistock.backend.constant;

/**
 * 错误码常量
 */
public class ErrorCode {
    public static final int SUCCESS = 200;
    public static final int CREATED = 201;
    
    // 业务错误
    public static final int INVENTORY_INSUFFICIENT = 5001;
    public static final int INVENTORY_VERSION_CONFLICT = 5002;
    public static final int INVENTORY_FLOW_NOT_FOUND = 5010;
    public static final int OUTBOUND_NOT_FOUND = 7002;
    public static final int OUTBOUND_STATUS_ERROR = 7003;
    public static final int OUTBOUND_INSUFFICIENT_INVENTORY = 7008;
    
    // 系统错误
    public static final int SYSTEM_ERROR = 500;
}
