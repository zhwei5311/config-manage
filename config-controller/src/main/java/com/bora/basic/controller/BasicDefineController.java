package com.bora.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.BasicTemplateDo;
import com.bora.basic.service.service.IBasicDefineService;
//import com.bora.commmon.domain.Result;
//import com.bora.commmon.page.PageList;
import com.bora.basic.service.service.IBasicTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
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

    @Autowired
    private IBasicTemplateService basicTemplateService;


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
        IPage<BasicDefineDo> defineDoPage = basicDefineService.page(page);
        return defineDoPage;
//        return Result.ok(new PageList(new com.bora.commmon.page.Page(pageIndex,pageSize),defineDoIPage.getRecords()));
    }

    /**
     * 新增
     * @param tenantId
     * @return
     */
    @PostMapping("/saveDefine")
    public boolean saveDefine(@RequestParam("tenantId") Integer tenantId){
        //查询条件为tenant
        QueryWrapper<BasicTemplateDo> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id",tenantId);
        //根据tenant得到模板中的list数据
        Collection<BasicTemplateDo> basicTemplateList = basicTemplateService.list(wrapper);

        //如果为空没必要往下执行
        if(CollectionUtils.isEmpty(basicTemplateList)){
            return false;
        }

        //定义空的Collection
        Collection<BasicDefineDo> basicDefineList = new ArrayList<>();
        //判断要遍历的对象是否为空，不为空才遍历
        //遍历模板的list
        for (BasicTemplateDo basicTemplate : basicTemplateList) {
            //创建个人模板对象
            BasicDefineDo basicDefine = new BasicDefineDo();
            //租户id
            basicDefine.setTenantId(tenantId);
            //参数可以直接写死
            basicDefine.setMark("物料");
            //字段
            basicDefine.setFieldKey(basicTemplate.getFieldKey());
            //字段中文名
            basicDefine.setFieldName(basicTemplate.getFieldName());
            //字段状态，1：启用；2：禁用
            basicDefine.setFieldStatus(basicTemplate.getFieldStatus());
            //字段类型
            basicDefine.setFieldType(basicTemplate.getFieldType());
            //字段属性，1：标准字段；2：可选字段
            basicDefine.setFieldAttr(basicTemplate.getFieldAttr());
            //根据字段属性判断是否显示字段，如果为1则置为显示，如果为2则不显示
            basicDefine.setIsShow(basicTemplate.getFieldAttr() == 1 ? 1 : 2);
            //将模板表t_template中的数据添加到t_define中
            basicDefineList.add(basicDefine);
        }
        //批量保存数据
        return basicDefineService.saveBatch(basicDefineList);
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
