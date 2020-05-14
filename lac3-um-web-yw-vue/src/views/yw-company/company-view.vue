<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="单位基本信息" name="tabCompany">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
          <el-button
            v-if="isMyCompany === false && company.status === 0 && checkPermission(['selfkh_org_edit']) === true"
            type="warning"
            icon="el-icon-lock"
            title="禁用"
            circle
            @click="toChangeStatus(1)"
          />
          <el-button
            v-if="isMyCompany === false && company.status === 1 && checkPermission(['selfkh_org_edit']) === true"
            type="warning"
            icon="el-icon-unlock"
            title="启用"
            circle
            @click="toChangeStatus(0)"
          />
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
      <el-tab-pane label="单位领导" name="tabCompanyLeaders">
        <aside>
          <i class="el-icon-info" /> 单位[ <span>{{ company.name }}</span> ]领导列表。您可以在此维护单位领导信息。
        </aside>
        <div class="filter-container">
          <el-input v-model="leaders.listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="queryLeaders" />
          <el-input v-model="leaders.listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-select v-model="leaders.listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in leaders.statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryLeaders" />
          <el-button
            v-permission="['selfkh_org_edit']"
            class="filter-item"
            style="float: right;"
            type="primary"
            icon="el-icon-plus"
            @click="handleComapnyLeaderAdd"
          >
            添加
          </el-button>
        </div>
        <el-table
          ref="leadersTable"
          :data="leaders.list"
          tooltip-effect="dark"
          border
          style="width: 100%"
        >
          <el-table-column label="" class-name="status-col" width="40" prop="status">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="姓名" width="200px" prop="name">
            <template slot-scope="{row}">
              <span v-if="checkPermission(['selfkh_user_view']) === false">{{ row.name }}</span>
              <router-link v-if="checkPermission(['selfkh_user_view']) === true" :to="'/User/user-view/'+row.id+'/'+row.uuid" class="link-type">
                <span>{{ row.name }}</span>
              </router-link>
              <el-tag v-if="row.type == 9">管</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="职位" width="200px" prop="jobPosition">
            <template slot-scope="scope">
              <el-tag size="small">{{ scope.row.jobPosition | positionFilter }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="排序号" width="120px" prop="sort">
            <template slot-scope="scope">
              <span>{{ scope.row.sort }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注说明" min-width="100px" />
          <el-table-column v-if="checkPermission(['selfkh_org_edit'])" label="操作" align="center" width="60" class-name="small-padding">
            <template slot-scope="{row}">
              <el-button type="danger" size="mini" icon="el-icon-close" title="移除" @click="onLeaderDelete(row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="leaders.total>0" :total="leaders.total" :page.sync="leaders.listQuery.page" :limit.sync="leaders.listQuery.limit" @pagination="getCompanyLeaders" />
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
        <aside>
          <i class="el-icon-info" /> 单位[ <span>{{ company.name }}</span> ]已许可应用列表。您可以在此查看或给子单位许可应用，配置应用权限。
        </aside>
        <el-row>
          <el-col :span="12">
            <div v-if="isMyCompany === false && checkPermission(['selfkh_org_app_perm']) === true" class="filter-container">
              <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="toSelectApps()">
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
              highlight-current-row
              @current-change="handleAppSelectedChange"
            >
              <el-table-column prop="status" label="" class-name="status-col" width="40">
                <template slot-scope="{row}">
                  <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
                </template>
              </el-table-column>
              <el-table-column label="应用名称" min-width="150px" prop="name">
                <template slot-scope="{row}">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="code" label="编号" width="200px" />
              <el-table-column v-if="isMyCompany === false && checkPermission(['selfkh_org_app_perm']) === true" label="操作" align="center" width="50" class-name="small-padding">
                <template slot-scope="{row}">
                  <el-button type="danger" size="mini" icon="el-icon-close" title="移除" @click="onCompanyAppDelete(row)" />
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="companyApps.total>0" :total="companyApps.total" :page.sync="companyApps.listQuery.page" :limit.sync="companyApps.listQuery.limit" @pagination="getCompanyApps" />
          </el-col>
          <el-col :span="12" style="padding-left: 10px;">
            <div v-if="isMyCompany === false && checkPermission(['selfkh_org_app_perm']) === true" class="filter-container">
              <el-button class="filter-item" type="primary" icon="el-icon-circle-check" @click="onSaveCompanyAppPermission()">
                保存应用权限配置
              </el-button>
            </div>
            <el-tabs type="border-card">
              <el-tab-pane label="菜单权限">
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
            </el-tabs>
            <div v-if="isMyCompany === false && checkPermission(['selfkh_org_app_perm']) === true" class="tabs-toolbar" title="切换全选状态">
              <el-checkbox v-model="checkAll" @change="triggerMenuTreeCheck" /> 全选
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="单位根区域" name="tabCompanyAreaRoots">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>单位[ <span>{{ company.name }}</span> ]根区域</span>
            <el-button v-if="isMyCompany === false && checkPermission(['selfkh_org_app_perm']) === true" style="float: right; padding: 3px 10px;" type="primary" @click="onCompanyAreasSetup()">设 置</el-button>
          </div>
          <div class="text item">
            <el-table
              :key="companyAreas.tableKey"
              ref="companyAreaTable"
              v-loading="companyAreas.listLoading"
              :data="companyAreas.list"
              tooltip-effect="dark"
              style="width: 100%"
              border
              fit
            >
              <el-table-column label="区域名称" width="400px">
                <template slot-scope="{row}">
                  <span>{{ row.value }}</span>
                </template>
              </el-table-column>
              <el-table-column label="区域编号" width="300px">
                <template slot-scope="{row}">
                  <span>{{ row.key }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
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
      <pagination v-show="apps.total>0" :total="apps.total" :page.sync="apps.listQuery.page" :limit.sync="apps.listQuery.limit" @pagination="findUnCompanyApps" />
    </el-dialog>

    <el-dialog title="单位根区域设置" :visible.sync="areaTree.dialogFormVisible">
      <el-tree
        ref="tree_area"
        :data="areaTree.data"
        :props="areaTree.defaultProps"
        node-key="id"
        :expand-on-click-node="false"
        :check-on-click-node="true"
        show-checkbox
        :check-strictly="true"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="areaTree.dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onSaveAreas()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog title="单位领导设置" :visible.sync="leaders.dialogFormVisible" width="50%">
      <el-form ref="leaderForm" :rules="leaders.rules" :inline="false" :model="leaders.leader" size="small" status-icon label-post="right" label-width="80px" style="width: 98%; margin-left:10px;">
        <el-form-item label="用户" prop="userName">
          <el-input v-model="leaders.leader.userName" :disabled="true">
            <el-button slot="append" icon="el-icon-search" @click="toSelectUser()" />
          </el-input>
        </el-form-item>
        <el-form-item label="职位" prop="jobPosition">
          <el-select v-model="leaders.leader.jobPosition" placeholder="请选择职位" clearable style="width:100%;">
            <el-option v-for="item in leaders.positions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model="leaders.leader.sort" placeholder="请输入排序号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="leaders.dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="addCompanyLeader()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog id="dlg-user-select4company-view" title="用户选择" :visible.sync="us.dialogFormVisible" width="80%">
      <lac-user-single-select
        :tree-type="us.treeType"
        :company-id="us.companyId"
        :company-uuid="us.companyUuid"
        @onUserSingleSelected="onUserSelected"
      />
    </el-dialog>

  </div>
</template>

<script>
import { fetchKhCompany,
  getFullOrgTree,
  addCompanyApps,
  removeCompanyApps,
  loadCompanyAreaTree,
  getConfigCompanyAreaRoots,
  getComapnyPermedMenuTree,
  saveCompanyAppMenuPerm,
  changeCompanyStatus,
  findCompanyLeaders,
  addCompanyLeader,
  deleteCompanyLeader
} from '@/api/organization'
import { findAppPage4Company, findAppPage4UnCompany } from '@/api/application'
import { saveCompanyConfig } from '@/api/config'
import { sheetClose, sheetRefresh, parseTreeNodeChecked, parseCheckedTreeIds, checkTree, unCheckTree } from '@/utils'
import { loadOrgCertificateType, loadPersonCertificateType, loadOrgType, loadCompanyPositions } from '@/utils/laccache'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { statusOptions, userTypeOptions, appTypeOptions } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数
import LacUserSingleSelect from '@/views/yw-user/components/UserSingleSelect'

const commonData = {
  certificateTypeOptions: [],
  personCertificateTypeOptions: [],
  orgTypeOptions: [],
  companyPositions: []
}

export default {
  name: 'CompanyView',
  components: { Pagination, LacUserSingleSelect },
  directives: { waves, permission },
  filters: {
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
    },
    positionFilter(type) {
      if (commonData.companyPositions && commonData.companyPositions.length > 0) {
        for (const ct of commonData.companyPositions) {
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
      statusOptions: statusOptions(),
      typeOptions: userTypeOptions(),
      company: {},
      companyTree: [],
      /* 单位领导 */
      leaders: {
        loaded: false,
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            orgId: { fv: undefined, oper: 'eq', stype: 'L' },
            orgType: { fv: 'Company', oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            mobile: { fv: undefined, oper: 'cn', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        leader: {
          dataType: 'Object',
          userId: '',
          userName: '',
          orgId: '',
          orgType: 'Company',
          orgName: '',
          jobPosition: '',
          sort: ''
        },
        user: {},
        rules: {
          userName: [{ required: true, message: '请选择用户', trigger: 'blur' }],
          jobPosition: [{ required: true, message: '请选择职位', trigger: 'blur' }],
          sort: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
        },
        statusOptions: statusOptions(),
        typeOptions: userTypeOptions(),
        positions: [],
        dialogFormVisible: false
      },
      us: {
        dialogFormVisible: false,
        treeType: 'SelfTree',
        companyId: 0,
        companyUuid: ''
      },
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
        statusOptions: statusOptions(),
        appTypeOptions: appTypeOptions(),
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
        statusOptions: statusOptions(),
        appTypeOptions,
        dialogFormVisible: false
      },
      /* 根区域 */
      companyAreas: {
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true
      },
      areaTree: {
        dialogFormVisible: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      /* 应用授权 */
      currentApp: null,
      checkAll: false,
      menuTree: {
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

    this.us.companyId = id
    this.us.companyUuid = uuid

    this.loadCommonData()
    this.fetchCompany(id, uuid)
  },
  methods: {
    checkPermission,
    handleTabClick(tab, event) {
      if (tab.name === 'tabCompanyLeaders') {
        this.leaders.positions = commonData.companyPositions
        if (!this.leaders.list) {
          this.getCompanyLeaders()
        }
      } else if (tab.name === 'tabCompanyTreeView') {
        if (!this.companyTree || this.companyTree.length === 0) {
          this.getCompanyTree()
        }
      } else if (tab.name === 'tabCompanyApps') {
        if (!this.companyApps.list) {
          this.getCompanyApps()
        }
      } else if (tab.name === 'tabCompanyAreaRoots') {
        if (!this.companyAreas.list) {
          this.findConfigCompanyAreaRoots()
        }
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
      }).catch(err => console.log(err))

      loadPersonCertificateType().then(ct => {
        commonData.personCertificateTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            commonData.personCertificateTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(err => console.log(err))

      loadOrgType().then(ct => {
        commonData.orgTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            commonData.orgTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(err => console.log(err))

      loadCompanyPositions().then(ct => {
        debugger
        commonData.companyPositions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            commonData.companyPositions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(err => console.log(err))
    },
    fetchCompany(id, uuid) {
      fetchKhCompany({ id, uuid }).then(response => {
        this.company = response.data
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
      this.companyApps.listQuery.rules.khCompanyId.fv = this.company.id
      this.companyApps.listQuery.rules.khCompanyUuid.fv = this.company.uuid

      this.companyApps.listLoading = true
      findAppPage4Company(this.companyApps.listQuery).then(response => {
        this.companyApps.list = response.data.data
        this.companyApps.total = response.data.recordsTotal
        this.companyApps.listLoading = false
        this.$nextTick(() => {
          if (this.companyApps.list && this.companyApps.list.length > 0) {
            this.currentApp = this.companyApps.list[0]
            this.$refs.companyAppTable.setCurrentRow(this.currentApp)
            // 加载权限
          }
        })
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
      this.apps.listQuery.rules.khCompanyId.fv = this.company.id
      this.apps.listQuery.rules.khCompanyUuid.fv = this.company.uuid

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
    },
    /* 单位根区域设置 */
    findConfigCompanyAreaRoots() {
      this.companyAreas.listLoading = true
      const req = { id: this.company.id, uuid: this.company.uuid }
      getConfigCompanyAreaRoots(req).then((response) => {
        this.companyAreas.list = response.data
        this.companyAreas.listLoading = false
      }).catch(() => {
      })
    },
    onCompanyAreasSetup() {
      loadCompanyAreaTree().then((response) => {
        this.areaTree.data = response.data
        this.areaTree.dialogFormVisible = true
        this.$nextTick(() => {
          const checkedIds = []
          if (this.companyAreas.list && this.companyAreas.list.length > 0) {
            for (const checkedArea of this.companyAreas.list) {
              parseTreeNodeChecked(checkedIds, checkedArea.key, this.areaTree.data)
            }
          }
          if (checkedIds.length > 0) {
            this.$refs.tree_area.setCheckedKeys(checkedIds)
          }
        })
      }).catch(() => {
      })
    },
    onSaveAreas() {
      const items = this.$refs.tree_area.getCheckedNodes()
      const areas = []
      if (items && items.length > 0) {
        for (const item of items) {
          areas.push({ key: item.id, value: item.name })
        }
      }
      // console.log(JSON.stringify(areas))
      const req = { orgId: this.company.id, orgUuid: this.company.uuid, key: 'area_roots', value: areas }
      saveCompanyConfig(req).then(() => {
        this.companyAreas.list = []
        if (areas && areas.length > 0) {
          for (let i = 0; i < areas.length; i++) {
            this.companyAreas.list.push(Object.assign({ }, areas[i]))
          }
        }
        this.areaTree.dialogFormVisible = false
        this.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
      }).catch(() => {
      })
    },
    /* 应用授权*/
    handleAppSelectedChange(val) {
      this.currentApp = val
      this.loadMenuTree(this.company.id, this.company.uuid, this.currentApp.id, this.currentApp.uuid)
      this.menuTree.loaded = true
    },
    loadMenuTree(companyId, companyUuid, appId, appUuid) {
      const req = { parentId: companyId, parentUuid: companyUuid, id: appId, uuid: appUuid }
      getComapnyPermedMenuTree(req).then(response => {
        this.menuTree.data = response.data
        const checkedIds = []
        parseCheckedTreeIds(checkedIds, this.menuTree.data)
        this.$refs.tree_menu.setCheckedKeys(checkedIds)
      })
    },
    onSaveCompanyAppPermission() {
      const items = this.$refs.tree_menu.getCheckedNodes(false, true)
      console.log(items)
      const req = { parentId: this.company.id, parentUuid: this.company.uuid, id: this.currentApp.id, uuid: this.currentApp.uuid, uuidIds: {}}
      for (let i = 0; i < items.length; i++) {
        const item = items[i]
        req.uuidIds[item.uuid] = item.id
      }
      saveCompanyAppMenuPerm(req).then(() => {
        this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
      }).catch(() => {
      })
    },
    triggerMenuTreeCheck() {
      if (this.checkAll) {
        checkTree(this.$refs.tree_menu, this.menuTree.data)
      } else {
        unCheckTree(this.$refs.tree_menu)
      }
    },
    toChangeStatus(status) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { id: this.company.id, uuid: this.company.uuid, status: status }
        changeCompanyStatus(req).then(() => {
          this.company.status = status
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((e) => console.log(e))
      }).catch((e) => console.log(e))
    },
    /*  单位领导 */
    getCompanyLeaders() {
      this.leaders.listQuery.rules.orgId.fv = this.company.id
      this.leaders.listQuery.rules.orgType.fv = 'Company'
      this.leaders.listLoading = true
      findCompanyLeaders(this.leaders.listQuery).then(response => {
        this.leaders.list = response.data.data
        this.leaders.total = response.data.recordsTotal
        this.leaders.listLoading = false
      }).catch((e) => console.log(e))
    },
    queryLeaders() {
      this.leaders.listQuery.page = 1
      this.getCompanyLeaders()
    },
    handleComapnyLeaderAdd() {
      this.leaders.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['leaderForm'].clearValidate()
      })
    },
    toSelectUser() {
      this.us.dialogFormVisible = true
    },
    onUserSelected(user) {
      this.leaders.user = user
      this.leaders.leader.userId = user.id
      this.leaders.leader.userName = user.name
      this.us.dialogFormVisible = false
    },
    addCompanyLeader() {
      this.$refs['leaderForm'].validate((valid) => {
        if (valid) {
          this.leaders.leader.orgId = this.company.id
          this.leaders.leader.orgType = 'Company'
          const leaderUser = Object.assign({ dataType: 'Object' }, this.leaders.leader)
          addCompanyLeader(leaderUser).then((response) => {
            const tempData = Object.assign({}, this.leaders.user)
            tempData.jobPosition = this.leaders.leader.jobPosition
            tempData.sort = this.leaders.leader.sort
            this.leaders.list.unshift(tempData)
            this.leaders.dialogFormVisible = false
            this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
          }).catch(err => {
            console.log(err)
          })
        }
      })
    },
    onLeaderDelete(row) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const leaderUser = {
          dataType: 'Object',
          userId: row.id,
          orgId: this.company.id,
          orgType: 'Company'
        }
        deleteCompanyLeader(leaderUser).then((response) => {
          const index = this.leaders.list.indexOf(row)
          this.leaders.list.splice(index, 1)
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(err => {
          console.log(err)
        })
      }).catch(err => {
        console.log(err)
      })
    }

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    .tabs-toolbar {
      position: absolute;right:10px;top:65px;
      /* color: #e6a23c;
      font-weight: 600; */
      font-size: 14px;
    }
</style>
