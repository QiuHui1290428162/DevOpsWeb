package com.lanf.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanf.system.model.SysI18n;
import com.lanf.system.vo.SysI18nQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
* @author tanlingfei
* @version 1.0
* @description 国际化语言 Mapper层
* @date 2023-10-31 13:47:32
*/
@Repository
@Mapper
public interface SysI18nMapper extends BaseMapper<SysI18n> {
    IPage<SysI18n> selectPage(Page<SysI18n> page, @Param("vo") SysI18nQueryVo sysI18nQueryVo);
    List<SysI18n> queryList(@Param("vo") SysI18nQueryVo sysI18nQueryVo);
}