import request from '@/utils/request'

export function findCustomerPage(data) {
  return request({
    url: '/api4umyw/face/KhCompany/page',
    method: 'post',
    data
  })
}

export function saveKhCompany(data) {
  return request({
    url: '/api4umyw/face/KhCompany/save',
    method: 'post',
    data
  })
}

export function deleteKhCompany(data) {
  return request({
    url: '/api4umyw/face/KhCompany/delete',
    method: 'post',
    data
  })
}

export function fetchKhCompany(data) {
  return request({
    url: '/api4umyw/face/KhCompany/fetch',
    method: 'post',
    data
  })
}

export function getFullOrgTree(data) {
  return request({
    url: '/api4umyw/face/KhCompany/loadFullTree',
    method: 'post',
    data
  })
}

export function addCompanyApps(data) {
  return request({
    url: '/api4umyw/face/KhCompany/addApps',
    method: 'post',
    data
  })
}

export function removeCompanyApps(data) {
  return request({
    url: '/api4umyw/face/KhCompany/removeApp',
    method: 'post',
    data
  })
}

export function getComapnyPermedMenuTree(data) {
  return request({
    url: '/api4umyw/face/KhCompany/getPermedAppMenuTree',
    method: 'post',
    data
  })
}

export function saveCompanyAppMenuPerm(data) {
  return request({
    url: '/api4umyw/face/KhCompany/saveAppMenuPerm',
    method: 'post',
    data
  })
}

export function changeCompanyStatus(data) {
  return request({
    url: '/api4umyw/face/KhCompany/changeStatus',
    method: 'post',
    data
  })
}
