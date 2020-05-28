import request from '@/utils/request'

export function fetchById(data) {
  return request({
    url: '/api4umyw/face/KhUser/fetch',
    method: 'post',
    data
  })
}

export function getPage(data) {
  return request({
    url: '/api4umyw/face/KhUser/page',
    method: 'post',
    data
  })
}

export function createUser(data) {
  return request({
    url: '/api4umyw/face/KhUser/save',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/api4umyw/face/KhUser/save',
    method: 'post',
    data
  })
}

export function updateMe(data) {
  return request({
    url: '/api4umyw/face/KhUser/updateMe',
    method: 'post',
    data
  })
}

export function updateHeaderImage(data) {
  return request({
    url: '/api4umyw/face/KhUser/updateHeaderImage',
    method: 'post',
    data
  })
}

export function updatePass(data) {
  return request({
    url: '/api4umyw/face/KhUser/updatePass',
    method: 'post',
    data
  })
}

export function deleteUser(data) {
  return request({
    url: '/api4umyw/face/KhUser/delete',
    method: 'post',
    data
  })
}

export function changeUserStatus(data) {
  return request({
    url: '/api4umyw/face/KhUser/changeStatus',
    method: 'post',
    data
  })
}

export function getPage4Role(data) {
  return request({
    url: '/api4umyw/face/KhUser/page4Role',
    method: 'post',
    data
  })
}

export function findRoleUsers(data) {
  return request({
    url: '/api4umyw/face/KhUser/page4Role4Yw',
    method: 'post',
    data
  })
}

export function findUnRoleUsers(data) {
  return request({
    url: '/api4umyw/face/KhUser/page4UnRole4Yw',
    method: 'post',
    data
  })
}

export function getJzPage(data) {
  return request({
    url: '/api4umyw/face/KhUser/page4PartTimeJob',
    method: 'post',
    data
  })
}

export function getJzPageOfUser(data) {
  return request({
    url: '/api4umyw/face/KhUser/page4PartTimeJobOfUser',
    method: 'post',
    data
  })
}

export function addPartTimeJob(data) {
  return request({
    url: '/api4umyw/face/KhUser/addPartTimeJob',
    method: 'post',
    data
  })
}

export function removePartTimeJob(data) {
  return request({
    url: '/api4umyw/face/KhUser/removePartTimeJob',
    method: 'post',
    data
  })
}
