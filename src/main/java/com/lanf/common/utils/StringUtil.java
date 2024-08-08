package com.lanf.common.utils;

public class StringUtil {

    /**
     * 判断对象是否为字符串类型，并且是否为null或空字符串
     * @param obj 要检查的对象
     * @return 如果是字符串类型并且为null或空字符串，是则返回false，否则返回true
     */
    public static boolean isNotNullOrEmptyString(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            return str != null && !str.trim().isEmpty();
        }
        return false;
    }

    /**
     * 判断对象是否为字符串类型，并且是否为null或空字符串
     * @param obj 要检查的对象
     * @return 如果是字符串类型并且不为null或空字符串，是则返回true，否则返回false
     */
    public static boolean isNullOrEmptyString(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            return str == null || str.trim().isEmpty();
        }
        return false;
    }
}
