import request from '@/utils/request'

export function loadTree4MyCompany(data) {
  return request({
    url: '/api4umyw/face/Area/loadTree4MyCompany',
    method: 'post',
    data
  })
}

export function loadTree(data) {
  return request({
    url: '/api4umyw/face/Area/loadTree',
    method: 'post',
    data
  })
}

export function saveArea(data) {
  return request({
    url: '/api4umyw/face/Area/save',
    method: 'post',
    data
  })
}

export function deleteArea(data) {
  return request({
    url: '/api4umyw/face/Area/delete',
    method: 'post',
    data
  })
}
