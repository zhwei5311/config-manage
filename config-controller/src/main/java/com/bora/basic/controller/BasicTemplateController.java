package com.bora.basic.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.BasicTemplateDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.bora.basic.service.service.IBasicTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/***
 *Ticket: 租户模板表
 * @author 赵威
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-06 09:52:03
 *****/
@RestController
@RequestMapping("/basic/template")
public class BasicTemplateController {

    @Autowired
    private IBasicTemplateService basicTemplateService;

    @Autowired
    private IBasicDefineService basicDefineService;

    /**
     * 分页查询
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public IPage<BasicTemplateDo> list(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        IPage<BasicTemplateDo> page = new Page<>(pageIndex, pageSize);
        IPage<BasicTemplateDo> templateDoIPage = basicTemplateService.page(page);
        return templateDoIPage;
    }

    /**
     * 添加数据
     *
     * @param templateList
     * @return
     */
    @PostMapping("/save")
    public boolean save(@RequestBody String templateList) {
        if (StringUtils.isEmpty(templateList)) {
            return false;
        }
        List<BasicTemplateDo> basicTemplateDos = JSONObject.parseObject(templateList, List.class);
        return basicTemplateService.saveBatch(basicTemplateDos);
    }

    /**
     * 根据id删除记录
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public boolean delete(@RequestParam("id") Long id) {
        return basicTemplateService.removeById(id);
    }

    /**
     * 编辑单条数据
     *
     * @param templateList
     * @return
     */
    @PostMapping("/edit")
    public boolean edit(@RequestBody String templateList) {
        if (StringUtils.isEmpty(templateList)) {
            return false;
        }
        BasicTemplateDo basicTemplateDo = JSONObject.parseObject(templateList, BasicTemplateDo.class);
        return basicTemplateService.updateById(basicTemplateDo);
    }

    @PostMapping("/saveToDefine")
    public boolean saveToDefine(@RequestParam("tenantId") Integer tenantId) {
        //查询条件为tenant
        QueryWrapper<BasicTemplateDo> wrapper = new QueryWrapper<>();
        wrapper.eq("tenantId",tenantId);
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
    }
}
