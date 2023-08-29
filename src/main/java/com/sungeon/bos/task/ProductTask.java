package com.sungeon.bos.task;

import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.entity.base.AttributeValueEntity;
import com.sungeon.bos.entity.base.DimEntity;
import com.sungeon.bos.entity.base.ProductEntity;
import com.sungeon.bos.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author : 刘国帅
 * @date : 2016-12-6
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class ProductTask extends BaseTask {

    @Autowired
    private IProductService productService;

    public void syncBsijaDim() {
        try {
            List<DimEntity> dims;
            String startTime = baseService.getThirdTime("BSIJA_DIM_SYNC_TIME");
            Date now = new Date();
            int page = 1;
            int pageSize = 200;
            do {
                // 年份
                dims = productService.syncBsijaDim("DIM2", "DIM5", false, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            page = 1;
            do {
                // 季节
                dims = productService.syncBsijaDim("DIM3", "DIM6", true, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            page = 1;
            do {
                // 性别
                dims = productService.syncBsijaDim("DIM4", "DIM20", false, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            page = 1;
            do {
                // 大类
                dims = productService.syncBsijaDim("DIM10", "DIM17", true, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            page = 1;
            do {
                // 中类
                dims = productService.syncBsijaDim("DIM6", "DIM18", true, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            // page = 1;
            // do {
            //     // 小类
            //     dims = productService.syncBsijaDim("DIM11", "DIM19", true, startTime,
            //             null, page++, pageSize);
            // } while (dims.size() == pageSize);
            page = 1;
            do {
                // 品类——毕厶迦-小类
                dims = productService.syncBsijaDim("DIM8", "DIM19", true, startTime,
                        null, page, pageSize);
            } while (dims.size() == pageSize);
            // do {
            //     // 产品风格
            //     dims = productService.syncBsijaDim("DIM9", "DIM8", true, startTime, null);
            // } while (CollectionUtils.isNotEmpty(dims));
            page = 1;
            do {
                // 波段
                dims = productService.syncBsijaDim("DIM7", "DIM16", true, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            // do {
            //     // 价格段
            //     dims = productService.syncBsijaDim("DIM12", "", true, startTime, null);
            // } while (CollectionUtils.isNotEmpty(dims));
            // do {
            //     // 货品类型
            //     dims = productService.syncBsijaDim("DIM17", "", true, startTime, null);
            // } while (CollectionUtils.isNotEmpty(dims));
            page = 1;
            do {
                // 执行标准
                dims = productService.syncBsijaDim("DIM15", "DIM13", true, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            page = 1;
            do {
                // 安全类别
                dims = productService.syncBsijaDim("DIM16", "DIM14", true, startTime,
                        null, page++, pageSize);
            } while (dims.size() == pageSize);
            baseService.updateThirdTime("BSIJA_DIM_SYNC_TIME", now);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void syncBsijaColor() {
        try {
            List<AttributeValueEntity> attributes;
            String startTime = baseService.getThirdTime("BSIJA_COLOR_SYNC_TIME");
            Date now = new Date();
            int page = 1;
            int pageSize = 100;
            do {
                attributes = productService.syncBsijaColor(startTime, null, page++, pageSize);
            } while (attributes.size() == pageSize);
            baseService.updateThirdTime("BSIJA_COLOR_SYNC_TIME", now);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void syncBsijaSize() {
        try {
            List<AttributeValueEntity> attributes;
            String startTime = baseService.getThirdTime("BSIJA_SIZE_SYNC_TIME");
            Date now = new Date();
            int page = 1;
            int pageSize = 100;
            do {
                attributes = productService.syncBsijaSize(startTime, null, page++, pageSize);
            } while (attributes.size() == pageSize);
            baseService.updateThirdTime("BSIJA_SIZE_SYNC_TIME", now);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void syncBsijaProduct() {
        Date now = new Date();
        try {
            List<ProductEntity> products;
            String startTime = baseService.getThirdTime("BSIJA_PRODUCT_SYNC_TIME");
            int page = 1;
            int pageSize = 10;
            List<String> productCodes = new ArrayList<>();
            boolean be = false;
            do {
                products = productService.syncBsijaProduct(startTime, null, page++, pageSize);
                if (CollectionUtils.isNotEmpty(products)) {
                    for (ProductEntity product : products) {
                        boolean exists = productCodes.stream().anyMatch(code -> product.getProductCode().equals(code));
                        if (exists) {
                            be = true;
                        } else {
                            productCodes.add(product.getProductCode());
                        }
                    }
                    if (be) {
                        break;
                    }
                }
            } while (products.size() == pageSize);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        baseService.updateThirdTime("BSIJA_PRODUCT_SYNC_TIME", now);
    }

}
