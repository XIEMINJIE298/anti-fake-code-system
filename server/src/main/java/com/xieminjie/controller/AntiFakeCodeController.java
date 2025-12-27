package com.xieminjie.controller;

import com.xieminjie.common.Result;
import com.xieminjie.dto.VerifyRequest;
import com.xieminjie.dto.VerifyResultDTO;
import com.xieminjie.service.AntiFakeCodeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class AntiFakeCodeController {

    @Autowired
    private AntiFakeCodeService antiFakeCodeService;

    /**
     * 上传Excel生成防伪码并返回文件
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/upload")
    public void uploadExcel(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        OutputStream out = null;
        InputStream in = null;

        try {
            // 处理文件
            Map<String, Object> result = antiFakeCodeService.uploadAndGenerateAntiFakeCode(file);

            // 获取生成的文件名
            String fileName = (String) result.get("fileName");

            // 使用系统兼容的路径分隔符
            Path filePath = Paths.get("temp", fileName);
            File resultFile = filePath.toFile();

            if (!resultFile.exists()) {
                throw new FileNotFoundException("生成的文件不存在: " + filePath);
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            // 对文件名进行URL编码
            String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            // 读取文件并写入响应
            in = new FileInputStream(resultFile);
            out = response.getOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.flush();

            // 删除临时文件
            resultFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain;charset=utf-8");
            try {
                out = response.getOutputStream();
                String errorMessage = "处理失败: " + e.getMessage();
                out.write(errorMessage.getBytes("UTF-8"));
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            // 确保关闭流
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /**
     * 静默关闭流
     */
    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }
    /**
     * 验证防伪码
     */
    @PostMapping("/verify")
    public Result<VerifyResultDTO> verifyCode(@RequestBody VerifyRequest request, HttpServletRequest httpRequest) {
        try {
            // 获取客户端IP
            String ip = getClientIp(httpRequest);

            // 获取User-Agent
            String userAgent = httpRequest.getHeader("User-Agent");

            // 执行验证
            VerifyResultDTO result = antiFakeCodeService.verifyCode(request, ip, userAgent);

            if (result.isValid()) {
                return Result.success(result);
            } else {
                return Result.error(result.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果经过多级代理，取第一个真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}