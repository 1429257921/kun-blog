package com.kun.common.database.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询参数
 *
 * @author gzc
 * @since 2022/10/6 13:38
 */
@Data
public abstract class QueryParam implements Serializable {
    private static final long serialVersionUID = -3263921252635611410L;

    private Integer page = 1;
    private Integer limit = 10;
    private String keyword;

    public void setCurrent(Integer current) {
        if (current == null || current <= 0) {
            this.page = 1;
        } else {
            this.page = current;
        }
    }

    public void setSize(Integer size) {
        if (size == null || size <= 0) {
            this.limit = 10;
        } else {
            this.limit = size;
        }
    }

}
