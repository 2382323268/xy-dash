package com.xy.dash.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.dash.entity.Migrations;
import com.xy.dash.utli.Query;
import com.xy.dash.utli.api.R;
import io.swagger.annotations.ApiOperation;
import com.xy.dash.service.MigrationsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据迁移配置 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@RestController
@RequestMapping("/admin_api/v1/migrations")
@Api(value = "数据迁移配置", tags = "数据迁移配置", description = "")
public class MigrationsController {

    @Autowired
    private MigrationsService migrationsService;

    @GetMapping("/{id}")
    @ApiOperation(value = "详情")
    public Migrations details(@PathVariable Long id) {
        return migrationsService.details(id);
    }

    @GetMapping("")
    @ApiOperation(value = "查询")
    public IPage<Migrations> page(@ApiIgnore @RequestParam Map<String, Object> param, Query query) {
        return migrationsService.queryPage(param, query);
    }

    @GetMapping("/list")
    @ApiOperation(value = "列表")
    public List<Migrations> list() {
        return migrationsService.list();
    }

    @PostMapping("")
    @ApiOperation(value = "保存")
    public R save(@Valid @RequestBody Migrations migrations) {
        migrationsService.add(migrations);
        return R.status(true);
    }

    @PostMapping("/{id}/delete")
    @ApiOperation(value = "删除")
    public R delete(@PathVariable Long id) {
        migrationsService.batchDelete(id);
        return R.status(true);
    }

    @PostMapping("/{id}/generatingCode")
    @ApiOperation(value = "生成代码")
    public R generatingCode(@PathVariable Long id, @RequestParam("remark") String remark) {
        migrationsService.generatingCode(id, remark);
        return R.status(true);
    }
}
