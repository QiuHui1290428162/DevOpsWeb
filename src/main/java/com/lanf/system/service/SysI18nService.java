package com.lanf.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanf.system.model.SysI18n;
import com.lanf.system.vo.SysI18nQueryVo;

import java.util.List;

/**
 * @author tanlingfei
 * @version 1.0
 * @description 国际化语言 Service接口
 * @date 2023-10-31 13:47:32
 */
public interface SysI18nService extends IService<SysI18n> {
    IPage<SysI18n> selectPage(Page<SysI18n> pageParam, SysI18nQueryVo queryVo);

    List<SysI18n> queryList(SysI18nQueryVo queryVo);

    public boolean save(SysI18n sysI18n);

    public boolean updateById(SysI18n sysI18n);

    public SysI18n getById(String id);

    public List<SysI18n> getByIds(List<String> ids);
}
