package com.shanjupay.common.util;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 描述 七牛云测试工具类
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/21 22:48
 **/
public class QiniuUtils {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuUtils.class);

    /**
     * 文件上传
     * @param accessKey
     * @param secretKey
     * @param bucket
     * @param bytes
     * @param fileName
     */
    public static void upload2qniuyun(String accessKey, String secretKey, String bucket, byte[] bytes, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            LOGGER.info(ex.response.toString());
            throw new RuntimeException();
        }
    }


    public static void testUploda() throws IOException {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "FYBWnTlxqJ3XDOJ-EecY6LZY7LiIEjqgTaxugIoz";
        String secretKey = "mXuBcEBYiwrOckhICu8cVrhFA8kMDy4j5KJnQ322";
        String bucket = "shanjupaymjb";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID().toString() + ".png";
        FileInputStream fileInputStream = null;
        try {
            String filePath = "F:\\io\\2.jpg";
            fileInputStream = new FileInputStream(new File(filePath));
            byte[] bytes = IOUtils.toByteArray(fileInputStream);
            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(bytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            //ignore
        }
    }

    @SneakyThrows
    public static void downLoad() {
        String fileName = "c4b0f7b1-ee8a-4dc0-92ab-874a6c5e8389.png";
        String domainOfBucket = "http://qtgoh4jxv.hn-bkt.clouddn.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        String accessKey = "FYBWnTlxqJ3XDOJ-EecY6LZY7LiIEjqgTaxugIoz";
        String secretKey = "mXuBcEBYiwrOckhICu8cVrhFA8kMDy4j5KJnQ322";
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);
    }

    public static void main(String[] args) throws IOException {
        QiniuUtils.downLoad();
    }

}
