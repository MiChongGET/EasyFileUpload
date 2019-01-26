package cn.buildworld.fileupload.service;

import cn.buildworld.fileupload.comm.ByteToInputStream;
import cn.buildworld.fileupload.comm.FileMD5Tools;
import cn.buildworld.fileupload.comm.IdWorker;
import cn.buildworld.fileupload.comm.Upload2QiNiuCloud;
import cn.buildworld.fileupload.dao.MyfileDao;
import cn.buildworld.fileupload.entity.Myfile;
import cn.buildworld.fileupload.entity.Result;
import cn.buildworld.fileupload.entity.StatusCode;
import com.qiniu.storage.model.DefaultPutRet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class MyfileService {

    @Autowired
    private MyfileDao myfileDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private Upload2QiNiuCloud upload2QiNiuCloudl;

    @Autowired
    public FileMD5Tools fileMD5Tools;

    /**
     * 文件上传,通过本地文件和在线文件测试比较，此方法存在底层数据流的bug
     *
     * @param inputStream
     * @param pictureFile_name
     * @return
     */
    @Deprecated
    public Result fileUpload(InputStream inputStream, String pictureFile_name) {
        try {

            InputStream stream = inputStream;
            //转一下，防止出现MD5校验之后出现空文件
            byte[] input2byte = ByteToInputStream.input2byte(inputStream);

            //获取文件的md5值，查询文件是否已经存在
            String md5 = fileMD5Tools.getFileMD5(inputStream);
            Myfile byMd5 = myfileDao.findByMd5AndState(md5, 1 + "");
            if (byMd5 != null) {

                return new Result(true, StatusCode.FAST_UPLOAD, "秒传成功!", byMd5.getUrl());
            }

            //七牛云文件上传工具包
            DefaultPutRet myclassmate = upload2QiNiuCloudl.uploadFile(ByteToInputStream.byte2Input(input2byte), pictureFile_name);

            if (myclassmate != null) {

                String picurl = upload2QiNiuCloudl.getFileurl() + myclassmate.key;

                //往数据库写入文件信息
                byMd5 = new Myfile();
                byMd5.setMd5(md5);
                byMd5.setName(pictureFile_name);
                byMd5.setUrl(picurl);
                byMd5.setCreatetime(new Date());
                byMd5.setState("1");
                byMd5.setId(idWorker.nextId() + "");
                myfileDao.save(byMd5);

                return new Result(true, StatusCode.OK, "上传成功!", picurl);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传失败!", " ");
        }
        return new Result(false, StatusCode.ERROR, "上传失败!", " ");
    }

    /**
     * 根据md5查询
     */
    public Myfile findByMd5(String md5) {
        return myfileDao.findByMd5AndState(md5, 1 + "");
    }

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Myfile> findAll() {
        return myfileDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Myfile> findSearch(Map whereMap, int page, int size) {
        Specification<Myfile> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return myfileDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Myfile> findSearch(Map whereMap) {
        Specification<Myfile> specification = createSpecification(whereMap);
        return myfileDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Myfile findById(String id) {
        return myfileDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param myfile
     */
    public void add(Myfile myfile) {
        myfile.setId(idWorker.nextId() + "");
        myfileDao.save(myfile);
    }

    /**
     * 修改
     *
     * @param myfile
     */
    public void update(Myfile myfile) {
        myfileDao.save(myfile);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        myfileDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Myfile> createSpecification(Map searchMap) {

        return new Specification<Myfile>() {

            @Override
            public Predicate toPredicate(Root<Myfile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                //
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                //
                if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%" + (String) searchMap.get("name") + "%"));
                }
                //
                if (searchMap.get("md5") != null && !"".equals(searchMap.get("md5"))) {
                    predicateList.add(cb.like(root.get("md5").as(String.class), "%" + (String) searchMap.get("md5") + "%"));
                }
                //
                if (searchMap.get("url") != null && !"".equals(searchMap.get("url"))) {
                    predicateList.add(cb.like(root.get("url").as(String.class), "%" + (String) searchMap.get("url") + "%"));
                }
                //
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%" + (String) searchMap.get("state") + "%"));
                }

                //根据上传时间排序
                query.where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
                query.orderBy(cb.desc(root.get("createtime")));
                return query.getRestriction();
            }
        };

    }

}
