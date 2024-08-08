package com.lanf.generator;

import com.lanf.generator.act.ActivationCodeGenerator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.lanf.common.utils.DbUtil;

public class Generator {
    public Generator() {
    }

    public static void generatOne(CodeVo codeVo) {
        String connectionURL = codeVo.getConnectionURL();
        String username = codeVo.getUsername();
        String password = codeVo.getPassword();
        String driverName = codeVo.getDriverName();
        DbUtil db = DbUtil.n(connectionURL, username, password, driverName);
        /*
        List<Map<String, Object>> list = db.queryList("select auth_ from sys_auth");
        String code = codeVo.getAuthCode();
        String tabName;
        String packageName;
        String filePath;

        if (list != null && list.size() > 0) {
            Map<String, Object> amap = (Map)list.get(0);
            packageName = "" + amap.get("auth_");
            if (!ActivationCodeGenerator.isEncryptionResultValid(packageName)) {
                if (StringUtils.isEmpty(code)) {
                    throw new RuntimeException("非法访问");
                }

                if (!ActivationCodeGenerator.isActivationCodeValid(code)) {
                    throw new RuntimeException("激活码无效");
                }

                filePath = ActivationCodeGenerator.generateEncryptionResult(code);
                db.exec("delete from sys_auth");
                db.exec("insert into sys_auth(auth_) values ('" + filePath + "')");
            }
        } else {
            if (StringUtils.isEmpty(code)) {
                throw new RuntimeException("请先激活");
            }

            if (!ActivationCodeGenerator.isActivationCodeValid(code)) {
                throw new RuntimeException("激活码无效");
            }

            tabName = ActivationCodeGenerator.generateEncryptionResult(code);
            db.exec("delete from sys_auth");
            db.exec("insert into sys_auth(auth_) values ('" + tabName + "')");
        }
        */
        String  tabName = codeVo.getTabName();
        String  packageName = codeVo.getPackageName();
        String  filePath = codeVo.getFilePath();
        filePath = filePath.replaceAll("\\\\", "/");
        String frontPath = codeVo.getFrontPath();
        frontPath = frontPath.replaceAll("\\\\", "/");
        String ftlPath = System.getProperty("user.dir") + "/src/main/resources/template/";
        String author = codeVo.getAuthor();
        List<String> genList = codeVo.getGenList();

        try {
            DbUtil dbUtil = DbUtil.n(connectionURL, username, password, driverName);

            try {
                dbUtil.exec("ALTER TABLE " + tabName + " ADD COLUMN  create_time timestamp NOT NULL COMMENT '创建时间'");
            } catch (Exception var54) {
            }

            try {
                dbUtil.exec("ALTER TABLE " + tabName + " ADD COLUMN  update_time timestamp NOT NULL COMMENT '修改时间'");
            } catch (Exception var53) {
            }

            try {
                dbUtil.exec("ALTER TABLE " + tabName + " ADD COLUMN  is_deleted tinyint NOT NULL");
            } catch (Exception var52) {
            }

            try {
                dbUtil.exec("ALTER TABLE " + tabName + " ADD COLUMN   version bigint NOT NULL");
            } catch (Exception var51) {
            }

            String tableType = dbUtil.getTableType(tabName);
            codeVo.setGenType(tableType);
            Map<String, Object> map = dbUtil.getTableInfo(tabName, codeVo.getGenType());
            map.put("isGenDept", "false");
            map.put("genType", codeVo.getGenType());
            map.put("isGenLeftDept", codeVo.getIsGenLeftDept());
            map.put("isLazyDept", codeVo.getIsLazyDept());
            map.put("isGenDeptMul", "false");
            map.put("packageName", packageName);
            String packageSub = "";
            String[] parr = packageName.split("\\.");
            if (parr.length < 3) {
                throw new RuntimeException("生成包路径必须2层以上");
            } else {
                map.put("packageSub", parr[2]);
                map.put("author", author);
                map.put("createTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
                String path = packageName.replaceAll("\\.", "/");
                List<CodeListVo> rules = codeVo.getMulList();
                if (rules != null && rules.size() > 0) {
                    List<TableVo> properties = (List)map.get("data");
                    List<String> dataTmp = new ArrayList();
                    List<String> dataTmp2 = new ArrayList();

                    String data;
                    label271:
                    for(Iterator var25 = rules.iterator(); var25.hasNext(); dataTmp.add(data)) {
                        CodeListVo r = (CodeListVo)var25.next();
                        String mod = r.getMod();
                        if (StringUtils.isEmpty(mod)) {
                            throw new RuntimeException("关联表信息\"输入方式\"必须选择");
                        }

                        String target = r.getTarget();
                        if (StringUtils.isEmpty(target)) {
                            throw new RuntimeException("关联表信息\"设置字段\"必须选择");
                        }

                        data = r.getData();
                        String isReapted = "false";
                        String isViewReapted = "false";
                        if (dataTmp.contains(data)) {
                            isReapted = "true";
                        }

                        if (dataTmp2.contains(data)) {
                            isViewReapted = "true";
                        }

                        String key = r.getKey();
                        if (StringUtils.isEmpty(key)) {
                            throw new RuntimeException("关联表信息\"对应值字段\"必须选择");
                        }

                        String value = r.getValue();
                        if (StringUtils.isEmpty(value)) {
                            throw new RuntimeException("关联表信息\"显示值字段\"必须选择");
                        }

                        String modRemark = r.getModRemark();
                        if (StringUtils.isEmpty(modRemark)) {
                            throw new RuntimeException("关联表信息\"说明\"必须填写");
                        }

                        String where = r.getWhere();
                        String pageDir = r.getPageDir();
                        if (StringUtils.isEmpty(pageDir)) {
                            throw new RuntimeException("关联表信息\"引入模块目录\"必须填写");
                        }

                        String isMul = r.getIsMul();
                        String mulTable = r.getMulTable();
                        String mulMainColum = r.getMulMainColum();
                        String mulSecColum = r.getMulSecColum();
                        if ("true".equals(isMul)) {
                            if (StringUtils.isEmpty(mulTable)) {
                                throw new RuntimeException("关联表信息\"中间表\"必须选择");
                            }

                            if (StringUtils.isEmpty(mulMainColum)) {
                                throw new RuntimeException("关联表信息\"主表对应字段名称\"必须选择");
                            }

                            if (StringUtils.isEmpty(mulSecColum)) {
                                throw new RuntimeException("关联表信息\"次表对应字段名称\"必须选择");
                            }
                        }

                        Map map2 = db.getTableInfo(data, (String)null);
                        map2.put("packageName", packageName);
                        map2.put("packageSub", packageName.split("\\.")[2]);
                        map2.put("author", author);
                        map2.put("createTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
                        map2.put("modelNameMv", map.get("modelName"));
                        map2.put("modelNameMv2", map.get("modelName2"));
                        map2.put("modRemark", modRemark);
                        map2.put("modTarget", target);
                        map2.put("modKey", key);
                        map2.put("modValue", value);
                        map2.put("modValuePro", Utils.lineToHump(value));
                        map2.put("pageDir", pageDir);
                        map2.put("modTabType", dbUtil.getTableType(data));
                        if (mulMainColum != null) {
                            map2.put("mulMainColum", Utils.lineToHump(mulMainColum));
                        }
                        List<TableVo> targetList = (List)map2.get("data");
                        Iterator var43 = properties.iterator();

                        while(true) {
                            TableVo v;
                            String randStr;
                            do {
                                if (!var43.hasNext()) {
                                    String vfName = "view.ftl";
                                    String vfPath = "View.vue";
                                    randStr = "viewDialog.ftl";
                                    String vsPath = "ViewDialog.vue";
                                    if ("false".equals(isReapted) && "pop".equals(mod) && !"dept_id".equals(target) && !"deptId".equals(target)) {
                                        String fName = null;
                                        String sName = null;
                                        String sPath = null;
                                        String fPath = null;
                                        if ("true".equals(isMul)) {
                                            fName = "select.ftl";
                                            sName = "selectDialog.ftl";
                                            fPath = "Select.vue";
                                            sPath = "SelectDialog.vue";
                                        } else {
                                            fName = "selectOne.ftl";
                                            sName = "selectOneDialog.ftl";
                                            fPath = "SelectOne.vue";
                                            sPath = "SelectOneDialog.vue";
                                        }

                                        if ("dept_id".equals(mulSecColum)) {
                                            map.put("isGenDept", "true");
                                            map.put("isGenDeptMul", "true");
                                        }

                                        if (genList.contains("vue")) {
                                            Freemarker.printFile(fName, map2, "views/pages/" + map.get("packageSub") + "/" + map.get("modelName2") + map2.get("modelName") + fPath, frontPath, ftlPath);
                                            Freemarker.printFile(sName, map2, "views/pages/" + map.get("packageSub") + "/" + map.get("modelName2") + map2.get("modelName") + sPath, frontPath, ftlPath);
                                        }
                                    } else if (("pop".equals(mod) || "select".equals(mod)) && ("dept_id".equals(target) || "deptId".equals(target))) {
                                        map.put("isGenDept", "true");
                                        map.put("isGenDeptMul", "false");
                                    }

                                    if ("false".equals(isViewReapted) && "true".equals(isMul)) {
                                        dataTmp2.add(data);
                                        if (!"sys_dic_item".equals(data) && genList.contains("vue")) {
                                            Freemarker.printFile(vfName, map2, "views/pages/" + map.get("packageSub") + "/" + map.get("modelName2") + map2.get("modelName") + vfPath, frontPath, ftlPath);
                                            Freemarker.printFile(randStr, map2, "views/pages/" + map.get("packageSub") + "/" + map.get("modelName2") + map2.get("modelName") + vsPath, frontPath, ftlPath);
                                        }
                                    }
                                    continue label271;
                                }

                                v = (TableVo)var43.next();
                            } while(!v.getColumeName().equals(target));

                            v.setMod(mod);
                            v.setModModule("" + map2.get("modelName"));
                            v.setModModule2("" + map2.get("modelName2"));
                            v.setModColKey(key);
                            v.setModColValue(value);
                            v.setModKey(Utils.lineToHump(key));
                            v.setModValue(Utils.lineToHump(value));
                            v.setModRemark(modRemark);
                            v.setModTable(data);
                            v.setModModuleType(dbUtil.getTableType(data));
                            System.out.println("table=" + data + ",genType=" + v.getModModuleType());
                            v.setIsMul(isMul != null ? isMul : "false");
                            v.setMulTableData(mulTable);
                            v.setMulTable2(Utils.lineToHump(mulTable));
                            v.setMulTable(Utils.toFirstUpper(v.getMulTable2()));
                            v.setMulMainColumData(mulMainColum);
                            v.setMulMainColum(Utils.lineToHump(mulMainColum));
                            v.setMulUpperMainColum(Utils.toFirstUpper(v.getMulMainColum()));
                            v.setMulSecColumData(mulSecColum);
                            v.setMulSecColum(Utils.lineToHump(mulSecColum));
                            v.setMulUpperSecColum(Utils.toFirstUpper(v.getMulSecColum()));
                            v.setIsReapted(isReapted);
                            v.setIsViewReapted(isViewReapted);
                            Iterator var45 = targetList.iterator();

                            while(var45.hasNext()) {
                                TableVo v2 = (TableVo)var45.next();
                                if (v2.getColumeName().equals(value)) {
                                    v.setModValueReamrk(v2.getRemarks());
                                    v.setModValueAlias(Utils.lineToHump(v.getColumeName().split("_")[0] + "_name"));
                                }

                                if (v2.getColumeName().toLowerCase().indexOf("_id") > -1) {
                                    v2.setMod("hello");
                                    v2.setModValueReamrk(v2.getRemarks().toLowerCase().replace("id", "") + "名称");
                                    v2.setModValueAlias(Utils.lineToHump(v2.getColumeName().split("_")[0] + "_name"));
                                }
                            }

                            if (pageDir != null && !"".equals(pageDir)) {
                                v.setPageDir(pageDir);
                            } else {
                                v.setPageDir("" + map2.get("packageSub"));
                            }

                            if (!StringUtils.isEmpty(where)) {
                                where = "" + JsonUtil.convertUnderlineToCamelCase(where);
                            }

                            v.setWhere(where);
                            if (where != null && !"".equals(where) && !"null".equals(where)) {
                                v.setWhereName("X" + where.replace(":", "").replace("{", "").replace("}", "").replace("\"", "").trim());
                            } else {
                                randStr = "X" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
                                v.setWhereName(randStr);
                            }

                            if ("id".equals(target) && "true".equals(isMul)) {
                                v.getMulTableVoList().add(new TableVo(v));
                            }
                        }
                    }
                }

                if (genList.contains("api")) {
                    Freemarker.printFile("api.ftl", map, "apis/" + map.get("packageSub") + "/" + map.get("modelName2") + ".js", frontPath, ftlPath);
                }

                if (genList.contains("vue")) {
                    Freemarker.printFile("list.ftl", map, "views/pages/" + map.get("packageSub") + "/" + map.get("modelName2") + ".vue", frontPath, ftlPath);
                }

                if (genList.contains("vo")) {
                    Freemarker.printFile("vo.ftl", map, "java/" + path + "/vo/" + map.get("modelName") + "QueryVo.java", filePath, ftlPath);
                }

                if (genList.contains("po")) {
                    Freemarker.printFile("po.ftl", map, "java/" + path + "/model/" + map.get("modelName") + ".java", filePath, ftlPath);
                }

                if (genList.contains("mapper")) {
                    Freemarker.printFile("mapper.ftl", map, "java/" + path + "/mapper/" + map.get("modelName") + "Mapper.java", filePath, ftlPath);
                }

                if (genList.contains("xml")) {
                    Freemarker.printFile("mapperXml.ftl", map, "resources/mapper/" + map.get("modelName") + "Mapper.xml", filePath, ftlPath);
                }

                if (genList.contains("service")) {
                    Freemarker.printFile("service.ftl", map, "java/" + path + "/service/" + map.get("modelName") + "Service.java", filePath, ftlPath);
                }

                if (genList.contains("impl")) {
                    Freemarker.printFile("service_impl.ftl", map, "java/" + path + "/service/impl/" + map.get("modelName") + "ServiceImpl.java", filePath, ftlPath);
                }

                if (genList.contains("controller")) {
                    Freemarker.printFile("controller.ftl", map, "java/" + path + "/controller/" + map.get("modelName") + "Controller.java", filePath, ftlPath);
                }

                if (genList.contains("main")) {
                    String mainName = packageName.substring(packageName.lastIndexOf(".") + 1).substring(0, 1).toUpperCase() + packageName.substring(packageName.lastIndexOf(".") + 1).substring(1);
                    map.put("mainName", mainName);
                    Freemarker.printFile("main.ftl", map, "java/" + path + "/" + mainName + "Application.java", filePath, ftlPath);
                }

            }
        } catch (RuntimeException var55) {
            throw var55;
        } catch (Exception var56) {
            var56.printStackTrace();
            throw new RuntimeException("代码生成失败，请检查配置信息是否填写正确");
        }
    }
}
