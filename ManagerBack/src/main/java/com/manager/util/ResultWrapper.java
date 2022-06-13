package com.manager.util;

import com.manager.constant.StatusConstants;
import com.manager.vo.ResultVO;

/**
 * 将 data 数据包装成最终的返回结果
 */
public class ResultWrapper {

    /**
     * 请求处理成功返回对象包装
     */
    public static ResultVO success(String msg, Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(StatusConstants.SUCCESS);
        resultVO.setMessage(msg);
        resultVO.setData(data);
        return resultVO;
    }

    /**
     * 请求处理失败返回对象包装
     */
    public static ResultVO error(String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(StatusConstants.ERROR);
        resultVO.setMessage(msg);
        resultVO.setData(null);
        return resultVO;
    }

    public static ResultVO error(int code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(StatusConstants.ERROR);
        resultVO.setMessage(msg);
        resultVO.setCode(code);
        resultVO.setData(null);
        return resultVO;
    }
}
