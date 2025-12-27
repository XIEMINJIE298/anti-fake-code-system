package com.xieminjie.mapper;


import com.xieminjie.entity.TUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TUserMapper {

    @Select("SELECT * FROM t_user WHERE username = #{username}")
    TUser findByUsername(String username);

    @Insert("INSERT INTO t_user(username,password) VALUES(#{username},#{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TUser user);

}
