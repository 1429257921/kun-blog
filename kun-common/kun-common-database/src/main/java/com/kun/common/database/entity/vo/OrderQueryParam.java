package com.kun.common.database.entity.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

/**
 * 排序查询参数
 *
 * @author gzc
 * @since 2022/10/6 13:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OrderQueryParam extends QueryParam {
    private static final long serialVersionUID = 57714391204790143L;

    private List<OrderItem> orders;

    public void defaultOrder(OrderItem orderItem) {
        this.defaultOrders(Arrays.asList(orderItem));
    }

    public void defaultOrders(List<OrderItem> orderItems) {
        if (CollectionUtil.isEmpty(orderItems)) {
            return;
        }
        this.orders = orderItems;
    }

}
