package com.kun.blog.service.impl;

import com.kun.blog.entity.po.Emoji;
import com.kun.blog.mapper.EmojiMapper;
import com.kun.blog.service.IEmojiService;
import com.kun.common.database.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gzc
 * @since 2022-10-01 20:04:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmojiServiceImpl extends BaseServiceImpl<EmojiMapper, Emoji> implements IEmojiService {


}
