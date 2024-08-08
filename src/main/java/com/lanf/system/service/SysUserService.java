package com.lanf.system.service;

import com.lanf.system.model.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanf.system.vo.SysPwdVo;
import com.lanf.system.vo.SysUserQueryVo;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo adminQueryVo);

    public void updateStatus(String id, Integer status);

    public boolean save(SysUser sysUser);

    public boolean updateById(SysUser sysUser);

    SysUser getByUsername(String username);

    /**
     * 根据用户名获取用户登录信息
     * @param username
     * @return
     */
    Map<String, Object> getUserInfo(String username);
    public SysUser getById(String id);

    public void changePwd(SysPwdVo sysPwdVo);
}
