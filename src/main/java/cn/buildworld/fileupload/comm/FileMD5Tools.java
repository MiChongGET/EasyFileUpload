package cn.buildworld.fileupload.comm;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 文件MD5获取工具
 *
 * @Author MiChong
 * @Email: 1564666023@qq.com
 * @Create 2019-01-25 20:15
 * @Version: V1.0
 */

public class FileMD5Tools {


    /**
     * 获取MD5
     * @param inputStream 输入流
     * @return
     * @throws Exception
     */
    public String getFileMD5(InputStream inputStream) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
            md.update(buffer, 0, length);
        }
        BigInteger bigInt = new BigInteger(1, md.digest());
        return bigInt.toString(16);
    }

}
