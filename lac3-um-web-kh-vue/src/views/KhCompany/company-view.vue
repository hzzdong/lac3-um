<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="单位基本信息" name="tabCompany">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
        </div>
        <el-form ref="companyForm" :model="company" size="small" status-icon label-position="right" label-width="120px" style="width: 100%;">
          <el-card class="box-card" style="margin-top: -10px; margin-bottom: 10px;">
            <div slot="header" class="clearfix">
              <span>单位基本信息</span>
              <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="上级单位:" prop="orgName">
                    <span class="el-span_view">{{ company.orgName || '无' }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="状态:" prop="status">
                    <span class="el-span_view">{{ company.status | statusFilter }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="单位名称:" prop="name">
                    <span class="el-span_view">{{ company.name }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="单位编码:" prop="govCode">
                    <span class="el-span_view">{{ company.govCode }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="电话:" prop="phone">
                    <span class="el-span_view">{{ company.phone }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="传真:" prop="fax">
                    <span class="el-span_view">{{ company.fax }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="所在区域:" prop="areaName">
                    <span class="el-span_view">{{ company.areaName }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="单位网址:" prop="url">
                    <span class="el-span_view">{{ company.url }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="单位地址:" prop="address">
                    <span class="el-span_view">{{ company.address }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="排序号:" prop="sort">
                    <span class="el-span_view">{{ company.sort }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.khTypeCode === 'kh_qy'">
                <el-col :span="12">
                  <el-form-item label="单位规模:" prop="scale">
                    <span class="el-span_view">{{ company.scale }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="业务开通:" prop="businessStart">
                    <span class="el-span_view">{{ company.businessStart }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.khTypeCode === 'kh_qy'">
                <el-col :span="12">
                  <el-form-item label="证照类型:" prop="certificateType">
                    <span class="el-span_view">{{ company.certificateType | certificateTypeFilter }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="证照号码:" prop="certificateNo">
                    <span class="el-span_view">{{ company.certificateNo }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.khTypeCode === 'kh_qy'">
                <el-col :span="24">
                  <el-form-item label="经营范围:" prop="business">
                    <el-input v-model="company.business" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="经营范围" class="el-textarea_view" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.khTypeCode === 'kh_qy'">
                <el-col :span="24">
                  <el-form-item label="单位资质:" prop="credentials">
                    <el-input v-model="company.credentials" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="单位资质" class="el-textarea_view" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="备注:">
                    <el-input v-model="company.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" class="el-textarea_view" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div></el-card>
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>单位联系人信息</span>
              <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="联系人/法人:" prop="juridical">
                    <span class="el-span_view">{{ company.juridical }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="联系电话:" prop="jphone">
                    <span class="el-span_view">{{ company.jphone }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="证照类型:" prop="jType">
                    <span class="el-span_view">{{ company.jType | personCertificateTypeFilter }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="证照号码:" prop="jNo">
                    <span class="el-span_view">{{ company.jNo }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="单位机构树预览" name="tabCompanyTreeView">
        <el-table
          ref="companyTreeViewTable"
          :data="companyTree"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="name" label="名称" width="250">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="govCode" label="编号" width="150" />
          <el-table-column label="联系人" width="120">
            <template slot-scope="{row}">
              <span>{{ row.attributes.linkUserName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="联系方式" width="110">
            <template slot-scope="{row}">
              <span>{{ row.attributes.linkUserPhone }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="类型" width="80">
            <template slot-scope="{row}">
              <span v-if="row.attributes.alias === 'Company'">公司</span>
              <span v-if="row.attributes.alias !== 'Company'">部门<span v-if="row.type === '1'">(管)</span></span>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="60" />
          <el-table-column label="" class-name="status-col" width="40" prop="status">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注说明" min-width="100" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="单位许可应用" name="tabCompanyApps">
        <div class="filter-container">
          <el-button v-if="isMyCompany === false" class="filter-item" style="float: right;" type="primary" icon="el-icon-plus" @click="toSelectApps()">
            添加许可
          </el-button>
        </div>
        <el-table
          :key="companyApps.tableKey"
          ref="companyAppTable"
          v-loading="companyApps.listLoading"
          :data="companyApps.list"
          tooltip-effect="dark"
          style="width: 100%"
          border
          fit
        >
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="应用名称" width="250px" prop="name">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="code" label="编号" width="200px" />
          <el-table-column prop="remark" label="备注说明" min-width="150px" />
          <el-table-column v-if="isMyCompany === false" label="操作" align="center" width="50" class-name="small-padding">
            <template slot-scope="{row}">
              <el-button type="danger" size="mini" icon="el-icon-close" title="移除" @click="onCompanyAppDelete(row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="companyApps.total>0" :total="companyApps.total" :page.sync="companyApps.listQuery.page" :limit.sync="companyApps.listQuery.limit" @pagination="getCompanyApps" />
      </el-tab-pane>
      <el-tab-pane label="单位根区域" name="tabCompanyAreaRoots">
        AREA
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="单位可许可应用选择" :visible.sync="apps.dialogFormVisible" width="75%">
      <aside>
        <i class="el-icon-info" /> 单位[ <span>{{ company.name }}</span> ]可许可应用列表。选择应用后点击“确定选择”按钮即可给单位许可选中应用。
      </aside>
      <div class="filter-container">
        <el-input v-model="apps.listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="queryRoleApps" />
        <el-input v-model="apps.listQuery.rules.code.fv" placeholder="编号 精确匹配" style="width: 150px;" class="filter-item" />
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryUnCompanyApps()" />
        <el-button class="filter-item" style="float: right;" type="primary" icon="el-icon-check" @click="onSelectApps()">
          确定选择
        </el-button>
      </div>
      <el-table
        ref="unCompanyAppTable"
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
        <el-table-column label="应用名称" width="250px" prop="name" sortable>
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="编号" width="200px" prop="code" sortable>
          <template slot-scope="scope">
            <span>{{ scope.row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注说明" min-width="100px" />
      </el-table>
      <pagination v-show="apps.total>0" :total="apps.total" :page.sync="apps.listQuery.page" :limit.sync="apps.listQuery.limit" @pagination="findUnRoleApps" />
    </el-dialog>

  </div>
</template>

<script>
import { fetchKhCompany, getFullOrgTree, addCompanyApps, removeCompanyApps } from '@/api/organization'
import { findAppPage4Company, findAppPage4UnCompany } from '@/api/application'
import { sheetClose, sheetRefresh } from '@/utils'
import { loadOrgCertificateType, loadPersonCertificateType, loadOrgType } from '@/utils/laccache'
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

const typeKeyValue = typeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

// arr to obj, such as { 0 : "正常", 1 : "禁用" }
const statusKeyValue = statusOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

const appTypeKeyValue = appTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

const commonData = {
  certificateTypeOptions: [],
  personCertificateTypeOptions: [],
  orgTypeOptions: []
}

export default {
  name: 'CompanyView',
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
    },
    certificateTypeFilter(type) {
      if (commonData.certificateTypeOptions && commonData.certificateTypeOptions.length > 0) {
        for (const ct of commonData.certificateTypeOptions) {
          if (ct.key === type) {
            return ct.display_name
          }
        }
      }
      return ''
    },
    personCertificateTypeFilter(type) {
      if (commonData.personCertificateTypeOptions && commonData.personCertificateTypeOptions.length > 0) {
        for (const ct of commonData.personCertificateTypeOptions) {
          if (ct.key === type) {
            return ct.display_name
          }
        }
      }
      return ''
    }
  },
  data() {
    return {
      tabPosition: 'right',
      activeTab: 'tabCompany',
      tempRoute: {},
      loading: false,
      statusOptions,
      typeOptions,
      company: {},
      companyTree: [],
      /* 应用许可 */
      companyApps: {
        loaded: false,
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            khCompanyId: { fv: undefined, oper: 'eq', stype: 'L' },
            khCompanyUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            code: { fv: undefined, oper: 'eq', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          }
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
            khCompanyId: { fv: undefined, oper: 'eq', stype: 'L' },
            khCompanyUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            code: { fv: undefined, oper: 'eq', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        statusOptions,
        appTypeOptions,
        dialogFormVisible: false
      }
      /*  */
    }
  },
  computed: {
    isMyCompany: function() {
      const cuser = this.$store.getters.currentUser
      if (this.company && this.company.id) {
        return this.company.id === cuser.companyId
      }
      return false
    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    const uuid = this.$route.params && this.$route.params.uuid
    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)

    this.loadCommonData()
    this.fetchCompany(id, uuid)
  },
  methods: {
    handleTabClick(tab, event) {
      if (tab.name === 'tabCompanyTreeView') {
        if (!this.companyTree || this.companyTree.length === 0) {
          this.getCompanyTree()
        }
      } else if (tab.name === 'tabCompanyApps') {
        this.getCompanyApps()
      } else if (tab.name === 'tabCompanyAreaRoots') {
        // TODO
      }
    },
    loadCommonData() {
      loadOrgCertificateType().then(ct => {
        commonData.certificateTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            commonData.certificateTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(() => {
      })

      loadPersonCertificateType().then(ct => {
        commonData.personCertificateTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            commonData.personCertificateTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(() => {
      })

      loadOrgType().then(ct => {
        commonData.orgTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            commonData.orgTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(() => {
      })
    },
    fetchCompany(id, uuid) {
      fetchKhCompany({ id, uuid }).then(response => {
        this.company = response.data
        this.companyApps.listQuery.rules.khCompanyId.fv = this.company.id
        this.companyApps.listQuery.rules.khCompanyUuid.fv = this.company.uuid
        this.apps.listQuery.rules.khCompanyId.fv = this.company.id
        this.apps.listQuery.rules.khCompanyUuid.fv = this.company.uuid
        this.setTagsViewTitle()
        this.setPageTitle()
      }).catch(err => console.log(err))
    },
    getCompanyTree() {
      getFullOrgTree({ id: this.company.id, uuid: this.company.uuid }).then(response => {
        this.companyTree = response.data
      })
    },
    getCompanyApps() {
      this.companyApps.listLoading = true
      findAppPage4Company(this.companyApps.listQuery).then(response => {
        this.companyApps.list = response.data.data
        this.companyApps.total = response.data.recordsTotal
        this.companyApps.listLoading = false
      }).catch(err => console.log(err))
    },
    setTagsViewTitle() {
      const title = '单位视图'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.company.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '单位视图'
      document.title = `${title} - ${this.company.name}`
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    /* 应用许可 */
    handleSelectAppChange(val) {
      this.selectedApps = val
    },
    handleAppRowClick(row, column, event) {
      this.$refs.unCompanyAppTable.toggleRowSelection(row)
    },
    findUnCompanyApps() {
      this.apps.listLoading = true
      findAppPage4UnCompany(this.apps.listQuery).then(response => {
        this.apps.list = response.data.data
        this.apps.total = response.data.recordsTotal
        this.apps.listLoading = false
      })
    },
    queryUnCompanyApps() {
      this.apps.listQuery.page = 1
      this.findUnCompanyApps()
    },
    toSelectApps() {
      this.findUnCompanyApps()
      this.apps.dialogFormVisible = true
    },
    onSelectApps() {
      if (!this.selectedApps || this.selectedApps.length <= 0) {
        this.$notify({ title: '提醒', message: '请先选择应用然后再操作！', type: 'error', duration: 4000 })
      } else {
        const req = { id: this.company.id, uuid: this.company.uuid, uuidIds: {}}
        for (let i = 0; i < this.selectedApps.length; i++) {
          const rowData = this.selectedApps[i]
          req.uuidIds[rowData.uuid] = rowData.id
        }

        addCompanyApps(req).then(() => {
          this.apps.dialogFormVisible = false
          for (let i = 0; i < this.selectedApps.length; i++) {
            this.companyApps.list.unshift(this.selectedApps[i])
          }
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }
    },
    onCompanyAppDelete(row) {
      this.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { parentId: this.company.id, parentUuid: this.company.uuid, id: row.id, uuid: row.uuid }
        removeCompanyApps(req).then(() => {
          const index = this.companyApps.list.indexOf(row)
          this.companyApps.list.splice(index, 1)
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }).catch(() => {
      })
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
