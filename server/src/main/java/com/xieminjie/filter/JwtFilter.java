package com.xieminjie.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xieminjie.common.Result;
import com.xieminjie.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


@Component
@Order(1)
public class JwtFilter extends OncePerRequestFilter {

    private final String[] WHITE = {
            "/api/auth/login",     // 添加登录路径
            "/api/auth/register",
            "/api/captcha/image",
            "/api/captcha/check"
    };
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        // 预检请求直接放过
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(req, res);
            return;
        }
        String token = req.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            token = req.getParameter("token");
        }
        if (token != null && token.startsWith("Bearer ")) token = token.substring(7);
        if (Arrays.stream(WHITE).anyMatch(req.getRequestURI()::equals) ||
                (token != null && jwtUtil.validate(token))) {
            if (token != null) req.setAttribute("username", jwtUtil.getUsername(token));
            chain.doFilter(req, res);
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json;charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(Result.error("未登录或令牌无效"));
            res.getWriter().write(json);
        }
    }
}
