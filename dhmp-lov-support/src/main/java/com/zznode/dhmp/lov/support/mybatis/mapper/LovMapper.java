package com.zznode.dhmp.lov.support.mybatis.mapper;

import com.zznode.dhmp.lov.domain.LovValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * LovMapper
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
public interface LovMapper {

    /**
     * 查询值集值列表
     *
     * @param lovCode lovCode
     * @return 值集值列表
     */
    @Select("select * from " + LovValue.LOV_VALUE_TABLE_NAME + " where lov_code = #{lovCode}")
    List<LovValue> selectLovValueList(@Param("lovCode") String lovCode);

}
