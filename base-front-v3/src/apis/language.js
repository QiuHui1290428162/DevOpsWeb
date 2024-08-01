//获取国际化语言数据
import request from "../request";

export const getLanguageInfo = () => {
    return request({
        url: '/admin/system/index/getI18n',
        method: "get",
        withCredentials: false
    });
};
