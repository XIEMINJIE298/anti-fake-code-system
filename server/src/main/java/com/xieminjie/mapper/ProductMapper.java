package com.xieminjie.mapper;

import com.xieminjie.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    void batchInsert(@Param("products") List<Product> products);

    List<Product> selectBySerialNumbers(@Param("serialNumbers") List<String> serialNumbers);

    Product selectById(@Param("id") Long id);
}
