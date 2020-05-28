import request from '@/utils/request'

export function getCompanyTree(data) {
  return request({
    url: '/api4umyw/face/KhCompany/loadCompanyTree',
    method: 'post',
    data
  })
}
