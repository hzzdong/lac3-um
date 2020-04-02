import store from '@/store'

export function loadCachedMyCompanyAreaTree() {
  const treeData = store.getters.myCompanyAreaTree
  if (treeData && treeData instanceof Array && treeData.length > 0) {
    return new Promise((resolve, reject) => {
      console.log('Load area tree from CACHE', treeData)
      resolve(treeData)
    })
  } else {
    return new Promise((resolve, reject) => {
      store
        .dispatch('laccache/loadMyCompanyAreaTree')
        .then(data => {
          console.log('Load area tree from SERVER', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export function loacOrgCertificateType() {
  const ocdata = store.getters.orgCertificateType
  if (ocdata && ocdata instanceof Array && ocdata.length > 0) {
    return new Promise((resolve, reject) => {
      console.log('Load OrgCertificateType from CACHE', ocdata)
      resolve(ocdata)
    })
  } else {
    return new Promise((resolve, reject) => {
      store
        .dispatch('laccache/loacOrgCertificateType')
        .then(data => {
          console.log('Load OrgCertificateType from SERVER', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export function loacPersonCertificateType() {
  const ocdata = store.getters.personCertificateType
  if (ocdata && ocdata instanceof Array && ocdata.length > 0) {
    return new Promise((resolve, reject) => {
      console.log('Load personCertificateType from CACHE', ocdata)
      resolve(ocdata)
    })
  } else {
    return new Promise((resolve, reject) => {
      store
        .dispatch('laccache/loacPersonCertificateType')
        .then(data => {
          console.log('Load personCertificateType from SERVER', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}
