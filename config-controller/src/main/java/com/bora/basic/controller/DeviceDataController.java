package com.bora.basic.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.DeviceDataDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.bora.basic.service.service.IDeviceDataService;
import com.bora.commmon.domain.Result;
import com.bora.commmon.page.PageList;
import com.bora.common.reflect.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.*;
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

    public static final Map<String, String> COLUMN_KEY = new HashMap<>();

    static {
        COLUMN_KEY.put("id", "id");
        COLUMN_KEY.put("tenant_id", "tenantId");
        COLUMN_KEY.put("tenant_id", "tenantId");
        COLUMN_KEY.put("dev_code", "dev_code");
        COLUMN_KEY.put("dev_name", "dev_name");
        COLUMN_KEY.put("dev_type", "dev_type");
        COLUMN_KEY.put("dev_status", "dev_status");
        COLUMN_KEY.put("dev_specify", "dev_specify");
        COLUMN_KEY.put("dev_model", "dev_model");
        COLUMN_KEY.put("workshop", "workshop");
        COLUMN_KEY.put("terminal", "terminal");
        COLUMN_KEY.put("description", "description");
        COLUMN_KEY.put("create_time", "create_time");
        COLUMN_KEY.put("creator", "creator");
        COLUMN_KEY.put("update_time", "update_time");
        COLUMN_KEY.put("updator", "updator");
        COLUMN_KEY.put("extInfo", "extInfo");
    }

    /**
     * 分页查询设备数据
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listByPage")
    public Result listByPage(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        //添加查询条件：租户和是否显示
        Integer tenantId = 1;
        String mark = "设备";
        QueryWrapper<BasicDefineDo> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("is_show", 1);
        wrapper.eq("mark", mark);
        //查询租户模板表中需要显示的字段信息，包括字段名、字段中文名等信息
        List<BasicDefineDo> basicDefineList = basicDefineService.list(wrapper);
        //判空，如果为空，则返回“暂无数据”
        if (CollectionUtils.isEmpty(basicDefineList)) {
            return Result.error("暂无数据");
        }
        //根据basicDefineList获取的结果查询记录表中的数据
        //拿到属性名
        List<String> basicField = new ArrayList<>();
        List<Map<String, String>> fieldNameMap = new ArrayList<>();
        //为了方便拆分检索出来的扩展字段
        Map<String, String> keyToName = new HashMap<>(4);
        List<String> keyToNameOrder = new ArrayList<>();
        for (int i = 0; i < basicDefineList.size(); i++) {
            BasicDefineDo basicDefineDo = basicDefineList.get(i);
            Map<String, String> fieldName = new HashMap<>();
            fieldName.put(basicDefineDo.getFieldKey(), basicDefineDo.getFieldName());
            keyToNameOrder.add(basicDefineDo.getFieldKey());
            fieldNameMap.add(fieldName);
            if (basicDefineDo.getFieldAttr().intValue() == 3) {
                keyToName.put(basicDefineDo.getFieldKey(), basicDefineDo.getFieldName());
                continue;
            }
            basicField.add(basicDefineDo.getFieldKey());
        }
        //获取属性名数组
        String[] fields = new String[basicField.size() + 2];
        basicField.toArray(fields);
        //还需要检索扩展字段
        fields[fields.length - 2] = "ext_info";
        fields[fields.length - 1] = "id";
        //分页查询
        QueryWrapper<DeviceDataDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(fields);
        IPage<Map<String, Object>> page = new Page<>(pageIndex, pageSize);
        IPage<Map<String, Object>> deviceDataIPage = deviceDataService.pageMaps(page, queryWrapper);
        com.bora.commmon.page.Page page1 = new com.bora.commmon.page.Page(pageIndex, pageSize);
        page1.setTotal((int) deviceDataIPage.getTotal());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", fieldNameMap);
        //对检索结果进行处理
        //拆分扩展字段合并到系统、可选字段同一等级
        List<Map<String, Object>> records = deviceDataIPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            Map<String, Object> record = records.get(i);
            if (record.containsKey("ext_info")) {
                JSONObject extInfo = JSONObject.parseObject(record.get("ext_info").toString());
                Set<String> strings = extInfo.keySet();
                Iterator<String> iterator = strings.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    //判断是不是在展示的结果里，如果不在则不需要展示
                    if (keyToName.containsKey(next)) {
                        record.put(next, extInfo.get(next));
                    }
                }
                record.remove("ext_info");
            }
        }
        return Result.ok(new PageList(page1, records), jsonObject);
    }

    /**
     * 获取所有的设备名称
     */
    @GetMapping("/getDevName")
    public Result getDevName() {
        Integer tenantId = 1;
        QueryWrapper<DeviceDataDo> wrapper = new QueryWrapper<>();
        wrapper.select("dev_code", "dev_name");
        wrapper.eq("tenant_id", tenantId);
        return Result.ok(deviceDataService.list(wrapper));
    }

    /**
     * 根据id获取记录
     *
     * @Param id
     */
    @GetMapping("getDeviceById")
    public Result getDeviceById(@RequestParam("id") Long id) {
        Integer tenantId = 1;
        //获取字段，并根据对应字段返回
        LambdaQueryWrapper<BasicDefineDo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //功能标识
        lambdaQueryWrapper.eq(BasicDefineDo::getMark, "设备");
        //租户
        lambdaQueryWrapper.eq(BasicDefineDo::getTenantId, tenantId);
        //只查询未被禁用的
        lambdaQueryWrapper.eq(BasicDefineDo::getFieldStatus, 1);
        //根据定义的排序号进行排序
        lambdaQueryWrapper.orderByAsc(BasicDefineDo::getOrderNo);
        //查询结果
        List<BasicDefineDo> list = basicDefineService.list(lambdaQueryWrapper);
        List<String> collect = list.stream().map(BasicDefineDo::getFieldKey).collect(Collectors.toList());
        //获取属性名数组
        String[] fields = new String[collect.size()];
        collect.toArray(fields);
        QueryWrapper<DeviceDataDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(fields);
        queryWrapper.eq("id", id);
        Map<String, Object> map = deviceDataService.getMap(queryWrapper);
        if (CollectionUtils.isEmpty(map)) {
            return Result.error("暂无数据查询");
        }
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (BasicDefineDo basicDefineDo : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fieldKey", basicDefineDo.getFieldKey());
            jsonObject.put("tenantId", basicDefineDo.getTenantId());
            jsonObject.put("fieldName", basicDefineDo.getFieldName());
            jsonObject.put("fieldType", basicDefineDo.getFieldType());
            jsonObject.put("value", map.get(basicDefineDo.getFieldKey()));
            jsonObjectList.add(jsonObject);
        }
        return Result.ok(jsonObjectList);
    }

    /**
     * 新增设备数据
     *
     * @param params
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestParam Map<String, Object> params) {
        DeviceDataDo deviceDataDo = ReflectUtil.mapToObject(params, COLUMN_KEY, DeviceDataDo.class);
        if (null == deviceDataDo) {
            return Result.error("您的操作有误！");
        }
        int tenantId = 1;
        deviceDataDo.setTenantId(tenantId);
        return Result.ok(deviceDataService.save(deviceDataDo));
    }

    /**
     * 编辑设备数据
     *
     * @param params
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestParam Map<String, Object> params) {
        DeviceDataDo deviceDataDo = ReflectUtil.mapToObject(params, COLUMN_KEY, DeviceDataDo.class);
        if (null == deviceDataDo || null == deviceDataDo.getId()) {
            return Result.error("您的操作有误！");
        }
        return Result.ok(deviceDataService.updateById(deviceDataDo));
    }

    /**
     * 根据id删除设备数据
     *
     * @param id
     * @return
     */
    @GetMapping("/remove")
    public Result remove(@RequestParam("id") Long id) {
        return Result.ok(deviceDataService.removeById(id));
    }
}


