package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dao.BabyMapper;
import com.tencent.wxcloudrun.model.BabyStretch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BabyService {
    final BabyMapper mapper;

    public BabyService(@Autowired BabyMapper mapper) {
        this.mapper = mapper;
    }

    public void newRecord(Date date) {
        mapper.insert(date);
    }

}
