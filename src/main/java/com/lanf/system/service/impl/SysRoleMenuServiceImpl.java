package com.lanf.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.model.system.SysRoleMenu;
import com.lanf.model.system.SysUserRole;
import com.lanf.system.mapper.SysRoleMenuMapper;
import com.lanf.system.mapper.SysUserRoleMapper;
import com.lanf.system.service.SysRoleMenuService;
import com.lanf.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

}
