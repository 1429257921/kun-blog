package com.kun.common.database.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 入库数据填充处理
 *
 * @author gzc
 * @since 2022/10/16 14:17
 */
public class KunMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("create_time", DateUtil.date());
        metaObject.setValue("update_time", DateUtil.date());
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("update_time", DateUtil.date());
    }
}
