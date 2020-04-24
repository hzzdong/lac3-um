import request from '@/utils/request'

export function getOrgTree(data) {
  return request({
    url: '/umkh/face/KhCompany/loadTree',
    method: 'post',
    data
  })
}

export function getFullOrgTree(data) {
  return request({
    url: '/umkh/face/KhCompany/loadFullTree',
    method: 'post',
    data
  })
}

export function getFullTreeOfCompany(data) {
  return request({
    url: '/umkh/face/KhCompany/loadTreeOfCompany',
    method: 'post',
    data
  })
}

export function fetchKhDepartment(data) {
  return request({
    url: '/umkh/face/KhDepartment/fetch',
    method: 'post',
    data
  })
}

export function createKhDepartment(data) {
  return request({
    url: '/umkh/face/KhDepartment/save',
    method: 'post',
    data
  })
}

export function updateKhDepartment(data) {
  return request({
    url: '/umkh/face/KhDepartment/save',
    method: 'post',
    data
  })
}

export function deleteKhDepartment(data) {
  return request({
    url: '/umkh/face/KhDepartment/delete',
    method: 'post',
    data
  })
}

export function fetchKhCompany(data) {
  return request({
    url: '/umkh/face/KhCompany/fetch',
    method: 'post',
    data
  })
}

export function createKhCompany(data) {
  return request({
    url: '/umkh/face/KhCompany/save',
    method: 'post',
    data
  })
}

export function updateKhCompany(data) {
  return request({
    url: '/umkh/face/KhCompany/save',
    method: 'post',
    data
  })
}

export function deleteKhCompany(data) {
  return request({
    url: '/umkh/face/KhCompany/delete',
    method: 'post',
    data
  })
}

export function loadCompanyAreaFullTree(data) {
  return request({
    url: '/umkh/face/KhCompany/loadCompanyAreaFullTree',
    method: 'post',
    data
  })
}

export function loadCompanyAreaTree(data) {
  return request({
    url: '/umkh/face/KhCompany/loadCompanyAreaTree',
    method: 'post',
    data
  })
}

export function getConfigCompanyAreaRootIds(data) {
  return request({
    url: '/umkh/face/KhCompany/getConfigCompanyAreaRootIds',
    method: 'post',
    data
  })
}

export function getConfigCompanyAreaRoots(data) {
  return request({
    url: '/umkh/face/KhCompany/getConfigCompanyAreaRoots',
    method: 'post',
    data
  })
}

export function addCompanyApps(data) {
  return request({
    url: '/umkh/face/KhCompany/addApps',
    method: 'post',
    data
  })
}

export function removeCompanyApps(data) {
  return request({
    url: '/umkh/face/KhCompany/removeApp',
    method: 'post',
    data
  })
}

export function getComapnyPermedMenuTree(data) {
  return request({
    url: '/umkh/face/KhCompany/getPermedAppMenuTree',
    method: 'post',
    data
  })
}

export function saveCompanyAppMenuPerm(data) {
  return request({
    url: '/umkh/face/KhCompany/saveAppMenuPerm',
    method: 'post',
    data
  })
}