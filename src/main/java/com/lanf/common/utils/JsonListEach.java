package com.lanf.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.*;
import com.lanf.system.model.SysI18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonListEach {
    public static <T> String convertListToJson(List<T> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> convertJsonToList(String json, Class<T> elementType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 国际化语言SysI18n数据转换为前端接收的格式
     *
     * @param list
     * @return
     */
    public static JsonObject convertSysI18nToJson(List<SysI18n> list) {
        JsonObject jsonObject = new JsonObject();
        for (SysI18n sys : list) {
            String key = sys.getName();
            String value = sys.getVal();
            if (key.contains(".")) {
                String[] nameParts = key.split("\\.");
                JsonObject currentObject = jsonObject;
                for (int i = 0; i < nameParts.length - 1; i++) {
                    if (!currentObject.has(nameParts[i])) {
                        JsonObject newObject = new JsonObject();
                        currentObject.add(nameParts[i], newObject);
                        currentObject = newObject;
                    } else {
                        JsonElement element = currentObject.get(nameParts[i]);
                        if (element.isJsonObject()) {
                            currentObject = element.getAsJsonObject();
                        } else {
                            JsonObject newObject = new JsonObject();
                            currentObject.add(nameParts[i], newObject);
                            currentObject = newObject;
                        }
                    }
                }
                currentObject.addProperty(nameParts[nameParts.length - 1], value);
            } else {
                jsonObject.addProperty(key, value);
            }
        }
        return jsonObject;
    }

    private static String convertToJsonString(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    public static List<SysI18n> convertJsonToList(String json) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        List<SysI18n> list = new ArrayList<>();
        convertJsonObjectToList(jsonObject, "", list);
        return list;
    }

    private static void convertJsonObjectToList(JsonObject jsonObject, String prefix, List<SysI18n> list) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                convertJsonObjectToList(value.getAsJsonObject(), prefix + key + ".", list);
            } else {
                SysI18n sys = new SysI18n();
                sys.setName(prefix + key);
                sys.setVal(value.getAsString());
                list.add(sys);
            }
        }
    }

    public static void main(String[] args) {
        String jsonStr = "{\n" +
                "  \"sitename\": \"权限管理系统\",\n" +
                "  \"personalCenter\": \"个人中心\",\n" +
                "  \"logout\": \"退出\",\n" +
                "  \"login\": \"登录\",\n" +
                "  \"tips404\": \"对不起！页面找不到了......\",\n" +
                "  \"backHome\": \"返回首页\",\n" +
                "  \"nodata\": \"暂无数据\",\n" +
                "  \"form\": {\n" +
                "    \"username\": \"用户名\",\n" +
                "    \"usernameHolder\": \"请输入用户名\",\n" +
                "    \"usernameError\": \"用户名由英文字母开头的长度4-32位字母、_和-组成\",\n" +
                "    \"password\": \"密码\",\n" +
                "    \"passwordHolder\": \"请输入密码\",\n" +
                "    \"passwordError\": \"请输入至少4个字符的密码\",\n" +
                "    \"cfpasswordError\": \"两次密码输入不一致\",\n" +
                "    \"publish\": \"立即发布\",\n" +
                "    \"titleRequired\": \"请输入标题\",\n" +
                "    \"titleError\": \"标题长度4-60\",\n" +
                "    \"contentRequired\": \"请输入公告内容\",\n" +
                "    \"contentError\": \"内容长度3-300\",\n" +
                "    \"roleRequired\": \"请选择管理组\",\n" +
                "    \"npassword\": \"新密码\",\n" +
                "    \"currPassword\": \"当前密码\",\n" +
                "    \"cfpassword\": \"确认密码\",\n" +
                "    \"resetPassword\": \"重置密码\",\n" +
                "    \"group\": \"管理组\",\n" +
                "    \"choose\": \"请选择\",\n" +
                "    \"email\": \"邮箱\",\n" +
                "    \"mobile\": \"手机号\",\n" +
                "    \"role\": \"角色\",\n" +
                "    \"dept\": \"机构\",\n" +
                "    \"keywordsHodler\": \"请输入关键字\",\n" +
                "    \"parent\": \"父级\",\n" +
                "    \"deptRequired\": \"请选择机构\",\n" +
                "    \"emailError\": \"邮箱格式错误\",\n" +
                "    \"mobileError\": \"手机号格式错误\",\n" +
                "    \"emailOrMobile\": \"手机或邮箱至少填一个\",\n" +
                "    \"roleIdsRequired\": \"请选择角色\",\n" +
                "    \"nameRequired\": \"请输入名称\",\n" +
                "    \"displayNameRequired\": \"请输入显示名称\",\n" +
                "    \"urlRequired\": \"请输入菜单路由\"\n" +
                "  },\n" +
                "  \"menu\": {\n" +
                "    \"App\": \"应用管理\",\n" +
                "    \"AppUser\": \"用户管理\",\n" +
                "    \"AppDept\": \"机构管理\",\n" +
                "    \"AppRole\": \"角色管理\",\n" +
                "    \"AppResource\": \"资源管理\",\n" +
                "    \"AppPermission\": \"授权管理\",\n" +
                "    \"Sys\": \"系统管理\",\n" +
                "    \"SysUser\": \"用户管理\",\n" +
                "    \"SysNotice\": \"公告管理\",\n" +
                "    \"Logs\": \"审计管理\",\n" +
                "    \"LogsVisit\": \"访问日志\",\n" +
                "    \"LogsOperation\": \"操作日志\"\n" +
                "  },\n" +
                "  \"action\": {\n" +
                "    \"operation\": \"操作\",\n" +
                "    \"add\": \"新增\",\n" +
                "    \"edit\": \"编辑\",\n" +
                "    \"delete\": \"删除\",\n" +
                "    \"batchDelete\": \"批量删除\",\n" +
                "    \"search\": \"查询\",\n" +
                "    \"submit\": \"提交\",\n" +
                "    \"confirm\": \"确定\",\n" +
                "    \"cancel\": \"取消\",\n" +
                "    \"reset\": \"重置\",\n" +
                "    \"bindResource\": \"绑定资源\",\n" +
                "    \"apply\": \"申请变更\",\n" +
                "    \"markedRead\": \"标为已读\",\n" +
                "    \"markedAllRead\": \"标记全部为已读\"\n" +
                "  },\n" +
                "  \"tips\": {\n" +
                "    \"title\": \"提示\",\n" +
                "    \"deleteTitle\": \"提示\",\n" +
                "    \"deleteConfirm\": \"确认删除选中记录吗？\",\n" +
                "    \"success\": \"操作成功！\",\n" +
                "    \"failed\": \"操作失败！\"\n" +
                "  },\n" +
                "  \"thead\": {\n" +
                "    \"ID\": \"ID\",\n" +
                "    \"IP\": \"IP\",\n" +
                "    \"operator\": \"操作人\",\n" +
                "    \"operation\": \"操作\",\n" +
                "    \"operateTime\": \"操作时间\",\n" +
                "    \"visitTime\": \"访问时间\",\n" +
                "    \"duration\": \"耗时 (ms)\",\n" +
                "    \"username\": \"用户名\",\n" +
                "    \"status\": \"状态\",\n" +
                "    \"publishTime\": \"发布时间\",\n" +
                "    \"createdBy\": \"创建人\",\n" +
                "    \"createdTime\": \"创建时间\",\n" +
                "    \"title\": \"标题\",\n" +
                "    \"content\": \"内容\",\n" +
                "    \"group\": \"管理组\",\n" +
                "    \"latestIp\": \"最近登录IP\",\n" +
                "    \"latestVisit\": \"最近登录时间\",\n" +
                "    \"email\": \"邮箱\",\n" +
                "    \"mobile\": \"手机号\",\n" +
                "    \"role\": \"角色\",\n" +
                "    \"dept\": \"机构\",\n" +
                "    \"roleName\": \"角色名\",\n" +
                "    \"remark\": \"备注\",\n" +
                "    \"updatedBy\": \"更新人\",\n" +
                "    \"updatedTime\": \"更新时间\",\n" +
                "    \"name\": \"名称\",\n" +
                "    \"icon\": \"图标\",\n" +
                "    \"type\": \"类型\",\n" +
                "    \"url\": \"菜单URL\",\n" +
                "    \"orderNum\": \"排序\",\n" +
                "    \"displayName\": \"显示名称\"\n" +
                "  },\n" +
                "  \"status\": {\n" +
                "    \"on\": \"在职\",\n" +
                "    \"off\": \"离职\",\n" +
                "    \"folder\": \"目录\",\n" +
                "    \"menu\": \"菜单\",\n" +
                "    \"button\": \"按钮\"\n" +
                "  },\n" +
                "  \"personal\": {\n" +
                "    \"title\": \"个人中心\",\n" +
                "    \"Profile\": \"基本资料\",\n" +
                "    \"ChangePsw\": \"修改密码\",\n" +
                "    \"Messages\": \"系统消息\",\n" +
                "    \"SiteMail\": \"站内信\"\n" +
                "  }\n" +
                "}";
        List<SysI18n> list = convertJsonToList(jsonStr);
        for(SysI18n sysI18n:list){
            System.out.println("name=="+sysI18n.getName());
            System.out.println("val=="+sysI18n.getVal());
        }
    }
}
