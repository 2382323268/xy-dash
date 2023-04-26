package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@TableName("xy_migrations")
@ToString(callSuper = true)
public class Migrations extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 线程数量
     */
    @ApiModelProperty("线程数量")
    private Integer threadCount;

    /**
     * 是否开启线程
     */
    @ApiModelProperty("是否开启线程")
    @NotNull(message = "startThread参数不能为空")
    private Boolean startThread;

    /**
     * 启动端口号
     */
    @ApiModelProperty("启动端口号")
    private Integer port;

    /**
     * 每次查询多少条
     */
    @ApiModelProperty("每次查询多少条")
    @NotNull(message = "count参数不能为空")
    private Integer count;

    /**
     * 是否sql拼接插入
     */
    @ApiModelProperty("是否sql拼接插入")
    private Boolean sqlSpliec;

    @ApiModelProperty("是否删除")
    @NotNull(message = "是否删除参数不能为空")
    private Boolean isDel;

    @TableField(exist = false)
    @Valid
    @NotEmpty(message = "库配置不能为空")
    private List<MigrationDataSources> migrationDataSources;
}
