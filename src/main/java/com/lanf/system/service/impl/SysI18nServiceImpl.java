package com.lanf.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.common.exception.GlobalExpiredException;
import com.lanf.common.result.ResultCodeEnum;
import com.lanf.system.mapper.SysI18nMapper;
import com.lanf.system.model.SysI18n;
import com.lanf.system.service.SysI18nService;
import com.lanf.system.vo.SysI18nQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tanlingfei
 * @version 1.0
 * @description 国际化语言 Service实现类
 * @date 2023-10-31 13:47:32
 */
@Transactional
@Service
public class SysI18nServiceImpl extends ServiceImpl
        <SysI18nMapper, SysI18n> implements SysI18nService {
    @Autowired
    private SysI18nMapper sysI18nMapper;

    @Override
    public IPage<SysI18n> selectPage(Page<SysI18n> pageParam, SysI18nQueryVo sysI18nQueryVo) {
        return sysI18nMapper.selectPage(pageParam, sysI18nQueryVo);
    }

    @Override
    public List<SysI18n> queryList(SysI18nQueryVo sysI18nQueryVo) {
        List<SysI18n> result = sysI18nMapper.queryList(sysI18nQueryVo);
        return result;
    }


    @Override
    public boolean save(SysI18n sysI18n) {
        int result = this.sysI18nMapper.insert(sysI18n);
        return result > 0;
    }

    @Override
    public boolean updateById(SysI18n sysI18n) {
        int row = this.sysI18nMapper.updateById(sysI18n);
        if (row <= 0) {
            throw new GlobalExpiredException(ResultCodeEnum.UPDATE_ERROR);
        }
        return row > 0;
    }

    @Override
    public SysI18n getById(String id) {
        SysI18n sysI18n = sysI18nMapper.selectById(id);
        return sysI18n;
    }

    @Override
    public List<SysI18n> getByIds(List<String> ids) {
        List<SysI18n> list = this.sysI18nMapper.selectBatchIds(ids);
        return list;
    }
}
