import request from '@/utils/request'

export function loacCertificateType4Org() {
  return request({
    url: '/umkh/face/Area/loadDicts',
    method: 'post',
    data: { data: 'certificate_type_org' }
  })
}

export function loacCertificateType4Person() {
  return request({
    url: '/umkh/face/Area/loadDicts',
    method: 'post',
    data: { data: 'certificate_type_person' }
  })
}
