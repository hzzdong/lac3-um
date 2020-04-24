import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/umkh/login',
    method: 'post',
    data
  })
}

/**
 * 返回sessionUser，包含权限信息
 * @param {*} token
 */
export function getInfo(token) {
  return request({
    url: '/umkh/face/KhUser/fetchLoginUser',
    method: 'post'
  })
}

/**
 * 返回KhUser
 */
export function getLoginUser() {
  return request({
    url: '/umkh/face/KhUser/getLoginUser',
    method: 'post'
  })
}

export function fetchById(data) {
  return request({
    url: '/umkh/face/KhUser/fetch',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/umkh/logout',
    method: 'post'
  })
}

export function getPage(data) {
  return request({
    url: '/umkh/face/KhUser/page',
    method: 'post',
    data
  })
}

export function createUser(data) {
  return request({
    url: '/umkh/face/KhUser/save',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/umkh/face/KhUser/save',
    method: 'post',
    data
  })
}

export function updateMe(data) {
  return request({
    url: '/umkh/face/KhUser/updateMe',
    method: 'post',
    data
  })
}

export function updatePass(data) {
  return request({
    url: '/umkh/face/KhUser/updatePass',
    method: 'post',
    data
  })
}

export function deleteUser(data) {
  return request({
    url: '/umkh/face/KhUser/delete',
    method: 'post',
    data
  })
}

export function getPage4Role(data) {
  return request({
    url: '/umkh/face/KhUser/page4Role',
    method: 'post',
    data
  })
}

export function findRoleUsers(data) {
  return request({
    url: '/umkh/face/KhUser/page4Role',
    method: 'post',
    data
  })
}

export function findUnRoleUsers(data) {
  return request({
    url: '/umkh/face/KhUser/page4Select',
    method: 'post',
    data
  })
}
