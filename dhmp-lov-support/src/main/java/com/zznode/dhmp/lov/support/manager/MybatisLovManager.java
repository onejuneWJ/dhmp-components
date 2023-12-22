package com.zznode.dhmp.lov.support.manager;

import com.zznode.dhmp.lov.LovManager;
import com.zznode.dhmp.lov.domain.LovValue;
import com.zznode.dhmp.lov.support.mybatis.mapper.LovMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 基于mybatis的lov管理
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
public class MybatisLovManager implements LovManager, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SqlSessionFactory sqlSessionFactory;

    public MybatisLovManager(SqlSessionFactory sqlSessionFactory) {
        Assert.notNull(sqlSessionFactory, "sqlSessionFactory cannot be null");
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<LovValue> getLovValues(String lovCode) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            LovMapper lovMapper = sqlSession.getMapper(LovMapper.class);
            return lovMapper.selectLovValueList(lovCode);
        }
    }


    @Override
    public void afterPropertiesSet() {
        if (!sqlSessionFactory.getConfiguration().hasMapper(LovManager.class)) {
            sqlSessionFactory.getConfiguration().addMapper(LovMapper.class);
        }
    }
}
