import { loadAreaTree } from '@/api/area'
import {
  loacCertificateType4Org,
  loacCertificateType4Person
} from '@/api/dict'

const state = {
  myCompanyAreaTree: [],
  orgCertificateType: [],
  personCertificateType: []
}

const mutations = {
  SET_MY_COMPANY_AREA_TREE: (state, treeData) => {
    state.myCompanyAreaTree = treeData
  },
  SET_ORG_CERTIFICATE_TYPE: (state, data) => {
    state.orgCertificateType = data
  },
  SET_PERSON_CERTIFICATE_TYPE: (state, data) => {
    state.personCertificateType = data
  }
}

const actions = {
  // 加载公司的区域树
  loadMyCompanyAreaTree({ commit }) {
    return new Promise((resolve, reject) => {
      loadAreaTree()
        .then(response => {
          const { data } = response
          commit('SET_MY_COMPANY_AREA_TREE', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 加载公司证照类型
  loacOrgCertificateType({ commit }) {
    return new Promise((resolve, reject) => {
      loacCertificateType4Org()
        .then(response => {
          const { data } = response
          commit('SET_ORG_CERTIFICATE_TYPE', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 加载个人证照类型
  loacPersonCertificateType({ commit }) {
    return new Promise((resolve, reject) => {
      loacCertificateType4Person()
        .then(response => {
          const { data } = response
          commit('SET_PERSON_CERTIFICATE_TYPE', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
