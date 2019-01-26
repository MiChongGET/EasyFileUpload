package cn.buildworld.fileupload.comm;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

/**
 * 上传文件到七牛云工具类
 *
 * @Author MiChong
 * @Email: 1564666023@qq.com
 * @Create 2018-03-06 0:47
 * @Version: V1.0
 */
@ConfigurationProperties(value = "qiniuyun.config")
public class Upload2QiNiuCloud {

    private String accesskey;
    private String secretkey;
    private String bucket;
    private String fileurl;

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Upload2QiNiuCloud() {
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    /**
     * 本地文件上传
     *
     * @param path
     * @param filename
     * @return
     */
    public DefaultPutRet uploadFile(String path, String filename) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = path;
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filename;
        Auth auth = Auth.create(accesskey, secretkey);
        String upToken = auth.uploadToken(bucket);
        DefaultPutRet putRet = null;
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return putRet;
    }


    /**
     * 字节数组上传
     *
     * @param bytes
     * @param filename
     * @return
     */
    public DefaultPutRet uploadFileByBytes(String bytes, String filename) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filename;
        DefaultPutRet putRet = null;

        try {
            byte[] uploadBytes = bytes.getBytes("utf-8");
            Auth auth = Auth.create(accesskey, secretkey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }

        return putRet;
    }


    /**
     * InputStream流上传
     *
     * @param inputStream
     * @param filename
     * @return
     */
    public DefaultPutRet uploadFile(InputStream inputStream, String filename) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filename;
        DefaultPutRet putRet = null;
        Auth auth = Auth.create(accesskey, secretkey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            //解析上传成功的结果
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

        return putRet;
    }

    /**
     * 断点续传
     *
     * @param path
     * @param filename
     * @return
     */
    public DefaultPutRet uploadFileByHTTP(String path, String filename) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filename;
        DefaultPutRet putRet = null;
        Auth auth = Auth.create(accesskey, secretkey);
        String upToken = auth.uploadToken(bucket);
        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(path, key, upToken);
                //解析上传成功的结果
                putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return putRet;
    }
}
