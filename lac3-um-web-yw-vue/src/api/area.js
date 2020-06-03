import request from '@/utils/request'

export function loadTree4MyCompany(data) {
  return request({
    url: '/api4umyw/face/Area/loadTree4MyCompany',
    method: 'post',
    data
  })
}

export function loadLevel1Tree(data) {
  return request({
    url: '/api4umyw/face/Area/loadLevel1Tree',
    method: 'post',
    data
  })
}

export function loadTreeNodes(data) {
  return request({
    url: '/api4umyw/face/Area/loadTreeNodes',
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
