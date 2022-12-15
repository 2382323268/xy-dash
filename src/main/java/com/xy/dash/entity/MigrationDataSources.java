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
@TableName("xy_migration_data_sources")
@ToString(callSuper = true)
public class MigrationDataSources extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据库名
     */
    @ApiModelProperty("数据库名")
    @NotBlank(message = "数据库名不能为空")
    private String databaseName;

    /**
     * 唯一名称
     */
    @ApiModelProperty("唯一名称")
    @NotBlank(message = "唯一名称不能为空")
    private String uniqueName;

    /**
     * 数据源id
     */
    @ApiModelProperty("数据源id")
    @NotNull(message = "数据源id不能为空")
    private Long sourceId;

    /**
     * 数据源id
     */
    @ApiModelProperty("迁移配置id")
    private Long migrationsId;

    /**
     * 数据库类型
     */
    @ApiModelProperty("数据库类型")
    @NotNull(message = "数据库类型不能为空")
    @TableField(exist = false)
    private Integer type;

    @TableField(exist = false)
    private List<MigrationTables> migrationTables;

}
