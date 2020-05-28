<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="用户信息" name="tabUser">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" title="关闭" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" title="刷新" circle plain @click="onRefresh()" />
          <el-button v-if="user.status !== 8 && checkPermission(['kh_company_m'])" type="success" icon="el-icon-edit" title="编辑" circle @click="toUpdateUser()" />
          <el-button v-if="user.status === 0 && checkPermission(['kh_company_m'])" type="warning" icon="el-icon-lock" title="禁用" circle @click="toChangeUserStatus(1)" />
          <el-button v-if="user.status === 1 && checkPermission(['kh_company_m'])" type="warning" icon="el-icon-unlock" title="启用" circle @click="toChangeUserStatus(0)" />
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
                <span class="el-span_view">{{ user.status | userStatusFilter }}</span>
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
                <span class="el-span_view">{{ user.type | userTypeFilter }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="昵称:" prop="nickName">
                <span class="el-span_view">{{ user.nickName }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="创建时间:" prop="createTime">
                <span class="el-span_view">{{ user.createTime }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新:" prop="updateTime">
                <span class="el-span_view">{{ user.updateTime }}</span>
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
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="角色名称" width="250px" prop="name" sortable>
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="govCode" label="角色编号" width="200px" />
          <el-table-column prop="remark" label="备注说明" min-width="150px" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="兼职机构" name="tabUserOrgs">
        <el-table
          :key="jz.tableKey"
          v-loading="jz.listLoading"
          :data="jz.list"
          border
          fit
          highlight-current-row
          style="width: 100%;"
          @sort-change="jzSortChange"
        >
          <el-table-column label="姓名" width="160px" prop="userName" sortable>
            <template slot-scope="{row}">
              <span>{{ row.userName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="归属机构" width="260px" prop="srcOrgName" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.srcOrgName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="兼职机构" width="260px" prop="destOrgName" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.destOrgName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="兼职时间" width="160px" prop="createTime" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.createTime }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注说明" min-width="100px" prop="remark">
            <template slot-scope="scope">
              <span>{{ scope.row.remark }}</span>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="jz.total>0" :total="jz.total" :page.sync="jz.listQuery.page" :limit.sync="jz.listQuery.limit" @pagination="getJzList" />

      </el-tab-pane>
    </el-tabs>

    <el-dialog title="用户编辑" :visible.sync="dialogFormVisible" width="75%">
      <el-form ref="dataForm" :rules="rules" :inline="false" :model="temp" size="small" status-icon label-position="right" label-width="80px" style="width: 98%; margin-left:10px;">
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
                    <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
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
                  <el-radio-group v-model="temp.type" size="small">
                    <el-radio-button v-for="item in typeOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                  </el-radio-group>
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-permission="['kh_company_m']" type="primary" @click="onUpdateUser()">保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { fetchById, updateUser, changeUserStatus, getJzPageOfUser } from '@/api/khuser'
import { validateMobile } from '@/utils/validate'
import { sheetClose, sheetRefresh } from '@/utils'
import md5 from 'js-md5'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import waves from '@/directive/waves' // waves directive
import { userStatusOptions, userTypeOptions } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'KhUserDetail',
  components: { Pagination },
  directives: { waves, permission },
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
        roleEnabled: false
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
      statusOptions: userStatusOptions(),
      typeOptions: userTypeOptions(),
      temp: {},
      jz: {
        dialogFormVisible: false,
        tableKey: 0,
        list: null,
        loaded: false,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            userId: { fv: undefined, oper: 'eq', stype: 'L' },
            userUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            srcOrgName: { fv: undefined, oper: 'cn', stype: 'S' },
            destOrgName: { fv: undefined, oper: 'cn', stype: 'S' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        rules: {
          name: [{ required: true, message: '请选择用户', trigger: 'blur' }],
          parentName: [{ required: true, message: '请选择兼职机构', trigger: 'blur' }]
        }
      }
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
  },
  methods: {
    checkPermission,
    handleTabClick(tab, event) {
      if (tab.name === 'tabUserOrgs') {
        if (this.jz.loaded === false) {
          this.getJzList()
          this.jz.loaded = true
        }
      }
    },
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
    toUpdateUser() {
      this.temp = Object.assign({ dataType: 'Object' }, this.user)
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['userForm'].clearValidate()
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
          userObj.roleEnabled = false
          updateUser(userObj).then(() => {
            this.user = Object.assign({}, userObj)
            this.dialogFormVisible = false
            this.$notify({ title: '提示', message: '用户信息保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    toChangeUserStatus(status) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { id: this.user.id, uuid: this.user.uuid, status: status }
        changeUserStatus(req).then(() => {
          this.user.status = status
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((e) => console.log(e))
      }).catch((e) => console.log(e))
    },
    /* 兼职管理 */
    getJzList() {
      this.jz.listQuery.rules.userId.fv = this.user.id
      this.jz.listQuery.rules.userUuid.fv = this.user.uuid
      this.jz.listLoading = true
      getJzPageOfUser(this.jz.listQuery).then(response => {
        this.jz.list = response.data.data
        this.jz.total = response.data.recordsTotal
        this.jz.listLoading = false
      }).catch(err => {
        console.log(err)
      })
    },
    handleJzFilter() {
      this.jz.listQuery.page = 1
      this.getJzList()
    },
    jzSortChange(data) {
      const { prop, order } = data
      this.jz.listQuery.orderby.orderby = prop
      if (order === 'ascending') {
        this.jz.listQuery.orderby.order = 'asc'
      } else {
        this.jz.listQuery.orderby.order = 'desc'
      }
      this.handleJzFilter()
    }

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
/*
.app-container >>> .el-dialog__body {
    padding: 0px 0px;
}
*/
#dlg-user-select4user-view >>> .el-dialog__body {
    padding: 0px 0px;
}
#dlg-org-select4user-view >>> .el-dialog__body {
    padding: 0px 20px;
}
.el-card__header {
    padding: 11px 20px;
    border-bottom: 1px solid #e6ebf5;
    box-sizing: border-box;
}
</style>
