package com.xieminjie.mapper;

import com.xieminjie.entity.AntiFakeCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AntiFakeCodeMapper {
    void batchInsert(@Param("codes") List<AntiFakeCode> codes);

    AntiFakeCode selectByCode(@Param("code") String code);

    void updateVerifyInfo(@Param("code") String code,
                          @Param("firstVerifyTime") java.util.Date firstVerifyTime,
                          @Param("verifyCount") Integer verifyCount);
}
