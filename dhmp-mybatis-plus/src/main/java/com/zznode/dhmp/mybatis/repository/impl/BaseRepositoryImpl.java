package com.zznode.dhmp.mybatis.repository.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zznode.dhmp.core.exception.OptimisticLockException;
import com.zznode.dhmp.mybatis.domain.BaseEntity;
import com.zznode.dhmp.mybatis.repository.BaseRepository;
import com.zznode.dhmp.mybatis.service.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * 基础Repository实现类
 *
 * @param <T> 实体类类型
 * @author 王俊
 */
public abstract class BaseRepositoryImpl<T> extends BaseServiceImpl<T> implements BaseRepository<T> {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(T entity) {
        boolean result = super.save(entity);
        this.setObjectVersionNumber(entity);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(T entity) {
        boolean result = super.updateById(entity);
        this.checkOptimisticLock(result, entity);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        boolean result = super.update(entity, updateWrapper);
        this.checkOptimisticLock(result, entity);
        return result;
    }


    /**
     * 检查乐观锁<br>
     * 检测到更新，修改失败时，抛出OptimisticLockException 异常
     *
     * @param res    update,delete 操作返回的值
     * @param record 操作参数
     */
    protected void checkOptimisticLock(boolean res, Object record) {
        if (!res && record instanceof BaseEntity) {
            throw new OptimisticLockException();
        }
    }

    /**
     * 使用通用插入方法插入数据时，默认objectVersionNumber返回1
     *
     * @param record 已经插入成功的数据
     */
    private void setObjectVersionNumber(Object record) {
        if (record instanceof BaseEntity baseEntity) {
            baseEntity.setObjectVersionNumber(DEFAULT_OBJECT_VERSION_NUMBER);
        }
    }
}
