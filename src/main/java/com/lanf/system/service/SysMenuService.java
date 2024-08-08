package com.lanf.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanf.system.model.SysMenu;
import com.lanf.system.vo.AssginMenuVo;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    /**
     * 菜单树形数据
     *
     * @return
     */
    List<SysMenu> findNodes();

    public List<SysMenu> findDir(String notId);

    public List<SysMenu> findMenu();

    public List<String> findSysMenuByRoleId(String roleId);

    void doAssign(AssginMenuVo assginMenuVo);

    /**
     * 获取用户菜单
     *
     * @param userId
     * @return
     */
    List<SysMenu> findUserMenuList(String userId);

    /**
     * 获取用户按钮权限
     * @param userId
     * @return
     */
    List<String> findUserPermsList(String userId);

}
