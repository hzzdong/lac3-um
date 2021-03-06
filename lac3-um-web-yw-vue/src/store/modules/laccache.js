import { loadTree4MyCompany } from '@/api/area'
import {
  loadCertificateType4Org,
  loadCertificateType4Person,
  loadOrgTypes,
  loadCompanyPositions
} from '@/api/dict'

const state = {
  myCompanyAreaTree: [],
  orgCertificateType: [],
  personCertificateType: [],
  orgTypes: [],
  companyPositions: []
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
  },
  SET_ORG_TYPE: (state, data) => {
    state.orgTypes = data
  },
  SET_COMPANY_POSTIONS: (state, data) => {
    state.companyPositions = data
  }
}

const actions = {
  // 加载公司的区域树
  loadMyCompanyAreaTree({ commit }) {
    return new Promise((resolve, reject) => {
      loadTree4MyCompany()
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
  loadOrgCertificateType({ commit }) {
    return new Promise((resolve, reject) => {
      loadCertificateType4Org()
        .then(response => {
          const { data } = response
          const cts = []
          if (data && data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              cts.push({ key: data[i].govCode, display_name: data[i].name })
            }
          }
          commit('SET_ORG_CERTIFICATE_TYPE', cts)
          resolve(cts)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 加载个人证照类型
  loadPersonCertificateType({ commit }) {
    return new Promise((resolve, reject) => {
      loadCertificateType4Person()
        .then(response => {
          const { data } = response
          const cts = []
          if (data && data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              cts.push({ key: data[i].govCode, display_name: data[i].name })
            }
          }
          commit('SET_PERSON_CERTIFICATE_TYPE', cts)
          resolve(cts)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 加载机构类型
  loadOrgType({ commit }) {
    return new Promise((resolve, reject) => {
      loadOrgTypes()
        .then(response => {
          const { data } = response
          const cts = []
          if (data && data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              cts.push({ key: data[i].govCode, display_name: data[i].name })
            }
          }
          commit('SET_ORG_TYPE', cts)
          resolve(cts)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 加载公司职位
  loadCompanyPositions({ commit }) {
    return new Promise((resolve, reject) => {
      loadCompanyPositions()
        .then(response => {
          const { data } = response
          const cts = []
          if (data && data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              cts.push({ key: data[i].govCode, display_name: data[i].name })
            }
          }
          commit('SET_COMPANY_POSTIONS', cts)
          resolve(cts)
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
