import request from '@/request'
const api_name = '/code/generator'
export default {
    save(data) {
        return request({
            url: `${api_name}/save`,
            method: 'post',
            data: data
        })
    }
}
