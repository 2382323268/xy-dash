package com.xy.dash.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xy.dash.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2023/2/17 17:50
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMenuVO extends BaseEntity implements Serializable {
    /**
     * 菜单主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 备注
     */
    private String remark;

    private List<AdminMenuVO> children;
}
