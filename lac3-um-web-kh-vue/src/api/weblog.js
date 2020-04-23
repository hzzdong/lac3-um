import request from '@/utils/request'

export function findWebLogPage(data) {
  return request({
    url: '/umkh/face/WebLog/page',
    method: 'post',
    data
  })
}

export function getWebLog(data) {
  return request({
    url: '/umkh/face/WebLog/fetch',
    method: 'post',
    data
  })
}
