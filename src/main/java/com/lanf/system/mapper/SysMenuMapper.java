package com.lanf.system.mapper;

import com.lanf.system.model.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findListByUserId(String userId,Integer type,List<Integer> typeList);

    List<SysMenu> queryList(String type,String notId);
}
