import request from '@/utils/request'

export function findAppPage4KhRole(data) {
  return request({
    url: '/umkh/face/Application/page4KhRole',
    method: 'post',
    data
  })
}

export function findAppPage4UnKhRole(data) {
  return request({
    url: '/umkh/face/Application/page4Select',
    method: 'post',
    data
  })
}
