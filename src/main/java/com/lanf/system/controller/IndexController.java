package com.lanf.system.controller;

import com.google.gson.JsonObject;
import com.lanf.common.helper.JwtHelper;
import com.lanf.common.result.Result;
import com.lanf.common.utils.BeanUtil;
import com.lanf.model.system.SysDept;
import com.lanf.model.system.SysMenu;
import com.lanf.model.system.SysUser;
import com.lanf.model.vo.SysPwdVo;
import com.lanf.system.model.SysI18n;
import com.lanf.system.service.SysI18nService;
import com.lanf.system.service.SysMenuService;
import com.lanf.system.service.SysUserService;
import com.lanf.system.utils.JsonListEach;
import com.lanf.system.utils.UserUtil;
import com.lanf.system.vo.SysI18nQueryVo;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台登录登出
 * </p>
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysI18nService sysI18nService;

    @Autowired
    private RedisTemplate redisTemplate;

    @CrossOrigin
    @GetMapping("/getI18n")
    public Result getI18n(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        SysI18nQueryVo sysI18nQueryVo = new SysI18nQueryVo();
        sysI18nQueryVo.setType("3001");
        List<SysI18n> list = sysI18nService.queryList(sysI18nQueryVo);
        JsonObject cn = JsonListEach.convertSysI18nToJson(list);
        sysI18nQueryVo.setType("3002");
        List<SysI18n> flist = sysI18nService.queryList(sysI18nQueryVo);
        JsonObject en = JsonListEach.convertSysI18nToJson(flist);
        Map<String, Object> map = new HashedMap<>();
        map.put("cn", cn.toString());
        map.put("en", en.toString());
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        String username = JwtHelper.getUsername(request.getHeader("token"));
        Map<String, Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }

    /**
     * 获取用户菜单权限
     *
     * @param request
     * @return
     */
    @GetMapping("/menuTree")
    public Result menuTree(HttpServletRequest request) {
        String username = JwtHelper.getUsername(request.getHeader("token"));
        List<SysMenu> menuList = sysMenuService.findUserMenuList(username);
        return Result.ok(menuList);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        SysUser sysUser = UserUtil.getUserInfo();
        redisTemplate.delete(sysUser.getUsername());
        redisTemplate.delete(sysUser.getId());
        return Result.ok();
    }

    @PostMapping("/changePwd")
    public Result changePwd(@RequestBody SysPwdVo sysPwdVo) {
        this.sysUserService.changePwd(sysPwdVo);
        return Result.ok();
    }

}
