package com.zznode.dhmp.mybatis.repository;


import com.zznode.dhmp.mybatis.service.BaseService;

/**
 * 基础Repository
 *
 * @param <T> 实体类型
 * @author 王俊 2021/4/19
 */
public interface BaseRepository<T> extends BaseService<T> {

    /**
     * 默认版本号
     */
    Long DEFAULT_OBJECT_VERSION_NUMBER = 1L;
}
