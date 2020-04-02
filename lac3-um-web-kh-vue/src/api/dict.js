import request from '@/utils/request'

export function loacCertificateType4Org() {
  return request({
    url: '/umkh/dict/loadDicts',
    method: 'get',
    params: { code: 'certificate_type_org' }
  })
}

export function loacCertificateType4Person() {
  return request({
    url: '/umkh/dict/loadDicts',
    method: 'get',
    params: { code: 'certificate_type_person' }
  })
}
