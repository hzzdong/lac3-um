import request from '@/utils/request'

export function loadAreaTree(data) {
  return request({
    url: '/umkh/face/Area/loadTree4MyCompany',
    method: 'post',
    data
  })
}
