package com.lanf.common.utils;

import com.alibaba.fastjson.JSON;
import com.lanf.common.helper.JwtHelper;
import com.lanf.system.model.SysUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tanlingfei
 * @version 1.0
 * @description TODO
 * @date 2023/4/27 15:53
 */
public class UserUtil {

    public static String getUserId() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request != null) {
            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)) {
                token = request.getParameter("token");
            }
            String userId = JwtHelper.getUserIds(token);
            return userId;
        }
        return null;
    }

    public static SysUser getUserInfo() {
        String userId = getUserId();
        if (StringUtil.isNotNullOrEmptyString(userId)) {
            RedisTemplate redisTemplate = (RedisTemplate) BeanUtil.getBean("redisTemplate");
            String result = (String) redisTemplate.opsForValue().get(userId);
            if (result != null) {
                return JSON.parseObject(result, SysUser.class);
            }
        }
        return null;
    }

    public static String getDeptId() {
        SysUser sysUser = getUserInfo();
        if (sysUser != null) {
            return sysUser.getDeptId();
        } else {
            return null;
        }
    }
}
