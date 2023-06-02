package com.kun.blog.entity.req;

import com.kun.common.database.anno.Query;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/2
 **/
@Data
public class UserLoginRecordPageListReq implements Serializable {
    /**
     * 操作类型（0、登录成功，1、登出成功，2、登录失败，3、登出失败）
     */
    @Query(type = Query.Type.EQUAL)
    private Integer loginStatus;
}
