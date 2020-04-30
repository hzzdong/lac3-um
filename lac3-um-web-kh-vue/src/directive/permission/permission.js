import store from '@/store'

export default {
  inserted(el, binding, vnode) {
    const { value } = binding
    const resources = store.getters && store.getters.resources

    if (value && value instanceof Array && value.length > 0) {
      const permissionResources = value

      const hasPermission = resources.some(res => {
        return permissionResources.includes(res)
      })

      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`need roles! Like v-permission="['admin','editor']"`)
    }
  }
}
