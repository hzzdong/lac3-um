import request from '@/utils/request'

export function findAppPage4KhRole(data) {
  return request({
    url: '/api4umkh/face/Application/page4KhRole',
    method: 'post',
    data
  })
}

export function findAppPage4UnKhRole(data) {
  return request({
    url: '/api4umkh/face/Application/page4Select',
    method: 'post',
    data
  })
}

export function findAppPage4Company(data) {
  return request({
    url: '/api4umkh/face/Application/page4KhCompany',
    method: 'post',
    data
  })
}

export function findAppPage4UnCompany(data) {
  return request({
    url: '/api4umkh/face/Application/page4KhCompany4Select',
    method: 'post',
    data
  })
}
