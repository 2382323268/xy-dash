package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.xy.dash.config.handler.StringToMapTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@TableName(value = "xy_migration_join_tables", autoResultMap = true)
@ToString(callSuper = true)
public class MigrationJoinTables extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表id
     */
    @ApiModelProperty("表id")
    private Long tableId;

    /**
     * 迁移表id
     */
    @ApiModelProperty("迁移表id")
    private Long migrationTableId;

    /**
     * 查询字段
     */
    @ApiModelProperty("查询字段")
    @NotBlank(message = "查询字段不能为空")
    private String field;

    /**
     * 查询字段类型
     */
    @ApiModelProperty("查询字段类型")
    @NotBlank(message = "查询字段类型不能为空")
    private String fieldType;

    /**
     * 关联字段
     */
    @ApiModelProperty("关联字段")
    @NotBlank(message = "关联字段不能为空")
    private String joinFiled;

    /**
     * 关联字段类型
     */
    @ApiModelProperty("关联字段类型")
    @NotBlank(message = "关联字段类型不能为空")
    private String joinFiledType;

    @ApiModelProperty("查询条件")
    @TableField(typeHandler = StringToMapTypeHandler.class)
    private Map<String, String> queryMap;

    @ApiModelProperty("查询字段列表")
    @NotBlank(message = "查询字段列表不能为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> selectList;

    @ApiModelProperty("表名")
    @NotBlank(message = "表名不能为空")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty("数据源名称")
    @NotBlank(message = "数据源名称不能为空")
    @TableField(exist = false)
    private String uniqueName;

    @ApiModelProperty("数据源id")
    @TableField(exist = false)
    private Long dataSourceId;

    @ApiModelProperty("数据库名")
    @TableField(exist = false)
    private String databaseName;
}
