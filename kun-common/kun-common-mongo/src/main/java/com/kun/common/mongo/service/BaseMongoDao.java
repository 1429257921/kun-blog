package com.kun.common.mongo.service;

import com.kun.common.mongo.vo.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * mongoDB基础持久层接口
 *
 * @author gzc
 * @since 2023/4/28 0028 13:43
 **/
public interface BaseMongoDao<T> {

    /**
     * 保存一个对象到mongodb
     *
     * @param entity 实体类
     * @return 保存的对象
     */
    T save(T entity);

    /**
     * 根据id删除对象
     *
     * @param t id
     */
    void deleteById(T t);

    /**
     * 根据对象的属性删除
     *
     * @param t 对象属性
     */
    void deleteByCondition(T t);


    /**
     * 根据id进行更新
     *
     * @param id 对象ID
     * @param t  更新对象
     */
    void updateById(String id, T t);


    /**
     * 根据对象的属性查询
     *
     * @param t 对象属性
     * @return 集合
     */
    List<T> findByCondition(T t);

    /**
     * 通过条件查询实体(集合)
     *
     * @param query 查询条件
     * @return 实体集合
     */
    List<T> find(Query query);

    /**
     * 通过一定的条件查询一个实体
     *
     * @param query 查询条件
     * @return 实体对象
     */
    T findOne(Query query);

    /**
     * 通过条件查询更新数据
     *
     * @param query  查询条件
     * @param update 更新条件
     */
    void update(Query query, Update update);

    /**
     * 通过ID获取记录
     *
     * @param id 对象ID
     * @return 实体对象
     */
    T findById(String id);

    /**
     * 通过ID获取记录,并且指定了集合名(表的意思)
     *
     * @param id             对象ID
     * @param collectionName 集合名称
     * @return 实体对象
     */
    T findById(String id, String collectionName);

    /**
     * 通过条件查询,查询分页结果
     *
     * @param page  分页条件
     * @param query 查询条件
     * @return 分页数据
     */
    Page<T> findPage(Page<T> page, Query query);

    /**
     * 求数据总和
     *
     * @param query 查询参数
     * @return 总数
     */
    long count(Query query);


    /**
     * 获取MongoDB模板操作
     *
     * @return mongo模板对象
     */
    MongoTemplate getMongoTemplate();
}

