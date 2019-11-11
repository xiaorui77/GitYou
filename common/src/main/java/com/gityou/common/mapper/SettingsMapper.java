package com.gityou.common.mapper;

import com.gityou.common.pojo.Settings;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SettingsMapper extends Mapper<Settings> {

    @Update("update `${table}` set `${key}` = #{value} where user = #{user}")
    Integer modify(Integer user, String table, String key, Object value);
}
