import request from '@/utils/request'

export function loadAreaTree() {
  return request({
    url: '/umkh/area/loadTree4MyCompany',
    method: 'get'
  })
}
