package com.xieminjie.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xieminjie.entity.Product;

import java.util.List;

public class ExcelImportListener extends AnalysisEventListener<Product> {

    private final List<Product> productList;
    private int rowIndex = 0; // 添加行号计数

    public ExcelImportListener(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public void invoke(Product product, AnalysisContext context) {
        // 校验必填字段
        if (product.getSerialNumber() == null || product.getSerialNumber().isEmpty()) {
            System.out.println("跳过空序列号的记录: " + product);
            return;
        }
        productList.add(product);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("Excel解析完成，共 " + productList.size() + " 条有效记录");
    }
}