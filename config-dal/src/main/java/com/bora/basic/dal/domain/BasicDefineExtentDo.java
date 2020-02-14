package com.bora.basic.dal.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/***
 *Ticket: 
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-13 15:30:30
 *****/
@Data
public class BasicDefineExtentDo extends BasicDefineDo {
    @TableField(exist = false)
    @ApiModelProperty(value = "单选或多选扩展")
    protected List<ExtensionDo> extensionDoList;
}
