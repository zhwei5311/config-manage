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

import java.util.*;

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
     * 分页查看系统功能 //todo 查询条件待定
     * @param pageIndex
     * @param pageSize
     * @param mark 功能标识
     * @return
     */
    @GetMapping("/listDefine")
    public Result<List<BasicDefineDo>> listDefine(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                  @RequestParam(value = "mark") String mark){
        LambdaQueryWrapper<BasicDefineDo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicDefineDo::getMark,mark);
        IPage<BasicDefineDo> page = new Page<>(pageIndex,pageSize);
        IPage<BasicDefineDo> defineDoPage = basicDefineService.page(page,queryWrapper);
        com.bora.commmon.page.Page page1 = new com.bora.commmon.page.Page(pageIndex, pageSize);
        page1.setTotal((int)defineDoPage.getTotal());
        return Result.ok(new PageList(page1,defineDoPage.getRecords()));
    }

    @GetMapping("/listDefineNoPage")
    public Result<Map<String,Object>> listDefine(@RequestParam(value = "mark") String mark){
        Integer tenantId = 1;
        LambdaQueryWrapper<BasicDefineDo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicDefineDo::getMark,mark);
        queryWrapper.eq(BasicDefineDo::getFieldAttr,1);
        queryWrapper.eq(BasicDefineDo::getTenantId,tenantId);
        Map<String,Object> map = new HashMap<>(3);

        //选择系统字段
        List<BasicDefineDo> systemBasic = basicDefineService.list(queryWrapper);
        map.put("system",systemBasic);

        //选择可选字段
        LambdaQueryWrapper<BasicDefineDo> selectWrapper = new LambdaQueryWrapper<>();
        selectWrapper.eq(BasicDefineDo::getMark,mark);
        selectWrapper.eq(BasicDefineDo::getFieldAttr,2);
        selectWrapper.eq(BasicDefineDo::getTenantId,tenantId);
        List<BasicDefineDo> selectBasic = basicDefineService.list(selectWrapper);
        map.put("select",selectBasic);

        //租户自定义字段
        LambdaQueryWrapper<BasicDefineDo> tenantWrapper = new LambdaQueryWrapper<>();
        tenantWrapper.eq(BasicDefineDo::getMark,mark);
        tenantWrapper.eq(BasicDefineDo::getFieldAttr,3);
        tenantWrapper.eq(BasicDefineDo::getTenantId,tenantId);

        List<BasicDefineDo> tenantBasic = basicDefineService.list(tenantWrapper);
        map.put("tenant",tenantBasic);

        return Result.ok(map);
    }

    /**
     * 新增系统字段、可选字段
     * 供创建租户时调用
     * @param mark
     * @return
     */
    @PostMapping("/saveDefine")
    public Result saveDefine(@RequestParam("mark") String mark){
        //查询条件为tenant
        QueryWrapper<BasicTemplateDo> wrapper = new QueryWrapper<>();
        int tenantId = 1;
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

    @PostMapping("/saveTenantConfig")
    public Result saveDefine(BasicDefineDo basicDefineDo){
        Integer tenantId = 1;
        if(basicDefineDo == null){
            return Result.error("新增字段不能为空！");
        }
        if(StringUtils.isEmpty(basicDefineDo.getMark())){
            return Result.error("未能识别功能标识符!");
        }
        if(basicDefineDo.getFieldAttr() == null || basicDefineDo.getFieldAttr().intValue() != 3){
            return Result.error("只能新增自定义字段！");
        }

        basicDefineDo.setTenantId(tenantId);
        return Result.ok(basicDefineService.save(basicDefineDo));
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
     * @param basicDefineDo
     * @return
     */
    @PostMapping("/editDefine")
    public Result editDefine(BasicDefineDo basicDefineDo){
        if(basicDefineDo == null){
            return Result.error("修改字段不能为空");
        }
        return Result.ok(basicDefineService.updateById(basicDefineDo));
    }

    @GetMapping("/getDefineById")
    public Result getDefineById(@RequestParam("id") Long id){
        return Result.ok(basicDefineService.getById(id));
    }


    /**
     * 根据租户和功能标识获取到对应的字段列表传递给前端
     * @param mark
     * @return
     */
    @GetMapping("/getPropertiesByMarkAndTenantId")
        public Result getPropertiesByMarkAndTenantId(@RequestParam("mark") String mark){
        int tenantId = 1;
        return Result.ok(basicDefineService.getPropertiesByMarkAndTenantId(tenantId,mark));
    }


    /**
     * 查询租户下所有显示和不显示的字段接口
     */
    @GetMapping("/getShowAndNotShowProperties")
    public Result<JSONObject> getShowAndNotShowProperties(@RequestParam("mark") String mark){
        if(StringUtils.isEmpty(mark)){
            return Result.error("输入的功能标识符不能为空");
        }
        int tenantId = 1;
        LambdaQueryWrapper<BasicDefineDo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BasicDefineDo::getTenantId,tenantId);
        lambdaQueryWrapper.eq(BasicDefineDo::getFieldStatus,1);
        lambdaQueryWrapper.eq(BasicDefineDo::getMark,mark);

        List<BasicDefineDo> basicDefineDos = basicDefineService.list(lambdaQueryWrapper);
        JSONObject jsonObject = new JSONObject();
        List<BasicDefineDo> showList = new ArrayList<>();
        List<BasicDefineDo> hiddenList = new ArrayList<>();
        for(BasicDefineDo basicDefineDo : basicDefineDos){
            if(basicDefineDo.getIsShow() != null && basicDefineDo.getIsShow().intValue() == 1){
                showList.add(basicDefineDo);
                continue;
            }
            hiddenList.add(basicDefineDo);
        }
        jsonObject.put("showList",showList);
        jsonObject.put("hiddenList",hiddenList);
        return Result.ok(jsonObject);
    }

    /**
     * 根据字段名称模糊查询
     * @param fieldName
     * @return
     */
    @GetMapping("/getPropertyByName")
    public Result<List<BasicDefineDo>> getPropertyByName(@RequestParam("fieldName") String fieldName,
                                                         @RequestParam("mark")String mark){
        int tenantId = 1;
        LambdaQueryWrapper<BasicDefineDo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(BasicDefineDo::getFieldStatus,1);
        queryWrapper.eq(BasicDefineDo::getTenantId,tenantId);
        queryWrapper.likeRight(BasicDefineDo::getFieldName,fieldName);
        queryWrapper.eq(BasicDefineDo::getMark,mark);

        return Result.ok(basicDefineService.list(queryWrapper));
    }

    @PostMapping("/updateShowProperties")
    public Result updateShowProperties(@RequestParam("showList") String showStr,@RequestParam("hiddenList") String hiddenStr){
//        LambdaQueryWrapper
        List<BasicDefineDo> showList = JSONObject.parseObject(showStr,List.class);
        List<BasicDefineDo> hiddenList = JSONObject.parseObject(hiddenStr,List.class);
        List<BasicDefineDo> basicDefineDos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(showList)){
            basicDefineDos.addAll(showList);
        }

        if(!CollectionUtils.isEmpty(hiddenList)){
            basicDefineDos.addAll(hiddenList);
        }
        if(CollectionUtils.isEmpty(basicDefineDos)){
            return Result.error("没有需要更新的数据");
        }
//        for()
//        basicDefineService.update
        return Result.ok(basicDefineService.updateBatchById(basicDefineDos));
    }

}
