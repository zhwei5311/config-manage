package com.bora.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.MaterialDataDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.bora.basic.service.service.IMaterialDataService;
import com.bora.commmon.domain.Result;
import com.bora.commmon.page.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/***
 *Ticket: 物料记录表
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-06 09:52:03
 *****/
@RestController
@RequestMapping("/basic/material")
public class MaterialDataController {

    @Autowired
    private IMaterialDataService iMaterialDataService;

    @Autowired
    private IBasicDefineService basicDefineService;

    /**
     * 添加新的物料记录
     * @param materialStr
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody String materialStr){
        //为空直接返回，不做操作
        if(StringUtils.isEmpty(materialStr)){
            return Result.error("您的操作有误！");
        }

        MaterialDataDo materialDataDo = JSONObject.parseObject(materialStr,MaterialDataDo.class);
        return Result.ok(iMaterialDataService.save(materialDataDo));
    }

    /**
     * 编辑单条物料记录
     * @param materialStr
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody String materialStr){
        //为空直接返回，不做操作
        if(StringUtils.isEmpty(materialStr)){
            return Result.error("您的操作有误！");
        }
        MaterialDataDo materialDataDo = JSONObject.parseObject(materialStr,MaterialDataDo.class);
        if(materialDataDo.getId() == null){
            return Result.error("您的操作有误！");
        }
        return Result.ok(iMaterialDataService.updateById(materialDataDo));
    }

    /**
     * 删除单条物料记录
     * @param id
     * @return
     */
    @GetMapping("/remove")
    public Result remove(@RequestParam("id") Long id){
        return Result.ok(iMaterialDataService.removeById(id));
    }

    /**
     * 分页查询物料代码  //todo 具体查询条件待定
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listByPage")
    public Result listMaterialData(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "tenantId",required = false, defaultValue = "null") Integer tenantId) {
        //添加查询条件：租户和是否显示
        QueryWrapper<BasicDefineDo> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id",tenantId);
        wrapper.eq("is_show",1);
        //查询租户模板表中需要显示的字段信息，包括字段名、字段中文名等信息
        List<BasicDefineDo> basicDefineList = basicDefineService.list(wrapper);
        //判空，如果为空return null
        if(CollectionUtils.isEmpty(basicDefineList)) {
            return null;
        }
        //根据basicDefineList获取的结果查询记录表中的数据
        //拿到属性名
        List<String> collect = basicDefineList.stream().map(BasicDefineDo::getFieldKey).collect(Collectors.toList());
        //获取字段中文名
        List<String> fieldName = basicDefineList.stream().map(BasicDefineDo::getFieldName).collect(Collectors.toList());
        //获取属性名数组
        String[] fields = new String[collect.size()];
        collect.toArray(fields);
        QueryWrapper<MaterialDataDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(fields);
        IPage<MaterialDataDo> page = new Page<>(pageIndex, pageSize);
        IPage<MaterialDataDo> materialDataDoIPage = iMaterialDataService.page(page, queryWrapper);
        return Result.ok(new PageList(new com.bora.commmon.page.Page(pageIndex,pageSize),materialDataDoIPage.getRecords()));
    }

    /**
     * 获取所有的物料名称
     * @param tenantId
     * @return
     */
    @GetMapping("/getMaterialName")
    public Result getMaterialName(@RequestBody Integer tenantId) {
        QueryWrapper<MaterialDataDo> wrapper = new QueryWrapper<>();
        wrapper.select("com_code","com_name");
        wrapper.eq("tenant_id",tenantId);
        return Result.ok(iMaterialDataService.list(wrapper));
    }

}
