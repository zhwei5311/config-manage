package com.bora.basic.dal.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
 * @author chengyankai
 * @since 2020-02-17
 */
@Data
    @EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_work_data")
@ApiModel(value="WorkDataDo对象", description="")
public class WorkDataDo implements Serializable {

private static final long serialVersionUID=1L;

        @ApiModelProperty(value = "主键：自增")
            @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        @ApiModelProperty(value = "租户号")
        private Integer tenantId;

        @ApiModelProperty(value = "工单编码")
        private String workCode;

        @ApiModelProperty(value = "物料名称")
        private String comName;

        @ApiModelProperty(value = "设备名称")
        private String deviceName;

        @ApiModelProperty(value = "计划数量")
        private Integer planCount;

        @ApiModelProperty(value = "完成数量")
        private Integer finishCount;

        @ApiModelProperty(value = "工单状态")
        private Integer workStatus;

        @ApiModelProperty(value = "设备编码")
        private String deviceCode;

        @ApiModelProperty(value = "物料编码")
        private String comCode;

        @ApiModelProperty(value = "不合格数量")
        private Integer failedCount;

        @ApiModelProperty(value = "开启时间")
        private Date openTime;

        @ApiModelProperty(value = "完结时间")
        private Date finishTime;

        @ApiModelProperty(value = "创建时间")
        private Date createTime;

        @ApiModelProperty(value = "创建人")
        private String creator;

        @ApiModelProperty(value = "更新时间")
        private Date updateTime;

        @ApiModelProperty(value = "更新人")
        private String updater;

        @ApiModelProperty(value = "自定义字段")
        private String extInfo;

        @ApiModelProperty(value = "执行时间")
        private Date executeTime;

        @ApiModelProperty(value = "工单进度")
        private String workSchedule;


        }