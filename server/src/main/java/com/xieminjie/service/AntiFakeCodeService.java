package com.xieminjie.service;

import com.xieminjie.dto.VerifyRequest;
import com.xieminjie.dto.VerifyResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AntiFakeCodeService {
    Map<String, Object> uploadAndGenerateAntiFakeCode(MultipartFile file);

    VerifyResultDTO verifyCode(VerifyRequest request, String ip, String userAgent);
}
