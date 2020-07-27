import request from '@/utils/request'

export function getCompanyHolidays4Month(data) {
  return request({
    url: '/api4umkh/face/Holiday/getByMonth',
    method: 'post',
    data
  })
}

export function setCompanyHoliday(data) {
  return request({
    url: '/api4umkh/face/Holiday/setHoliday',
    method: 'post',
    data
  })
}

export function getCompanyWorkTime(data) {
  return request({
    url: '/api4umkh/face/WorkTime/get',
    method: 'post',
    data
  })
}

export function initCompanyWorkTime(data) {
  return request({
    url: '/api4umkh/face/WorkTime/initCompanyWorkTime',
    method: 'post',
    data
  })
}

export function setCompanyWorkTime(data) {
  return request({
    url: '/api4umkh/face/WorkTime/setCompanyWorkTime',
    method: 'post',
    data
  })
}
