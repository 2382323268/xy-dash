package com.xy.dash.vo;

import com.xy.dash.enums.TemplatePathType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
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
public class TemplatesJoinTables {

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
    private String field;

    /**
     * 关联字段
     */
    @ApiModelProperty("关联字段")
    private String joinFiled;

    /**
     * 查询字段类型
     */
    @ApiModelProperty("查询字段类型")
    private String fieldType;

    /**
     * 关联字段类型
     */
    @ApiModelProperty("关联字段类型")
    private String joinFiledType;

    private String entityName;

    private String serviceName;

    private String mapperName;

    private String implName;

    private String handlerName;

    private String lowerField;

    private String lowerJoinFiled;

    private String lowerEntityName;

    private String lowerServiceName;

    private String lowerMapperName;

    private String lowerImplName;

    private String lowerHandlerName;

    private String uniqueName;

    private List<String> selectList;

    private Map<String, String> queryMap;

    private Map<String, String> queryMapType;

}
