package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName("xy_migration_tables")
@ToString(callSuper = true)
public class MigrationTables extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据源配置id
     */
    @ApiModelProperty("数据源配置id")
    private Long migrationSourcesId;

    /**
     * 是否迁移表
     */
    @TableField("is_migration")
    @ApiModelProperty("是否迁移表")
    @NotNull(message = "is_migration不能为空")
    private Boolean migration;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    @NotBlank(message = "表名不能为空")
    private String name;

    /**
     * 权重（数据迁移顺序）
     */
    @ApiModelProperty("权重")
    private Integer position;

    @ApiModelProperty("来源类名")
    private String sourceName;

    @ApiModelProperty("来源唯一名称")
    private String sourceUniqueName;


    @TableField(exist = false)
    private List<MigrationFields> migrationFields;

    @ApiModelProperty("数据库类型")
    @TableField(exist = false)
    private Integer type;
}
