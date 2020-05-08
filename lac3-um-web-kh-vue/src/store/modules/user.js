import { login, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import defaultSettings from '@/settings'

const { fssBaseUrl, defaultIco } = defaultSettings

const state = {
  token: getToken(),
  loginMode: 0,
  name: '',
  mobile: '',
  nickName: '',
  govCode: '',
  ico: '',
  remark: '',
  loginName: '',
  userType: '',
  appId: '',
  appCode: '',
  appName: '',
  areaId: '',
  areaLevel: '',
  areaName: '',
  companyId: '',
  companyName: '',
  orgId: '',
  orgName: '',
  orgType: '',
  menuPermissions: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, ico) => {
    if (ico) {
      state.ico = fssBaseUrl + ico
    } else {
      state.ico = defaultIco
    }
  },
  SET_PERMISSIONS: (state, menuPermissions) => {
    state.menuPermissions = menuPermissions
  },
  SET_USER: (state, user) => {
    state.loginMode = user.loginMode || 0
    state.name = user.sid.name
    state.mobile = user.mobiel
    state.nickName = user.nickName
    state.govCode = user.govCode
    state.remark = user.remark
    // state.ico = user.ico // || 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
    if (user.ico) {
      state.ico = fssBaseUrl + user.ico
    } else {
      state.ico = defaultIco
    }
    state.loginName = user.sid.code
    state.userType = user.userType
    state.appId = user.app.id
    state.appCode = user.app.code
    state.appName = user.app.name
    state.areaId = user.area.id
    state.areaLevel = user.areaLevel
    state.areaName = user.area.name
    state.companyId = user.company.id
    state.companyName = user.company.name
    state.orgId = user.org.id
    state.orgName = user.org.name
    state.orgType = user.userType
    // state.menuPermissions = user.menuPermissions || []
  },
  RESET_USER: (state, user) => {
    state.loginMode = user.loginMode || 0
    state.name = user.name
    state.mobile = user.mobiel
    state.nickName = user.nickName
    state.govCode = user.govCode
    state.remark = user.remark
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ loginName: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data)
        setToken(data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user login by token
  loginByToken({ commit }, token) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', token)
      setToken(token)
      resolve(token)
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        commit('SET_USER', data)
        if (data.menuPermissions && data.menuPermissions.length > 0) {
          commit('SET_PERMISSIONS', data.menuPermissions)
        } else {
          commit('SET_PERMISSIONS', ['NO_PERMISSION'])
        }

        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  refreshUserInfo({ commit }, user) {
    return new Promise((resolve, reject) => {
      commit('RESET_USER', user)
      resolve()
    })
  },

  updateHeaderImage({ commit }, ico) {
    return new Promise((resolve, reject) => {
      commit('SET_AVATAR', ico)
      resolve()
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      // logout(state.token).then(() => {
      //   commit('SET_TOKEN', '')
      //   commit('SET_PERMISSIONS', [])
      //   removeToken()
      //   resetRouter()

      //   // reset visited views and cached views
      //   // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      //   dispatch('tagsView/delAllViews', null, { root: true })

      //   resolve()
      // }).catch(error => {
      //   reject(error)
      // })

      commit('SET_TOKEN', '')
      commit('SET_PERMISSIONS', [])
      removeToken()
      resetRouter()

      // reset visited views and cached views
      // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      dispatch('tagsView/delAllViews', null, { root: true })

      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_PERMISSIONS', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      // reset visited views and cached views
      dispatch('tagsView/delAllViews', null, { root: true })

      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
