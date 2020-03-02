package com.liu.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiNiuYunTest2 {
    public static void main(String[] args) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huabei());
//...其他参数参考类注释
        String accessKey = "k6BkvEtMjzy-8ZF-0ZhIunNgeOFaCs99RIlAIjvn";
        String secretKey = "fhSt_3plNx1rrgeYHSxHFKtF9FJoCWUJHNzFz1FE";
        String bucket = "myhealthpicture";
        String key = "FoV-rmnh0udpqiDnK7olTS0vaOD6";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
