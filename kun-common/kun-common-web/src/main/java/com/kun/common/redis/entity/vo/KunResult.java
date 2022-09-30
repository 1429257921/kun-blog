package com.kun.common.redis.entity.vo;

import com.kun.common.redis.util.KunResultUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 接口响应vo对象
 *
 * @author gzc
 * @since 2022/9/22
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KunResult<T> extends KunResultUtil implements Serializable {
    /**
     * 响应状态码
     */
    private String errCode;
    /**
     * 响应消息
     */
    private String errMsg;
    /**
     * 响应结果
     */
    private T data;
}
