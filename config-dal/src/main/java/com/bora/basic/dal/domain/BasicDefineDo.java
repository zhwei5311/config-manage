package com.bora.basic.dal.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

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
@TableName("t_basic_define")
@ApiModel(value = "BasicDefineDo对象", description = "")
public class BasicDefineDo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键：自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "功能标识，物料、终端等")
    private String mark;

    @ApiModelProperty(value = "字段标识")
    private String fieldKey;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段状态，1：启用；2：禁用")
    private Integer fieldStatus;

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "字段属性，1：系统；2：可选")
    private Integer fieldAttr;

    @ApiModelProperty(value = "是否显示，1：显示；2：不显示")
    private Integer isShow;

    @ApiModelProperty(value = "字段排序号")
    private Integer orderNo;

    @ApiModelProperty(value = "备注")
    private String description;



}