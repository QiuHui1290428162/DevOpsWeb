import request from '@/request'
const api_name = '/${packageSub}/${modelName2}'
export default {
listPage(page, limit, searchObj) {
    return request({
        url: `${r'${api_name}'}/${r'${page}'}/${r'${limit}'}`,
        method: 'get',
        params: searchObj // url查询字符串或表单键值对
    })
},
getAll${modelName}() {
    return request({
        url: `${r'${api_name}'}/findAll`,
        method: 'get'
    })
},
getById(id) {
    return request({
        url: `${r'${api_name}'}/get/${r'${id}'}`,
        method: 'get'
    })
},
save(data) {
    return request({
        url: `${r'${api_name}'}/save`,
        method: 'post',
        data: data
    })
},
update(data) {
    return request({
        url: `${r'${api_name}'}/update`,
        method: 'put',
        data: data
    })
},

remove(ids) {
    return request({
        url: `${r'${api_name}'}/batchRemove`,
        method: "delete",
        data: ids
    })
},

}
