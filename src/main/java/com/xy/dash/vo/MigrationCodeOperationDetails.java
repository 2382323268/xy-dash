package com.xy.dash.vo;

import com.xy.dash.entity.MigrationCodeOperationHistorys;
import com.xy.dash.entity.MigrationCodeOperations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2023/7/23 16:41
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MigrationCodeOperationDetails extends MigrationCodeOperations implements Serializable {
    private List<MigrationCodeOperationHistorys> migrationCodeOperationHistorysList;
}
