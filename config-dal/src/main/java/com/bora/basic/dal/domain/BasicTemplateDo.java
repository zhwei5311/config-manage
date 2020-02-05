package com.bora.basic.dal.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("t_basic_template")
@ApiModel(value="BasicTemplateDo对象", description="")
public class BasicTemplateDo implements Serializable {

private static final long serialVersionUID=1L;

        @ApiModelProperty(value = "主键：自增")
        private Integer id;

        @ApiModelProperty(value = "字段标识")
private String fieldKey;

        @ApiModelProperty(value = "字段名称")
private String fieldName;

        @ApiModelProperty(value = "字段类型")
private String fieldType;

        @ApiModelProperty(value = "字段状态，1：启用；2：禁用")
private Integer fieldStatus;

        @ApiModelProperty(value = "字段属性，1：标准；2：可选；3：自定义")
private Integer fieldAttr;

        @ApiModelProperty(value = "备注")
private String description;


        }