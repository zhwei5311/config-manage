package com.bora.basic.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.BasicDefineExtentDo;
import com.bora.basic.dal.domain.ProductionDataDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.bora.basic.service.service.IProductionDataService;
import com.bora.commmon.domain.Result;
import com.bora.commmon.page.PageList;
import com.bora.common.reflect.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 生产报工记录表 前端控制器
 * </p>
 *
 * @author chengyankai
 * @since 2020-02-18
 */
@RestController
@RequestMapping("/work/report")
public class ProductionDataController {
    @Autowired
    private IProductionDataService iProductionDataService;

    @Autowired
    private IBasicDefineService basicDefineService;

    public static final Map<String,String> COLUMN_KEY = new HashMap<>();

    static{
        COLUMN_KEY.put("id","id");
        COLUMN_KEY.put("tenant_id","tenantId");
        COLUMN_KEY.put("work_code","workCode");
        COLUMN_KEY.put("com_name","comName");
        COLUMN_KEY.put("device_name","deviceName");
        COLUMN_KEY.put("plan_count","planCount");
        COLUMN_KEY.put("finish_count","finishCount");
        COLUMN_KEY.put("execute_time","executeTime");
        COLUMN_KEY.put("work_schedule","workSchedule");
        COLUMN_KEY.put("device_code","deviceCode");
        COLUMN_KEY.put("com_code","comCode");
        COLUMN_KEY.put("failed_count","failedCount");
        COLUMN_KEY.put("remark","remark");
        COLUMN_KEY.put("create_time","createTime");
        COLUMN_KEY.put("creator","creator");
        COLUMN_KEY.put("update_time","updateTime");
        COLUMN_KEY.put("updater","updater");
        COLUMN_KEY.put("extInfo","extInfo");
    }

    /**
     * 添加新的报工记录
     * @param params
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestParam Map<String,Object> params){
        ProductionDataDo productionDataDo = ReflectUtil.mapToObject(params, COLUMN_KEY, ProductionDataDo.class);
        if(productionDataDo == null){
            return Result.error("您的操作有误！");
        }
        //其他限制字段待定
        int tenantId = 1;
        productionDataDo.setTenantId(tenantId);
        return Result.ok(iProductionDataService.save(productionDataDo));
    }

    /**
     * 编辑单条报工记录
     * @param params
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestParam Map<String,Object> params){
        //为空直接返回，不做操作
        ProductionDataDo productionDataDo = ReflectUtil.mapToObject(params, COLUMN_KEY, ProductionDataDo.class);
        if(productionDataDo == null || productionDataDo.getId() == null){
            return Result.error("您的操作有误！");
        }
        //其他限制字段待定
        return Result.ok(iProductionDataService.updateById(productionDataDo));
    }

    /**
     * 删除单条报工记录
     * @param id
     * @return
     */
    @GetMapping("/remove")
    public Result remove(@RequestParam("id") Long id){
        return Result.ok(iProductionDataService.removeById(id));
    }

    /**
     * 查询报工记录
     * @param id
     * @return
     */
    @GetMapping("/getMaterialById")
    public Result getMaterialById(@RequestParam("id") Long id){
        ProductionDataDo productionDataDo = iProductionDataService.getById(id);
        if(productionDataDo == null){
            return Result.ok("暂无数据");
        }
        int tenantId = 1;
        Map<String, List<BasicDefineExtentDo>> markAndTenantId = basicDefineService.getPropertiesByMarkAndTenantId(tenantId, "物料");
        if(CollectionUtils.isEmpty(markAndTenantId)){
            return Result.ok("暂无数据");
        }
        Map<String, Object> objectMap = ReflectUtil.objectToMap(productionDataDo);
        String extInfo = (String) objectMap.get("extInfo");
        if(extInfo != null){
            Map<String,Object> mapInfo = JSONObject.parseObject(extInfo,Map.class);
            objectMap.putAll(mapInfo);
        }
        List<JSONObject> list = new ArrayList<>();
        Set<String> strings = markAndTenantId.keySet();
        for(String key : strings){
            List<BasicDefineExtentDo> basicDefineExtentDos = markAndTenantId.get(key);
            for(BasicDefineExtentDo basicDefineExtentDo : basicDefineExtentDos){
                String fieldKey = basicDefineExtentDo.getFieldKey();
                String column = COLUMN_KEY.get(fieldKey);
                if(objectMap.containsKey(column) || objectMap.containsKey(fieldKey)){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fieldKey",fieldKey);
                    jsonObject.put("fieldName",basicDefineExtentDo.getFieldName());
                    jsonObject.put("value",objectMap.get(column));
                    jsonObject.put("extensionDoList",basicDefineExtentDo.getExtensionDoList());
                    jsonObject.put("fieldType",basicDefineExtentDo.getFieldType());
                    list.add(jsonObject);
                }
            }
        }
        return Result.ok(list);
    }

    /**
     * 分页查询报工记录 //todo 具体查询条件待定
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listByPage")
    public Result listMaterialData(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        //添加查询条件：租户和是否显示
        Integer tenantId = 1;
        QueryWrapper<BasicDefineDo> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id",tenantId);
        wrapper.eq("is_show",1);
        wrapper.eq("mark","报工");
        wrapper.orderByAsc("order_no");
        wrapper.eq("field_status",1);
        //查询租户模板表中需要显示的字段信息，包括字段名、字段中文名等信息
        List<BasicDefineDo> basicDefineList = basicDefineService.list(wrapper);
        //判空，如果为空return null
        if(CollectionUtils.isEmpty(basicDefineList)) {
            return Result.error("暂无数据");
        }
        //根据basicDefineList获取的结果查询记录表中的数据
        //拿到属性名
        List<String> basicField = new ArrayList<>();
        List<Map<String,String>> fieldNameMap = new ArrayList<>();
        //为了方便拆分检索出来的扩展字段
        Map<String,String> keyToName = new HashMap<>(4);
        List<String>  keyToNameOrder = new ArrayList<>();
        for(int i = 0; i < basicDefineList.size(); i++){
            BasicDefineDo basicDefineDo = basicDefineList.get(i);
            Map<String,String>  fieldName = new HashMap<>(1);
            fieldName.put(basicDefineDo.getFieldKey(),basicDefineDo.getFieldName());
            keyToNameOrder.add(basicDefineDo.getFieldKey());
            fieldNameMap.add(fieldName);
            if(basicDefineDo.getFieldAttr().intValue() == 3){
                //自定义字段
                keyToName.put(basicDefineDo.getFieldKey(),basicDefineDo.getFieldName());
                continue;
            }
            basicField.add(basicDefineDo.getFieldKey());
        }

        //获取属性名数组
        String[] fields = new String[basicField.size()+2];
        basicField.toArray(fields);
        //还需要检索扩展字段
        fields[fields.length -2] = "ext_info";
        fields[fields.length -1] = "id";
        //分页查询
        QueryWrapper<ProductionDataDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(fields);
        IPage<Map<String, Object>> page = new Page<>(pageIndex, pageSize);
        IPage<Map<String, Object>> productionDataDoIPage = iProductionDataService.pageMaps(page, queryWrapper);

        com.bora.commmon.page.Page page1 = new com.bora.commmon.page.Page(pageIndex, pageSize);
        page1.setTotal((int)productionDataDoIPage.getTotal());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title",fieldNameMap);
        //对检索结果进行处理
        //拆分扩展字段合并到系统、可选字段同一等级
        List<Map<String, Object>> records = productionDataDoIPage.getRecords();
        for(int i = 0 ;i < records.size(); i++){
            Map<String, Object> record = records.get(i);
            if(record.containsKey("ext_info")){
                JSONObject extInfo = JSONObject.parseObject(record.get("ext_info").toString());
                Set<String> strings = extInfo.keySet();
                Iterator<String> iterator = strings.iterator();
                while(iterator.hasNext()){
                    String next = iterator.next();
                    //判断是不是在展示的结果里，如果不在则不需要展示
                    if(keyToName.containsKey(next)){
                        record.put(next,extInfo.get(next));
                    }
                }
                record.remove("ext_info");
            }

        }
        return Result.ok(new PageList(page1,records),jsonObject);
    }


}

