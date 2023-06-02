package com.kun.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kun.blog.entity.po.UserLoginRecord;
import com.kun.blog.entity.req.UserLoginRecordPageListReq;
import com.kun.blog.entity.vo.UserLoginRecordPageListVO;
import com.kun.common.database.entity.dto.PageParameter;
import com.kun.common.database.service.BaseService;

/**
 * 坤坤云用户登录记录表业务接口
 *
 * @author gzc
 * @since 2023-01-05 18:04:37
 */
public interface IUserLoginRecordService extends BaseService<UserLoginRecord> {

    /**
     * 分页查询
     *
     * @param pageParameter 分页查询参数
     * @return 分页数据
     */
    Page<UserLoginRecordPageListVO> pageList(PageParameter<UserLoginRecordPageListReq> pageParameter);
}
