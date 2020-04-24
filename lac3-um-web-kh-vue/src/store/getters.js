const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  currentUser: state => state.user,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  loginMode: state => state.user.loginMode,
  resources: state => state.user.menuPermissions,
  permission_routes: state => state.permission.routes,
  myCompanyAreaTree: state => state.laccache.myCompanyAreaTree,
  orgCertificateType: state => state.laccache.orgCertificateType,
  personCertificateType: state => state.laccache.personCertificateType,
  orgType: state => state.laccache.orgType,
  errorLogs: state => state.errorLog.logs
}
export default getters
