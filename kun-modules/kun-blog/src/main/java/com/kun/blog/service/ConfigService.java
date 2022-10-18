package com.kun.blog.service;

import com.kun.blog.entity.vo.GetConfigVO;

/**
 * 配置信息接口
 *
 * @author gzc
 * @since 2022/10/18 1:42
 **/
public interface ConfigService {
    /**
     * 获取配置
     *
     * @return
     */
    GetConfigVO getConfig();
}
