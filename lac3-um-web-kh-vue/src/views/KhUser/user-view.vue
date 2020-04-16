<template>
  <div class="app-container">
    <el-tabs :tab-position="tabPosition" style="height: 100%;">
      <el-tab-pane label="用户信息">
        <el-form ref="userForm" :model="user" size="small" status-icon label-position="right" label-width="120px" style="width: 98%;">
          <el-row>
            <el-col :span="12">
              <el-form-item label="所属机构:" prop="orgName">
                <span class="el-span_view">{{ user.orgFullName || user.companyName }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态:" prop="status">
                <span class="el-span_view">{{ user.status | statusFilter }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名:" prop="name">
                <span class="el-span_view">{{ user.name }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机:" prop="mobile">
                <span class="el-span_view">{{ user.mobile }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="账号:" prop="account">
                <span class="el-span_view">{{ user.account }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="工号:" prop="govCode">
                <span class="el-span_view">{{ user.govCode }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户类型:" prop="type">
                <span class="el-span_view">{{ user.type | typeFilter }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="昵称:" prop="nickName">
                <span class="el-span_view">{{ user.nickName }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注:">
                <el-input v-model="user.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" class="el-textarea_view" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="角色列表">
        <el-table
          ref="roleTable"
          :data="roles"
          tooltip-effect="dark"
          style="width: 100%"
        >
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="角色名称" width="250px">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleUpdate(row)">{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="govCode" label="角色编号" width="200px" />
          <el-table-column prop="remark" label="备注说明" min-width="150px" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { fetchById } from '@/api/user'
import { findUserRoles } from '@/api/khrole'

const defaultUser = {
  id: undefined,
  uuid: '',
  parentId: '',
  parentClass: '',
  orgName: '',
  orgFullName: '',
  name: '',
  account: '',
  govCode: '',
  mobile: '',
  nickName: '',
  type: 1,
  status: 0,
  remark: ''
}

const statusOptions = [
  { key: 0, display_name: '正常' },
  { key: 1, display_name: '禁用' }
]

const typeOptions = [
  { key: 1, display_name: '普通用户' },
  { key: 9, display_name: '管理员' }
]

const typeKeyValue = typeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

// arr to obj, such as { 0 : "正常", 1 : "禁用" }
const statusKeyValue = statusOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'UserDetail',
  filters: {
    statusFilter(status) {
      return statusKeyValue[status]
    },
    statusTypeFilter(status) {
      const statusMap = {
        0: 'success',
        1: 'warning',
        9: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return typeKeyValue[type]
    }
  },
  data() {
    return {
      user: Object.assign({}, defaultUser),
      loading: false,
      roles: [],
      tempRoute: {},
      tabPosition: 'right'
    }
  },
  computed: {
    // TODO
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    const uuid = this.$route.params && this.$route.params.uuid
    this.fetchData(id, uuid)
    this.findUserRoles(id, uuid)

    // set tagsview title
    this.setTagsViewTitle()

    // set page title
    this.setPageTitle()

    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)
  },
  methods: {
    fetchData(id, uuid) {
      fetchById({ id, uuid }).then(response => {
        this.user = response.data
      }).catch(err => {
        console.log(err)
      })
    },
    findUserRoles(id, uuid) {
      findUserRoles({ id, uuid }).then(response => {
        this.roles = response.data
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const route = Object.assign({}, this.tempRoute, { title: '用户视图' })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      document.title = '用户视图 - 用户中心'
    }

  }
}
</script>

<style>
    .el-input_view input {
        border-radius: 0px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
        border-bottom-width: 1px;
        /*outline: medium;*/
    }
    .el-textarea_view textarea {
        border-radius: 0px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
        border-bottom-width: 1px;
    }
    .el-span_view {
        width: 100%;
        padding: 0 15px;
        display: inline-block;
        border-bottom:1px solid #DCDFE6;
    }
</style>
