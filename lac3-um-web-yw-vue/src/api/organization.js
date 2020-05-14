import request from '@/utils/request'

export function getOrgTree(data) {
  return request({
    url: '/api4umyw/face/YwCompany/loadTree',
    method: 'post',
    data
  })
}

export function getCompanyTree(data) {
  return request({
    url: '/api4umyw/face/YwCompany/loadCompanyTree',
    method: 'post',
    data
  })
}

export function getFullOrgTree(data) {
  return request({
    url: '/api4umyw/face/YwCompany/loadFullTree',
    method: 'post',
    data
  })
}

export function getFullTreeOfCompany(data) {
  return request({
    url: '/api4umyw/face/YwCompany/loadTreeOfCompany',
    method: 'post',
    data
  })
}

export function fetchYwCompany(data) {
  return request({
    url: '/api4umyw/face/YwCompany/fetch',
    method: 'post',
    data
  })
}

export function fetchMyCompany(data) {
  return request({
    url: '/api4umyw/face/YwCompany/fetchMyCompany',
    method: 'post',
    data
  })
}

export function fetchParentYwCompanyAsTreeNode(data) {
  return request({
    url: '/api4umyw/face/YwCompany/fetchParentTreeNode',
    method: 'post',
    data
  })
}

export function createYwCompany(data) {
  return request({
    url: '/api4umyw/face/YwCompany/save',
    method: 'post',
    data
  })
}

export function updateYwCompany(data) {
  return request({
    url: '/api4umyw/face/YwCompany/save',
    method: 'post',
    data
  })
}

export function deleteYwCompany(data) {
  return request({
    url: '/api4umyw/face/YwCompany/delete',
    method: 'post',
    data
  })
}

export function loadCompanyAreaFullTree(data) {
  return request({
    url: '/api4umyw/face/YwCompany/loadCompanyAreaFullTree',
    method: 'post',
    data
  })
}

export function loadCompanyAreaTree(data) {
  return request({
    url: '/api4umyw/face/YwCompany/loadCompanyAreaTree',
    method: 'post',
    data
  })
}

export function getConfigCompanyAreaRootIds(data) {
  return request({
    url: '/api4umyw/face/YwCompany/getConfigCompanyAreaRootIds',
    method: 'post',
    data
  })
}

export function getConfigCompanyAreaRoots(data) {
  return request({
    url: '/api4umyw/face/YwCompany/getConfigCompanyAreaRoots',
    method: 'post',
    data
  })
}

export function addCompanyApps(data) {
  return request({
    url: '/api4umyw/face/YwCompany/addApps',
    method: 'post',
    data
  })
}

export function removeCompanyApps(data) {
  return request({
    url: '/api4umyw/face/YwCompany/removeApp',
    method: 'post',
    data
  })
}

export function getComapnyPermedMenuTree(data) {
  return request({
    url: '/api4umyw/face/YwCompany/getPermedAppMenuTree',
    method: 'post',
    data
  })
}

export function saveCompanyAppMenuPerm(data) {
  return request({
    url: '/api4umyw/face/YwCompany/saveAppMenuPerm',
    method: 'post',
    data
  })
}

export function changeCompanyStatus(data) {
  return request({
    url: '/api4umyw/face/YwCompany/changeStatus',
    method: 'post',
    data
  })
}

export function findCompanyLeaders(data) {
  return request({
    url: '/api4umyw/face/YwCompany/leaderPage',
    method: 'post',
    data
  })
}

export function addCompanyLeader(data) {
  return request({
    url: '/api4umyw/face/YwCompany/addLeader',
    method: 'post',
    data
  })
}

export function deleteCompanyLeader(data) {
  return request({
    url: '/api4umyw/face/YwCompany/deleteLeader',
    method: 'post',
    data
  })
}

export function updateCompanyLogo(data) {
  return request({
    url: '/api4umyw/face/YwCompany/updateCompanyLogo',
    method: 'post',
    data
  })
}

export function fetchYwDepartment(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/fetch',
    method: 'post',
    data
  })
}

export function fetchParentYwDepartmentAsTreeNode(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/fetchParentTreeNode',
    method: 'post',
    data
  })
}

export function createYwDepartment(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/save',
    method: 'post',
    data
  })
}

export function updateYwDepartment(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/save',
    method: 'post',
    data
  })
}

export function deleteYwDepartment(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/delete',
    method: 'post',
    data
  })
}

export function changeDepartmentStatus(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/changeStatus',
    method: 'post',
    data
  })
}

export function findDepartmentLeaders(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/leaderPage',
    method: 'post',
    data
  })
}

export function addDepartmentLeader(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/addLeader',
    method: 'post',
    data
  })
}

export function deleteDepartmentLeader(data) {
  return request({
    url: '/api4umyw/face/YwDepartment/deleteLeader',
    method: 'post',
    data
  })
}
