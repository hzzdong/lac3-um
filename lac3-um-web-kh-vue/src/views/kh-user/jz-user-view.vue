<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="用户信息" name="tabUser">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" title="关闭" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" title="刷新" circle plain @click="onRefresh()" />
          <el-button v-if="user.status !== 8 && checkPermission(['selfkh_user_edit'])" type="success" icon="el-icon-edit" title="编辑" circle @click="toUpdateUser()" />
          <el-button v-if="user.status === 0 && checkPermission(['selfkh_user_edit'])" type="warning" icon="el-icon-lock" title="禁用" circle @click="toChangeUserStatus(1)" />
          <el-button v-if="user.status === 1 && checkPermission(['selfkh_user_edit'])" type="warning" icon="el-icon-unlock" title="启用" circle @click="toChangeUserStatus(0)" />
          <el-button v-if="user.status !== 8 && checkPermission(['selfkh_user_edit'])" type="warning" icon="el-icon-user" title="离职" circle @click="toChangeUserStatus(8)" />
          <el-button v-if="checkPermission(['selfkh_user_del'])" type="danger" icon="el-icon-delete" title="删除" circle @click="onDeleteUser()" />
        </div>
        <aside style="margin-top:15px;">
          <i class="el-icon-info" /> 用户视图。您可以通过选择本视图右侧的页签查看或编辑相关功能信息。
        </aside>
        <el-form ref="userViewForm" :model="user" size="small" status-icon label-position="right" label-width="120px" style="width: 98%;">
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
              <el-form-item label="登录账号:" prop="account">
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
              <el-form-item label="身份证:" prop="idCard">
                <span class="el-span_view">{{ user.idCard }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="性别:" prop="sex">
                <span class="el-span_view">{{ user.sex | sexFilter }}</span>
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
              <el-form-item label="是否党员:" prop="dyType">
                <span class="el-span_view">{{ user.dyType | yesNoFilter }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="user.dyType === 1">
            <el-col :span="12">
              <el-form-item label="入党时间:" prop="rdDate">
                <span class="el-span_view">{{ user.rdDate }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所在党组织:" prop="dorg">
                <span class="el-span_view">{{ user.dorg }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="岗位:" prop="jobPosition">
                <span class="el-span_view">{{ user.jobPosition }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职务:" prop="job">
                <span class="el-span_view">{{ user.job }}</span>
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
          <el-row>
            <el-col :span="24">
              <el-form-item label="头像:" prop="ico">
                <my-upload
                  v-model="show"
                  field="file"
                  :width="300"
                  :height="300"
                  url="/fss/pub/Archive/upload"
                  :params="params"
                  :headers="headers"
                  img-format="png"
                  @src-file-set="srcFileSet"
                  @crop-success="cropSuccess"
                  @crop-upload-success="cropUploadSuccess"
                  @crop-upload-fail="cropUploadFail"
                />
                <el-avatar :size="100" :src="user.ico" />
                <el-button @click="toggleShow()">更换头像</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="兼职角色" name="tabUserRoles">
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
              <span v-if="checkPermission(['selfkh_perm_role']) === false">{{ row.name }}</span>
              <router-link v-if="checkPermission(['selfkh_perm_role']) === true" :to="'/Role/role-view/'+row.id+'/'+row.uuid" class="link-type">
                <span>{{ row.name }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="govCode" label="角色编号" width="200px" />
          <el-table-column prop="remark" label="备注说明" min-width="150px" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="兼职机构" name="tabUserOrgs">
        <div class="filter-container">
          <el-input v-model="jz.listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleJzFilter" />
          <el-input v-model="jz.listQuery.rules.srcOrgName.fv" placeholder="归属机构 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-input v-model="jz.listQuery.rules.destOrgName.fv" placeholder="兼职机构 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleJzFilter" />
          <el-button
            v-if="user.status !== 8 && checkPermission(['selfkh_user_edit'])"
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
          <el-table-column v-if="user.status !== 8 && checkPermission(['selfkh_user_edit'])" label="操作" width="70px">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" icon="el-icon-delete" title="取消兼职" @click="removeJzUser(scope.row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="jz.total>0" :total="jz.total" :page.sync="jz.listQuery.page" :limit.sync="jz.listQuery.limit" @pagination="getJzList" />

      </el-tab-pane>
    </el-tabs>

    <el-dialog title="兼职用户编辑" :visible.sync="dialogFormVisible" width="75%">
      <el-form ref="userForm" :rules="rules" :inline="false" :model="temp" size="small" status-icon label-position="right" label-width="80px" style="width: 98%; margin-left:10px;">
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
                <span>兼职角色分配</span>
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
        <el-button v-permission="['selfkh_user_edit']" type="primary" @click="onUpdateUser()">保存</el-button>
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
        <el-button type="primary" @click="addJzUser()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog id="dlg-user-select4user-view" title="用户选择" :visible.sync="us.dialogFormVisible" width="80%">
      <lac-user-single-select
        :tree-type="us.treeType"
        :company-id="us.companyId"
        :company-uuid="us.companyUuid"
        :can-select-jz="us.canSelectJz"
        @onUserSingleSelected="onUserSelected"
      />
    </el-dialog>

    <el-dialog id="dlg-org-select4user-view" title="机构选择" :visible.sync="os.dialogFormVisible" width="40%">
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
import { fetchById, updateJzUser, deleteUser, changeUserStatus, getJzPageOfUser, addPartTimeJob, removePartTimeJob, updateUserHeaderImage } from '@/api/user'
import { findJzCompanyRoles, findJzUserRoles } from '@/api/khrole'
import { validateMobile } from '@/utils/validate'
import { sheetClose, sheetRefresh } from '@/utils'
import md5 from 'js-md5'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import waves from '@/directive/waves' // waves directive
import { userStatusOptions, userTypeOptions, sexOptions, yesNoOptions } from '@/filters'
import LacUserSingleSelect from '@/views/kh-user/components/UserSingleSelect'
import LacOrgSingleSelect from '@/views/kh-company/components/OrgSingleSelect'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数
import myUpload from 'vue-image-crop-upload'

export default {
  name: 'UserDetail',
  components: {
    Pagination,
    LacUserSingleSelect,
    LacOrgSingleSelect,
    'my-upload': myUpload
  },
  directives: { waves, permission },
  data() {
    var validatePass = (rule, value, callback) => {
      if (this.temp.id) {
        if (value !== '' && this.temp.checkPass !== '') {
          this.$refs.userForm.validateField('checkPass')
        }
        callback()
      } else {
        if (value === '') {
          callback(new Error('请输入密码'))
        } else {
          if (this.temp.checkPass !== '') {
            this.$refs.userForm.validateField('checkPass')
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
      show: false,
      params: {
        appId: '16',
        appName: '用户中心',
        objectId: '',
        objectType: '10001'
      },
      headers: {
        smail: '*_~'
      },
      user: {
        id: undefined,
        uuid: '',
        parentId: '',
        parentClass: '',
        orgName: '',
        orgFullName: '',
        companyName: '',
        name: '',
        idCard: '',
        sex: 'M',
        birthday: '',
        jobPosition: '',
        job: '',
        account: '',
        govCode: '',
        mobile: '',
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
      dialogFormVisible: false,
      statusOptions: userStatusOptions(),
      typeOptions: userTypeOptions(),
      sexOptions: sexOptions(),
      yesNoOptions: yesNoOptions(),
      temp: {},
      comapnyRoles: [],
      userRoles: [],
      checkedUserRoles: [],
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
    this.findJzUserRoles(id, uuid)
    this.findJzCompanyRoles(id, uuid)
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
        if (this.user.ico) {
          this.user.ico = this.$store.state.settings.fssBaseUrl + this.user.ico
        }
        // console.log('user', this.user)
        this.setTagsViewTitle()
        this.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    findJzUserRoles(id, uuid) {
      findJzUserRoles({ id, uuid }).then(response => {
        this.userRoles = response.data
      }).catch(err => {
        console.log(err)
      })
    },
    findJzCompanyRoles(id, uuid) {
      findJzCompanyRoles({ id, uuid }).then(response => {
        this.comapnyRoles = response.data
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = '兼职用户'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.user.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '兼职用户'
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
          updateJzUser(userObj).then(() => {
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
    },
    handleJzAdd() {
      this.jz.user.id = this.user.id
      this.jz.user.uuid = this.user.uuid
      this.jz.user.name = this.user.name + ' (归属机构：' + (this.user.orgFullName || this.user.companyName) + ')'
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
      this.jz.user.parentUuid = org.uuid
      this.jz.user.parentName = org.name
      if (org.id > 0) {
        this.jz.user.parentId = org.id
        this.jz.user.parentClass = 'KhDepartment'
      } else {
        this.jz.user.parentId = org.id.substring(1)
        this.jz.user.parentClass = 'KhCompany'
      }
      this.os.dialogFormVisible = false
    },
    addJzUser() {
      this.$refs['jzForm'].validate((valid) => {
        if (valid) {
          const user = Object.assign({}, this.jz.user)
          addPartTimeJob(user).then((response) => {
            if (this.jz.user.id === this.user.id) {
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
    toggleShow() {
      this.show = true
    },
    srcFileSet(fileName, fileType, fileSize) {
      this.params.objectId = this.user.uuid
    },
    /**
			 * crop success
			 *
			 * [param] imgDataUrl
			 * [param] field
			 */
    cropSuccess(imgDataUrl, field) {
      console.log('-------- crop success --------')
      this.user.ico = imgDataUrl
    },
    /**
			 * upload success
			 *
			 * [param] jsonData   服务器返回数据，已进行json转码
			 * [param] field
			 */
    cropUploadSuccess(jsonData, field) {
      const that = this
      console.log('-------- upload success --------')
      console.log(jsonData)
      console.log('field: ' + field)
      if (jsonData && jsonData.code === '0') {
        const icoReq = { dataType: 'Object', key: that.user.id, value: jsonData.data.path, title: that.user.uuid }
        updateUserHeaderImage(icoReq).then((response) => {
          // that.$store.dispatch('user/updateHeaderImage', icoReq.value)
          that.show = false
          that.$notify({ title: '提示', message: '头像上传成功！', type: 'success', duration: 2000 })
          sheetRefresh(that)
        })
      }
    },
    /**
			 * upload fail
			 *
			 * [param] status    server api return error status, like 500
			 * [param] field
			 */
    cropUploadFail(status, field) {
      console.log('-------- upload fail --------')
      console.log(status)
      console.log('field: ' + field)
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
