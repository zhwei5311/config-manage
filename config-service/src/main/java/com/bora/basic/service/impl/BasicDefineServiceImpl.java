package com.bora.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bora.basic.dal.dao.BasicDefineMapper;
import com.bora.basic.dal.dao.ExtensionMapper;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.dal.domain.BasicDefineExtentDo;
import com.bora.basic.dal.domain.ExtensionDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bora.basic.service.service.IExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangjialei
 * @since 2020-02-05
 */
@Service
public class BasicDefineServiceImpl extends ServiceImpl<BasicDefineMapper, BasicDefineDo> implements IBasicDefineService {

    @Autowired
    private IExtensionService extensionService;

    @Override
    public List<BasicDefineDo> getDefineByMark(String mark) {
        LambdaQueryWrapper<BasicDefineDo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicDefineDo::getMark,mark);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String,List<BasicDefineExtentDo>> getPropertiesByMarkAndTenantId(Integer tenantId, String mark) {
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

        List<BasicDefineDo> list = this.list(queryWrapper);
        Map<String,List<BasicDefineExtentDo>> result = new HashMap<>(2);
        List<BasicDefineExtentDo> basicDefineExtentDos = new ArrayList<>();
        List<BasicDefineExtentDo> tenantDefineExtentDos = new ArrayList<>();

        //这里需要对查询结果进行处理，主要是处理单选和多选值
        for(BasicDefineDo basicDefineDo : list){
            BasicDefineExtentDo basicDefineExtentDo = new BasicDefineExtentDo();
            //ID
            basicDefineExtentDo.setId(basicDefineDo.getId());
            //租户
            basicDefineExtentDo.setTenantId(basicDefineDo.getTenantId());
            //功能标识
            basicDefineExtentDo.setMark(basicDefineDo.getMark());
            //字段标识
            basicDefineExtentDo.setFieldKey(basicDefineDo.getFieldKey());
            //字段名称
            basicDefineExtentDo.setFieldName(basicDefineDo.getFieldName());
            //字段状态
            basicDefineExtentDo.setFieldStatus(basicDefineDo.getFieldStatus());
            //字段类型
            basicDefineExtentDo.setFieldType(basicDefineDo.getFieldType());
            //数据类型
            basicDefineExtentDo.setDataType(basicDefineDo.getDataType());
            //字段属性
            basicDefineExtentDo.setFieldAttr(basicDefineDo.getFieldAttr());
            //是否显示
            basicDefineExtentDo.setIsShow(basicDefineDo.getIsShow());
            //字段排序号
            basicDefineExtentDo.setOrderNo(basicDefineDo.getOrderNo());
            //备注
            basicDefineExtentDo.setDescription(basicDefineDo.getDescription());
            if(basicDefineDo.getFieldType().equals("单选")){
                LambdaQueryWrapper<ExtensionDo> extensionDoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                extensionDoLambdaQueryWrapper.eq(ExtensionDo::getTenantId,tenantId);
                extensionDoLambdaQueryWrapper.eq(ExtensionDo::getFieldKey,basicDefineDo.getFieldKey());
                List<ExtensionDo> extensionDos = extensionService.list(extensionDoLambdaQueryWrapper);
                basicDefineExtentDo.setExtensionDoList(extensionDos);
            }
            if(basicDefineExtentDo.getFieldAttr() != null && basicDefineExtentDo.getFieldAttr().intValue() == 3){
                tenantDefineExtentDos.add(basicDefineExtentDo);
            }else {
                basicDefineExtentDos.add(basicDefineExtentDo);
            }
        }

        result.put("tenant",tenantDefineExtentDos);
        result.put("basic",basicDefineExtentDos);
        return result;
    }
}
