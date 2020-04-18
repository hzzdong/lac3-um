<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="角色信息" name="tabRole">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
          <el-button type="success" icon="el-icon-edit" circle @click="toRoleUpdate()" />
          <el-button type="danger" icon="el-icon-delete" circle @click="onRoleDelete()" />
        </div>
        <aside style="margin-top:15px;">
          <i class="el-icon-info" /> 角色视图。您可以通过选择本视图右侧的页签查看和编辑相关功能信息。
        </aside>
        <el-form ref="roleForm" :model="role" size="small" status-icon label-position="right" label-width="120px" style="width: 98%;">
          <el-form-item label="角色名称:" prop="name">
            <span class="el-span_view">{{ role.name }}</span>
          </el-form-item>
          <el-form-item label="角色编号:" prop="govCode">
            <span class="el-span_view">{{ role.govCode }}</span>
          </el-form-item>
          <el-form-item label="状态:" prop="status">
            <span class="el-span_view">{{ role.status | statusFilter }}</span>
          </el-form-item>
          <el-form-item label="排序号:" prop="sort">
            <span class="el-span_view">{{ role.sort }}</span>
          </el-form-item>
          <el-form-item label="创建时间:" prop="createTime">
            <span class="el-span_view">{{ role.createTime }}</span>
          </el-form-item>
          <el-form-item label="备注:">
            <el-input v-model="role.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" class="el-textarea_view" />
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="角色分配" name="tabRoleUsers">
        <aside>
          <i class="el-icon-info" /> 角色[ <span>{{ role.name }}</span> ]已分配用户列表。您可以在此给角色分配用户，移除用户。
        </aside>
        <div class="filter-container">
          <el-input v-model="roleUsers.listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="queryRoleUsers" />
          <el-input v-model="roleUsers.listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-select v-model="roleUsers.listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in roleUsers.statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryRoleUsers" />
          <el-button class="filter-item" style="float: right;" type="primary" icon="el-icon-plus" @click="toSelectUsers()">
            添加
          </el-button>
        </div>
        <el-table
          ref="userTable"
          :data="roleUsers.list"
          tooltip-effect="dark"
          border
          style="width: 100%"
        >
          <el-table-column label="" class-name="status-col" width="40" prop="status" sortable>
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="姓名" width="180px" prop="name" sortable>
            <template slot-scope="{row}">
              <router-link :to="'/KhCompany/user-view/'+row.id+'/'+row.uuid" class="link-type">
                <span>{{ row.name }}</span>
              </router-link>
              <el-tag v-if="row.type == 9">管</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="账号" width="150px" align="center" prop="account" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.account }}</span>
            </template>
          </el-table-column>
          <el-table-column label="部门" width="250px" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.orgFullName || scope.row.companyName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="手机" width="120px" align="center" prop="mobile" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.mobile }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注说明" min-width="100px" />
          <el-table-column label="操作" align="center" width="60" class-name="small-padding">
            <template slot-scope="{row}">
              <el-button type="danger" size="mini" icon="el-icon-close" title="移除" @click="onRoleUserDelete(row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="roleUsers.total>0" :total="roleUsers.total" :page.sync="roleUsers.listQuery.page" :limit.sync="roleUsers.listQuery.limit" @pagination="findRoleUsers" />
      </el-tab-pane>
      <el-tab-pane label="应用授权" name="tabRoleApps">
        <aside>
          <i class="el-icon-info" /> 角色[ <span>{{ role.name }}</span> ]已许可应用列表。您可以在此给角色许可应用，并配置应用权限。
        </aside>
        <el-row>
          <el-col :span="12">
            <div class="filter-container">
              <el-input v-model="roleApps.listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 180px;" class="filter-item" @keyup.enter.native="queryRoleUsers" />
              <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryRoleApps" />
              <el-button class="filter-item" style="float: right;" type="primary" icon="el-icon-plus" @click="toSelectApps()">
                添加许可
              </el-button>
            </div>
            <el-table
              ref="roleAppTable"
              :data="roleApps.list"
              tooltip-effect="dark"
              border
              style="width: 100%"
              highlight-current-row
              @current-change="handleAppSelectedChange"
            >
              <el-table-column label="" class-name="status-col" width="40" prop="status" sortable>
                <template slot-scope="{row}">
                  <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
                </template>
              </el-table-column>
              <el-table-column label="名称" min-width="100px" prop="name" sortable>
                <template slot-scope="{row}">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="编号" width="160px" prop="code" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.code }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="50" class-name="small-padding">
                <template slot-scope="{row}">
                  <el-button type="danger" size="mini" icon="el-icon-close" title="移除" @click="onRoleAppDelete(row)" />
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="roleApps.total>0" :total="roleApps.total" :page.sync="roleApps.listQuery.page" :limit.sync="roleApps.listQuery.limit" @pagination="findRoleApps" />
          </el-col>
          <el-col :span="12">
            <div class="filter-container">
              <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-circle-check" @click="onSaveRoleAppPermission()">
                保存权限配置
              </el-button>
            </div>
            <div style="padding-left: 10px;">
              <el-tabs v-model="activePermissionTab" type="border-card" @tab-click="handlePermissionTabClick">
                <el-tab-pane name="tabPermMenu" label="菜单权限">
                  <el-tree
                    ref="tree_menu"
                    node-key="id"
                    show-checkbox
                    :data="menuTree.data"
                    :props="menuTree.defaultProps"
                    :default-expand-all="true"
                    :expand-on-click-node="false"
                    :check-on-click-node="true"
                    :check-strictly="true"
                  />
                </el-tab-pane>
                <el-tab-pane name="tabPermOrg" label="机构权限">
                  <el-tree
                    ref="tree_org"
                    node-key="id"
                    show-checkbox
                    :data="orgTree.data"
                    :props="orgTree.defaultProps"
                    :default-expand-all="true"
                    :expand-on-click-node="false"
                    :check-on-click-node="true"
                    :check-strictly="true"
                  />
                </el-tab-pane>
                <el-tab-pane name="tabPermArea" label="区域权限">
                  <el-tree
                    ref="tree_area"
                    node-key="id"
                    show-checkbox
                    :data="areaTree.data"
                    :props="areaTree.defaultProps"
                    :expand-on-click-node="false"
                    :check-on-click-node="true"
                    :check-strictly="true"
                  />
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="角色编辑" :visible.sync="dialogFormVisible">
      <el-form ref="roleForm" :rules="rules" :inline="false" :model="temp" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="角色编号" prop="govCode">
          <el-input v-model="temp.govCode" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="temp.status" size="small">
            <el-radio-button label="0">正常</el-radio-button>
            <el-radio-button label="1">禁用</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model="temp.sort" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="onRoleUpdate()">
          保存
        </el-button>
      </div>
    </el-dialog>

    <el-dialog title="角色可分配人员选择" :visible.sync="users.dialogFormVisible" width="75%">
      <aside>
        <i class="el-icon-info" /> 角色[ <span>{{ role.name }}</span> ]可分配用户列表。选择用户后点击“确定选择”按钮即可把角色分配给选中用户。
      </aside>
      <div class="filter-container">
        <el-input v-model="users.listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="queryRoleUsers" />
        <el-input v-model="users.listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryUnRoleUsers()" />
        <el-button class="filter-item" style="float: right;" type="primary" icon="el-icon-check" @click="onSelectUsers()">
          确定选择
        </el-button>
      </div>
      <el-table
        ref="unRoleUserTable"
        :data="users.list"
        tooltip-effect="dark"
        border
        style="width: 100%"
        @selection-change="handleSelectUserChange"
        @row-click="handleUserRowClick"
      >
        <el-table-column type="selection" width="40" />
        <el-table-column label="" class-name="status-col" width="40" prop="status" sortable>
          <template slot-scope="{row}">
            <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
          </template>
        </el-table-column>
        <el-table-column label="姓名" width="180px" prop="name" sortable>
          <template slot-scope="{row}">
            <router-link :to="'/KhCompany/user-view/'+row.id+'/'+row.uuid" class="link-type">
              <span>{{ row.name }}</span>
            </router-link>
            <el-tag v-if="row.type == 9">管</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="账号" width="150px" align="center" prop="account" sortable>
          <template slot-scope="scope">
            <span>{{ scope.row.account }}</span>
          </template>
        </el-table-column>
        <el-table-column label="部门" width="250px" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.orgFullName || scope.row.companyName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="手机" width="120px" align="center" prop="mobile" sortable>
          <template slot-scope="scope">
            <span>{{ scope.row.mobile }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注说明" min-width="100px" />
      </el-table>
      <pagination v-show="users.total>0" :total="users.total" :page.sync="users.listQuery.page" :limit.sync="users.listQuery.limit" @pagination="findUnRoleUsers" />
    </el-dialog>

    <el-dialog title="角色可许可应用选择" :visible.sync="apps.dialogFormVisible" width="75%">
      <aside>
        <i class="el-icon-info" /> 角色[ <span>{{ role.name }}</span> ]可许可应用列表。选择应用后点击“确定选择”按钮即可给角色许可选中应用。
      </aside>
      <div class="filter-container">
        <el-input v-model="apps.listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="queryRoleApps" />
        <el-input v-model="apps.listQuery.rules.code.fv" placeholder="编号 精确匹配" style="width: 150px;" class="filter-item" />
        <el-select v-model="apps.listQuery.rules.type.fv" placeholder="请选择类型" clearable class="filter-item" style="width: 150px">
          <el-option v-for="item in apps.appTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryUnRoleApps()" />
        <el-button class="filter-item" style="float: right;" type="primary" icon="el-icon-check" @click="onSelectApps()">
          确定选择
        </el-button>
      </div>
      <el-table
        ref="unRoleAppTable"
        :data="apps.list"
        tooltip-effect="dark"
        border
        style="width: 100%"
        @selection-change="handleSelectAppChange"
        @row-click="handleAppRowClick"
      >
        <el-table-column type="selection" width="40" />
        <el-table-column label="" class-name="status-col" width="40" prop="status" sortable>
          <template slot-scope="{row}">
            <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
          </template>
        </el-table-column>
        <el-table-column label="应用名称" width="180px" prop="name" sortable>
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="编号" width="150px" prop="code" sortable>
          <template slot-scope="scope">
            <span>{{ scope.row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column label="类型" width="80" prop="type" align="center" sortable>
          <template slot-scope="{row}">
            <el-tag size="small">{{ row.type | appTypeFilter }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注说明" min-width="100px" />
      </el-table>
      <pagination v-show="apps.total>0" :total="apps.total" :page.sync="apps.listQuery.page" :limit.sync="apps.listQuery.limit" @pagination="findUnRoleApps" />
    </el-dialog>

  </div>
</template>

<script>
import { fetchById, updateKhRole, deleteKhRole, addRoleUsers, removeRoleUser, addRoleApps, removeRoleApp, getPermedMenuTree, getPermedOrgTree, getPermedAreaTree, saveRoleAppMenuPerm, saveRoleAppOrgPerm, saveRoleAppAreaPerm } from '@/api/khrole'
import { findRoleUsers, findUnRoleUsers } from '@/api/user'
import { findAppPage4KhRole, findAppPage4UnKhRole } from '@/api/application'
import { sheetClose, sheetRefresh, parseCheckedTreeIds } from '@/utils'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const statusOptions = [
  { key: 0, display_name: '正常' },
  { key: 1, display_name: '禁用' }
]

const typeOptions = [
  { key: 1, display_name: '普通用户' },
  { key: 9, display_name: '管理员' }
]

const appTypeOptions = [
  { key: 0, display_name: '内部' },
  { key: 1, display_name: '外部' }
]

// arr to obj, such as { 0 : "正常", 1 : "禁用" }
const statusKeyValue = statusOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

const typeKeyValue = typeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

const appTypeKeyValue = appTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'RoleDetail',
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
    },
    appTypeFilter(type) {
      return appTypeKeyValue[type]
    }
  },
  data() {
    return {
      tabPosition: 'right',
      activeTab: 'tabRole',
      tempRoute: {},
      role: {
        id: undefined,
        name: '',
        govCode: '',
        status: 0,
        remark: ''
      },
      rules: {
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
        govCode: [{ required: true, message: '角色编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到32 个字符', trigger: 'blur' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
      },
      dialogFormVisible: false,
      temp: {},
      loading: false,
      /* 角色分配 */
      roleUsers: {
        loaded: false,
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            roleId: { fv: undefined, oper: 'eq', stype: 'L' },
            roleUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            mobile: { fv: undefined, oper: 'cn', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        statusOptions,
        typeOptions,
        dialogFormVisible: false
      },
      users: {
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            roleId: { fv: undefined, oper: 'eq', stype: 'L' },
            roleUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            mobile: { fv: undefined, oper: 'cn', stype: 'S' },
            status: { fv: 0, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        statusOptions,
        typeOptions,
        dialogFormVisible: false
      },
      selectedUsers: [],
      /* 应用许可 */
      roleApps: {
        loaded: false,
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            roleId: { fv: undefined, oper: 'eq', stype: 'L' },
            roleUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            roleType: { fv: 'KhRole', oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            code: { fv: undefined, oper: 'eq', stype: 'S' },
            type: { fv: undefined, oper: 'eq', stype: 'I' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        statusOptions,
        appTypeOptions,
        dialogFormVisible: false
      },
      selectedApps: [],
      apps: {
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            roleId: { fv: undefined, oper: 'eq', stype: 'L' },
            roleUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            roleType: { fv: 'KhRole', oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            code: { fv: undefined, oper: 'eq', stype: 'S' },
            type: { fv: undefined, oper: 'eq', stype: 'I' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        statusOptions,
        appTypeOptions,
        dialogFormVisible: false
      },
      /* 菜单权限 */
      currentApp: undefined,
      activePermissionTab: 'tabPermMenu',
      menuTree: {
        loaded: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      orgTree: {
        loaded: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      areaTree: {
        loaded: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
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
    this.roleUsers.listQuery.rules.roleId.fv = id
    this.roleUsers.listQuery.rules.roleUuid.fv = uuid
    this.users.listQuery.rules.roleId.fv = id
    this.users.listQuery.rules.roleUuid.fv = uuid
    this.roleApps.listQuery.rules.roleId.fv = id
    this.roleApps.listQuery.rules.roleUuid.fv = uuid
    this.apps.listQuery.rules.roleId.fv = id
    this.apps.listQuery.rules.roleUuid.fv = uuid
    this.fetchData(id, uuid)
    this.tempRoute = Object.assign({}, this.$route)
  },
  methods: {
    fetchData(id, uuid) {
      fetchById({ id, uuid }).then(response => {
        this.role = response.data
        this.setTagsViewTitle()
        this.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = '角色视图'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.role.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '角色视图'
      document.title = `${title} - ${this.role.name}`
    },
    handleTabClick(tab, event) {
      if (tab.name === 'tabRoleUsers') {
        if (this.roleUsers.loaded === false) {
          this.findRoleUsers()
          this.roleUsers.loaded = true
        }
      } else if (tab.name === 'tabRoleApps') {
        if (this.roleApps.loaded === false) {
          this.findRoleApps()
          this.roleApps.loaded = true
        }
      }
    },
    toRoleUpdate() {
      this.temp = Object.assign({ dataType: 'Object' }, this.role)
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['roleForm'].clearValidate()
      })
    },
    onRoleUpdate() {
      this.$refs['roleForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({ dataType: 'Object' }, this.temp)
          updateKhRole(tempData).then(() => {
            this.role = Object.assign({}, this.temp)
            this.dialogFormVisible = false
            this.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    onRoleDelete() {
      this.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = this.role
        deleteKhRole({ id, uuid }).then(() => {
          this.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
          this.onClose()
        }).catch(() => {
          this.$notify({ title: '错误', message: '删除失败！', type: 'error', duration: 4000 })
        })
      }).catch(() => {
      })
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    // 以下为角色用户分配
    findRoleUsers() {
      this.roleUsers.listLoading = true
      findRoleUsers(this.roleUsers.listQuery).then(response => {
        this.roleUsers.list = response.data.data
        this.roleUsers.total = response.data.recordsTotal
        this.roleUsers.listLoading = false
      })
    },
    findUnRoleUsers() {
      this.users.listLoading = true
      findUnRoleUsers(this.users.listQuery).then(response => {
        this.users.list = response.data.data
        this.users.total = response.data.recordsTotal
        this.users.listLoading = false
      })
    },
    queryRoleUsers() {
      this.roleUsers.listQuery.page = 1
      this.findRoleUsers()
    },
    queryUnRoleUsers() {
      this.users.listQuery.page = 1
      this.findUnRoleUsers()
    },
    toSelectUsers() {
      this.findUnRoleUsers()
      this.users.dialogFormVisible = true
    },
    handleSelectUserChange(val) {
      this.selectedUsers = val
    },
    handleUserRowClick(row, column, event) {
      this.$refs.unRoleUserTable.toggleRowSelection(row)
    },
    onSelectUsers() {
      if (!this.selectedUsers || this.selectedUsers.length <= 0) {
        this.$notify({ title: '提醒', message: '请先选择用户然后再操作！', type: 'error', duration: 4000 })
      } else {
        const req = { id: this.role.id, uuid: this.role.uuid, uuidIds: {}}
        for (let i = 0; i < this.selectedUsers.length; i++) {
          const rowData = this.selectedUsers[i]
          req.uuidIds[rowData.uuid] = rowData.id
        }

        addRoleUsers(req).then(() => {
          this.users.dialogFormVisible = false
          for (let i = 0; i < this.selectedUsers.length; i++) {
            this.roleUsers.list.unshift(this.selectedUsers[i])
          }
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }
    },
    onRoleUserDelete(row) {
      this.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { parentId: this.role.id, parentUuid: this.role.uuid, id: row.id, uuid: row.uuid }
        removeRoleUser(req).then(() => {
          const index = this.roleUsers.list.indexOf(row)
          this.roleUsers.list.splice(index, 1)
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }).catch(() => {
      })
    },
    // 以下是角色应用许可授权
    findRoleApps() {
      this.roleApps.listLoading = true
      findAppPage4KhRole(this.roleApps.listQuery).then(response => {
        this.roleApps.list = response.data.data
        this.roleApps.total = response.data.recordsTotal
        this.roleApps.listLoading = false
        this.$nextTick(() => {
          if (this.roleApps.list && this.roleApps.list.length > 0) {
            this.currentApp = this.roleApps.list[0]
            this.$refs.roleAppTable.setCurrentRow(this.currentApp)
            // 加载权限
          }
        })
      })
    },
    findUnRoleApps() {
      this.apps.listLoading = true
      findAppPage4UnKhRole(this.apps.listQuery).then(response => {
        this.apps.list = response.data.data
        this.apps.total = response.data.recordsTotal
        this.apps.listLoading = false
      })
    },
    queryRoleApps() {
      this.roleApps.listQuery.page = 1
      this.findRoleApps()
    },
    queryUnRoleApps() {
      this.apps.listQuery.page = 1
      this.findUnRoleApps()
    },
    toSelectApps() {
      this.findUnRoleApps()
      this.apps.dialogFormVisible = true
    },
    onSelectApps() {
      if (!this.selectedApps || this.selectedApps.length <= 0) {
        this.$notify({ title: '提醒', message: '请先选择应用然后再操作！', type: 'error', duration: 4000 })
      } else {
        const req = { id: this.role.id, uuid: this.role.uuid, uuidIds: {}}
        for (let i = 0; i < this.selectedApps.length; i++) {
          const rowData = this.selectedApps[i]
          req.uuidIds[rowData.uuid] = rowData.id
        }

        addRoleApps(req).then(() => {
          this.apps.dialogFormVisible = false
          for (let i = 0; i < this.selectedApps.length; i++) {
            this.roleApps.list.unshift(this.selectedApps[i])
          }
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }
    },
    onRoleAppDelete(row) {
      this.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { parentId: this.role.id, parentUuid: this.role.uuid, id: row.id, uuid: row.uuid }
        removeRoleApp(req).then(() => {
          const index = this.roleApps.list.indexOf(row)
          this.roleApps.list.splice(index, 1)
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }).catch(() => {
      })
    },
    handleSelectAppChange(val) {
      this.selectedApps = val
    },
    handleAppRowClick(row, column, event) {
      this.$refs.unRoleAppTable.toggleRowSelection(row)
    },
    // 以下是应用授权
    handlePermissionTabClick(tab, event) {
      if (tab.name === 'tabPermMenu') {
        if (this.menuTree.loaded === false) {
          this.loadMenuTree(this.role.id, this.role.uuid, this.currentApp.id, this.currentApp.uuid)
          this.menuTree.loaded = true
        }
      } else if (tab.name === 'tabPermOrg') {
        if (this.orgTree.loaded === false) {
          this.loadOrgTree(this.role.id, this.role.uuid, this.currentApp.id, this.currentApp.uuid)
          this.orgTree.loaded = true
        }
      } else if (tab.name === 'tabPermArea') {
        if (this.areaTree.loaded === false) {
          this.loadAreaTree(this.role.id, this.role.uuid, this.currentApp.id, this.currentApp.uuid)
          this.areaTree.loaded = true
        }
      }
    },
    handleAppSelectedChange(val) {
      this.currentApp = val
      if (this.activePermissionTab === 'tabPermMenu') {
        this.loadMenuTree(this.role.id, this.role.uuid, this.currentApp.id, this.currentApp.uuid)
        this.menuTree.loaded = true
        this.orgTree.loaded = false
        this.areaTree.loaded = false
      } else if (this.activePermissionTab === 'tabPermOrg') {
        this.loadOrgTree(this.role.id, this.role.uuid, this.currentApp.id, this.currentApp.uuid)
        this.menuTree.loaded = false
        this.orgTree.loaded = true
        this.areaTree.loaded = false
      } else if (this.activePermissionTab === 'tabPermArea') {
        this.loadAreaTree(this.role.id, this.role.uuid, this.currentApp.id, this.currentApp.uuid)
        this.menuTree.loaded = false
        this.orgTree.loaded = false
        this.areaTree.loaded = true
      }
    },
    loadMenuTree(roleId, roleUuid, appId, appUuid) {
      const req = { parentId: roleId, parentUuid: roleUuid, id: appId, uuid: appUuid }
      getPermedMenuTree(req).then(response => {
        this.menuTree.data = response.data
        const checkedIds = []
        parseCheckedTreeIds(checkedIds, this.menuTree.data)
        this.$refs.tree_menu.setCheckedKeys(checkedIds)
      })
    },
    loadOrgTree(roleId, roleUuid, appId, appUuid) {
      const req = { parentId: roleId, parentUuid: roleUuid, id: appId, uuid: appUuid }
      getPermedOrgTree(req).then(response => {
        this.orgTree.data = response.data
        const checkedIds = []
        parseCheckedTreeIds(checkedIds, this.orgTree.data)
        this.$refs.tree_org.setCheckedKeys(checkedIds)
      })
    },
    loadAreaTree(roleId, roleUuid, appId, appUuid) {
      const req = { parentId: roleId, parentUuid: roleUuid, id: appId, uuid: appUuid }
      getPermedAreaTree(req).then(response => {
        this.areaTree.data = response.data
        const checkedIds = []
        parseCheckedTreeIds(checkedIds, this.areaTree.data)
        this.$refs.tree_area.setCheckedKeys(checkedIds)
      })
    },
    onSaveRoleAppPermission() {
      if (this.activePermissionTab === 'tabPermMenu') {
        const items = this.$refs.tree_menu.getCheckedNodes()
        const req = { parentId: this.role.id, parentUuid: this.role.uuid, id: this.currentApp.id, uuid: this.currentApp.uuid, uuidIds: {}}
        for (let i = 0; i < items.length; i++) {
          const item = items[i]
          req.uuidIds[item.uuid] = item.id
        }
        saveRoleAppMenuPerm(req).then(() => {
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      } else if (this.activePermissionTab === 'tabPermOrg') {
        const items = this.$refs.tree_org.getCheckedNodes()
        const req = { parentId: this.role.id, parentUuid: this.role.uuid, id: this.currentApp.id, uuid: this.currentApp.uuid, uuidIds: {}}
        for (let i = 0; i < items.length; i++) {
          const item = items[i]
          req.uuidIds[item.uuid] = item.id
        }
        saveRoleAppOrgPerm(req).then(() => {
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      } else if (this.activePermissionTab === 'tabPermArea') {
        const items = this.$refs.tree_area.getCheckedNodes()
        const req = { parentId: this.role.id, parentUuid: this.role.uuid, id: this.currentApp.id, uuid: this.currentApp.uuid, uuidIds: {}}
        for (let i = 0; i < items.length; i++) {
          const item = items[i]
          req.uuidIds[item.uuid] = item.id
        }
        saveRoleAppAreaPerm(req).then(() => {
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }
    }

  }
}
</script>
