package com.xy.dash.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.entity.Migrations;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class TemplatesDataSources {

    private Long id;

    /**
     * 连接ip
     */
    @ApiModelProperty("连接ip")
    private String ip;

    /**
     * 端口
     */
    @ApiModelProperty("端口")
    private Integer port;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 类型 0 mysql/1 sqlserver
     */
    @ApiModelProperty("类型")
    private Integer type;

    /**
     * 数据库名
     */
    @ApiModelProperty("数据库名")
    private String databaseName;

    /**
     * 唯一名称
     */
    @ApiModelProperty("唯一名称")
    private String uniqueName;
}
