package com.bora.basic.dal.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备数据记录表
 * </p>
 *
 * @author zhwei
 * @since 2020-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_device_data")
@ApiModel(value = "DeviceDataDo对象", description = "设备数据记录表")
public class DeviceDataDo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键：自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "租户号")
    private Integer tenantId;

    @ApiModelProperty(value = "设备编号")
    private String devCode;

    @ApiModelProperty(value = "设备名称")
    private String devName;

    @ApiModelProperty(value = "设备类型")
    private String devType;

    @ApiModelProperty(value = "设备状态；1：运行；2：待机；3：离线；4：告警")
    private Integer devStatus;

    @ApiModelProperty(value = "设备规格")
    private String devSpecify;

    @ApiModelProperty(value = "设备型号")
    private String devModel;

    @ApiModelProperty(value = "所属车间")
    private String workshop;

    @ApiModelProperty(value = "绑定工控机")
    private String terminal;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updator;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "自定义字段")
    private JSONObject extInfo;
}