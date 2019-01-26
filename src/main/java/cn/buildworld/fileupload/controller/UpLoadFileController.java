package cn.buildworld.fileupload.controller;

import cn.buildworld.fileupload.comm.ByteToInputStream;
import cn.buildworld.fileupload.comm.FileMD5Tools;
import cn.buildworld.fileupload.comm.FileUtils;
import cn.buildworld.fileupload.comm.Upload2QiNiuCloud;
import cn.buildworld.fileupload.entity.Myfile;
import cn.buildworld.fileupload.entity.Result;
import cn.buildworld.fileupload.entity.StatusCode;
import cn.buildworld.fileupload.service.MyfileService;
import com.qiniu.storage.model.DefaultPutRet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文件上传控制器
 *
 * @Author MiChong
 * @Email: 1564666023@qq.com
 * @Create 2018-03-06 12:02
 * @Version: V1.0
 */
@RestController
@CrossOrigin
public class UpLoadFileController {

    @Autowired
    private Upload2QiNiuCloud upload2QiNiuCloudl;

    @Autowired
    public FileMD5Tools fileMD5Tools;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    public MyfileService myfileService;

    /**
     * 直接上传文件
     *
     * @param pictureFile
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result uploadPic(@RequestParam(value = "file") MultipartFile pictureFile) {

        //原始文件名称
        String pictureFile_name = pictureFile.getOriginalFilename();

        try {
            InputStream inputStream = pictureFile.getInputStream();

            //获取文件的md5值，查询文件是否已经存在
            String md5 = fileMD5Tools.getFileMD5(inputStream);
            Myfile byMd5 = myfileService.findByMd5(md5);
            if (byMd5 != null) {

                return new Result(true, StatusCode.FAST_UPLOAD, "秒传成功!", byMd5.getUrl());
            }

            //七牛云文件上传工具包
            DefaultPutRet myclassmate = upload2QiNiuCloudl.uploadFile(pictureFile.getInputStream(), pictureFile_name);

            if (myclassmate != null) {

                String picurl = upload2QiNiuCloudl.getFileurl() + myclassmate.key;

                //往数据库写入文件信息
                byMd5 = new Myfile();
                byMd5.setMd5(md5);
                byMd5.setName(pictureFile_name);
                byMd5.setUrl(picurl);
                byMd5.setCreatetime(new Date());
                byMd5.setSize(fileUtils.getSize((int)pictureFile.getSize()));
                byMd5.setState("1");
                myfileService.add(byMd5);

                return new Result(true, StatusCode.OK, "上传成功!", picurl);
            } else {
                return new Result(false, StatusCode.ERROR, "上传失败!", " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传失败!", " ");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传失败!", " ");
        }


    }


    /**
     * 通过url上传文件
     *
     * @param fileUrl
     * @return
     */
    @PostMapping(value = "/uploadByUrl")
    public Result uploadByUrl(@RequestParam(value = "fileUrl") String fileUrl) {

        if ("".equals(fileUrl) || fileUrl != null) {
            try {
                // 参数为本地图片二进制数组
                byte[] file = FileUtils.getFileFromNetByUrl(fileUrl);
                InputStream inputStream = ByteToInputStream.byte2Input(file);
                int size = inputStream.available();
                //获取文件名称
                String[] split = fileUrl.split("/");
                int length = split.length;
                String fileName = split[length - 1];

                //获取文件的md5值，查询文件是否已经存在
                String md5 = fileMD5Tools.getFileMD5(inputStream);
                Myfile byMd5 = myfileService.findByMd5(md5);
                if (byMd5 != null) {

                    return new Result(true, StatusCode.FAST_UPLOAD, "秒传成功!", byMd5.getUrl());
                }

                //七牛云文件上传工具包
                DefaultPutRet myclassmate = upload2QiNiuCloudl.uploadFile(ByteToInputStream.byte2Input(file), fileName);

                if (myclassmate != null) {

                    String picurl = upload2QiNiuCloudl.getFileurl() + myclassmate.key;

                    //往数据库写入文件信息
                    byMd5 = new Myfile();
                    byMd5.setMd5(md5);
                    byMd5.setName(fileName);
                    byMd5.setUrl(picurl);
                    byMd5.setCreatetime(new Date());
                    byMd5.setState("1");
                    byMd5.setSize(fileUtils.getSize(size));
                    myfileService.add(byMd5);

                    return new Result(true, StatusCode.OK, "上传成功!", picurl);

                }

            } catch (Exception e) {
                return new Result(false, StatusCode.ERROR, "上传失败!", " ");
            }
        }
        return new Result(false, StatusCode.ERROR, "上传失败!", " ");

    }
}
