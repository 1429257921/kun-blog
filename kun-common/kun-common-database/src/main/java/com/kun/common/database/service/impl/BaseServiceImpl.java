package com.kun.common.database.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.kun.common.database.entity.vo.OrderQueryParam;
import com.kun.common.database.entity.vo.QueryParam;
import com.kun.common.database.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

/**
 * 公共service实现类
 *
 * @author gzc
 * @since 2022/9/30 22:32
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    protected Page setPageParam(QueryParam queryParam) {
        return setPageParam(queryParam, null);
    }

    protected Page setPageParam(QueryParam queryParam, OrderItem defaultOrder) {
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(queryParam.getPage());
        // 设置页大小
        page.setSize(queryParam.getLimit());
        /**
         * 如果是queryParam是OrderQueryParam，并且不为空，则使用前端排序
         * 否则使用默认排序
         */
        if (queryParam instanceof OrderQueryParam) {
            OrderQueryParam orderQueryParam = (OrderQueryParam) queryParam;
            List<OrderItem> orderItems = orderQueryParam.getOrders();
            if (CollectionUtil.isEmpty(orderItems)) {
                page.setOrders(Arrays.asList(defaultOrder));
            } else {
                page.setOrders(orderItems);
            }
        } else {
            page.setOrders(Arrays.asList(defaultOrder));
        }

        return page;
    }

    /**
     * 分页
     */
    protected void getPage(Pageable pageable) {
        String order = null;
        if (pageable.getSort() != null) {
            order = pageable.getSort().toString();
            order = order.replace(":", "");
            if ("UNSORTED".equals(order)) {
                order = "id desc";
            }
        }
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize(), order);
    }

}
