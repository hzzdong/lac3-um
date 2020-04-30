// import parseTime, formatTime and set to filter
export { parseTime, formatTime } from '@/utils'

/**
 * Show plural label if time is plural number
 * @param {number} time
 * @param {string} label
 * @return {string}
 */
function pluralize(time, label) {
  if (time === 1) {
    return time + label
  }
  return time + label + 's'
}

/**
 * @param {number} time
 */
export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time)
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute')
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour')
  } else {
    return pluralize(~~(between / 86400), ' day')
  }
}

/**
 * Number formatting
 * like 10000 => 10k
 * @param {number} num
 * @param {number} digits
 */
export function numberFormatter(num, digits) {
  const si = [
    { value: 1E18, symbol: 'E' },
    { value: 1E15, symbol: 'P' },
    { value: 1E12, symbol: 'T' },
    { value: 1E9, symbol: 'G' },
    { value: 1E6, symbol: 'M' },
    { value: 1E3, symbol: 'k' }
  ]
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (num / si[i].value).toFixed(digits).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol
    }
  }
  return num.toString()
}

/**
 * 10000 => "10,000"
 * @param {number} num
 */
export function toThousandFilter(num) {
  return (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}

/**
 * Upper case first char
 * @param {String} string
 */
export function uppercaseFirst(string) {
  return string.charAt(0).toUpperCase() + string.slice(1)
}

export function operateResult(ors) {
  const operateResultKeyValue = {
    0: '成功',
    1: '失败'
  }
  return operateResultKeyValue[ors]
}

export function statusOptions() {
  return [
    { key: 0, display_name: '正常' },
    { key: 1, display_name: '禁用' },
    { key: 8, display_name: '注销' }
  ]
}

export function statusFilter(status) {
  const statusKeyValue = statusOptions().reduce((acc, cur) => {
    acc[cur.key] = cur.display_name
    return acc
  }, {})
  return statusKeyValue[status]
}

export function userStatusOptions() {
  return [
    { key: 0, display_name: '正常' },
    { key: 1, display_name: '禁用' },
    // { key: 7, display_name: '兼职' },
    { key: 8, display_name: '离职' }
  ]
}

export function userStatusFilter(status) {
  const statusKeyValue = userStatusOptions().reduce((acc, cur) => {
    acc[cur.key] = cur.display_name
    return acc
  }, {})
  return statusKeyValue[status]
}

export function statusTypeFilter(status) {
  const statusMap = {
    0: 'success',
    1: 'danger',
    7: '',
    8: 'warning'
  }
  return statusMap[status]
}

export function userTypeOptions() {
  return [
    { key: 1, display_name: '普通用户' },
    { key: 9, display_name: '管理员' }
  ]
}

export function userTypeFilter(type) {
  const typeKeyValue = userTypeOptions().reduce((acc, cur) => {
    acc[cur.key] = cur.display_name
    return acc
  }, {})
  return typeKeyValue[type]
}

export function orgTypeFilter(type) {
  const typeMap = {
    'Company': '单位',
    'Department': '部门'
  }
  return typeMap[type]
}

export function appTypeOptions() {
  return [
    { key: 0, display_name: '内部' },
    { key: 1, display_name: '外部' }
  ]
}

export function appTypeFilter(type) {
  const typeKeyValue = appTypeOptions().reduce((acc, cur) => {
    acc[cur.key] = cur.display_name
    return acc
  }, {})
  return typeKeyValue[type]
}
