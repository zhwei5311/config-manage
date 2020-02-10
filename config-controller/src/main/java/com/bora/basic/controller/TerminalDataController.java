package com.bora.basic.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.TerminalDataDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.bora.basic.service.service.ITerminalDataService;
import com.bora.commmon.domain.Result;
import com.bora.commmon.page.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Ticket: TerminalDataController
 *
 * @author zhwei
 * @email zhaowei@boranet.com.cn
 * @Date: 2020/2/7 17:35
 */
@RestController
@RequestMapping("/basic/terminal")
public class TerminalDataController {

    @Autowired
    private ITerminalDataService terminalDataService;

    @Autowired
    private IBasicDefineService basicDefineService;

    /**
     * 分页查询终端数据
     * @param pageIndex
     * @param pageSize
     * @param tenantId
     * @return
     */
    @GetMapping("/listByPage")
    public Result listByPage(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
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
        QueryWrapper<TerminalDataDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(fields);
        IPage<TerminalDataDo> page = new Page<>(pageIndex, pageSize);
        IPage<TerminalDataDo> terminalDataDoIPage = terminalDataService.page(page, queryWrapper);
        return Result.ok(new PageList(new com.bora.commmon.page.Page(pageIndex,pageSize),terminalDataDoIPage.getRecords()));
    }

    /**
     * 新增终端数据
     * @param terminalStr
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody String terminalStr) {
        if(StringUtils.isEmpty(terminalStr)) {
            return Result.error("您的操作有误！");
        }
        TerminalDataDo terminalDataDo = JSONObject.parseObject(terminalStr, TerminalDataDo.class);
        return Result.ok(terminalDataService.save(terminalDataDo));
    }

    /**
     * 编辑终端数据
     * @param terminalStr
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody String terminalStr) {
        if(StringUtils.isEmpty(terminalStr)) {
            return Result.error("您的操作有误！");
        }
        TerminalDataDo terminalDataDo = JSONObject.parseObject(terminalStr, TerminalDataDo.class);
        if(null == terminalDataDo.getId()) {
            return Result.error("您的操作有误！");
        }
        return Result.ok(terminalDataService.updateById(terminalDataDo));
    }

    /**
     * 根据id删除终端数据
     * @param id
     * @return
     */
    @GetMapping("/remove")
    public Result remove(@RequestParam("id") Long id) {
        return Result.ok(terminalDataService.removeById(id));
    }
}
