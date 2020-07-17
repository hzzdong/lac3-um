import request from '@/utils/request'

export function findCompanyRolePage(data) {
  return request({
    url: '/api4umyw/face/YwRole/findCompanyRolePage',
    method: 'post',
    data
  })
}

export function findCompanyAllRolePage(data) {
  return request({
    url: '/api4umyw/face/YwRole/findCompanyAllRolePage',
    method: 'post',
    data
  })
}

export function findCompanyRoles(data) {
  return request({
    url: '/api4umyw/face/YwRole/findCompanyRoles',
    method: 'post',
    data
  })
}

export function findJzCompanyRoles(data) {
  return request({
    url: '/api4umyw/face/YwRole/findJzCompanyRoles',
    method: 'post',
    data
  })
}

export function findUserRoleIds(data) {
  return request({
    url: '/api4umyw/face/YwRole/findUserRoleIds',
    method: 'post',
    data
  })
}

export function findJzUserRoleIds(data) {
  return request({
    url: '/api4umyw/face/YwRole/findJzUserRoleIds',
    method: 'post',
    data
  })
}

export function findUserRoles(data) {
  return request({
    url: '/api4umyw/face/YwRole/findUserRoles',
    method: 'post',
    data
  })
}

export function findJzUserRoles(data) {
  return request({
    url: '/api4umyw/face/YwRole/findJzUserRoles',
    method: 'post',
    data
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/YwRole/fetch',
    method: 'post',
    data
  })
}

export function createYwRole(data) {
  return request({
    url: '/api4umyw/face/YwRole/save',
    method: 'post',
    data
  })
}

export function updateYwRole(data) {
  return request({
    url: '/api4umyw/face/YwRole/save',
    method: 'post',
    data
  })
}

export function deleteYwRole(data) {
  return request({
    url: '/api4umyw/face/YwRole/delete',
    method: 'post',
    data
  })
}

export function changeRoleStatus(data) {
  return request({
    url: '/api4umyw/face/YwRole/changeStatus',
    method: 'post',
    data
  })
}

export function addRoleUsers(data) {
  return request({
    url: '/api4umyw/face/YwRole/addUsers',
    method: 'post',
    data
  })
}

export function removeRoleUser(data) {
  return request({
    url: '/api4umyw/face/YwRole/removeUser',
    method: 'post',
    data
  })
}

export function addRoleApps(data) {
  return request({
    url: '/api4umyw/face/YwRole/addApps',
    method: 'post',
    data
  })
}

export function removeRoleApp(data) {
  return request({
    url: '/api4umyw/face/YwRole/removeApp',
    method: 'post',
    data
  })
}

export function getPermedMenuTree(data) {
  return request({
    url: '/api4umyw/face/YwRole/getPermedMenuTree',
    method: 'post',
    data
  })
}

export function saveRoleAppMenuPerm(data) {
  return request({
    url: '/api4umyw/face/YwRole/saveRoleAppMenuPerm',
    method: 'post',
    data
  })
}

export function getPermedOrgTree(data) {
  return request({
    url: '/api4umyw/face/YwRole/getPermedOrgTree',
    method: 'post',
    data
  })
}

export function saveRoleAppOrgPerm(data) {
  return request({
    url: '/api4umyw/face/YwRole/saveRoleAppOrgPerm',
    method: 'post',
    data
  })
}

export function getPermedAreaTree(data) {
  return request({
    url: '/api4umyw/face/YwRole/getPermedAreaTree',
    method: 'post',
    data
  })
}

export function saveRoleAppAreaPerm(data) {
  return request({
    url: '/api4umyw/face/YwRole/saveRoleAppAreaPerm',
    method: 'post',
    data
  })
}
