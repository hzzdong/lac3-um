import request from '@/utils/request'

export function loadAppMenuTree(data) {
  return request({
    url: '/api4umyw/face/Menu/loadAppMenuTree',
    method: 'post',
    data
  })
}

export function saveMenu(data) {
  return request({
    url: '/api4umyw/face/Menu/save',
    method: 'post',
    data
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/Menu/fetch',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: '/api4umyw/face/Menu/save',
    method: 'post',
    data
  })
}

export function deleteMenu(data) {
  return request({
    url: '/api4umyw/face/Menu/delete',
    method: 'post',
    data
  })
}

export function changeMenuStatus(data) {
  return request({
    url: '/api4umyw/face/Menu/changeStatus',
    method: 'post',
    data
  })
}

export function getMenuOperationPage(data) {
  return request({
    url: '/api4umyw/face/Operation/page',
    method: 'post',
    data
  })
}

export function saveOperation(data) {
  return request({
    url: '/api4umyw/face/Operation/save',
    method: 'post',
    data
  })
}

export function deleteOperation(data) {
  return request({
    url: '/api4umyw/face/Operation/delete',
    method: 'post',
    data
  })
}
