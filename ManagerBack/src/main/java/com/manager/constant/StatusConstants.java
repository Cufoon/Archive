package com.manager.constant;

/**
 * StatusConstants
 * 定义一些常用的状态字符串
 */
public final class StatusConstants {

    /* Response 部分 */

    /* 成功 */
    public static final String SUCCESS = "success";

    /* 失败 */
    public static final String ERROR = "error";

    /* 未知错误 */
    public static final String UNFORESEEN = "unforeseen error";

    /* Code 部分 */

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_UNFORESEEN = 1;

    public static final int CODE_UNSPECIFIED = 1000;

    public static final int CODE_WRONG_USER = 1001;

    public static final int CODE_WRONG_PASSWORD = 1002;

    public static final int CODE_FORBIDDEN_USER = 1003;
}
