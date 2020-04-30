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
        <div class="filter-container">
          <el-input v-model="listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
          <el-input v-model="listQuery.rules.account.fv" placeholder="登录账号 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-input v-model="listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-select v-model="listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 100px">
            <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter" />
          <el-button class="filter-item" style="margin-left: 10px; float: right;" type="primary" icon="el-icon-edit" @click="onSelect">
            添加
          </el-button>
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
          <el-table-column label="部门" width="200px">
            <template slot-scope="scope">
              <span>{{ scope.row.orgFullName || (tree.checkedNode ? tree.checkedNode.name : '') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="手机" width="110px" align="center" prop="mobile" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.mobile }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
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

  </div>
</template>

<script>
import { getPage } from '@/api/user'
import { findCompanyRoles } from '@/api/khrole'
import { getOrgTree, getFullOrgTree } from '@/api/organization'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { userStatusOptions, userTypeOptions } from '@/filters'

export default {
  name: 'LacUserSelect',
  components: { Pagination },
  directives: { waves },
  props: {
    fullOrgs: {
      type: Boolean,
      default: false
    },
    multiSelect: {
      type: Boolean,
      default: false
    }
  },
  data() {
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
          account: { fv: undefined, oper: 'cn', stype: 'S' },
          mobile: { fv: undefined, oper: 'cn', stype: 'S' },
          status: { fv: undefined, oper: 'eq', stype: 'I' }
        },
        orderby: { orderby: 'id', order: 'desc' }
      },
      statusOptions: userStatusOptions(),
      typeOptions: userTypeOptions(),
      dialogFormVisible: false
    }
  },
  created() {
    this.getList()
    this.getTree()
  },
  methods: {
    getTree() {
      if (this.fullOrgs === true) {
        getFullOrgTree().then(response => {
          this.tree.data = response.data
          if (this.tree.data[0]) {
            this.tree.checkedNode = this.tree.data[0]
            this.$refs.tree.setCheckedKeys([this.tree.checkedNode.id])
          }
        })
      } else {
        getOrgTree().then(response => {
          this.tree.data = response.data
          if (this.tree.data[0]) {
            this.tree.checkedNode = this.tree.data[0]
            this.$refs.tree.setCheckedKeys([this.tree.checkedNode.id])
          }
        })
      }
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
    onSelect() {

    }
  }
}
</script>
