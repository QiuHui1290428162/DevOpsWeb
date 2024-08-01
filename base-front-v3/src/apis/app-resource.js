import request from "@/request";
const api_name = '/admin/system/sysMenu'
/*
 * 菜单管理模块
 */

// 查找导航菜单树
export const listTree = () => {
  return request({
    url: `${api_name}/findNodes`,
    method: "get",
  });
};

export const getResourceByRoleId = (roleId) => {
  return request({
    url: `${api_name}/toAssign/${roleId}`,
    method: "get",
  });
};

// 查询
export const listTreeParents = (notId) => {
  return request({
    url: `${api_name}/findDir/${notId}`,
    method: "get"
  });
};

export const listMenuParents = () => {
  return request({
    url: `${api_name}/findMenu`,
    method: "get"
  });
};

export const getById = (id) => {
  return request({
    url: `${api_name}/get/${id}`,
    method: "get",
  });
};

// 新增
export const save = (data) => {
  return request({
    url: `${api_name}/save`,
    method: "post",
    data,
  });
};
// 编辑
export const update = (data) => {
  return request({
    url: `${api_name}/update`,
    method: "post",
    data,
  });
};
// 删除
export const remove = (data) => {
  return request({
    url: `${api_name}/batchRemove`,
    method: "delete",
    data,
  });
};
