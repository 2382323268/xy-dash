package com.xy.dash.service.impl;

import com.xy.dash.entity.AdminMenu;
import com.xy.dash.enums.MenuType;
import com.xy.dash.mapper.AdminMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.AdminMenuService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.vo.AdminMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-02-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminMenuServiceImpl extends ServiceImpl<AdminMenuMapper, AdminMenu> implements AdminMenuService {

    @Override
    public List<AdminMenuVO> queryList() {
        List<AdminMenu> list = list();
        List<AdminMenuVO> c = BeanUtil.copyProperties(list.stream().filter(e -> e.getMenuType().equals(MenuType.C.getValue())).collect(Collectors.toList()), AdminMenuVO.class);
        List<AdminMenuVO> m = BeanUtil.copyProperties(list.stream().filter(e -> e.getMenuType().equals(MenuType.M.getValue())).collect(Collectors.toList()), AdminMenuVO.class);
        List<AdminMenuVO> f = BeanUtil.copyProperties(list.stream().filter(e -> e.getMenuType().equals(MenuType.F.getValue())).collect(Collectors.toList()), AdminMenuVO.class);
        c.forEach(e -> {
            e.setChildren(f.stream().filter(v -> e.getId().equals(v.getParentId())).collect(Collectors.toList()));
        });
        m.forEach(e -> {
            e.setChildren(c.stream().filter(v -> e.getId().equals(v.getParentId())).collect(Collectors.toList()));
        });
        return m;
    }
}
