import request from '@/utils/request'

export function findCompanyRolePage(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/findCompanyRolePage',
    method: 'post',
    data
  })
}

export function findCompanyAllRolePage(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/findCompanyAllRolePage',
    method: 'post',
    data
  })
}

export function findCompanyRoles(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/findCompanyRoles',
    method: 'post',
    data
  })
}

export function findUserRoleIds(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/findUserRoleIds',
    method: 'post',
    data
  })
}

export function findUserRoles(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/findUserRoles',
    method: 'post',
    data
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/fetch',
    method: 'post',
    data
  })
}

export function createSysKhRole(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/save',
    method: 'post',
    data
  })
}

export function updateSysKhRole(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/save',
    method: 'post',
    data
  })
}

export function deleteSysKhRole(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/delete',
    method: 'post',
    data
  })
}

export function changeRoleStatus(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/changeStatus',
    method: 'post',
    data
  })
}

export function addRoleUsers(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/addUsers',
    method: 'post',
    data
  })
}

export function removeRoleUser(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/removeUser',
    method: 'post',
    data
  })
}

export function addRoleApps(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/addApps',
    method: 'post',
    data
  })
}

export function removeRoleApp(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/removeApp',
    method: 'post',
    data
  })
}

export function getPermedMenuTree4SysKhRole(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/getPermedMenuTree4SysKhRole',
    method: 'post',
    data
  })
}

export function saveRoleAppMenuPerm(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/saveRoleAppMenuPerm',
    method: 'post',
    data
  })
}

export function getPermedOrgTree(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/getPermedOrgTree',
    method: 'post',
    data
  })
}

export function saveRoleAppOrgPerm(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/saveRoleAppOrgPerm',
    method: 'post',
    data
  })
}

export function getPermedAreaTree(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/getPermedAreaTree',
    method: 'post',
    data
  })
}

export function saveRoleAppAreaPerm(data) {
  return request({
    url: '/api4umyw/face/SysKhRole/saveRoleAppAreaPerm',
    method: 'post',
    data
  })
}
