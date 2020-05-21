import request from '@/utils/request'

export function findCompanyRolePage(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/findCompanyRolePage',
    method: 'post',
    data
  })
}

export function findCompanyAllRolePage(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/findCompanyAllRolePage',
    method: 'post',
    data
  })
}

export function findCompanyRoles(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/findCompanyRoles',
    method: 'post',
    data
  })
}

export function findUserRoleIds(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/findUserRoleIds',
    method: 'post',
    data
  })
}

export function findUserRoles(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/findUserRoles',
    method: 'post',
    data
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/fetch',
    method: 'post',
    data
  })
}

export function createSysYwRole(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/save',
    method: 'post',
    data
  })
}

export function updateSysYwRole(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/save',
    method: 'post',
    data
  })
}

export function deleteSysYwRole(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/delete',
    method: 'post',
    data
  })
}

export function changeRoleStatus(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/changeStatus',
    method: 'post',
    data
  })
}

export function addRoleUsers(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/addUsers',
    method: 'post',
    data
  })
}

export function removeRoleUser(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/removeUser',
    method: 'post',
    data
  })
}

export function addRoleApps(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/addApps',
    method: 'post',
    data
  })
}

export function removeRoleApp(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/removeApp',
    method: 'post',
    data
  })
}

export function getPermedMenuTree(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/getPermedMenuTree',
    method: 'post',
    data
  })
}

export function saveRoleAppMenuPerm(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/saveRoleAppMenuPerm',
    method: 'post',
    data
  })
}

export function getPermedOrgTree(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/getPermedOrgTree',
    method: 'post',
    data
  })
}

export function saveRoleAppOrgPerm(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/saveRoleAppOrgPerm',
    method: 'post',
    data
  })
}

export function getPermedAreaTree(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/getPermedAreaTree',
    method: 'post',
    data
  })
}

export function saveRoleAppAreaPerm(data) {
  return request({
    url: '/api4umyw/face/SysYwRole/saveRoleAppAreaPerm',
    method: 'post',
    data
  })
}
