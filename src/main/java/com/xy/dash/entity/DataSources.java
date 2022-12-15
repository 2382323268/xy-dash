package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

@Data
@TableName("xy_data_sources")
@ToString(callSuper = true)
public class DataSources extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 连接ip
     */
    @ApiModelProperty("连接ip")
    private String ip;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

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

}
