import request from '@/utils/request'

export function getOrgTree() {
  return request({
    url: '/umkh/SelfKhCompany/loadTree',
    method: 'get'
  })
}

export function createKhDepartment(data) {
  return request({
    url: '/umkh/SelfKhDepartment/save',
    method: 'post',
    data
  })
}

export function updateKhDepartment(data) {
  return request({
    url: '/umkh/SelfKhDepartment/save',
    method: 'post',
    data
  })
}

export function deleteKhDepartment(params) {
  return request({
    url: '/umkh/SelfKhDepartment/delete',
    method: 'post',
    params
  })
}

export function fetchKhCompany(params) {
  return request({
    url: '/umkh/SelfKhCompany/get',
    method: 'get',
    params
  })
}

export function createKhCompany(data) {
  return request({
    url: '/umkh/SelfKhCompany/save',
    method: 'post',
    data
  })
}

export function updateKhCompany(data) {
  return request({
    url: '/umkh/SelfKhCompany/save',
    method: 'post',
    data
  })
}

export function deleteKhCompany(params) {
  return request({
    url: '/umkh/SelfKhCompany/delete',
    method: 'post',
    params
  })
}
