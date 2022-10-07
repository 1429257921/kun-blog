package com.kun.blog.service.impl;

import com.kun.blog.entity.po.KunLog;
import com.kun.blog.mapper.KunLogMapper;
import com.kun.blog.service.IKunLogService;
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
public class KunLogServiceImpl extends BaseServiceImpl<KunLogMapper, KunLog> implements IKunLogService {


}
