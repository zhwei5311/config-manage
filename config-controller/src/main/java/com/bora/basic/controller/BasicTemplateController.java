package com.bora.basic.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicTemplateDo;
import com.bora.basic.service.service.IBasicTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *Ticket: 租户模板表
 * @author 赵威
 * @email zhaowei@boranet.com.cn
 * @Date: 2020-02-06 09:52:03
 */
@RestController
@RequestMapping("/basic/template")
public class BasicTemplateController {

    @Autowired
    private IBasicTemplateService basicTemplateService;


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
        IPage<BasicTemplateDo> templateDoPage = basicTemplateService.page(page);
        return templateDoPage;
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


}
