package com.omnistock.backend.exception;

public final class ErrorCode {

    private ErrorCode() {
    }

    public static final int PARAM_ERROR = 400;

    public static final int SYSTEM_ERROR = 500;

    public static final int STOCK_NOT_FOUND = 5004;

    public static final int STOCK_NOT_ENOUGH = 5001;

    public static final int STOCK_VERSION_CONFLICT = 5002;

    public static final int WAREHOUSE_NOT_FOUND = 6001;

    public static final int LOCATION_NOT_FOUND = 6002;

    public static final int SKU_NOT_FOUND = 6003;

    public static final int SKU_CODE_DUPLICATE = 6004;
}
