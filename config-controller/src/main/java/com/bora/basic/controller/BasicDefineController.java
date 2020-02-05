package com.bora.basic.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.service.service.IBasicDefineService;
//import com.bora.commmon.domain.Result;
//import com.bora.commmon.page.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 *Ticket: 系统提供的基本配置字段
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-05 15:04:38
 *****/
@RestController
@RequestMapping("/basic/define")
public class BasicDefineController {

    @Autowired
    private IBasicDefineService basicDefineService;


    /**
     * 根据功能标识查询系统字段
     * @param mark
     * @return
     */
    @GetMapping("/getDefineByMark")
    public List<BasicDefineDo> getDefineByMark(String mark){
        List<BasicDefineDo> defineByMark = basicDefineService.getDefineByMark(mark);
        return defineByMark;
//        return Result.ok(defineByMark);
    }

    /**
     * 分页查看系统功能 //todo 查询条件待定
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listDefine")
    public IPage<BasicDefineDo> listDefine(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        IPage<BasicDefineDo> page = new Page<>(pageIndex,pageSize);
        IPage<BasicDefineDo> defineDoIPage = basicDefineService.page(page);
        return defineDoIPage;
//        return Result.ok(new PageList(new com.bora.commmon.page.Page(pageIndex,pageSize),defineDoIPage.getRecords()));
    }

    /**
     * 新增
     * @param defineList
     * @return
     */
    @PostMapping("/saveDefine")
    public boolean saveDefine(@RequestBody String defineList){
        if(StringUtils.isEmpty(defineList)){
//            return Result.error("");
            return false;
        }
        List<BasicDefineDo> basicDefineDos = JSONObject.parseObject(defineList, List.class);
        return basicDefineService.saveBatch(basicDefineDos);
//        return Result.ok(basicDefineService.saveBatch(basicDefineDos));
    }

    /**
     * 单个删除
     * @param id
     * @return
     */
    @PostMapping("/deleteDefine")
    public boolean deleteDefine(@RequestParam("id") Long id){
        return basicDefineService.removeById(id);
//        return Result.ok(basicDefineService.removeById(id));
    }

    /**
     * 单个编辑
     * @param defineList
     * @return
     */
    @PostMapping("/editDefine")
    public boolean editDefine(@RequestBody String defineList){
        if(StringUtils.isEmpty(defineList)){
//            return Result.error("");
            return false;
        }
        BasicDefineDo basicDefineDo = JSONObject.parseObject(defineList, BasicDefineDo.class);
        return basicDefineService.updateById(basicDefineDo);
//        return Result.ok(basicDefineService.updateById(basicDefineDo));
    }


}
