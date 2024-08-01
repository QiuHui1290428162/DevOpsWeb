import request from "@/request";
const api_name = '/admin/system/sysDept'
/*
 * 机构管理模块
 */
// 查询机构树
export const listTree = (page,limit,searchObj) => {
  return request({
    url: `${api_name}/findNodes`,
    method: "get",
    params: searchObj
  });
};

export const getDeptById = (id) => {
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
    method: "put",
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
