<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
      <el-select v-model="listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter" />
      <el-button class="filter-item" style="margin-left: 10px; float: right;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
    </div>

    <el-row>
      <el-col :span="6">
        <el-card class="box-card" shadow="never" style="margin-right:10px;">
          <div slot="header" class="clearfix">
            <span>组织机构</span>
          </div>
          <div class="text item">
            <el-tree
              ref="tree"
              :data="tree.data"
              :props="tree.defaultProps"
              node-key="id"
              :default-expand-all="true"
              :expand-on-click-node="false"
              :check-on-click-node="true"
              show-checkbox
              :check-strictly="true"
              @node-click="handleNodeClick"
              @check-change="handleCheckChange"
            />
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-table
          :key="tableKey"
          v-loading="listLoading"
          :data="list"
          border
          fit
          highlight-current-row
          style="width: 100%;"
          @sort-change="sortChange"
        >
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="姓名" width="160px" prop="name" sortable>
            <template slot-scope="{row}">
              <router-link :to="'/User/user-view/'+row.id+'/'+row.uuid" class="link-type">
                <span>{{ row.name }}</span>
              </router-link>
              <el-tag v-if="row.type == 9">管</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="账号" width="160px" prop="account" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.account }}</span>
            </template>
          </el-table-column>
          <el-table-column label="部门" width="200px">
            <template slot-scope="scope">
              <span>{{ scope.row.orgName || (tree.checkedNode ? tree.checkedNode.name : '') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="手机" width="110px" align="center" prop="mobile" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.mobile }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注说明" min-width="100px" prop="remark">
            <template slot-scope="scope">
              <span>{{ scope.row.remark }}</span>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </el-col>
    </el-row>

    <el-dialog title="新增用户" :visible.sync="dialogFormVisible" width="75%">
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
                  ref="roleTable"
                  :data="roles"
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
        <el-button type="primary" @click="createData()">保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { getPage, createUser } from '@/api/user'
import { findCompanyRoles } from '@/api/khrole'
import { getOrgTree } from '@/api/organization'
import waves from '@/directive/waves' // waves directive
import { validateMobile } from '@/utils/validate'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
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
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
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
      tree: {
        checkedNode: undefined,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      roles: [],
      userRoles: [],
      userRoleIds: [],
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        rules: {
          parentId: { fv: undefined, oper: 'eq', stype: 'L' },
          companyId: { fv: undefined, oper: 'eq', stype: 'L' },
          name: { fv: undefined, oper: 'cn', stype: 'S' },
          mobile: { fv: undefined, oper: 'cn', stype: 'S' },
          status: { fv: undefined, oper: 'eq', stype: 'I' }
        },
        orderby: { orderby: 'id', order: 'desc' }
      },
      statusOptions,
      typeOptions,
      temp: {
        id: undefined,
        uuid: '',
        parentId: '',
        parentClass: '',
        orgName: '',
        name: '',
        account: '',
        govCode: '',
        mobile: '',
        password: '',
        checkPass: '',
        nickName: '',
        type: 1,
        status: 0,
        remark: '',
        roleEnabled: true,
        roleIds: []
      },
      dialogFormVisible: false,
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
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getTree()
  },
  methods: {
    getTree() {
      getOrgTree().then(response => {
        this.tree.data = response.data
        if (this.tree.data[0]) {
          this.tree.checkedNode = this.tree.data[0]
          this.$refs.tree.setCheckedKeys([this.tree.checkedNode.id])
        }
      })
    },
    getCompanyRoles(parentId, parentClass) {
      findCompanyRoles({ parentId, parentClass }).then(response => {
        this.roles = response.data
      })
    },
    handleUserRoleChange(val) {
      this.userRoles = val
      this.userRoleIds = []
      if (this.userRoles && this.userRoles.length > 0) {
        for (let i = 0; i < this.userRoles.length; i++) {
          this.userRoleIds[i] = this.userRoles[i].id
        }
      }
    },
    handleNodeClick(data, checked, node) {
      this.tree.checkedNode = data
      this.$refs.tree.setCheckedNodes([data])
    },
    handleCheckChange(data, checked, node) {
      if (checked === true) {
        this.tree.checkedNode = data
        this.$refs.tree.setCheckedNodes([data])
        if (data.id > 0) {
          this.listQuery.rules.parentId.fv = data.id
          this.listQuery.rules.companyId.fv = undefined
          this.handleFilter()
        } else {
          this.listQuery.rules.parentId.fv = undefined
          this.listQuery.rules.companyId.fv = data.id.substring(1)
          this.handleFilter()
        }
      }
    },
    getList() {
      this.listLoading = true
      getPage(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.recordsTotal
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({ message: '操作Success', type: 'success' })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      this.sort(prop, order)
    },
    sort(prop, order) {
      this.listQuery.orderby.orderby = prop
      if (order === 'ascending') {
        this.listQuery.orderby.order = 'asc'
      } else {
        this.listQuery.orderby.order = 'desc'
      }
      this.handleFilter()
    },
    resetTemp() {
      const selectNode = this.$refs.tree.getCheckedNodes()[0]
      this.temp = {
        id: undefined,
        uuid: '',
        parentId: this.tree.checkedNode.id,
        parentClass: this.tree.checkedNode.id > 0 ? 'KhDepartment' : 'KhCompany',
        orgName: selectNode.name,
        name: '',
        account: '',
        govCode: '',
        mobile: '',
        password: '',
        checkPass: '',
        nickName: '',
        type: 1,
        status: 0,
        remark: '',
        roleEnabled: true,
        roleIds: []
      }
    },
    handleCreate() {
      if (!this.tree.checkedNode) {
        this.$notify({ title: '提示', message: '请先选择一个机构节点，再继续操作！', type: 'warning', duration: 4000 })
        return
      }
      if (this.tree.checkedNode.id > 0) {
        this.getCompanyRoles(this.tree.checkedNode.id, 'KhDepartment')
      } else {
        this.getCompanyRoles(this.tree.checkedNode.id.substring(1), 'KhCompany')
      }
      this.resetTemp()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const user = Object.assign({ dataType: 'Object' }, this.temp)
          user.password = md5(user.password)
          user.checkPass = ''
          user.roleEnabled = true
          user.roleIds = this.userRoleIds
          createUser(user).then((response) => {
            const tempData = Object.assign({}, response.data)
            this.list.unshift(tempData)
            this.dialogFormVisible = false
            this.$notify({ title: '提示', message: '用户创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    }
  }
}
</script>

<style>
.el-card__header {
    padding: 11px 20px;
    border-bottom: 1px solid #e6ebf5;
    box-sizing: border-box;
}
</style>
