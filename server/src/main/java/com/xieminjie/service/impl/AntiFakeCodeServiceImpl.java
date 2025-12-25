package com.xieminjie.service.impl;

import com.alibaba.excel.EasyExcel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xieminjie.dto.VerifyRequest;
import com.xieminjie.dto.VerifyResultDTO;
import com.xieminjie.entity.AntiFakeCode;
import com.xieminjie.entity.Product;
import com.xieminjie.entity.VerificationRecord;
import com.xieminjie.listener.ExcelImportListener;
import com.xieminjie.mapper.AntiFakeCodeMapper;
import com.xieminjie.mapper.ProductMapper;
import com.xieminjie.mapper.VerificationRecordMapper;
import com.xieminjie.service.AntiFakeCodeService;
import com.xieminjie.vo.ProductWithCodeVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AntiFakeCodeServiceImpl implements AntiFakeCodeService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AntiFakeCodeMapper antiFakeCodeMapper;

    @Autowired
    private VerificationRecordMapper verificationRecordMapper;

    @Value("${app.qr-domain}")
    private String qrDomain;

    @Override
    public Map<String, Object> uploadAndGenerateAntiFakeCode(MultipartFile file) {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".xlsx")) {
            throw new RuntimeException("仅支持.xlsx格式文件");
        }

        // 2. 读取Excel数据
        List<Product> productList = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), Product.class, new ExcelImportListener(productList))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException("读取Excel文件失败: " + e.getMessage());
        }

        if (productList.isEmpty()) {
            throw new RuntimeException("Excel中没有有效数据");
        }

        // 3. 批量插入产品数据
        Date now = new Date();
        for (Product product : productList) {
            product.setCreatedTime(now);
            product.setUpdatedTime(now);
        }

        batchInsertProducts(productList);

        // 4. 查询刚插入的产品（获取ID）
        List<String> serialNumbers = productList.stream()
                .map(Product::getSerialNumber)
                .collect(Collectors.toList());
        List<Product> savedProducts = productMapper.selectBySerialNumbers(serialNumbers);

        // 5. 批量生成防伪码
        List<AntiFakeCode> antiFakeCodeList = new ArrayList<>();
        for (Product product : savedProducts) {
            String code = generateAntiFakeCode(product);

            AntiFakeCode antiFakeCode = new AntiFakeCode();
            antiFakeCode.setCode(code);
            antiFakeCode.setSerialNumber(product.getSerialNumber());
            antiFakeCode.setProductId(product.getId());
            antiFakeCode.setStatus(1); // 有效
            antiFakeCode.setGenerateTime(now);
            antiFakeCode.setVerifyCount(0);
            antiFakeCode.setCreatedTime(now);

            antiFakeCodeList.add(antiFakeCode);
        }

        // 6. 批量插入防伪码
        if (!antiFakeCodeList.isEmpty()) {
            batchInsertAntiFakeCodes(antiFakeCodeList);
        }

        // 7. 生成结果Excel
        String outputFileName = "防伪码_" + originalFilename;
        try {
            generateResultExcel(productList, antiFakeCodeList, outputFileName);
        } catch (IOException e) {
            throw new RuntimeException("生成结果Excel失败: " + e.getMessage());
        }

        // 8. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("totalProducts", productList.size());
        result.put("totalCodes", antiFakeCodeList.size());
        result.put("fileName", outputFileName);
        result.put("downloadUrl", "/download/" + outputFileName);

        return result;
    }


    @Override
    public VerifyResultDTO verifyCode(VerifyRequest request, String ip, String userAgent) {
        String code = request.getCode();
        VerifyResultDTO result = new VerifyResultDTO();
        Date now = new Date();

        // 1. 查询防伪码信息
        AntiFakeCode antiFakeCode = antiFakeCodeMapper.selectByCode(code);

        if (antiFakeCode == null) {
            result.setValid(false);
            result.setMessage("防伪码不存在，请检查输入是否正确");
            return result;
        }

        // 2. 检查防伪码状态
        if (antiFakeCode.getStatus() == 0) {
            result.setValid(false);
            result.setMessage("该防伪码已作废，产品可能为假冒");
            return result;
        }

        // 3. 查询产品信息
        Product product = productMapper.selectById(antiFakeCode.getProductId());
        if (product == null) {
            result.setValid(false);
            result.setMessage("产品信息不存在");
            return result;
        }

        // 4. 更新防伪码验证信息
        Integer verifyCount = antiFakeCode.getVerifyCount() + 1;
        Date firstVerifyTime = antiFakeCode.getFirstVerifyTime();
        if (firstVerifyTime == null) {
            firstVerifyTime = now;
            antiFakeCodeMapper.updateVerifyInfo(code, firstVerifyTime, verifyCount);
        } else {
            antiFakeCodeMapper.updateVerifyInfo(code, firstVerifyTime, verifyCount);
        }

        // 5. 记录验证历史
        VerificationRecord record = new VerificationRecord();
        record.setAntiFakeCode(code);
        record.setProductId(product.getId());
        record.setVerifyIp(ip);
        record.setVerifyTime(now);
        record.setUserAgent(userAgent);
        verificationRecordMapper.insert(record);

        // 6. 组装返回结果
        result.setValid(true);
        result.setMessage("验证成功！产品为正品，请放心使用");
        result.setSerialNumber(product.getSerialNumber());
        result.setProductName(product.getProductName());
        result.setCategory(product.getCategory());
        result.setModel(product.getModel());
        result.setBatchNumber(product.getBatchNumber());
        result.setManufacturer(product.getManufacturer());
        result.setVerifyCount(verifyCount);
        result.setFirstVerifyTime(firstVerifyTime);
        result.setCurrentVerifyTime(now);

        return result;
    }

    /**
     * 生成防伪码：格式为 产品序列号(8位) + 随机码(8位) + 校验位(1位)
     */
    private String generateAntiFakeCode(Product product) {
        String serialNumber = product.getSerialNumber();
        if (serialNumber.length() > 8) {
            serialNumber = serialNumber.substring(0, 8);
        } else {
            serialNumber = String.format("%-8s", serialNumber).replace(' ', '0');
        }

        String randomCode = generateRandomCode(8);
        String base = serialNumber + randomCode;
        char checkDigit = calculateCheckDigit(base);

        return serialNumber + randomCode + checkDigit;
    }

    /**
     * 生成随机码
     */
    private String generateRandomCode(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNPQRSTUVWXYZ"; // 排除O避免混淆
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 计算校验位
     */
    private char calculateCheckDigit(String base) {
        int sum = 0;
        for (char c : base.toCharArray()) {
            sum += c;
        }
        int checkValue = sum % 36;
        return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(checkValue);
    }

    /**
     * 分批插入产品
     */
    private void batchInsertProducts(List<Product> products) {
        int batchSize = 500;
        for (int i = 0; i < products.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, products.size());
            List<Product> batch = products.subList(i, endIndex);
            productMapper.batchInsert(batch);
        }
    }

    /**
     * 分批插入防伪码
     */
    private void batchInsertAntiFakeCodes(List<AntiFakeCode> codes) {
        int batchSize = 500;
        for (int i = 0; i < codes.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, codes.size());
            List<AntiFakeCode> batch = codes.subList(i, endIndex);
            antiFakeCodeMapper.batchInsert(batch);
        }
    }

    /**
     * 生成结果Excel
     */
    private void generateResultExcel(List<Product> products,
                                     List<AntiFakeCode> codes,
                                     String fileName) throws IOException {
        // 创建防伪码映射
        Map<String, String> codeMap = codes.stream()
                .collect(Collectors.toMap(AntiFakeCode::getSerialNumber, AntiFakeCode::getCode));

        // 创建结果数据
        List<ProductWithCodeVO> resultData = products.stream()
                .map(product -> {
                    ProductWithCodeVO vo = new ProductWithCodeVO();
                    vo.setSerialNumber(product.getSerialNumber());
                    vo.setProductName(product.getProductName());
                    vo.setCategory(product.getCategory());
                    vo.setModel(product.getModel());
                    vo.setBatchNumber(product.getBatchNumber());
                    vo.setManufacturer(product.getManufacturer());
                    vo.setAntiFakeCode(codeMap.get(product.getSerialNumber()));
                    return vo;
                })
                .collect(Collectors.toList());

        // 写入Excel
        String filePath = "temp/" + fileName;
        File dir = new File("temp");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        EasyExcel.write(filePath, ProductWithCodeVO.class)
                .sheet("防伪码信息")
                .doWrite(resultData);

        // 4. 再用 POI 把二维码图片逐行插入
        File excelFile = new File(filePath);
        for (int i = 0; i < products.size(); i++) {
            String serial = products.get(i).getSerialNumber();
            String code   = codeMap.get(serial);
            String qrName = "qr_" + serial;
            String verifyUrl = qrDomain + "/verify?code=" + code;
            String qrPath = generateQrImage(verifyUrl, qrName);
            writeQrToExcel(excelFile, i + 1, qrPath);           // +1 跳过表头
            new File(qrPath).delete();                          // 图片已写入 Excel，本地文件可删
        }
    }

    /**
     * 根据防伪码文本生成二维码图片文件，返回文件绝对路径
     */
    private String generateQrImage(String content, String fileName) throws IOException {
        int width = 200;
        int height = 200;
        String format = "png";
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            Path path = Path.of("temp", fileName + ".png");
            MatrixToImageWriter.writeToPath(matrix, format, path);
            return path.toAbsolutePath().toString();
        } catch (Exception e) {
            throw new IOException("生成二维码失败", e);
        }
    }

    /**
     * 把二维码图片写入 Excel 指定行列（EasyExcel 不支持图片，这里用 POI 补充）
     */
    private void writeQrToExcel(File excelFile, int rowIndex, String qrPath) throws IOException {
        File tmp = new File(excelFile.getAbsolutePath() + ".tmp");
        try (XSSFWorkbook wb = new XSSFWorkbook(excelFile);
             FileOutputStream fos = new FileOutputStream(tmp)) {

            Sheet sheet = wb.getSheetAt(0);
            // 创建图片辅助类
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            // 读取二维码字节
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(qrPath));
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

            // 锚点：第7列（G列），rowIndex行，适度缩放
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 7, rowIndex, 8, rowIndex + 1);
            drawing.createPicture(anchor, pictureIdx);

            wb.write(fos);
        } catch (InvalidFormatException e) {
            throw new IOException("XSSFWorkbook抛出异常");
        }
        // 替换原文件
        if (!excelFile.delete() || !tmp.renameTo(excelFile)) {
            throw new IOException("二维码写入后文件替换失败");
        }
    }
}