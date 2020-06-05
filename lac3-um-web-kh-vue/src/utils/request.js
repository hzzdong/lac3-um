import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 10000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    config.headers.post['content-Type'] = 'application/json; charset=UTF-8'
    // config.responseType = 'json'
    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['token'] = getToken()
    }

    if (config.data) {
      if (config.data.dataType && config.data.dataType === 'Object') {
        const faceReq = { appCode: 'lac_app_um_kh', data: config.data }
        config.data = faceReq
      } else {
        const data = config.data
        if (data.rules || data.orderby || (data.page && data.limit)) {
          const req = {
            appCode: 'lac_app_um_kh',
            start: 0,
            length: 20,
            query: {
              cnds: [],
              orderby: { orderby: 'id', order: 'desc' }
            }
          }

          for (const field in data.rules) { // ListFaceRequest
            const rule = data.rules[field]
            if (rule && rule.fv !== undefined && rule.fv !== '') {
              req.query.cnds.push({
                field: field,
                data: rule.fv,
                type: rule.stype,
                op: rule.oper
              })
            }
          }

          if (data.orderby && data.orderby.orderby !== '') {
            req.query.orderby.orderby = data.orderby.orderby
            req.query.orderby.order = data.orderby.order
          }

          if (data.page && data.limit && Number.parseInt(data.page) > 0 && Number.parseInt(data.limit) > 0) {
            req.start = (data.page - 1) * data.limit
            req.length = data.limit
          }

          config.data = req
        } else {
          const faceReq = Object.assign({ appCode: 'lac_app_um_kh' }, config.data)
          config.data = faceReq
        }
      }
    } else {
      config.data = { appCode: 'lac_app_um_kh' }
    }

    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // if the custom code is not 0, it is judged as an error.
    if (res.code !== '0') {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 9021: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 9021 || res.code === 'e.face.login') {
        // to re-login
        // MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
        //   confirmButtonText: 'Re-Login',
        //   cancelButtonText: 'Cancel',
        //   type: 'warning'
        // }).then(() => {
        // })
        store.dispatch('user/resetToken').then(() => {
          // location.reload()
          window.location = '/api4umkh/ssoauth'
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
