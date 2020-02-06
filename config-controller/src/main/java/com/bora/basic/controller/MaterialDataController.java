package com.bora.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.MaterialDataDo;
import com.bora.basic.service.service.IMaterialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 添加新的物料记录
     * @param materialStr
     * @return
     */
    @PostMapping("/save")
    public boolean save(@RequestBody String materialStr){
        //为空直接返回，不做操作
        if(StringUtils.isEmpty(materialStr)){
            return false;
        }

        MaterialDataDo materialDataDo = JSONObject.parseObject(materialStr,MaterialDataDo.class);
        return iMaterialDataService.save(materialDataDo);
    }

    /**
     * 编辑物料记录
     * @param materialStr
     * @return
     */
    @PostMapping("/edit")
    public boolean edit(@RequestBody String materialStr){
        //为空直接返回，不做操作
        if(StringUtils.isEmpty(materialStr)){
            return false;
        }
        MaterialDataDo materialDataDo = JSONObject.parseObject(materialStr,MaterialDataDo.class);
        if(materialDataDo.getId() == null){
            return false;
        }
        return iMaterialDataService.updateById(materialDataDo);
    }

    @GetMapping("/remove")
    public boolean remove(@RequestParam("id") Long id){
        return iMaterialDataService.removeById(id);
    }

    /**
     * 分页查询物料代码  //todo 具体查询条件待定
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listByPage")
    public IPage<MaterialDataDo> listMaterialData(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        IPage<MaterialDataDo> page = new Page<>(pageIndex, pageSize);
        return iMaterialDataService.page(page);
    }

}
