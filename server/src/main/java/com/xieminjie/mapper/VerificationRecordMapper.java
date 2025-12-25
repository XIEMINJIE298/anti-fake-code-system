package com.xieminjie.mapper;

import com.xieminjie.entity.VerificationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VerificationRecordMapper {
    void insert(VerificationRecord record);
}
