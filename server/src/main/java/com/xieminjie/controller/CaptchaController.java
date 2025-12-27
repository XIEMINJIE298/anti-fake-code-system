package com.xieminjie.controller;

import com.wf.captcha.SpecCaptcha;
import com.xieminjie.common.Result;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    /** 内存缓存：uuid -> 计算结果 */
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    /** 定时线程池，每 5 秒清理一次过期数据 */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    {
        scheduler.scheduleWithFixedDelay(() -> {
            // 简单策略：>120 秒未访问就删
            long now = System.currentTimeMillis();
            cache.entrySet().removeIf(e -> now - Long.parseLong(e.getKey().split("_")[1]) > 120_000);
        }, 5, 5, TimeUnit.SECONDS);
    }

    /** 生成算术验证码图片 */
    @GetMapping("/image")
    public void image(@RequestParam String uuid, HttpServletResponse resp) throws IOException {
        System.out.println("生成验证码：" + uuid);
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 4); // 4位字符
        String text = captcha.text();           // 计算结果
        cache.put(uuid + "_" + System.currentTimeMillis(), text);
        resp.setContentType("image/png");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        captcha.out(resp.getOutputStream());
    }

    /** 校验验证码 */
    @PostMapping("/check")
    public Result<Boolean> check(@RequestBody Map<String, String> param) {
        String uuid = param.get("uuid");
        String code = param.get("code");
        if (uuid == null || code == null) {
            return Result.error("参数缺失");
        }
        String key = cache.keySet().stream()
                .filter(k -> k.startsWith(uuid + "_"))
                .findFirst()
                .orElse(null);
        if (key == null) {
            return Result.error("验证码已过期");
        }
        String cacheCode = cache.get(key);
        if (cacheCode.equalsIgnoreCase(code)) {
            cache.remove(key);          // 一次性
            return Result.success(true);
        }
        return Result.error("验证码错误");
    }
}