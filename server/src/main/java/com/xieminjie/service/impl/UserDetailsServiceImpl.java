package com.xieminjie.service.impl;

import com.xieminjie.entity.TUser;
import com.xieminjie.mapper.TUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TUserMapper tuserMapper;

    // 手动构造器（不用 Lombok 也行）
    public UserDetailsServiceImpl(TUserMapper tuserMapper) {
        this.tuserMapper = tuserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser user = tuserMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 把数据库用户包装成 Spring Security 需要的 UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER")   // 暂时写死，后面可扩展权限表
                .build();
    }
}
