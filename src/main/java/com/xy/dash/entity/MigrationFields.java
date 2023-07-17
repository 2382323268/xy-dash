package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName(value = "xy_migration_fields", autoResultMap = true)
@ToString(callSuper = true)
public class MigrationFields extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字段名称
     */
    @NotBlank(message = "字段名称不能为空")
    private String fieldName;

    /**
     * 表id
     */
    private Long tableId;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 字段类型
     */
    @NotBlank(message = "字段名称不能为空")
    private String fieldType;

    /**
     * 属性类型
     */
    private String propertyType;

    /**
     * 包名
     */
    private String pkg;

    /**
     * 字段对应值
     */
    private String value;

    /**
     * 字段对应值
     */
    private String valueType;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> valueMap;

    /**
     * 默认值
     */
    private String defaulted;

    @ApiModelProperty("数据库类型")
    @TableField(exist = false)
    private Integer type;

    private String remark;

    private Integer status;
}
