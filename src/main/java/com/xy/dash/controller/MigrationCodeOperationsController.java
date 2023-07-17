package com.xy.dash.controller;


import com.xy.dash.service.MigrationCodeOperationsService;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 代码运行记录表 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-17
 */
@Slf4j
@Controller
@Api(value = "", tags = "", description="")
public class MigrationCodeOperationsController {

    @Autowired
    private MigrationCodeOperationsService migrationCodeOperationsService;
}
