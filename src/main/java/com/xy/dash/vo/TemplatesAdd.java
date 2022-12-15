package com.xy.dash.vo;

import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.entity.Migrations;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2022/11/16 15:42
 * @Description
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TemplatesAdd {

    private Migrations migrations;

    private List<TemplatesDataSources> migrationDataSources;

    private List<TemplatesTables> templatesTables;

    private String fileName;

}
