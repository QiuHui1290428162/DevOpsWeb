import request from '@/request'
const api_name = '/tasks/taskScheduledEmail'
export default {
listPage(page, limit, searchObj) {
    return request({
        url: `${api_name}/${page}/${limit}`,
        method: 'get',
        params: searchObj // url查询字符串或表单键值对
    })
},
getAllTaskScheduledEmail() {
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
save(data) {
    return request({
        url: `${api_name}/save`,
        method: 'post',
        data: data
    })
},
update(data) {
    return request({
        url: `${api_name}/update`,
        method: 'put',
        data: data
    })
},

remove(ids) {
    return request({
        url: `${api_name}/batchRemove`,
        method: "delete",
        data: ids
    })
},
}

export const getById = (id) => {
    return request({
        url: `${api_name}/get/${id}`,
        method: 'get'
    })
};

export const sendMail = (id) => {
    return request({
        url: `${api_name}/sendMail`,
        method: 'post',
        data: id
    })
};
