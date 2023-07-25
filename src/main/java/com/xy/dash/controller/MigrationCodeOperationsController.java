package com.xy.dash.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.dash.entity.MigrationCodeOperations;
import com.xy.dash.entity.MigrationCodes;
import com.xy.dash.entity.Migrations;
import com.xy.dash.service.MigrationCodeOperationsService;
import com.xy.dash.utli.Condition;
import com.xy.dash.utli.Query;
import com.xy.dash.utli.XyConstant;
import com.xy.dash.utli.api.R;
import com.xy.dash.vo.MigrationCodeOperationDetails;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 代码运行记录表 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-17
 */
@Slf4j
@RestController
@RequestMapping("/admin_api/v1/migrationCodeOperations")
@Api(value = "", tags = "", description = "")
public class MigrationCodeOperationsController {

    @Autowired
    private MigrationCodeOperationsService migrationCodeOperationsService;

    @GetMapping("")
    @ApiOperation(value = "查询")
    public IPage<MigrationCodeOperations> page(@ApiIgnore @RequestParam Map<String, Object> param, Query query) {
        QueryWrapper<MigrationCodeOperations> queryWrapper = Condition.getQueryWrapper(param, MigrationCodeOperations.class);
        return migrationCodeOperationsService.page(Condition.getPage(query), queryWrapper);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "详情")
    public MigrationCodeOperationDetails details(@PathVariable("id") Long id) {
        return migrationCodeOperationsService.details(id);
    }

    @GetMapping("/{id}/log")
    @ApiOperation(value = "详情")
    public List<String> log(@PathVariable("id") Long id) {
        MigrationCodeOperations migrationCodeOperations = migrationCodeOperationsService.getById(id);
        String format = String.format(XyConstant.LOG_PATH, migrationCodeOperations.getFileName(), id);
        return readFile(format);
    }

    /**
     * 读取一个文本 一行一行读取
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static List<String> readFile(String path) {
        List<String> value = new ArrayList<>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis, "GBK");
            br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                value.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
