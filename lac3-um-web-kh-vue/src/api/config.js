import request from '@/utils/request'

export function findCompanyConfigs(data) {
  return request({
    url: '/umkh/face/KhSystemConfig/find',
    method: 'post',
    data
  })
}

export function saveCompanyConfig(data) {
  return request({
    url: '/umkh/face/KhSystemConfig/change',
    method: 'post',
    data
  })
}
