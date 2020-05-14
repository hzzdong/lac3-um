import request from '@/utils/request'

export function loadCertificateType4Org() {
  return request({
    url: '/api4umyw/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'certificate_type_org' }
  })
}

export function loadCertificateType4Person() {
  return request({
    url: '/api4umyw/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'certificate_type_person' }
  })
}

export function loadOrgTypes() {
  return request({
    url: '/api4umyw/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'org_type' }
  })
}

export function loadCompanyPositions() {
  return request({
    url: '/api4umyw/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'company_position' }
  })
}

export function loadDictTree() {
  return request({
    url: '/api4umyw/face/Dict/loadDictTree',
    method: 'post'
  })
}

export function saveDictTree(data) {
  return request({
    url: '/api4umyw/face/Dict/saveDictTree',
    method: 'post',
    data
  })
}

export function deleteDictType(data) {
  return request({
    url: '/api4umyw/face/Dict/deleteDictTree',
    method: 'post',
    data
  })
}

export function getDictPage(data) {
  return request({
    url: '/api4umyw/face/Dict/page',
    method: 'post',
    data
  })
}

export function saveDict(data) {
  return request({
    url: '/api4umyw/face/Dict/save',
    method: 'post',
    data
  })
}
