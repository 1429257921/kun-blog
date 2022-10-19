package com.kun.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kun.blog.entity.po.KunUser;
import com.kun.blog.entity.vo.GetUserDetailsVO;
import com.kun.blog.mapper.KunUserMapper;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.IKunUserService;
import com.kun.blog.util.SecurityUtils;
import com.kun.common.database.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author gzc
 * @since 2022-10-07 21:05:29
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KunUserServiceImpl extends BaseServiceImpl<KunUserMapper, KunUser> implements IKunUserService {


    @Override
    public GetUserDetailsVO getUserDetails() {
        JwtUser jwtUser = (JwtUser) SecurityUtils.getUserDetails();
        KunUser kunUser = baseMapper.selectById(jwtUser.getId());
        return BeanUtil.copyProperties(kunUser,GetUserDetailsVO.class);
    }
}
