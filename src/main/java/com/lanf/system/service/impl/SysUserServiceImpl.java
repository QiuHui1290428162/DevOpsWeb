package com.lanf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.common.utils.MD5;
import com.lanf.model.system.SysDept;
import com.lanf.model.system.SysUser;
import com.lanf.model.system.SysUserRole;
import com.lanf.model.vo.SysPwdVo;
import com.lanf.model.vo.SysUserQueryVo;
import com.lanf.system.exception.LanfException;
import com.lanf.system.mapper.SysUserMapper;
import com.lanf.system.service.SysDeptService;
import com.lanf.system.service.SysMenuService;
import com.lanf.system.service.SysUserRoleService;
import com.lanf.system.service.SysUserService;
import com.lanf.system.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

@Transactional
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo userQueryVo) {

        SysUser sysUser = UserUtil.getUserInfo();
        if ("1".equals(sysUser.getId())) {
            userQueryVo.setDeptId(null);
        } else {
            if (StringUtils.isEmpty(sysUser.getDeptId())) {
                return null;
            }
            userQueryVo.setDeptId(sysUser.getDeptId());
        }
        return sysUserMapper.selectPage(pageParam, userQueryVo);
    }


    @Override
    public void updateStatus(String id, Integer status) {
        SysUser sysUser = sysUserMapper.selectById(id);
        sysUser.setStatusData(sysUser.getStatus() ? 1 : 0);
        sysUserMapper.updateById(sysUser);
    }

    @Override
    public SysUser getByUsername(String username) {
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        return sysUser;
    }

    @Override
    public boolean save(SysUser sysUser) {
        String pwd = MD5.encrypt(sysUser.getUsername());
        sysUser.setPassword(pwd);
        sysUser.setStatusData(sysUser.getStatus() ? 1 : 0);
        int result = this.sysUserMapper.insert(sysUser);
        List<String> roleList = sysUser.getRoleList();
        if (roleList != null && roleList.size() > 0) {
            List<SysUserRole> saveRoles = new ArrayList<>();
            for (String roleId : roleList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(roleId);
                saveRoles.add(sysUserRole);
            }
            this.sysUserRoleService.saveBatch(saveRoles);
        }
        return result > 0;
    }

    @Override
    public boolean updateById(SysUser sysUser) {
        if (!StringUtils.isEmpty(sysUser.getNewpassword()) && !"null".equals(sysUser.getNewpassword())) {
            String pwd = MD5.encrypt(sysUser.getNewpassword());
            sysUser.setPassword(pwd);
        }
        sysUser.setStatusData(sysUser.getStatus() ? 1 : 0);
        int row = this.sysUserMapper.updateById(sysUser);
        List<String> roleList = sysUser.getRoleList();
        if (roleList != null && roleList.size() > 0) {
            List<SysUserRole> saveRoles = new ArrayList<>();
            for (String roleId : roleList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(roleId);
                saveRoles.add(sysUserRole);
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", sysUser.getId());
            sysUserRoleService.remove(queryWrapper);
            this.sysUserRoleService.saveBatch(saveRoles);
        }
        return row > 0;
    }

    @Override
    public SysUser getById(String id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        sysUser.setStatus("1".equals(sysUser.getStatusData().toString()));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("role_id");
        queryWrapper.eq("user_id", sysUser.getId());
        Function<Object, String> f = (o -> o.toString());
        List<String> roleList = sysUserRoleService.listObjs(queryWrapper, f);
        sysUser.setRoleList(roleList);
        return sysUser;
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        SysUser sysUser = this.getByUsername(username);
        if (sysUser != null) {
            result.put("username", sysUser.getUsername());
            result.put("name", sysUser.getName());
            result.put("mobile", sysUser.getMobile());
            result.put("email", sysUser.getEmail());
            SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
            result.put("deptName", sysDept.getName());
            result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            result.put("roles", new HashSet<>());
            List<String> buttons = sysMenuService.findUserPermsList(sysUser.getId());
            result.put("buttons", buttons);
        }
        return result;
    }

    @Override
    public void changePwd(SysPwdVo sysPwdVo) {
        SysUser sysUser = this.getById(UserUtil.getUserId());
        if (!MD5.encrypt(sysPwdVo.getPassword()).equals(sysUser.getPassword())) {
            throw new LanfException(5240, "旧密码错误");
        }
        if (!sysPwdVo.getCfpassword().equals(sysPwdVo.getNpassword())) {
            throw new LanfException(5240, "两次密码不一致");
        }
        sysUser.setPassword(MD5.encrypt(sysPwdVo.getCfpassword()));
        this.updateById(sysUser);
    }
}
