package com.bora.basic.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicTemplateDo;
import com.bora.basic.service.service.IBasicTemplateService;
import com.bora.commmon.domain.Result;
import com.bora.commmon.page.PageList;
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
    public Result<List<BasicTemplateDo>> list(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        IPage<BasicTemplateDo> page = new Page<>(pageIndex, pageSize);
        IPage<BasicTemplateDo> templateDoPage = basicTemplateService.page(page);
        com.bora.commmon.page.Page page1 = new com.bora.commmon.page.Page(pageIndex, pageSize);
        page1.setTotal((int)templateDoPage.getTotal());
        return Result.ok(new PageList(page1,templateDoPage.getRecords()));
    }

    /**
     * 添加数据
     *
     * @param templateList
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody String templateList) {
        if (StringUtils.isEmpty(templateList)) {
            return Result.error("您的操作有误！");
        }
        List<BasicTemplateDo> basicTemplateDos = JSONObject.parseObject(templateList, List.class);
        return Result.ok(basicTemplateService.saveBatch(basicTemplateDos));
    }

    /**
     * 根据id删除记录
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam("id") Long id) {
        return Result.ok(basicTemplateService.removeById(id));
    }

    /**
     * 编辑单条数据
     *
     * @param templateList
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody String templateList) {
        if (StringUtils.isEmpty(templateList)) {
            return Result.error("您的操作有误！");
        }
        BasicTemplateDo basicTemplateDo = JSONObject.parseObject(templateList, BasicTemplateDo.class);
        return Result.ok(basicTemplateService.updateById(basicTemplateDo));
    }


}
