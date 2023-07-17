package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.entity.MigrationJoinTables;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.mapper.MigrationJoinTablesMapper;
import com.xy.dash.mapper.MigrationTablesMapper;
import com.xy.dash.service.MigrationFieldsService;
import com.xy.dash.service.MigrationJoinTablesService;
import com.xy.dash.service.MigrationTablesService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.StringUtil;
import com.xy.dash.vo.TemplatesDataSources;
import com.xy.dash.vo.TemplatesTables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据迁移表配置 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MigrationJoinTablesServiceImpl extends ServiceImpl<MigrationJoinTablesMapper, MigrationJoinTables> implements MigrationJoinTablesService {

}
