package com.kun.blog.entity.vo;

import lombok.Data;

/**
 * 获取配置信息接口出参对象
 *
 * @author gzc
 * @since 2022/10/18 1:42
 **/
@Data
public class GetConfigVO {

    /**
     * 图片基础地址
     */
    private String pictureBaseUrl;

}
