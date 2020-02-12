package com.bora.basic.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.DeviceDataDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.bora.basic.service.service.IDeviceDataService;
import com.bora.commmon.domain.Result;
import com.bora.commmon.page.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 设备数据记录表 前端控制器
 * </p>
 *
 * @author zhwei
 * @since 2020-02-11
 */
@RestController
@RequestMapping("/basic/device")
public class DeviceDataController {
    @Autowired
    private IDeviceDataService deviceDataService;

    @Autowired
    private IBasicDefineService basicDefineService;

    /**
     * 分页查询设备数据
     * @param pageIndex
     * @param pageSize
     * @param tenantId
     * @param mark
     * @return
     */
    @GetMapping("/listByPage")
    public Result listByPage(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "tenantId", required = false, defaultValue = "null") Integer tenantId,
                             @RequestParam(value = "mark", required = false, defaultValue = "null") String mark) {
        //添加查询条件：租户和是否显示
        QueryWrapper<BasicDefineDo> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("is_show", 1);
        wrapper.eq("mark", mark);
        //查询租户模板表中需要显示的字段信息，包括字段名、字段中文名等信息
        List<BasicDefineDo> basicDefineList = basicDefineService.list(wrapper);
        if (CollectionUtils.isEmpty(basicDefineList)) {
            return  Result.error("暂无数据");
        }
        //根据basicDefineList获取的结果查询记录表中的数据
        //拿到属性名
        List<String> collect = basicDefineList.stream().map(BasicDefineDo::getFieldKey).collect(Collectors.toList());
        //获取字段中文名
        List<String> fieldName = basicDefineList.stream().map(BasicDefineDo::getFieldName).collect(Collectors.toList());
        //获取属性名数组
        String[] fields = new String[collect.size()];
        collect.toArray(fields);
        QueryWrapper<DeviceDataDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(fields);
        IPage<DeviceDataDo> page = new Page<>(pageIndex, pageSize);
        IPage<DeviceDataDo> deviceDataIPage = deviceDataService.page(page, queryWrapper);
        return Result.ok(new PageList(new com.bora.commmon.page.Page(pageIndex, pageSize), deviceDataIPage.getRecords()));
    }

    /**
     * 新增设备数据
     * @param deviceString
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody String deviceString) {
        if(StringUtils.isEmpty(deviceString)) {
            return Result.error("您的操作有误！");
        }
        DeviceDataDo deviceDataDo = JSONObject.parseObject(deviceString,DeviceDataDo.class);
        return Result.ok(deviceDataService.save(deviceDataDo));
    }

    /**
     * 编辑设备数据
     * @param deviceString
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody String deviceString) {
        if(StringUtils.isEmpty(deviceString)) {
            return Result.error("您的操作有误！");
        }
        DeviceDataDo deviceDataDo = JSONObject.parseObject(deviceString,DeviceDataDo.class);
        if(null == deviceDataDo.getId()) {
            return Result.error("您的操作有误！");
        }
        return Result.ok(deviceDataService.updateById(deviceDataDo));
    }

    /**
     * 根据id删除设备数据
     * @param id
     * @return
     */
    @GetMapping("/remove")
    public Result remove(@RequestParam("id") Long id) {
        return Result.ok(deviceDataService.removeById(id));
    }
}


