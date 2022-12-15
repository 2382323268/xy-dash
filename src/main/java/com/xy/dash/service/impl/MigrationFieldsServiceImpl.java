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

import java.util.List;

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
    public List<TemplatesFields> getTemplatesFields(Long tableId) {
        List<MigrationFields> migrationFields = list(new LambdaQueryWrapper<MigrationFields>()
                .eq(MigrationFields::getTableId, tableId));
        List<TemplatesFields> templatesFields = BeanUtil.copyProperties(migrationFields, TemplatesFields.class);
        templatesFields.forEach(e -> {
            e.setPropertyName(FieldStatus.getPropertyName(e.getPropertyName(), e.getStatus()));
            e.setLowerPropertyName(StringUtil.lowerCase(e.getPropertyName()));
        });
        return templatesFields;
    }
}
