package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("xy_migration_code_operation_historys")
public class MigrationCodeOperationHistorys extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作id
     */
    private Long operationId;

    /**
     * 0 执行失败 1 执行成功 2 开始执行 3 生成jar 4 运行中
     */
    private Integer status;

    private String msg;

    @TableField(exist = false)
    private String color;

}
