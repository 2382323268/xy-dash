package com.xy.dash.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.dash.entity.DataSources;
import com.xy.dash.entity.MigrationCodes;
import com.xy.dash.entity.Migrations;
import com.xy.dash.service.MigrationCodesService;
import com.xy.dash.utli.Condition;
import com.xy.dash.utli.Query;
import com.xy.dash.utli.XyConstant;
import com.xy.dash.utli.ZipUtils;
import com.xy.dash.utli.api.R;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 代码记录表 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-17
 */
@Slf4j
@RestController
@RequestMapping("/admin_api/v1/migrationCodes")
@Api(value = "", tags = "", description = "")
public class MigrationCodesController {

    @Autowired
    private MigrationCodesService migrationCodesService;

    @GetMapping("")
    @ApiOperation(value = "查询")
    public IPage<MigrationCodes> page(@ApiIgnore @RequestParam Map<String, Object> param, Query query) {
        return migrationCodesService.queryPage(param, query);
    }

    @GetMapping("/list")
    @ApiOperation(value = "列表")
    public List<MigrationCodes> list() {
        return migrationCodesService.list();
    }

    @PostMapping("/{id}/delete")
    @ApiOperation(value = "删除")
    public R delete(@PathVariable Long id) {
        migrationCodesService.delete(id);
        return R.status(true);
    }

    @PostMapping("/{id}/download")
    @ApiOperation(value = "下载")
    public void download(@PathVariable Long id) {
        MigrationCodes migrationCodes = migrationCodesService.getById(id);
        String fileName = migrationCodes.getFileName();
        String path = XyConstant.PATH.concat(fileName);
        ZipUtils.fileToZip(path, fileName);
    }

    @PostMapping("/{id}/run")
    @ApiOperation(value = "运行")
    public void run(@PathVariable Long id, @RequestParam String remark) {
        migrationCodesService.run(id, remark);
    }
}
