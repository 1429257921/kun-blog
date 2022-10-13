package com.kun.blog.service.impl;

import com.kun.blog.entity.po.KunUser;
import com.kun.blog.mapper.KunUserMapper;
import com.kun.blog.service.IKunUserService;
import com.kun.common.database.service.impl.BaseServiceImpl;
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


}
