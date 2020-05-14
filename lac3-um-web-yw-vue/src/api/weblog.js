import request from '@/utils/request'

export function findWebLogPage(data) {
  return request({
    url: '/api4umyw/face/WebLog/page',
    method: 'post',
    data
  })
}

export function getWebLog(data) {
  return request({
    url: '/api4umyw/face/WebLog/fetch',
    method: 'post',
    data
  })
}
