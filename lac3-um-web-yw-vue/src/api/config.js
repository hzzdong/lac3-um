import request from '@/utils/request'

export function findCompanyConfigs(data) {
  return request({
    url: '/api4umyw/face/YwSystemConfig/find',
    method: 'post',
    data
  })
}

export function saveCompanyConfig(data) {
  return request({
    url: '/api4umyw/face/YwSystemConfig/change',
    method: 'post',
    data
  })
}
