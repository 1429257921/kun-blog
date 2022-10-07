package com.kun.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.kun.blog.entity.dto.UserLoginReq;
import com.kun.blog.entity.po.KunUser;
import com.kun.blog.entity.vo.UserLoginVO;
import com.kun.blog.mapper.KunUserMapper;
import com.kun.blog.service.IKunUserService;
import com.kun.blog.service.AuthService;
import com.kun.common.core.utils.ip.IPUtil;
import com.kun.common.database.service.impl.BaseServiceImpl;
import com.kun.common.web.util.WebContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gzc
 * @since 2022-10-07 21:05:29
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KunUserServiceImpl extends BaseServiceImpl<KunUserMapper, KunUser> implements IKunUserService {

    private final AuthService tokenService;

    @Override
    public UserLoginVO login(UserLoginReq userLoginReq) {
        KunUser kunUser = new KunUser();
        kunUser.setUsername(userLoginReq.getUserName());
        kunUser.setPassword(userLoginReq.getPassWord());
        kunUser.setAddIp(IPUtil.getIp(WebContextUtil.getRequest()));
        kunUser.setCreateTime(DateUtil.date());
        kunUser.setUpdateTime(DateUtil.date());
        kunUser.setLoginType("routine");
        baseMapper.insert(kunUser);
        return null;
    }
}
