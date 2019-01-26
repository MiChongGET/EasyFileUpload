package cn.buildworld.fileupload.dao;

import cn.buildworld.fileupload.entity.Myfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface MyfileDao extends JpaRepository<Myfile,String>,JpaSpecificationExecutor<Myfile>{

    /**
     * 根据md5查询已经存在的文件
     */
    public Myfile findByMd5AndState(String md5, String state);
}
