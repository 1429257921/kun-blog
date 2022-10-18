package com.kun.blog.service.impl;

import com.kun.blog.entity.vo.GetConfigVO;
import com.kun.blog.service.ConfigService;
import com.kun.common.core.utils.ip.IPUtil;
import com.kun.common.web.util.WebContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author gzc
 * @since 2022/10/18 2:49
 **/
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Value("${fdfs.fileServerUrl}")
    private String fileServerUrl;

    @Override
    public GetConfigVO getConfig() {
        String ip = IPUtil.getIp(WebContextUtil.getRequest());
        System.out.println(IPUtil.getCityInfo(ip));
        GetConfigVO getConfigVO = new GetConfigVO();
        getConfigVO.setPictureBaseUrl(fileServerUrl);
        return getConfigVO;
    }
}
