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
 * @author wangjialei
 * @since 2020-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_material_data")
@ApiModel(value = "MaterialDataDo对象", description = "")
public class MaterialDataDo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键：自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "租户号")
    private Integer tenantId;

    @ApiModelProperty(value = "物料编码")
    private String comCode;

    @ApiModelProperty(value = "物料名称")
    private String comName;

    @ApiModelProperty(value = "物料规格")
    private String specify;

    @ApiModelProperty(value = "图号")
    private String pictureNo;

    @ApiModelProperty(value = "属性")
    private String attribute;

    @ApiModelProperty(value = "计量单位1")
    private String measureUnit1;

    @ApiModelProperty(value = "计量单位2")
    private String measureUnit2;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "客户")
    private String customer;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updater;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "自定义字段")
    private JSONObject extInfo;


}