<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;">
      <el-tab-pane label="用户信息" name="tabUser">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
          <el-button type="success" icon="el-icon-edit" circle @click="toUpdateUser()" />
          <el-button type="danger" icon="el-icon-delete" circle @click="onDeleteUser()" />
        </div>
        <aside style="margin-top:15px;">
          <i class="el-icon-info" /> 用户视图。您可以通过选择本视图右侧的页签查看或编辑相关功能信息。
        </aside>
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
      <el-tab-pane label="角色列表" name="tabUserRoles">
        <el-table
          ref="roleTable"
          :data="userRoles"
          tooltip-effect="dark"
          style="width: 100%"
        >
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="角色名称" width="250px" prop="name" sortable>
            <template slot-scope="{row}">
              <router-link :to="'/Role/role-view/'+row.id+'/'+row.uuid" class="link-type">
                <span>{{ row.name }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="govCode" label="角色编号" width="200px" />
          <el-table-column prop="remark" label="备注说明" min-width="150px" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="用户编辑" :visible.sync="dialogFormVisible" width="75%">
      <el-form ref="dataForm" :rules="rules" :inline="false" :model="temp" size="small" status-icon label-position="right" label-width="80px" style="width: 98%; margin-left:10px;">
        <el-row>
          <el-col :span="16">
            <el-card class="box-card" style="margin-top: -20px;">
              <div slot="header" class="clearfix">
                <span>用户基本信息</span>
                <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
              </div>
              <div class="text item">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="所属机构" prop="orgName">
                      <el-input v-model="temp.orgName" :disabled="true">
                        <el-button slot="append" icon="el-icon-search" :disabled="true" />
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="状态" prop="status">
                      <el-radio-group v-model="temp.status" size="small">
                        <el-radio-button label="0">正常</el-radio-button>
                        <el-radio-button label="1">禁用</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="姓名" prop="name">
                      <el-input v-model="temp.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="手机" prop="mobile">
                      <el-input v-model="temp.mobile" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="账号" prop="account">
                      <el-input v-model="temp.account" :disabled="temp.id != undefined" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="工号" prop="govCode">
                      <el-input v-model="temp.govCode" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="密码" prop="password">
                      <el-input v-model="temp.password" type="password" autocomplete="off" placeholder="请输入密码" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="确认密码" prop="checkPass">
                      <el-input v-model="temp.checkPass" type="password" autocomplete="off" placeholder="请输入确认密码" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="用户类型" prop="type">
                      <el-select v-model="temp.type" class="filter-item" placeholder="请选择" style="width:100%;">
                        <el-option v-for="item in typeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="昵称" prop="nickName">
                      <el-input v-model="temp.nickName" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="备注">
                      <el-input v-model="temp.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </el-card>

          </el-col>
          <el-col :span="8">
            <el-card class="box-card" style="margin-top: -20px; margin-left: 10px;">
              <div slot="header" class="clearfix">
                <span>用户角色分配</span>
                <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
              </div>
              <div class="text item">
                <el-table
                  ref="role4EditTable"
                  :data="comapnyRoles"
                  tooltip-effect="dark"
                  style="width: 100%"
                  @selection-change="handleUserRoleChange"
                >
                  <el-table-column
                    type="selection"
                    width="55"
                  />
                  <el-table-column
                    prop="name"
                    label="请选择角色分配给用户"
                  />
                </el-table>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onUpdateUser()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchById, updateUser, deleteUser } from '@/api/user'
import { findCompanyRoles, findUserRoles } from '@/api/khrole'
import { validateMobile } from '@/utils/validate'
import { sheetClose, sheetRefresh } from '@/utils'
import md5 from 'js-md5'

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
    var validatePass = (rule, value, callback) => {
      if (this.temp.id) {
        if (value !== '' && this.temp.checkPass !== '') {
          this.$refs.dataForm.validateField('checkPass')
        }
        callback()
      } else {
        if (value === '') {
          callback(new Error('请输入密码'))
        } else {
          if (this.temp.checkPass !== '') {
            this.$refs.dataForm.validateField('checkPass')
          }
          callback()
        }
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (this.temp.id) {
        if (value !== '' && value !== this.temp.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      } else {
        if (value === '') {
          callback(new Error('请再次输入密码'))
        } else if (value !== this.temp.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }
    }
    return {
      tabPosition: 'right',
      activeTab: 'tabUser',
      tempRoute: {},
      loading: false,
      user: {
        id: undefined,
        uuid: '',
        parentId: '',
        parentClass: '',
        orgName: '',
        orgFullName: '',
        companyName: '',
        name: '',
        account: '',
        govCode: '',
        mobile: '',
        nickName: '',
        type: 1,
        status: 0,
        remark: '',
        roleEnabled: true,
        roleIds: []
      },
      rules: {
        orgName: [{ required: true, message: '请选择归属机构', trigger: 'blur' }],
        account: [{ required: true, message: '账号不能为空', trigger: 'blur' }, { min: 6, max: 64, message: '账号长度在 2 到 64 个字符', trigger: 'blur' }],
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }],
        mobile: [{ validator: (rule, value, callback) => {
          if (value && value !== '') {
            if (!validateMobile(value)) {
              callback(new Error('请输入有效的手机号码'))
              return
            }
          }
          callback()
        }, trigger: 'blur' }],
        govCode: [{ min: 2, max: 64, message: '工号长度在 2 到 64 个字符', trigger: 'blur' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
        checkPass: [{ validator: validatePass2, trigger: 'blur' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
      },
      dialogFormVisible: false,
      statusOptions,
      typeOptions,
      temp: {},
      comapnyRoles: [],
      userRoles: [],
      checkedUserRoles: []
    }
  },
  computed: {
    // TODO
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    const uuid = this.$route.params && this.$route.params.uuid
    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)
    this.fetchData(id, uuid)
    this.findUserRoles(id, uuid)
    this.findCompanyRoles(id, uuid)
  },
  methods: {
    fetchData(id, uuid) {
      fetchById({ id, uuid }).then(response => {
        this.user = response.data
        if (!this.user.orgName || this.user.orgName === '') {
          if (this.user.orgFullName && this.user.orgFullName !== '') {
            this.user.orgName = this.user.orgFullName
          } else {
            this.user.orgName = this.user.companyName
          }
        }
        // console.log('user', this.user)
        this.setTagsViewTitle()
        this.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    findUserRoles(id, uuid) {
      findUserRoles({ id, uuid }).then(response => {
        this.userRoles = response.data
      }).catch(err => {
        console.log(err)
      })
    },
    findCompanyRoles(id, uuid) {
      findCompanyRoles({ id, uuid }).then(response => {
        this.comapnyRoles = response.data
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = '用户视图'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.user.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '用户视图'
      document.title = `${title} - ${this.user.name}`
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    handleUserRoleChange(val) {
      this.checkedUserRoles = val
    },
    parseSelectedUserRoleIds() {
      const ids = []
      if (this.checkedUserRoles && this.checkedUserRoles.length > 0) {
        for (let i = 0; i < this.checkedUserRoles.length; i++) {
          ids[i] = this.checkedUserRoles[i].id
        }
      }
      return ids
    },
    toUpdateUser() {
      this.temp = Object.assign({ dataType: 'Object' }, this.user)
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['userForm'].clearValidate()
        if (this.userRoles && this.userRoles.length > 0) {
          const selectedRoles = []
          for (let i = 0; i < this.userRoles.length; i++) {
            for (let j = 0; j < this.comapnyRoles.length; j++) {
              if (this.userRoles[i].id === this.comapnyRoles[j].id) {
                selectedRoles.push(this.comapnyRoles[j])
                break
              }
            }
          }
          this.$refs.role4EditTable.clearSelection()
          selectedRoles.forEach(row => {
            this.$refs.role4EditTable.toggleRowSelection(row, true)
          })
        }
      })
    },
    onUpdateUser() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          const userObj = Object.assign({ dataType: 'Object' }, this.temp)
          if (userObj.password && userObj.password !== '') {
            userObj.password = md5(userObj.password)
            userObj.checkPass = ''
          }
          userObj.roleEnabled = true
          userObj.roleIds = this.parseSelectedUserRoleIds()
          updateUser(userObj).then(() => {
            this.user = Object.assign({}, userObj)
            this.userRoles = []
            if (this.checkedUserRoles && this.checkedUserRoles.length > 0) {
              for (let i = 0; i < this.checkedUserRoles.length; i++) {
                this.userRoles.push(Object.assign({ }, this.checkedUserRoles[i]))
              }
            }
            this.dialogFormVisible = false
            this.$notify({ title: '提示', message: '用户信息保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    onDeleteUser() {
      this.$confirm('您确定要执行删除操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { id: this.user.id, uuid: this.user.uuid }
        deleteUser(req).then(() => {
          this.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
          this.onClose()
        }).catch(() => {
          this.$notify({ title: '错误', message: '删除失败！', type: 'error', duration: 4000 })
        })
      }).catch((e) => console.log(e))
    }

  }
}
</script>

