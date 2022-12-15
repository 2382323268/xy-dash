package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: xiangwei
 * @Date: 2022/4/13 14:12
 * @Description 公共Entity
 **/
@Data
public class BaseEntity implements Serializable {
    /**
     * 创建时间
     */
    @TableField(value = "created_time",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private LocalDateTime modifiedTime;

    /**
     * 创建员 ID
     */
    @TableField(value = "creater_id",fill = FieldFill.INSERT)
    @ApiModelProperty("创建员 ID")
    private Long createrId;

    /**
     * 创建员
     */
    @TableField(value = "creater",fill = FieldFill.INSERT)
    @ApiModelProperty("创建员")
    private String creater;

    /**
     * 修改员 ID
     */
    @TableField(value = "modifier_id",fill = FieldFill.UPDATE)
    @ApiModelProperty("修改员 ID")
    private Long modifierId;

    /**
     * 修改员
     */
    @TableField(value = "modifier",fill = FieldFill.UPDATE)
    @ApiModelProperty("修改员")
    private String modifier;

    /**
     * 是否删除 （0/否 1/是）
     */
    @TableLogic
    @ApiModelProperty("是否已删除")
    private Boolean deleted;
}
