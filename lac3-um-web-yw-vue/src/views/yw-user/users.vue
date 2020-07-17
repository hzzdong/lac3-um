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
              <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter" />
              <el-button
                v-permission="['yw_user_add']"
                class="filter-item"
                style="margin-left: 10px; float: right;"
                type="primary"
                icon="el-icon-edit"
                @click="handleCreate"
              >
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
              <el-table-column prop="status" label="" class-name="status-col" width="40">
                <template slot-scope="{row}">
                  <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
                </template>
              </el-table-column>
              <el-table-column label="姓名" width="160px" prop="name" sortable>
                <template slot-scope="{row}">
                  <span v-if="checkPermission(['yw_user_view']) === false">{{ row.name }}</span>
                  <router-link v-if="checkPermission(['yw_user_view']) === true" :to="'/user/user-view/'+row.id+'/'+row.uuid" class="link-type">
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
          </el-tab-pane>
          <el-tab-pane label="兼职管理" name="tabJzUser">
            <div class="filter-container">
              <el-input v-model="jz.listQuery.rules.userName.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
              <el-input v-model="jz.listQuery.rules.srcOrgName.fv" placeholder="归属机构 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-input v-model="jz.listQuery.rules.destOrgName.fv" placeholder="兼职机构 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleJzFilter" />
              <el-button
                v-permission="['yw_user_add']"
                class="filter-item"
                style="margin-left: 10px; float: right;"
                type="primary"
                icon="el-icon-edit"
                @click="handleJzAdd"
              >
                添加
              </el-button>
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
              <el-table-column label="姓名" width="130px" prop="userName" sortable>
                <template slot-scope="{row}">
                  <span v-if="checkPermission(['yw_user_view']) === false || tree.checkedNode.id <= 0">{{ row.userName }}</span>
                  <router-link v-if="checkPermission(['yw_user_view']) === true && tree.checkedNode.id > 0" :to="'/user/jz-user-view/'+row.userId+'/'+row.userUuid" class="link-type">
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
              <el-table-column label="备注说明" min-width="100px" prop="remark">
                <template slot-scope="scope">
                  <span>{{ scope.row.remark }}</span>
                </template>
              </el-table-column>
              <el-table-column v-if="checkPermission(['yw_user_del'])" label="操作" width="60px">
                <template slot-scope="scope">
                  <el-button type="danger" size="mini" icon="el-icon-delete" title="取消兼职" @click="removeJzUser(scope.row)" />
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="jz.total>0" :total="jz.total" :page.sync="jz.listQuery.page" :limit.sync="jz.listQuery.limit" @pagination="getJzList" />

          </el-tab-pane>
          <el-tab-pane label="离职管理" name="tabLzUser">
            <div class="filter-container">
              <el-input v-model="lz.listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleLzFilter" />
              <el-input v-model="lz.listQuery.rules.account.fv" placeholder="登录账号 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-input v-model="lz.listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
              <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleLzFilter" />
            </div>
            <el-table
              :key="lz.tableKey"
              v-loading="lz.listLoading"
              :data="lz.list"
              border
              fit
              highlight-current-row
              style="width: 100%;"
            >
              <el-table-column label="姓名" width="130px" prop="name" sortable>
                <template slot-scope="{row}">
                  <router-link :to="'/user/user-view/'+row.id+'/'+row.uuid" class="link-type">
                    <span>{{ row.name }}</span>
                  </router-link>
                  <el-tag v-if="row.type == 9">管</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="账号" width="150px" prop="account" sortable>
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
              <el-table-column label="离职时间" width="160px" prop="updateTime" sortable>
                <template slot-scope="scope">
                  <span>{{ scope.row.updateTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="备注说明" min-width="100px" prop="remark">
                <template slot-scope="scope">
                  <span>{{ scope.row.remark }}</span>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="lz.total>0" :total="lz.total" :page.sync="lz.listQuery.page" :limit.sync="lz.listQuery.limit" @pagination="getLzList" />
          </el-tab-pane>
        </el-tabs>

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
                    <el-form-item label="登录账号" prop="account">
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
                    <el-form-item label="身份证" prop="idCard">
                      <el-input v-model="temp.idCard" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="性别" prop="sex">
                      <el-radio-group v-model="temp.sex" size="small">
                        <el-radio-button v-for="item in sexOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                      </el-radio-group>
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
                    <el-form-item label="党员" prop="dyType">
                      <el-radio-group v-model="temp.dyType" size="small">
                        <el-radio-button v-for="item in yesNoOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row v-if="temp.dyType === 1">
                  <el-col :span="12">
                    <el-form-item label="入党时间" prop="rdDate">
                      <el-date-picker v-model="temp.rdDate" type="date" placeholder="请选择" style="width:100%;" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="党组织" prop="dorg">
                      <el-input v-model="temp.dorg" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="岗位" prop="jobPosition">
                      <el-input v-model="temp.jobPosition" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="职务" prop="job">
                      <el-input v-model="temp.job" />
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
        <el-button v-permission="['yw_user_add']" type="primary" @click="createData()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog title="用户兼职" :visible.sync="jz.dialogFormVisible" width="50%">
      <el-form ref="jzForm" :rules="jz.rules" :inline="false" :model="jz.user" size="small" status-icon label-position="right" label-width="80px" style="width: 98%; margin-left:10px;">
        <el-form-item label="用户" prop="name">
          <el-input v-model="jz.user.name" :disabled="true">
            <el-button slot="append" icon="el-icon-search" @click="toSelectUser()" />
          </el-input>
        </el-form-item>
        <el-form-item label="兼职机构" prop="parentName">
          <el-input v-model="jz.user.parentName" :disabled="true">
            <el-button slot="append" icon="el-icon-search" @click="toSelectOrg()" />
          </el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="jz.user.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="jz.dialogFormVisible = false">取消</el-button>
        <el-button v-permission="['yw_user_add']" type="primary" @click="addJzUser()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog id="dlg-user-select" title="用户选择" :visible.sync="us.dialogFormVisible" width="80%">
      <lac-user-single-select
        :tree-type="us.treeType"
        :company-id="us.companyId"
        :company-uuid="us.companyUuid"
        :can-select-jz="us.canSelectJz"
        @onUserSingleSelected="onUserSelected"
      />
    </el-dialog>

    <el-dialog id="dlg-org-select" title="机构选择" :visible.sync="os.dialogFormVisible" width="40%">
      <lac-org-single-select
        :tree-type="os.treeType"
        :company-id="os.companyId"
        :company-uuid="os.companyUuid"
        @onOrgSingleSelected="onOrgSelected"
      />
    </el-dialog>

  </div>
</template>

<script>
import { getPage, createUser, getJzPage, addPartTimeJob, removePartTimeJob } from '@/api/user'
import { findCompanyRoles } from '@/api/ywrole'
import { getOrgTree } from '@/api/organization'
import waves from '@/directive/waves' // waves directive
import { validateMobile } from '@/utils/validate'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import md5 from 'js-md5'
import LacUserSingleSelect from '@/views/yw-user/components/user-single-select'
import LacOrgSingleSelect from '@/views/yw-company/components/org-single-select'
import { userStatusOptions, userTypeOptions, sexOptions, yesNoOptions } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'YwUser',
  components: {
    Pagination,
    LacUserSingleSelect,
    LacOrgSingleSelect
  },
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
      list: null,
      loaded: false,
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
          status: { fv: undefined, oper: 'eq', stype: 'I' },
          statusNe: { fv: 8, oper: 'eq', stype: 'I' }
        },
        orderby: { orderby: 'id', order: 'desc' }
      },
      statusOptions: userStatusOptions(),
      typeOptions: userTypeOptions(),
      sexOptions: sexOptions(),
      yesNoOptions: yesNoOptions(),
      temp: {
        id: undefined,
        uuid: '',
        parentId: '',
        parentClass: '',
        orgName: '',
        name: '',
        idCard: '',
        sex: 'M',
        birthday: '',
        jobPosition: '',
        job: '',
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
        roleIds: [],
        dyType: 0,
        rdDate: '',
        dorg: ''
      },
      dialogFormVisible: false,
      rules: {
        orgName: [{ required: true, message: '请选择归属机构', trigger: 'blur' }],
        account: [{ required: true, message: '账号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '账号长度在 2 到 64 个字符', trigger: 'blur' }],
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
        },
        user: {
          id: undefined,
          uuid: '',
          name: '',
          parentId: '',
          parentUuid: '',
          parentClass: '',
          parentName: '',
          remark: ''
        },
        rules: {
          name: [{ required: true, message: '请选择用户', trigger: 'blur' }],
          parentName: [{ required: true, message: '请选择兼职机构', trigger: 'blur' }]
        }
      },
      us: {
        dialogFormVisible: false,
        treeType: 'FullTree',
        companyId: 0,
        companyUuid: '',
        canSelectJz: false
      },
      os: {
        dialogFormVisible: false,
        treeType: 'FullTree',
        companyId: 0,
        companyUuid: ''
      },
      lz: {
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
            parentId: { fv: undefined, oper: 'eq', stype: 'L' },
            companyId: { fv: undefined, oper: 'eq', stype: 'L' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            account: { fv: undefined, oper: 'cn', stype: 'S' },
            mobile: { fv: undefined, oper: 'cn', stype: 'S' },
            status: { fv: 8, oper: 'eq', stype: 'I' }
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
    checkPermission,
    loadTab(activityTabName) {
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
      } else if (activityTabName === 'tabLzUser') {
        if (this.lz.loaded === false) {
          this.getLzList()
          this.lz.loaded = true
        }
      }
    },
    resetTab() {
      this.loaded = false
      this.jz.loaded = false
      this.lz.loaded = false
    },
    handleTabClick(tab, event) {
      this.loadTab(tab.name)
    },
    getTree() {
      getOrgTree().then(response => {
        this.tree.data = response.data
        if (this.tree.data[0]) {
          this.tree.checkedNode = this.tree.data[0]
          this.$refs.tree.setCheckedKeys([this.tree.checkedNode.id])
          this.getList()
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
    handleModifyStatus(row, status) {
      this.$message({ message: '操作Success', type: 'success' })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
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
        parentClass: this.tree.checkedNode.id > 0 ? 'YwDepartment' : 'YwCompany',
        orgName: selectNode.name,
        name: '',
        idCard: '',
        sex: 'M',
        birthday: '',
        jobPosition: '',
        job: '',
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
        roleIds: [],
        dyType: 0,
        rdDate: '',
        dorg: ''
      }
    },
    handleCreate() {
      if (!this.tree.checkedNode) {
        this.$notify({ title: '提示', message: '请先选择一个机构节点，再继续操作！', type: 'warning', duration: 4000 })
        return
      }
      if (this.tree.checkedNode.id > 0) {
        this.getCompanyRoles(this.tree.checkedNode.id, 'YwDepartment')
      } else {
        this.getCompanyRoles(this.tree.checkedNode.id.substring(1), 'YwCompany')
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
    },
    /* 兼职管理 */
    getJzList() {
      if (this.tree.checkedNode.id > 0) {
        this.jz.listQuery.rules.destOrgId.fv = this.tree.checkedNode.id
        this.jz.listQuery.rules.destOrgClass.fv = 'YwDepartment'
      } else {
        this.jz.listQuery.rules.destOrgId.fv = this.tree.checkedNode.id.substring(1)
        this.jz.listQuery.rules.destOrgClass.fv = 'YwCompany'
      }

      this.jz.listLoading = true
      getJzPage(this.jz.listQuery).then(response => {
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
    },
    handleJzAdd() {
      this.jz.user.parentUuid = this.tree.checkedNode.uuid
      this.jz.user.parentName = this.tree.checkedNode.name
      if (this.tree.checkedNode.id > 0) {
        this.jz.user.parentId = this.tree.checkedNode.id
        this.jz.user.parentClass = 'YwDepartment'
      } else {
        this.jz.user.parentId = this.tree.checkedNode.id.substring(1)
        this.jz.user.parentClass = 'YwCompany'
      }
      this.jz.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['jzForm'].clearValidate()
      })
    },
    toSelectUser() {
      this.us.dialogFormVisible = true
    },
    onUserSelected(user) {
      this.jz.user.id = user.id
      this.jz.user.uuid = user.uuid
      this.jz.user.name = user.name + ' (归属机构：' + user.orgFullName + ')'
      this.us.dialogFormVisible = false
    },
    toSelectOrg() {
      this.os.dialogFormVisible = true
    },
    onOrgSelected(org) {
      console.log('org', org)
      this.jz.user.parentUuid = org.uuid
      this.jz.user.parentName = org.name
      if (org.id > 0) {
        this.jz.user.parentId = org.id
        this.jz.user.parentClass = 'YwDepartment'
      } else {
        this.jz.user.parentId = org.id.substring(1)
        this.jz.user.parentClass = 'YwCompany'
      }
      this.os.dialogFormVisible = false
    },
    addJzUser() {
      this.$refs['jzForm'].validate((valid) => {
        if (valid) {
          const user = Object.assign({}, this.jz.user)
          addPartTimeJob(user).then((response) => {
            if ((this.jz.user.parentClass === 'YwDepartment' && this.jz.user.parentId === this.tree.checkedNode.id) ||
            (this.jz.user.parentClass === 'YwCompany' && ('-' + this.jz.user.parentId) === this.tree.checkedNode.id)) {
              const tempData = Object.assign({}, response.data)
              this.jz.list.unshift(tempData)
            }
            this.jz.dialogFormVisible = false
            this.$notify({ title: '提示', message: '用户兼职成功！', type: 'success', duration: 2000 })
          }).catch(err => {
            console.log(err)
          })
        }
      })
    },
    removeJzUser(row) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = row
        removePartTimeJob({ id, uuid }).then((response) => {
          const index = this.jz.list.indexOf(row)
          this.jz.list.splice(index, 1)
          this.$notify({ title: '提示', message: '取消兼职成功！', type: 'success', duration: 2000 })
        }).catch(err => {
          console.log(err)
        })
      }).catch(err => {
        console.log(err)
      })
    },
    /* 离职管理 */
    getLzList() {
      if (this.tree.checkedNode.id > 0) {
        this.lz.listQuery.rules.parentId.fv = this.tree.checkedNode.id
        this.lz.listQuery.rules.companyId.fv = undefined
      } else {
        this.lz.listQuery.rules.parentId.fv = undefined
        this.lz.listQuery.rules.companyId.fv = this.tree.checkedNode.id.substring(1)
      }
      this.lz.listLoading = true
      getPage(this.lz.listQuery).then(response => {
        this.lz.list = response.data.data
        this.lz.total = response.data.recordsTotal
        this.lz.listLoading = false
      })
    },
    handleLzFilter() {
      this.lz.listQuery.page = 1
      this.getLzList()
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
#dlg-user-select >>> .el-dialog__body {
    padding: 0px 0px;
}
#dlg-org-select >>> .el-dialog__body {
    padding: 0px 20px;
}
.el-card__header {
    padding: 11px 20px;
    border-bottom: 1px solid #e6ebf5;
    box-sizing: border-box;
}
</style>
