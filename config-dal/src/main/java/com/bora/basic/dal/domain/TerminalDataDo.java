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
 *
 * </p>
 *
 * @author zhwei
 * @since 2020-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_terminal_data")
@ApiModel(value = "TerminalDataDo对象", description = "")
public class TerminalDataDo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键：自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "终端编号")
    private String terminalCode;

    @ApiModelProperty(value = "终端名称")
    private String terminalName;

    @ApiModelProperty(value = "终端状态，1：启用；2：禁用；")
    private Integer terminalStatus;

    @ApiModelProperty(value = "绑定设备1")
    private String relateDevice1;

    @ApiModelProperty(value = "绑定设备2")
    private String relateDevice2;

    @ApiModelProperty(value = "绑定设备3")
    private String relateDevice3;

    @ApiModelProperty(value = "终端位置")
    private String location;

    @ApiModelProperty(value = "IP地址")
    private String terminalIp;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updater;

    @ApiModelProperty(value = "描述")
    private String description;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "自定义字段")
    private JSONObject extInfo;
}