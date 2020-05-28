import request from '@/utils/request'

export function loadAreaTree(data) {
  return request({
    url: '/api4umkh/face/Area/loadTree4MyCompany',
    method: 'post',
    data
  })
}

export function loadTree(data) {
  return request({
    url: '/api4umkh/face/Area/loadTree',
    method: 'post',
    data
  })
}
