package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.dash.entity.MigrationFields;
import com.xy.dash.enums.FieldStatus;
import com.xy.dash.mapper.MigrationFieldsMapper;
import com.xy.dash.service.MigrationFieldsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.StringUtil;
import com.xy.dash.vo.TemplatesFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段配置表 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MigrationFieldsServiceImpl extends ServiceImpl<MigrationFieldsMapper, MigrationFields> implements MigrationFieldsService {

    @Override
    public List<TemplatesFields> getTemplatesFields(Long tableId, List<String> tableNames) {
        List<MigrationFields> migrationFields = list(new LambdaQueryWrapper<MigrationFields>()
                .eq(MigrationFields::getTableId, tableId));
        migrationFields = migrationFields.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(MigrationFields::getFieldName))),
                ArrayList::new));
        Map<Long, MigrationFields> map = migrationFields.stream().collect(Collectors.toMap(MigrationFields::getId, Function.identity()));
        List<TemplatesFields> templatesFields = BeanUtil.copyProperties(migrationFields, TemplatesFields.class);

        templatesFields.forEach(e -> {

            e.setPropertyName(FieldStatus.getPropertyName(e.getPropertyName(), e.getStatus()));
            e.setLowerPropertyName(StringUtil.lowerCase(e.getPropertyName()));
            MigrationFields field = map.get(e.getId());

            if (!CollectionUtils.isEmpty(field.getValueMap())) {

                List<String> valueMap = field.getValueMap();
                String tableName = valueMap.get(1);
                String entity = StringUtil.upperCase(StringUtil.lineToHump(tableName));

                // 存在相同表名 则需要拼上唯一名称
                if (tableNames.contains(tableName)) {
                    entity = valueMap.get(0).toLowerCase().concat(entity);
                }

                String concat = entity.concat("By").concat(StringUtil.upperCase(StringUtil.lineToHump(valueMap.get(2))));
                e.setValueMapEntity(StringUtil.upperCase(entity));
                e.setValueMap(StringUtil.lowerCase(concat));
            }
        });
        return templatesFields;
    }
}
