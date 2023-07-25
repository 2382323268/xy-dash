package com.xy.dash.service;

import com.xy.dash.vo.TemplatesAdd;

import java.io.File;

/**
 * @Author: xiangwei
 * @Date: 2022/11/14 15:59
 * @Description
 **/
public interface TemplatesService {

    /**
     * 生成数据迁移代码
     *
     * @param add
     */
    void create(TemplatesAdd add);

    void deleteFile(File file);
}
