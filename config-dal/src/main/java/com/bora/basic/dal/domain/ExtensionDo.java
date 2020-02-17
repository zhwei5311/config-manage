package com.bora.basic.dal.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author zhwei
 * @since 2020-02-14
 */
@Data
    @EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_extension")
@ApiModel(value="ExtensionDo对象", description="")
public class ExtensionDo implements Serializable {

private static final long serialVersionUID=1L;

        @ApiModelProperty(value = "主键：自增")
            @TableId(value = "id", type = IdType.AUTO)
    private Long id;

        @ApiModelProperty(value = "租户id")
private Integer tenantId;

        @ApiModelProperty(value = "关联字段")
private String fieldKey;

        @ApiModelProperty(value = "字段类型；如单选、多选等")
private String fieldType;

        @ApiModelProperty(value = "字段值：如male/female等")
private String fieldValue;

        @ApiModelProperty(value = "字段中文：如男、女等")
private String fieldName;

        @ApiModelProperty(value = "描述")
private String description;


        }