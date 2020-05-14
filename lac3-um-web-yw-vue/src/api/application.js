import request from '@/utils/request'

export function findAppPage4KhRole(data) {
  return request({
    url: '/api4umyw/face/Application/page4KhRole',
    method: 'post',
    data
  })
}

export function findAppPage4UnKhRole(data) {
  return request({
    url: '/api4umyw/face/Application/page4Select',
    method: 'post',
    data
  })
}

export function findAppPage4Company(data) {
  return request({
    url: '/api4umyw/face/Application/page4YwCompany',
    method: 'post',
    data
  })
}

export function findAppPage4UnCompany(data) {
  return request({
    url: '/api4umyw/face/Application/page4YwCompany4Select',
    method: 'post',
    data
  })
}

/* YW */
export function findAppPage(data) {
  return request({
    url: '/api4umyw/face/Application/page',
    method: 'post',
    data
  })
}

export function saveApp(data) {
  return request({
    url: '/api4umyw/face/Application/save',
    method: 'post',
    data
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/Application/fetch',
    method: 'post',
    data
  })
}

export function updateApp(data) {
  return request({
    url: '/api4umyw/face/Application/save',
    method: 'post',
    data
  })
}

export function updateAppInterface(data) {
  return request({
    url: '/api4umyw/face/Application/saveInter',
    method: 'post',
    data
  })
}

export function updateAppMappingInterface(data) {
  return request({
    url: '/api4umyw/face/Application/saveMappingInter',
    method: 'post',
    data
  })
}

export function deleteApp(data) {
  return request({
    url: '/api4umyw/face/Application/delete',
    method: 'post',
    data
  })
}

export function changeAppStatus(data) {
  return request({
    url: '/api4umyw/face/Application/changeStatus',
    method: 'post',
    data
  })
}
