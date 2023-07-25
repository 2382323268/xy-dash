package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("xy_migration_code_operations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(callSuper = true)
public class MigrationCodeOperations extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 代码id
     */
    private Long codeId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;
}
