package com.manager.util;

import com.manager.constant.StatusConstants;

public class ServiceResult<DataType> {

    private int code = StatusConstants.CODE_SUCCESS;

    private String message = null;

    private DataType data = null;

    public ServiceResult() {
    }

    public ServiceResult(int code) {
        this.code = code;
    }

    public ServiceResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceResult(DataType data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == StatusConstants.CODE_SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataType getData() {
        return data;
    }

    public ServiceResult<DataType> setCode(int code) {
        this.code = code;
        return this;
    }

    public ServiceResult<DataType> setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceResult<DataType> setData(DataType data) {
        this.data = data;
        return this;
    }
}
