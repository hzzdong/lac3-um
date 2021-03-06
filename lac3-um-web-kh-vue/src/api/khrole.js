import request from '@/utils/request'

export function findCompanyRolePage(data) {
  return request({
    url: '/api4umkh/face/KhRole/findCompanyRolePage',
    method: 'post',
    data
  })
}

export function findCompanyAllRolePage(data) {
  return request({
    url: '/api4umkh/face/KhRole/findCompanyAllRolePage',
    method: 'post',
    data
  })
}

export function findCompanyRoles(data) {
  return request({
    url: '/api4umkh/face/KhRole/findCompanyRoles',
    method: 'post',
    data
  })
}

export function findJzCompanyRoles(data) {
  return request({
    url: '/api4umkh/face/KhRole/findJzCompanyRoles',
    method: 'post',
    data
  })
}

export function findUserRoleIds(data) {
  return request({
    url: '/api4umkh/face/KhRole/findUserRoleIds',
    method: 'post',
    data
  })
}

export function findJzUserRoleIds(data) {
  return request({
    url: '/api4umkh/face/KhRole/findJzUserRoleIds',
    method: 'post',
    data
  })
}

export function findUserRoles(data) {
  return request({
    url: '/api4umkh/face/KhRole/findUserRoles',
    method: 'post',
    data
  })
}

export function findJzUserRoles(data) {
  return request({
    url: '/api4umkh/face/KhRole/findJzUserRoles',
    method: 'post',
    data
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umkh/face/KhRole/fetch',
    method: 'post',
    data
  })
}

export function createKhRole(data) {
  return request({
    url: '/api4umkh/face/KhRole/save',
    method: 'post',
    data
  })
}

export function updateKhRole(data) {
  return request({
    url: '/api4umkh/face/KhRole/save',
    method: 'post',
    data
  })
}

export function deleteKhRole(data) {
  return request({
    url: '/api4umkh/face/KhRole/delete',
    method: 'post',
    data
  })
}

export function changeRoleStatus(data) {
  return request({
    url: '/api4umkh/face/KhRole/changeStatus',
    method: 'post',
    data
  })
}

export function addRoleUsers(data) {
  return request({
    url: '/api4umkh/face/KhRole/addUsers',
    method: 'post',
    data
  })
}

export function removeRoleUser(data) {
  return request({
    url: '/api4umkh/face/KhRole/removeUser',
    method: 'post',
    data
  })
}

export function addRoleApps(data) {
  return request({
    url: '/api4umkh/face/KhRole/addApps',
    method: 'post',
    data
  })
}

export function removeRoleApp(data) {
  return request({
    url: '/api4umkh/face/KhRole/removeApp',
    method: 'post',
    data
  })
}

export function getPermedMenuTree(data) {
  return request({
    url: '/api4umkh/face/KhRole/getPermedMenuTree',
    method: 'post',
    data
  })
}

export function saveRoleAppMenuPerm(data) {
  return request({
    url: '/api4umkh/face/KhRole/saveRoleAppMenuPerm',
    method: 'post',
    data
  })
}

export function getPermedOrgTree(data) {
  return request({
    url: '/api4umkh/face/KhRole/getPermedOrgTree',
    method: 'post',
    data
  })
}

export function saveRoleAppOrgPerm(data) {
  return request({
    url: '/api4umkh/face/KhRole/saveRoleAppOrgPerm',
    method: 'post',
    data
  })
}

export function getPermedAreaTree(data) {
  return request({
    url: '/api4umkh/face/KhRole/getPermedAreaTree',
    method: 'post',
    data
  })
}

export function saveRoleAppAreaPerm(data) {
  return request({
    url: '/api4umkh/face/KhRole/saveRoleAppAreaPerm',
    method: 'post',
    data
  })
}
