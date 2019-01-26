package cn.buildworld.fileupload;

import cn.buildworld.fileupload.comm.FileMD5Tools;
import cn.buildworld.fileupload.comm.FileUtils;
import cn.buildworld.fileupload.comm.IdWorker;
import cn.buildworld.fileupload.comm.Upload2QiNiuCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FileuploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileuploadApplication.class, args);
    }
    @Bean
    public Upload2QiNiuCloud upload2QiNiuCloud() {
        return new Upload2QiNiuCloud();
    }

    @Bean
    public FileMD5Tools fileMD5Tools() {
        return new FileMD5Tools();
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
    @Bean
    public FileUtils fileUtils(){
        return new FileUtils();
    }
}

