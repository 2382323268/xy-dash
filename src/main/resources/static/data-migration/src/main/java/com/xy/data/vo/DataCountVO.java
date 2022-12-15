package com.xy.data.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2022/10/24 17:42
 * @Description
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataCountVO {

    // 添加数量
    private Integer save;

    // 删除数量
    private Integer delete;

    private List<String> neIds;

    private LocalDateTime start;

    private String id;

}
