import request from '@/utils/request'

export function findAppPage4YwRole(data) {
  return request({
    url: '/api4umyw/face/Application/page4YwRole',
    method: 'post',
    data
  })
}

export function findAppPage4UnYwRole(data) {
  return request({
    url: '/api4umyw/face/Application/page4YwRole4Select',
    method: 'post',
    data
  })
}

export function findAppPage4YwCompany(data) {
  return request({
    url: '/api4umyw/face/Application/page4YwCompany',
    method: 'post',
    data
  })
}

export function findAppPage4UnYwCompany(data) {
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

export function saveAppIco(data) {
  return request({
    url: '/api4umyw/face/Application/updateIco',
    method: 'post',
    data
  })
}

/** 以下为 KH */
export function findAppPage4SysKhRole(data) {
  return request({
    url: '/api4umyw/face/Application/page4SysKhRole',
    method: 'post',
    data
  })
}

export function findAppPage4UnSysKhRole(data) {
  return request({
    url: '/api4umyw/face/Application/page4SysKhRole4Select',
    method: 'post',
    data
  })
}

export function findAppPage4KhCompany(data) {
  return request({
    url: '/api4umyw/face/Application/page4KhCompany',
    method: 'post',
    data
  })
}

export function findAppPage4UnKhCompany(data) {
  return request({
    url: '/api4umyw/face/Application/page4KhCompany4Select',
    method: 'post',
    data
  })
}
