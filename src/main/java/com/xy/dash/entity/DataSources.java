package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.xy.dash.config.handler.AesEncrytHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

@Data
@TableName(value = "xy_data_sources",autoResultMap = true)
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
    @TableField(typeHandler = AesEncrytHandler.class)
    private String password;

    /**
     * 类型 0 mysql/1 q
     */
    @ApiModelProperty("类型")
    private Integer type;

}
