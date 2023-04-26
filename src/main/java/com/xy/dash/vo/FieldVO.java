package com.xy.dash.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xiangwei
 * @Date: 2023/4/7 11:48
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldVO {

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段类型(mysql)")
    private String type;

    @ApiModelProperty("字段大小")
    private String size;

    @ApiModelProperty("字段描述")
    private String remark;

    @ApiModelProperty("字段状态")
    private Integer status;

    @ApiModelProperty("是否为空")
    private String isNull;

    @ApiModelProperty("数据库默认值")
    private String def;

    @ApiModelProperty("字段类型(java)")
    private String javaType;

}
