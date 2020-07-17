import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
// import componentsRouter from './modules/components'
// import chartsRouter from './modules/charts'
// import tableRouter from './modules/table'
// import nestedRouter from './modules/nested'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: '个人设置', icon: 'user', noCache: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/org',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/yw-company/tree'),
        name: 'yw-company',
        meta: { title: '组织机构', icon: 'tree-table', requires: ['yw_org'] }
      },
      {
        path: 'company-view/:id(\\d+)/:uuid',
        component: () => import('@/views/yw-company/company-view'),
        name: 'CompanyView',
        meta: { title: '单位预览', noCache: true, activeMenu: '/org/index', requires: ['yw_org_view'] },
        hidden: true
      },
      {
        path: 'dep-view/:id(\\d+)/:uuid',
        component: () => import('@/views/yw-company/dep-view'),
        name: 'DepView',
        meta: { title: '部门预览', noCache: true, activeMenu: '/org/index', requires: ['yw_dep_view'] },
        hidden: true
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/yw-user/users'),
        name: 'YwUser',
        meta: { title: '用户管理', icon: 'peoples', requires: ['yw_user'] }
      },
      {
        path: 'user-view/:id(\\d+)/:uuid',
        component: () => import('@/views/yw-user/user-view'),
        name: 'UserView',
        meta: { title: '用户预览', noCache: true, activeMenu: '/user/index', requires: ['yw_user_view'] },
        hidden: true
      },
      {
        path: 'jz-user-view/:id(\\d+)/:uuid',
        component: () => import('@/views/yw-user/jz-user-view'),
        name: 'JzUserView',
        meta: { title: '兼职用户', noCache: true, activeMenu: '/user/index', requires: ['yw_user_view'] },
        hidden: true
      }
    ]
  },
  {
    path: '/role',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/yw-permission/roles'),
        name: 'YwRole',
        meta: { title: '权限管理', icon: 'skill', requires: ['yw_perm'] }
      },
      {
        path: 'yw-role-view/:id(\\d+)/:uuid',
        component: () => import('@/views/yw-permission/role-view'),
        name: 'YwRoleView',
        meta: { title: '角色预览', noCache: true, activeMenu: '/role/index', requires: ['yw_perm_role'] },
        hidden: true
      },
      {
        path: 'sys-kh-role-view/:id(\\d+)/:uuid',
        component: () => import('@/views/kh-permission/sys-kh-role-view'),
        name: 'KhRoleView',
        meta: { title: '角色预览', noCache: true, activeMenu: '/sysrole/syskhrole', requires: ['kh_sys_role'] },
        hidden: true
      }
    ]
  },
  {
    path: '/application',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/application/index'),
        name: 'Application',
        meta: { title: '应用管理', icon: 'component', requires: ['yw_app'] }
      },
      {
        path: 'app-view/:id(\\d+)/:uuid',
        component: () => import('@/views/application/app-view'),
        name: 'AppView',
        meta: { title: '应用预览', noCache: true, activeMenu: '/application/index', requires: ['yw_app_edit', 'yw_app_menu'] },
        hidden: true
      },
      {
        path: 'menu-view/:id(\\d+)/:uuid',
        component: () => import('@/views/menu/menu-view'),
        name: 'MenuView',
        meta: { title: '菜单项预览', noCache: true, activeMenu: '/application/app-view', requires: ['yw_app_menu'] },
        hidden: true
      }
    ]
  },
  {
    path: '/config',
    component: Layout,
    redirect: '/config/dict',
    name: 'YwSystemConfig',
    meta: {
      title: '系统设置', icon: 'example', requires: ['yw_sys_config']
    },
    children: [
      {
        path: 'dict',
        component: () => import('@/views/yw-system-config/dict'),
        name: 'Dict',
        meta: { title: '数据字典', requires: ['yw_sys_config_dict'] }
      },
      {
        path: 'area',
        component: () => import('@/views/yw-system-config/area'),
        name: 'Area',
        meta: { title: '区域管理', requires: ['yw_sys_config_area'] }
      },
      {
        path: 'setup',
        component: () => import('@/views/yw-system-config/config'),
        name: 'YwSetup',
        meta: { title: '系统设置', requires: ['yw_sys_config_setup'] }
      }
    ]
  },

  {
    path: '/sysrole',
    component: Layout,
    redirect: '/sysrole/sysywrole',
    name: 'SysRole',
    meta: {
      title: '系统角色', icon: 'lock', requires: ['yw_sys_role', 'kh_sys_role']
    },
    children: [
      {
        path: 'sysywrole',
        component: () => import('@/views/yw-permission/sys-yw-roles'),
        name: 'YwSysRole',
        meta: { title: '运维系统角色', requires: ['yw_sys_role_m'] }
      },
      {
        path: 'syskhrole',
        component: () => import('@/views/kh-permission/sys-kh-roles'),
        name: 'KhSysRole',
        meta: { title: '客户系统角色', requires: ['kh_sys_role_m'] }
      }
    ]
  },

  {
    path: '/customer',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/customer/index'),
        name: 'Customer',
        meta: { title: '客户管理', icon: 'international', requires: ['kh_company'] }
      },
      {
        path: 'customer-view/:id(\\d+)/:uuid',
        component: () => import('@/views/customer/customer-view'),
        name: 'RoleView',
        meta: { title: '单位预览', noCache: true, activeMenu: '/customer/index', requires: ['kh_company_m', 'kh_company_user_m', 'kh_company_app'] },
        hidden: true
      },
      {
        path: 'user-view/:id(\\d+)/:uuid',
        component: () => import('@/views/customer/user-view'),
        name: 'KhUserView',
        meta: { title: '用户预览', noCache: true, activeMenu: '/customer/index', requires: ['kh_company_m'] },
        hidden: true
      }
    ]
  },

  {
    path: '/log',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/yw-log/logs'),
        name: 'YwLog',
        meta: { title: '日志查看', icon: 'excel', requires: ['yw_sys_log'] }
      }
    ]
  },
  {
    path: '/dw',
    component: Layout,
    redirect: '/dw/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/dw/index'),
        name: 'DW',
        meta: { title: '系统代维', icon: 'user', noCache: true }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
