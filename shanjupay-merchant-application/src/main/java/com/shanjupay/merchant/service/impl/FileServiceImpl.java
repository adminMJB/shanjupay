package com.shanjupay.merchant.service.impl;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.QiniuUtils;
import com.shanjupay.merchant.service.FileService;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/22 13:09
 **/
@Service
public class FileServiceImpl implements FileService {
    /**
     * 下载链接
     */
    @Value("${oss.qiniu.url}")
    private String qiniuUrl;
    /**
     * accessKey
     */
    @Value("${oss.qiniu.accessKey}")
    private String accessKey;
    /**
     * secretKey
     */
    @Value("${oss.qiniu.secretKey}")
    private String secretKey;
    /**
     * bucket
     */
    @Value("${oss.qiniu.bucket}")
    private String bucket;
    /**
     * 文件上传
     * @param bytes 文件字节数组
     * @param fileName 文件名
     * @return
     * @throws BusinessException
     */
    @Override
    public String upload(byte[] bytes, String fileName) throws BusinessException {
        try {
            QiniuUtils.upload2qniuyun(accessKey,secretKey,bucket,bytes,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100106);
        }
        return qiniuUrl+fileName;
    }
}
