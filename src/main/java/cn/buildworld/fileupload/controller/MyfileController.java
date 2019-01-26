package cn.buildworld.fileupload.controller;

import cn.buildworld.fileupload.entity.Myfile;
import cn.buildworld.fileupload.entity.Result;
import cn.buildworld.fileupload.entity.StatusCode;
import cn.buildworld.fileupload.service.MyfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/myfile")
public class MyfileController {

	@Autowired
	private MyfileService myfileService;
	

	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",myfileService.findById(id));
	}

	/**
	 * 文件分页列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public Result findListByPage(@RequestParam int page, @RequestParam int limit,@RequestParam(required = false)String name){
		Map searchMap = new HashMap();
		searchMap.put("name",name);
		Page<Myfile> pageList = myfileService.findSearch(searchMap, page, limit);
		return  new Result(true,0,"查询成功",pageList.getTotalElements(),pageList.getContent());
	}

	/**
	 * 修改
	 * @param myfile
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Myfile myfile, @PathVariable String id ){
		myfile.setId(id);
		myfileService.update(myfile);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		myfileService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
