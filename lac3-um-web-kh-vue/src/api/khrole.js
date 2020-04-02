import request from '@/utils/request'

export function getPage(data) {
  return request({
    url: '/umkh/SelfKhRole/page',
    method: 'post',
    data
  })
}

export function createKhRole(data) {
  return request({
    url: '/umkh/SelfKhRole/save',
    method: 'post',
    data
  })
}

export function updateKhRole(data) {
  return request({
    url: '/umkh/SelfKhRole/save',
    method: 'post',
    data
  })
}

export function deleteKhRole(params) {
  return request({
    url: '/umkh/SelfKhRole/delete',
    method: 'post',
    params
  })
}
