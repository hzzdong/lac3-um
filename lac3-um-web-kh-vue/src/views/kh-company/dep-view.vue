<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="部门基本信息" name="tabDepartment">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
          <el-button v-permission="['selfkh_org_edit']" type="success" icon="el-icon-edit" circle @click="toDepUpdate()" />
          <el-button
            v-if="dep.status === 0 && checkPermission(['selfkh_org_edit']) === true"
            type="warning"
            icon="el-icon-lock"
            title="禁用"
            circle
            @click="toChangeStatus(1)"
          />
          <el-button
            v-if="dep.status === 1 && checkPermission(['selfkh_org_edit']) === true"
            type="warning"
            icon="el-icon-unlock"
            title="启用"
            circle
            @click="toChangeStatus(0)"
          />
          <el-button
            v-if="checkPermission(['selfkh_org_edit']) === true"
            type="danger"
            icon="el-icon-delete"
            circle
            @click="onDepDelete()"
          />
        </div>
        <el-form ref="departmentForm" :model="dep" size="small" status-icon label-position="right" label-width="120px" style="width: 100%;">
          <el-card class="box-card" style="margin-top: -10px; margin-bottom: 10px;">
            <div slot="header" class="clearfix">
              <span>部门基本信息</span>
              <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="上级机构:" prop="orgName">
                    <span class="el-span_view">{{ dep.orgName || '无' }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="状态:" prop="status">
                    <span class="el-span_view">{{ dep.status | statusFilter }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="部门名称:" prop="name">
                    <span class="el-span_view">{{ dep.name }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="部门编码:" prop="govCode">
                    <span class="el-span_view">{{ dep.govCode }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="联系人:" prop="linkUserName">
                    <span class="el-span_view">{{ dep.linkUserName }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="联系电话:" prop="linkUserPhone">
                    <span class="el-span_view">{{ dep.linkUserPhone }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="机构类型:" prop="type">
                    <span class="el-span_view">{{ dep.type | depOrgTypeFilter }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="排序号:" prop="sort">
                    <span class="el-span_view">{{ dep.sort }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="创建时间:" prop="createTime">
                    <span class="el-span_view">{{ dep.createTime }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="最后更新:" prop="updateTime">
                    <span class="el-span_view">{{ dep.updateTime }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="备注:">
                    <el-input v-model="dep.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" class="el-textarea_view" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="部门领导" name="tabDepartmentLeaders">
        <aside>
          <i class="el-icon-info" /> 部门[ <span>{{ dep.name }}</span> ]领导列表。您可以在此维护部门领导信息。
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
            @click="handleDepartmentLeaderAdd"
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
              <router-link v-if="checkPermission(['selfkh_user_view']) === true" :to="'/user/user-view/'+row.id+'/'+row.uuid" class="link-type">
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
        <pagination v-show="leaders.total>0" :total="leaders.total" :page.sync="leaders.listQuery.page" :limit.sync="leaders.listQuery.limit" @pagination="getDepartmentLeaders" />
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="部门领导设置" :visible.sync="leaders.dialogVisible" width="50%">
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
        <el-button @click="leaders.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addDepartmentLeader()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog id="dlg-user-select4company-view" title="用户选择" :visible.sync="us.dialogVisible" width="80%">
      <lac-user-single-select
        :tree-type="us.treeType"
        :company-id="us.companyId"
        :company-uuid="us.companyUuid"
        @onUserSingleSelected="onUserSelected"
      />
    </el-dialog>

    <el-dialog title="部门编辑" :visible.sync="dialogVisible" width="60%">
      <el-form ref="depForm" :rules="rules" :model="entity" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-row>
          <el-col :span="12">
            <el-form-item label="上级部门">
              <el-input v-model="entity.orgName" :disabled="true">
                <el-button slot="append" icon="el-icon-search" :disabled="true" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="entity.status" size="small">
                <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="entity.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编号" prop="govCode">
              <el-input v-model="entity.govCode" :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人" prop="linkUserName">
              <el-input v-model="entity.linkUserName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="linkUserPhone">
              <el-input v-model="entity.linkUserPhone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="type">
              <el-select v-model="entity.type" class="filter-item" placeholder="请选择" style="width:100%;">
                <el-option v-for="item in depTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sort">
              <el-input v-model.number="entity.sort" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false"> 取消 </el-button>
        <el-button type="primary" @click="onUpdateDep()"> 保存 </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  fetchKhDepartment,
  updateKhDepartment,
  deleteKhDepartment,
  changeDepartmentStatus,
  findDepartmentLeaders,
  addDepartmentLeader,
  deleteDepartmentLeader
} from '@/api/organization'
import { sheetClose, sheetRefresh } from '@/utils'
import { loadOrgType, loadCompanyPositions } from '@/utils/laccache'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { statusOptions, userTypeOptions } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数
import LacUserSingleSelect from '@/views/kh-user/components/UserSingleSelect'

const commonData = {
  orgTypeOptions: [],
  companyPositions: []
}

export default {
  name: 'KhDepView',
  components: { Pagination, LacUserSingleSelect },
  directives: { waves, permission },
  filters: {
    depOrgTypeFilter(type) {
      const otype = type + ''
      if (commonData.orgTypeOptions && commonData.orgTypeOptions.length > 0) {
        for (const ct of commonData.orgTypeOptions) {
          if (ct.key === otype) {
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
      activeTab: 'tabDepartment',
      tempRoute: {},
      loading: false,
      dep: {},
      entity: {},
      dialogVisible: false,
      statusOptions: statusOptions(),
      depTypeOptions: [],
      rules: {
        name: [{ required: true, message: '部门名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
        govCode: [{ required: true, message: '部门编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到 64 个字符', trigger: 'blur' }],
        linkUserName: [{ min: 2, max: 32, message: '联系人长度在 2 到 32 个字符', trigger: 'blur' }],
        linkUserPhone: [{ min: 2, max: 32, message: '联系方式长度在 2 到 32 个字符', trigger: 'blur' }],
        sort: [{ type: 'number', message: '排序号必须为数字值' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
      },
      /* 部门领导 */
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
          orgType: 'Department',
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
        dialogVisible: false
      },
      us: {
        dialogVisible: false,
        treeType: 'SelfTree',
        companyId: 0,
        companyUuid: ''
      }

    }
  },
  created() {
    const self = this
    const id = self.$route.params && self.$route.params.id
    const uuid = self.$route.params && self.$route.params.uuid
    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    self.tempRoute = Object.assign({}, self.$route)
    self.loadCommonData().then(response => {
      commonData.orgTypeOptions = (response[0] && response[0].length > 0) ? response[0] : []
      self.depTypeOptions = commonData.orgTypeOptions

      commonData.companyPositions = (response[1] && response[1].length > 0) ? response[1] : []
      self.companyPositions = commonData.companyPositions

      self.fetchDepartment(id, uuid)
    }).catch((e) => console.log(e))
  },
  methods: {
    checkPermission,
    handleTabClick(tab, event) {
      if (tab.name === 'tabDepartmentLeaders') {
        this.leaders.positions = commonData.companyPositions
        if (!this.leaders.list) {
          this.getDepartmentLeaders()
        }
      }
    },
    loadCommonData() {
      const p1 = loadOrgType()
      const p2 = loadCompanyPositions()
      return Promise.all([p1, p2])
    },
    fetchDepartment(id, uuid) {
      fetchKhDepartment({ id, uuid }).then(response => {
        this.dep = response.data
        this.dep.type = this.dep.type + ''
        this.setTagsViewTitle()
        this.setPageTitle()
      }).catch(err => console.log(err))
    },
    setTagsViewTitle() {
      const title = '部门视图'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.dep.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '部门视图'
      document.title = `${title} - ${this.dep.name}`
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    toChangeStatus(status) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { id: this.dep.id, uuid: this.dep.uuid, status: status }
        changeDepartmentStatus(req).then(() => {
          this.dep.status = status
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((e) => console.log(e))
      }).catch((e) => console.log(e))
    },
    toDepUpdate() {
      const that = this
      that.entity = Object.assign({ dataType: 'Object' }, that.dep)
      console.log(that.entity)
      that.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['depForm'].clearValidate()
      })
    },
    onUpdateDep() {
      const that = this
      that.$refs['depForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({ dataType: 'Object' }, that.entity)
          updateKhDepartment(tempData).then(() => {
            that.dep = tempData
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    onDepDelete() {
      const that = this
      that.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = that.dep
        deleteKhDepartment({ id, uuid }).then(() => {
          that.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
          that.onClose()
        }).catch((err) => console.log(err))
      }).catch((err) => console.log(err))
    },
    /*  部门领导 */
    getDepartmentLeaders() {
      this.leaders.listQuery.rules.orgId.fv = this.dep.id
      this.leaders.listQuery.rules.orgType.fv = 'Department'
      this.leaders.listLoading = true
      findDepartmentLeaders(this.leaders.listQuery).then(response => {
        this.leaders.list = response.data.data
        this.leaders.total = response.data.recordsTotal
        this.leaders.listLoading = false
      }).catch((e) => console.log(e))
    },
    queryLeaders() {
      this.leaders.listQuery.page = 1
      this.getDepartmentLeaders()
    },
    handleDepartmentLeaderAdd() {
      this.leaders.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['leaderForm'].clearValidate()
      })
    },
    toSelectUser() {
      this.us.dialogVisible = true
    },
    onUserSelected(user) {
      this.leaders.user = user
      this.leaders.leader.userId = user.id
      this.leaders.leader.userName = user.name
      this.us.dialogVisible = false
    },
    addDepartmentLeader() {
      this.$refs['leaderForm'].validate((valid) => {
        if (valid) {
          this.leaders.leader.orgId = this.dep.id
          this.leaders.leader.orgType = 'Department'
          const leaderUser = Object.assign({ dataType: 'Object' }, this.leaders.leader)
          addDepartmentLeader(leaderUser).then((response) => {
            const tempData = Object.assign({}, this.leaders.user)
            tempData.jobPosition = this.leaders.leader.jobPosition
            tempData.sort = this.leaders.leader.sort
            this.leaders.list.unshift(tempData)
            this.leaders.dialogVisible = false
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
          orgId: this.dep.id,
          orgType: 'Department'
        }
        deleteDepartmentLeader(leaderUser).then((response) => {
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
