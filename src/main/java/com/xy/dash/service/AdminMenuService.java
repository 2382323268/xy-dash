package com.xy.dash.service;

import com.xy.dash.entity.AdminMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.vo.AdminMenuVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2023-02-12
 */
public interface AdminMenuService extends IService<AdminMenu> {

    List<AdminMenuVO> queryList();
}
