import request from '@/utils/request'

export function getCompanyHolidays4Month(data) {
  return request({
    url: '/api4umyw/face/Holiday/getByMonth',
    method: 'post',
    data
  })
}
