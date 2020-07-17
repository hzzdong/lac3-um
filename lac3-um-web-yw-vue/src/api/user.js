import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api4umyw/login',
    method: 'post',
    data
  })
}

/**
 * 返回sessionUser，包含权限信息
 */
export function getSessionUserInfo() {
  return request({
    url: '/api4umyw/face/YwUser/fetchLoginUser',
    method: 'post'
  })
}

/**
 * 返回YwUser
 */
export function getLoginUser() {
  return request({
    url: '/api4umyw/face/YwUser/getLoginUser',
    method: 'post'
  })
}

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/YwUser/fetch',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/api4umyw/logout',
    method: 'post'
  })
}

export function getPage(data) {
  return request({
    url: '/api4umyw/face/YwUser/page',
    method: 'post',
    data
  })
}

export function createUser(data) {
  return request({
    url: '/api4umyw/face/YwUser/save',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/api4umyw/face/YwUser/save',
    method: 'post',
    data
  })
}

export function updateJzUser(data) {
  return request({
    url: '/api4umyw/face/YwUser/updateJz',
    method: 'post',
    data
  })
}

export function updateMe(data) {
  return request({
    url: '/api4umyw/face/YwUser/updateMe',
    method: 'post',
    data
  })
}

export function updateHeaderImage(data) {
  return request({
    url: '/api4umyw/face/YwUser/updateHeaderImage',
    method: 'post',
    data
  })
}

export function updatePass(data) {
  return request({
    url: '/api4umyw/face/YwUser/updatePass',
    method: 'post',
    data
  })
}

export function deleteUser(data) {
  return request({
    url: '/api4umyw/face/YwUser/delete',
    method: 'post',
    data
  })
}

export function changeUserStatus(data) {
  return request({
    url: '/api4umyw/face/YwUser/changeStatus',
    method: 'post',
    data
  })
}

export function getPage4Role(data) {
  return request({
    url: '/api4umyw/face/YwUser/page4Role',
    method: 'post',
    data
  })
}

export function findRoleUsers(data) {
  return request({
    url: '/api4umyw/face/YwUser/page4Role',
    method: 'post',
    data
  })
}

export function findUnRoleUsers(data) {
  return request({
    url: '/api4umyw/face/YwUser/page4UnRole',
    method: 'post',
    data
  })
}

export function getJzPage(data) {
  return request({
    url: '/api4umyw/face/YwUser/page4PartTimeJob',
    method: 'post',
    data
  })
}

export function getJzPageOfUser(data) {
  return request({
    url: '/api4umyw/face/YwUser/page4PartTimeJobOfUser',
    method: 'post',
    data
  })
}

export function addPartTimeJob(data) {
  return request({
    url: '/api4umyw/face/YwUser/addPartTimeJob',
    method: 'post',
    data
  })
}

export function removePartTimeJob(data) {
  return request({
    url: '/api4umyw/face/YwUser/removePartTimeJob',
    method: 'post',
    data
  })
}
