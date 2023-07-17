package com.xy.dash.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.enums.TemplatePathType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author: xiangwei
 * @Date: 2022/11/16 15:42
 * @Description
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TemplatesTables {

    private Long id;

    private Integer idType;

    private Boolean sqlSpliec;

    /**
     * 数据源配置id
     */
    @ApiModelProperty("数据源配置id")
    private Long migrationSourcesId;

    /**
     * 是否迁移表
     */
    @ApiModelProperty("是否迁移表")
    private Boolean migration;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String name;

    @ApiModelProperty("来源类名")
    private String sourceName;

    @ApiModelProperty("来源唯一名称")
    private String sourceUniqueName;

    private String entityName;

    private String serviceName;

    private String mapperName;

    private String implName;

    private String handlerName;

    private String lowerEntityName;

    private String lowerServiceName;

    private String lowerMapperName;

    private String lowerImplName;

    private String lowerHandlerName;

    /**
     * 权重（数据迁移顺序）
     */
    @ApiModelProperty("权重")
    private Integer position;

    /**
     * 迁移字段映射
     */
    @ApiModelProperty("迁移字段映射")
    private String fieldMap;

    /**
     * 唯一名称
     */
    @ApiModelProperty("唯一名称")
    private String uniqueName;

    @ApiModelProperty("类型")
    private Integer type;

    /**
     * 迁移类型
     */
    private String migrationType;

    private Map<String, String> queryMap;

    private Map<String, String> queryMapType;

    private List<TemplatesFields> templatesFields;

    private List<TemplatesFields> distinctTemplatesFields;

    private List<TemplatesJoinTables> distinctTemplatesJoinTables;

    private List<TemplatesJoinTables> templatesJoinTables;

    public String getName(TemplatePathType e) {
        switch (e.getKey()) {
            case "vms/entity.java.vm":
                return entityName;
            case "vms/iservice.java.vm":
                return serviceName;
            case "vms/serviceImpl.java.vm":
                return implName;
            case "vms/mapper.java.vm":
                return mapperName;
            case "vms/handler.java.vm":
                return handlerName;
            default:
                return null;
        }
    }
}
