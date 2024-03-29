package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("xy_migration_codes")
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MigrationCodes extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 迁移id
     */
    private Long migrationId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 运行次数
     */
    private Integer runCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 迁移id
     */
    @TableField(exist = false)
    private String migrationName;


}
