package com.kun.blog.mongo.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author gzc
 * @since 2023/4/28 0028 14:58
 **/
@Data
@Document(value = "t_sys_log")
public class SysLog implements Serializable {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 店铺ID
     */
    @Indexed
    private Long sid;

    /**
     * 用户Id
     */
    @Indexed
    private Long userId;

    /**
     * 业务类型(1：课程考试、2：课程学习、3：班级学习、4：班级考试、5：班级作业)
     */
    @Indexed
    private Integer bizType;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 章节ID
     */
    private Long chapterId;

    /**
     * 课时ID
     */
    private Long periodId;

    /**
     * 班级ID
     */
    private Long classroomId;

    /**
     * 班级作业ID
     */
    private Long classroomTaskId;

    /**
     * 班级考试ID
     */
    private Long classroomExamId;

    /**
     * 返回信息
     */
    private String resultMsg;

    /**
     * 签到类型(1：web端、2：app端)
     */
    private Integer signBizType;

    /**
     * 签到结果(0：失败、1：成功)
     */
    private Integer signResult;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态(0:禁用，1:可用)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
