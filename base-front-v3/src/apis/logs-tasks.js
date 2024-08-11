import request from '@/request'
const api_name = '/system/sysTaskLog'
export default {
listPage(page, limit, searchObj) {
    return request({
        url: `${api_name}/${page}/${limit}`,
        method: 'get',
        params: searchObj // url查询字符串或表单键值对
    })
},
getAllSysTaskLog() {
    return request({
        url: `${api_name}/findAll`,
        method: 'get'
    })
},
getById(id) {
    return request({
        url: `${api_name}/get/${id}`,
        method: 'get'
    })
},





}
