package com.kun.common.database.entity.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kun.common.database.util.MpQueryUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

/**
 * 分页查询参数
 *
 * @author guozhongcheng
 * @since 2023/6/2 13:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageParameter<T> extends Page<T> {
    /**
     * 查询参数对象
     */
    @Valid
    private T selectParameter;
    /**
     * 排序对象
     */
    private List<OrderItem> orders;
    /**
     * 排序参数
     */
    private String orderBy;
    /**
     * 当前页
     */
    private Integer pageCurrent;
    /**
     * 页大小
     */
    private Integer pageSize;

    public void setPageCurrent(Integer pageCurrent) {
        this.pageCurrent = pageCurrent;
        super.current = pageCurrent;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        super.size = pageSize;
    }

    public <R> QueryWrapper<R> buildQueryWrapper(Class<R> cls) {
        return MpQueryUtil.getPredicate(this.selectParameter, cls);
    }

    @SuppressWarnings("ALL")
    public Page buildQueryPage() {
        return this;
    }
}
