package com.xieminjie.controller;

import com.xieminjie.common.Result;
import com.xieminjie.dto.LoginDTO;
import com.xieminjie.entity.TUser;
import com.xieminjie.mapper.TUserMapper;
import com.xieminjie.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final TUserMapper tuserMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtUtil jwtUtil;

    /** 手动构造器 —— Spring 构造器注入 */
    public AuthController(TUserMapper tuserMapper,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.tuserMapper = tuserMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@Valid @RequestBody LoginDTO dto) {
        TUser user = tuserMapper.findByUsername(dto.getUsername());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        String token = jwtUtil.generate(user.getUsername());
        Map<String,Object> map = new HashMap<>();
        map.put("token", token);
        return Result.success(map);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody LoginDTO dto) {
        if (tuserMapper.findByUsername(dto.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        TUser user = new TUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        tuserMapper.insert(user);
        return Result.success(null);
    }
}
