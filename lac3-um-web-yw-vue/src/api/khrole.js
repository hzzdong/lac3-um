import request from '@/utils/request'

export function findCompanyRolePage(data) {
  return request({
    url: '/api4umyw/face/KhRole/findCompanyRolePage',
    method: 'post',
    data
  })
}

export function findCompanyAllRolePage(data) {
  return request({
    url: '/api4umyw/face/KhRole/findCompanyAllRolePage',
    method: 'post',
    data
  })
}

export function findCompanyRoles(data) {
  return request({
    url: '/api4umyw/face/KhRole/findCompanyRoles',
    method: 'post',
    data
  })
}

export function findUserRoleIds(data) {
  return request({
    url: '/api4umyw/face/KhRole/findUserRoleIds',
    method: 'post',
    data
  })
}

export function findUserRoles(data) {
  return request({
    url: '/api4umyw/face/KhRole/findUserRoles',
    method: 'post',
    data
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/KhRole/fetch',
    method: 'post',
    data
  })
}

export function createKhRole(data) {
  return request({
    url: '/api4umyw/face/KhRole/save',
    method: 'post',
    data
  })
}

export function updateKhRole(data) {
  return request({
    url: '/api4umyw/face/KhRole/save',
    method: 'post',
    data
  })
}

export function deleteKhRole(data) {
  return request({
    url: '/api4umyw/face/KhRole/delete',
    method: 'post',
    data
  })
}

export function addRoleUsers(data) {
  return request({
    url: '/api4umyw/face/KhRole/addUsers',
    method: 'post',
    data
  })
}

export function removeRoleUser(data) {
  return request({
    url: '/api4umyw/face/KhRole/removeUser',
    method: 'post',
    data
  })
}

export function addRoleApps(data) {
  return request({
    url: '/api4umyw/face/KhRole/addApps',
    method: 'post',
    data
  })
}

export function removeRoleApp(data) {
  return request({
    url: '/api4umyw/face/KhRole/removeApp',
    method: 'post',
    data
  })
}

export function getPermedMenuTree(data) {
  return request({
    url: '/api4umyw/face/KhRole/getPermedMenuTree',
    method: 'post',
    data
  })
}

export function saveRoleAppMenuPerm(data) {
  return request({
    url: '/api4umyw/face/KhRole/saveRoleAppMenuPerm',
    method: 'post',
    data
  })
}

export function getPermedOrgTree(data) {
  return request({
    url: '/api4umyw/face/KhRole/getPermedOrgTree',
    method: 'post',
    data
  })
}

export function saveRoleAppOrgPerm(data) {
  return request({
    url: '/api4umyw/face/KhRole/saveRoleAppOrgPerm',
    method: 'post',
    data
  })
}

export function getPermedAreaTree(data) {
  return request({
    url: '/api4umyw/face/KhRole/getPermedAreaTree',
    method: 'post',
    data
  })
}

export function saveRoleAppAreaPerm(data) {
  return request({
    url: '/api4umyw/face/KhRole/saveRoleAppAreaPerm',
    method: 'post',
    data
  })
}
