package com.xy.dash.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

@Data
@TableName("xy_migration_code_operations")
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

}
