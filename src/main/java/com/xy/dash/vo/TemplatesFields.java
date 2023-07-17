package com.xy.dash.vo;

import com.xy.dash.enums.TemplatePathType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class TemplatesFields {

    private Long id;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 属性名称
     */
    private String propertyName;

    private String lowerPropertyName;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 属性类型
     */
    private String propertyType;

    /**
     * 包名
     */
    private String pkg;

    private String valueType;

    /**
     * 字段对应值
     */
    private String value;

    private String valueMap;

    private String valueMapEntity;

    /**
     * 默认值
     */
    private String defaulted;

    private String remark;

    private Integer status;

}
