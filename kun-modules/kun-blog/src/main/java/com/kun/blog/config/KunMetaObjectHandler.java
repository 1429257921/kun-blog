//package com.kun.blog.config;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//
///**
// * mybatis-plus值填充
// *
// * @author gzc
// * @since 2022/12/30 12:21
// */
//@Component
//public class KunMetaObjectHandler implements MetaObjectHandler {
//    private final static String CREATE_BY = "createBy";
//    private final static String CREATE_TIME = "createTime";
//    private final static String UPDATE_BY = "updateBy";
//    private final static String UPDATE_TIME = "updateTime";
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.strictInsertFill(metaObject, "createTime", Timestamp.class, Timestamp.valueOf(LocalDateTime.now()));
//        this.strictInsertFill(metaObject, "createDate", Timestamp.class, Timestamp.valueOf(LocalDateTime.now()));
////        setUserNameValue(metaObject, CREATE_BY);
//        setDateValue(metaObject, CREATE_TIME);
////        setUserNameValue(metaObject, UPDATE_BY);
//        setDateValue(metaObject, UPDATE_TIME);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.strictUpdateFill(metaObject, "updateTime", Timestamp.class, Timestamp.valueOf(LocalDateTime.now()));
//        this.strictUpdateFill(metaObject, "updateDate", Timestamp.class, Timestamp.valueOf(LocalDateTime.now()));
//        this.strictUpdateFill(metaObject, "modifyTime", Timestamp.class, Timestamp.valueOf(LocalDateTime.now()));
////        setUserNameValue(metaObject, UPDATE_BY);
//        setDateValue(metaObject, UPDATE_TIME);
//    }
//
//
//    /**
//     * 赋值日期类型
//     *
//     * @param metaObject 原对象
//     * @param fieldName  类属性名称
//     */
//    private void setDateValue(MetaObject metaObject, String fieldName) {
//        if (metaObject.hasSetter(fieldName)) {
//            // 获取值类型
//            Class<?> valueType = metaObject.getGetterType(fieldName);
//            metaObject.setValue(fieldName, null);
//            if (LocalDateTime.class.equals(valueType)) {
//                this.fillStrategy(metaObject, fieldName, LocalDateTime.now());
//            } else if (LocalDate.class.equals(valueType)) {
//                this.fillStrategy(metaObject, fieldName, LocalDate.now());
//            } else if (Timestamp.class.equals(valueType)) {
//                this.fillStrategy(metaObject, fieldName, new Timestamp(System.currentTimeMillis()));
//            } else {
//                this.fillStrategy(metaObject, fieldName, new Date());
//            }
//        }
//    }
//
//}
