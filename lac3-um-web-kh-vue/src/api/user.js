import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/umkh/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/umkh/face/KhUser/fetchLoginUser',
    method: 'post'
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
    url: '/umkh/SelfKhUser/page',
    method: 'post',
    data
  })
}

export function createUser(data) {
  return request({
    url: '/umkh/SelfKhUser/save',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/umkh/SelfKhUser/save',
    method: 'post',
    data
  })
}

export function deleteUser(params) {
  return request({
    url: '/umkh/SelfKhUser/delete',
    method: 'post',
    params
  })
}
