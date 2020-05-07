import request from '@/utils/request'

export function loadCertificateType4Org() {
  return request({
    url: '/api4umkh/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'certificate_type_org' }
  })
}

export function loadCertificateType4Person() {
  return request({
    url: '/api4umkh/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'certificate_type_person' }
  })
}

export function loadOrgTypes() {
  return request({
    url: '/api4umkh/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'org_type' }
  })
}

export function loadCompanyPositions() {
  return request({
    url: '/api4umkh/face/Dict/loadDicts',
    method: 'post',
    data: { data: 'company_position' }
  })
}
