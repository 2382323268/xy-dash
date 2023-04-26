package com.xy.dash.controller;


import com.xy.dash.aspect.GlobalLog;
import com.xy.dash.service.AdminMenuService;
import com.xy.dash.vo.AdminMenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-02-12
 */
@Slf4j
@RestController
@RequestMapping("/admin_api/v1/menus")
@Api(value = "菜单", tags = "菜单", description = "")
public class AdminMenuController {

    @Autowired
    private AdminMenuService adminMenuService;

    /**
     * 分页
     */
    @GetMapping("")
    @ApiOperation(value = "查询")
    public List<AdminMenuVO> list() throws InterruptedException {
      return adminMenuService.queryList();
    }
}
