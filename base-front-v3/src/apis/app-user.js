/*
 * 用户管理模块
 */
import request from "../request";
const api_name = '/admin/system/sysUser'

// 分页查询
export const listPage = (page, limit, searchObj) => {
  return request({
    url: `${api_name}/${page}/${limit}`,
    method: "get",
    params: searchObj
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

export const getById = (id) => {
  return request({
    url: `${api_name}/getUser/${id}`,
    method: "get",
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
