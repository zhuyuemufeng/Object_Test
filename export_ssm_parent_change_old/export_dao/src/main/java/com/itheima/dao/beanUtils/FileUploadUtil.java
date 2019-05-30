package com.itheima.dao.beanUtils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Component
@PropertySource("classpath:fileLoad/qiniu.properties")
public class FileUploadUtil {

    @Value("${qiniu.accessKey}")
    private String accessKey ;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.rtValue}")
    private String rtValue;

    /**
     * 将图片上传到七牛云服务
     *      1.更新用户图片信息（用户id=key）
     *      2.访问图片
     *          存储空间分配的临时域名（免费用户有效期一个月）：http://pkbivgfrm.bkt.clouddn.com+上传的文件名
     *      3.对于更新之后访问图片，防止缓存
     *          更新图片之后：访问的时候，再请求连接添加上时间戳
     *
     */
    public String upload(MultipartFile multipartFile)throws Exception{
	    String img = "";
        try {
            //取出原始文件名
            String fileName = multipartFile.getOriginalFilename();
            //随机化文件名
            String uuid = UUID.randomUUID().toString().replace("-","").toUpperCase();
            fileName = uuid+"_"+fileName;
            //构造一个带指定Zone对象的配置类
            //指定上传文件服务器地址：
            Configuration cfg = new Configuration(Zone.zone0());
            //...其他参数参考类注释
            //上传管理器
            UploadManager uploadManager = new UploadManager(cfg);
            //身份认证
            Auth auth = Auth.create(accessKey, secretKey);
            //指定覆盖上传
            String upToken = auth.uploadToken(bucket,fileName);
            //上传
            Response response = uploadManager.put(multipartFile.getBytes(), fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            img = rtValue+"/"+fileName;
        } catch (QiniuException ex) {
            System.err.println(ex.getMessage());
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
            }
        }
        return img;
    }

    public void delete(String url) {
        if (url != null && url != ""&&url.contains("/")) {
            Auth auth = Auth.create(accessKey, secretKey);
            Configuration config = new Configuration(Zone.autoZone());
            BucketManager bucketMgr = new BucketManager(auth, config);
            int lastIndex = url.lastIndexOf("/");
            String imgName = url.substring(lastIndex + 1);
            try {
                bucketMgr.delete(bucket, imgName);
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
    }

    public void batchDelete(List<String> list){
        Configuration cfg = new Configuration(Zone.autoZone());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            String[] keyList = list.toArray(new String[1]);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }

    }
}
