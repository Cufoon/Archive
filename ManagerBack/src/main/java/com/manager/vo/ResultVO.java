package com.manager.vo;

import com.manager.constant.StatusConstants;
import lombok.Data;

/**
 * 最终的返回结果结构
 */
@Data
public class ResultVO {

    private String status;

    private int code = StatusConstants.CODE_UNSPECIFIED;

    private String message;

    private Object data;
}
