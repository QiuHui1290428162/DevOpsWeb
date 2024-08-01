import request from "@/request";
const api_name = '/log/sysOperLog'
// 分页查询
export const listPage = (page, limit, searchObj) => {
  return request({
    url: `${api_name}/${page}/${limit}`,
    method: "get",
    params: searchObj
  });
};
