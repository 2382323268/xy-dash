package com.xy.dash.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class DataSourcesReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 连接ip
     */
    @ApiModelProperty("连接ip")
    @NotBlank(message = "连接ip不能为空")
    private String ip;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 端口
     */
    @ApiModelProperty("端口")
    @NotNull(message = "端口不能为空")
    private Integer port;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 类型 0 mysql/1 q
     */
    @ApiModelProperty("类型")
    @NotNull(message = "类型不能为空")
    private Integer type;

}
