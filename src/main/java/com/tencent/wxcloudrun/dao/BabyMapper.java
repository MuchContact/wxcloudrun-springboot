package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.BabyStretch;
import com.tencent.wxcloudrun.model.Counter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface BabyMapper {

  Counter getCounter(@Param("id") Integer id);

  void upsertCount(Counter counter);

  void clearCount(@Param("id") Integer id);

    int insert(@Param("ot")Date time);
}
