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

export function loadOrgCertificateType() {
  const ocdata = store.getters.orgCertificateType
  if (ocdata && ocdata instanceof Array && ocdata.length > 0) {
    return new Promise((resolve, reject) => {
      console.log('Load OrgCertificateType from CACHE', ocdata)
      resolve(ocdata)
    })
  } else {
    return new Promise((resolve, reject) => {
      store
        .dispatch('laccache/loadOrgCertificateType')
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

export function loadPersonCertificateType() {
  const ocdata = store.getters.personCertificateType
  if (ocdata && ocdata instanceof Array && ocdata.length > 0) {
    return new Promise((resolve, reject) => {
      console.log('Load personCertificateType from CACHE', ocdata)
      resolve(ocdata)
    })
  } else {
    return new Promise((resolve, reject) => {
      store
        .dispatch('laccache/loadPersonCertificateType')
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

export function loadOrgType() {
  const ocdata = store.getters.orgType
  if (ocdata && ocdata instanceof Array && ocdata.length > 0) {
    return new Promise((resolve, reject) => {
      console.log('Load orgType from CACHE', ocdata)
      resolve(ocdata)
    })
  } else {
    return new Promise((resolve, reject) => {
      store
        .dispatch('laccache/loadOrgType')
        .then(data => {
          console.log('Load orgType from SERVER', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}
