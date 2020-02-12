package com.bora.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.BasicTemplateDo;
import com.bora.basic.dal.domain.ExtensionDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.bora.basic.service.service.IExtensionService;
import com.bora.commmon.domain.Result;
import com.bora.commmon.page.PageList;
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

    @Autowired
    private IExtensionService extensionService;


    /**
     * 根据功能标识查询系统字段
     * @param mark
     * @return
     */
    @GetMapping("/getDefineByMark")
    public Result getDefineByMark(String mark){
        List<BasicDefineDo> defineByMark = basicDefineService.getDefineByMark(mark);
        return Result.ok(defineByMark);
    }

    /**
     * 分页查看系统功能 //todo 查询条件待定
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listDefine")
    public Result<List<BasicDefineDo>> listDefine(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        IPage<BasicDefineDo> page = new Page<>(pageIndex,pageSize);
        IPage<BasicDefineDo> defineDoPage = basicDefineService.page(page);
        com.bora.commmon.page.Page page1 = new com.bora.commmon.page.Page(pageIndex, pageSize);
        page1.setTotal((int)defineDoPage.getTotal());
        return Result.ok(new PageList(page1,defineDoPage.getRecords()));
    }

    /**
     * 新增
     * @param tenantId
     * @return
     */
    @PostMapping("/saveDefine")
    public Result saveDefine(@RequestParam("tenantId") Integer tenantId,
                             @RequestParam("mark") String mark){
        //查询条件为tenant
        QueryWrapper<BasicTemplateDo> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id",tenantId);
        //根据tenant得到模板中的list数据
        Collection<BasicTemplateDo> basicTemplateList = basicTemplateService.list(wrapper);

        //如果为空没必要往下执行
        if(CollectionUtils.isEmpty(basicTemplateList)){
            return Result.error("您的操作有误！");
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
            //功能标识符
            basicDefine.setMark(mark);
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
        return Result.ok(basicDefineService.saveBatch(basicDefineList));
    }

    /**
     * 单个删除
     * @param id
     * @return
     */
    @PostMapping("/deleteDefine")
    public Result deleteDefine(@RequestParam("id") Long id){
        return Result.ok(basicDefineService.removeById(id));
    }

    /**
     * 单个编辑
     * @param defineList
     * @return
     */
    @PostMapping("/editDefine")
    public Result editDefine(@RequestBody String defineList){
        if(StringUtils.isEmpty(defineList)){
            return Result.error("您的操作有误！");
        }
        BasicDefineDo basicDefineDo = JSONObject.parseObject(defineList, BasicDefineDo.class);
        return Result.ok(basicDefineService.updateById(basicDefineDo));
    }


    /**
     * 根据租户和功能标识获取到对应的字段列表传递给前端
     * @param mark
     * @param tenantId
     * @return
     */
    @GetMapping("/getPropertiesByMarkAndTenantId")
    public Result getPropertiesByMarkAndTenantId(@RequestParam("mark") String mark,
                                                 @RequestParam("tenantId") Integer tenantId){
        //设置查询条件
        QueryWrapper<BasicDefineDo> queryWrapper = new QueryWrapper<>();
        //功能标识
        queryWrapper.eq("mark",mark);
        //租户
        queryWrapper.eq("tenant_id",tenantId);
        //只查询未被禁用的
        queryWrapper.eq("field_status",1);
        //根据定义的排序号进行排序
        queryWrapper.orderByAsc("order_no");
        //查询结果
        List<BasicDefineDo> list = basicDefineService.list(queryWrapper);

        //这里需要对查询结果进行处理，主要是处理单选和多选值

        for(BasicDefineDo basicDefineDo : list){
            if(basicDefineDo.getFieldType().equals("单选")){
                LambdaQueryWrapper<ExtensionDo> extensionDoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                extensionDoLambdaQueryWrapper.eq(ExtensionDo::getTenantId,tenantId);
                extensionDoLambdaQueryWrapper.eq(ExtensionDo::getFieldKey,basicDefineDo.getFieldKey());
                List<ExtensionDo> extensionDos = extensionService.list(extensionDoLambdaQueryWrapper);
                basicDefineDo.setExtensionDoList(extensionDos);
            }
        }
        return Result.ok(list);
    }

}
