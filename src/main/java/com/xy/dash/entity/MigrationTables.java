package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.xy.dash.config.handler.StringToMapTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@TableName(value = "xy_migration_tables", autoResultMap = true)
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

    @ApiModelProperty("下标")
    private Integer random;

    @ApiModelProperty("查询条件")
    @TableField(typeHandler = StringToMapTypeHandler.class)
    private Map<String, String> queryMap;

    @ApiModelProperty("id生成策略")
    private Integer idType;

    @TableField(exist = false)
    @NotEmpty(message = "字段配置不能为空")
    @Valid
    private List<MigrationFields> migrationFields;

    @ApiModelProperty("数据库类型")
    @TableField(exist = false)
    private Integer type;

    @ApiModelProperty("唯一名称")
    @TableField(exist = false)
    private String uniqueName;

    @TableField(exist = false)
    private List<MigrationJoinTables> migrationJoinTables;
}
