<template>
  <div class="app-container">
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
        <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
          <el-tab-pane label="用户列表" name="tabUser">

            <div class="filter-container">
              <el-input v-model="listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
              <el-input v-model="listQuery.rules.account.fv" placeholder="登录账号 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-input v-model="listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-select v-model="listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 100px">
                <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
              </el-select>
              <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter" />
            </div>
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
                  <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
                </template>
              </el-table-column>
              <el-table-column label="姓名" width="160px" prop="name" sortable>
                <template slot-scope="{row}">
                  <span>{{ row.name }}</span>
                  <el-tag v-if="row.type == 9">管</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="账号" width="160px" prop="account" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.account }}</span>
                </template>
              </el-table-column>
              <el-table-column label="部门" min-width="100px">
                <template slot-scope="scope">
                  <span>{{ scope.row.orgFullName || (tree.checkedNode ? tree.checkedNode.name : '') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="手机" width="110px" align="center" prop="mobile" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.mobile }}</span>
                </template>
              </el-table-column>
              <el-table-column label="工号" width="110px" prop="govCode" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.govCode }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="70px">
                <template slot-scope="scope">
                  <el-button type="primary" size="mini" title="选择" @click="onUserSelect(scope.row)">选择</el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
          </el-tab-pane>
          <el-tab-pane label="兼职列表" name="tabJzUser" :disabled="!canSelectJz">
            <div class="filter-container">
              <el-input v-model="jz.listQuery.rules.userName.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
              <el-input v-model="jz.listQuery.rules.srcOrgName.fv" placeholder="归属机构 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-input v-model="jz.listQuery.rules.destOrgName.fv" placeholder="兼职机构 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleJzFilter" />
            </div>
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
              <el-table-column label="姓名" min-width="100px" prop="userName" sortable>
                <template slot-scope="{row}">
                  <span v-if="checkPermission(['selfkh_user_view']) === false">{{ row.userName }}</span>
                  <router-link v-if="checkPermission(['selfkh_user_view']) === true" :to="'/User/user-view/'+row.userId+'/'+row.userUuid" class="link-type">
                    <span>{{ row.userName }}</span>
                  </router-link>
                </template>
              </el-table-column>
              <el-table-column label="归属机构" width="220px" prop="srcOrgName" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.srcOrgName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="兼职机构" width="220px" prop="destOrgName" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.destOrgName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="兼职时间" width="160px" prop="createTime" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.createTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="70px">
                <template slot-scope="scope">
                  <el-button type="primary" size="mini" title="选择" @click="onJzUserSelect(scope.row)">选择</el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="jz.total>0" :total="jz.total" :page.sync="jz.listQuery.page" :limit.sync="jz.listQuery.limit" @pagination="getJzList" />
          </el-tab-pane>
        </el-tabs>

      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getPage, getJzPage } from '@/api/user'
import { findCompanyRoles } from '@/api/ywrole'
import { getCompanyTree } from '@/api/organization'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { userStatusOptions, userTypeOptions } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'LacUserSingleSelect',
  components: { Pagination },
  directives: { waves, permission },
  props: {
    treeType: {
      type: String,
      default: 'SelfTree'
    },
    companyId: {
      type: Number,
      default: -1
    },
    companyUuid: {
      type: String,
      default: ''
    },
    canSelectJz: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      checkPermission,
      activeTab: 'tabUser',
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
      loaded: false,
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
          account: { fv: undefined, oper: 'cn', stype: 'S' },
          mobile: { fv: undefined, oper: 'cn', stype: 'S' },
          status: { fv: 0, oper: 'eq', stype: 'I' }
        },
        orderby: { orderby: 'id', order: 'desc' }
      },
      statusOptions: userStatusOptions(),
      typeOptions: userTypeOptions(),
      dialogFormVisible: false,
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
            destOrgId: { fv: undefined, oper: 'eq', stype: 'L' },
            destOrgClass: { fv: undefined, oper: 'eq', stype: 'S' },
            userStatusNe: { fv: 8, oper: 'eq', stype: 'I' },
            userName: { fv: undefined, oper: 'cn', stype: 'S' },
            srcOrgName: { fv: undefined, oper: 'cn', stype: 'S' },
            destOrgName: { fv: undefined, oper: 'cn', stype: 'S' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        }
      }
    }
  },
  created() {
    this.getTree()
  },
  methods: {
    loadTab(activityTabName) {
      debugger
      if (activityTabName === 'tabUser') {
        if (this.loaded === false) {
          this.getList()
          this.loaded = true
        }
      } else if (activityTabName === 'tabJzUser') {
        if (this.jz.loaded === false) {
          this.getJzList()
          this.jz.loaded = true
        }
      }
    },
    resetTab() {
      this.loaded = false
      this.jz.loaded = false
    },
    handleTabClick(tab, event) {
      this.loadTab(tab.name)
    },
    getTree() {
      const req = { id: this.companyId, uuid: this.companyUuid, type: this.treeType }
      getCompanyTree(req).then(response => {
        this.tree.data = response.data
        if (this.tree.data[0]) {
          this.tree.checkedNode = this.tree.data[0]
          this.$refs.tree.setCheckedKeys([this.tree.checkedNode.id])
        }
        this.getList()
      })
    },
    getCompanyRoles(parentId, parentClass) {
      findCompanyRoles({ parentId, parentClass }).then(response => {
        this.roles = response.data
      })
    },
    handleNodeClick(data, checked, node) {
      this.tree.checkedNode = data
      this.$refs.tree.setCheckedNodes([data])
    },
    handleCheckChange(data, checked, node) {
      if (checked === true) {
        this.tree.checkedNode = data
        this.$refs.tree.setCheckedNodes([data])
        this.resetTab()
        this.loadTab(this.activeTab)
      }
    },
    getList() {
      if (this.tree.checkedNode.id > 0) {
        this.listQuery.rules.parentId.fv = this.tree.checkedNode.id
        this.listQuery.rules.companyId.fv = undefined
      } else {
        this.listQuery.rules.parentId.fv = undefined
        this.listQuery.rules.companyId.fv = this.tree.checkedNode.id.substring(1)
      }
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
    onUserSelect(row) {
      // console.log('row', row)
      if (!row.orgFullName || row.orgFullName === '') {
        row.orgFullName = row.companyName
      }
      this.$emit('onUserSingleSelected', row)
    },
    /* 兼职管理 */
    getJzList() {
      const that = this
      const orgId = Number.parseInt(that.tree.checkedNode.id)
      if (orgId > 0) {
        that.jz.listQuery.rules.destOrgId.fv = orgId
        that.jz.listQuery.rules.destOrgClass.fv = 'YwDepartment'
      } else {
        that.jz.listQuery.rules.destOrgId.fv = orgId * -1 // this.tree.checkedNode.id.substring(1)
        that.jz.listQuery.rules.destOrgClass.fv = 'YwCompany'
      }

      that.jz.listLoading = true
      getJzPage(that.jz.listQuery).then(response => {
        that.jz.list = response.data.data
        that.jz.total = response.data.recordsTotal
        that.jz.listLoading = false
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
    },
    onJzUserSelect(row) {
      // console.log('row', row)
      const selectedUser = { status: 7, id: row.userId, uuid: row.userUuid, name: row.userName, orgFullName: row.destOrgName, orgName: row.destOrgName, srcOrgName: row.srcOrgName }
      this.$emit('onUserSingleSelected', selectedUser)
    }
  }
}
</script>

