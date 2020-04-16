<template>
  <div class="app-container">
    <zk-table
      ref="table"
      sum-text="sum"
      index-text="#"
      :data="data"
      :columns="columns"
      :stripe="props.stripe"
      :border="props.border"
      :show-header="props.showHeader"
      :show-summary="props.showSummary"
      :show-row-hover="props.showRowHover"
      :show-index="props.showIndex"
      :tree-type="props.treeType"
      :is-fold="props.isFold"
      :expand-type="props.expandType"
      :selection-type="props.selectionType"
    >
      <template slot="typeTemplate" slot-scope="{row}">
        <el-tag :effect="row.attributes.alias==='Company' ? 'dark' : 'plain'">{{ row.attributes.alias | typeFilter }}<span v-if="row.type === '1'">(管)</span></el-tag>
      </template>
      <template slot="statusTemplate" slot-scope="{row}">
        <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
      </template>
      <template slot="operations" slot-scope="{row}">
        <el-button
          v-if="row.attributes.alias === 'Company' && row.pId"
          type="primary"
          size="mini"
          icon="el-icon-menu"
          title="应用开通"
          @click="handleCompanyApps(row)"
        />
        <el-button
          v-if="row.attributes.alias !== 'Company' || !row.pId"
          type="primary"
          size="mini"
          icon="el-icon-plus"
          title="新增部门"
          @click="handleAddDepartment(row)"
        />
        <el-button
          v-if="row.pId"
          type="primary"
          size="mini"
          icon="el-icon-edit"
          title="编辑"
          @click="handleEdit(row)"
        />
        <el-button v-if="row.pId" type="danger" size="mini" icon="el-icon-delete" title="删除" @click="handleDelete(row)" />
        <el-button
          v-if="row.attributes.alias === 'Company' && !row.pId"
          type="primary"
          size="mini"
          icon="el-icon-circle-plus-outline"
          title="新增子单位"
          @click="handleAddCompany(row)"
        />
        <router-link v-if="row.attributes.alias === 'Company' && !row.pId" :to="'/Org/tree-view'" class="link-type" style="margin-left: 10px;">
          <el-button
            type="primary"
            size="mini"
            title="组织树预览"
            @click="handleShowOrgTree(row)"
          >
            <svg-icon icon-class="tree" />
          </el-button>
        </router-link>
      </template>
    </zk-table>

    <el-dialog :title="department.dialogHeaderMap[department.dialogStatus]" :visible.sync="department.dialogVisible" width="60%">
      <el-form ref="depForm" :rules="department.rules" :model="department.entity" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-row>
          <el-col :span="12">
            <el-form-item label="上级机构" prop="orgName">
              <el-input v-model="department.entity.orgName" :disabled="true">
                <el-button slot="append" icon="el-icon-search" :disabled="true" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="department.entity.status" size="small">
                <el-radio-button label="0">正常</el-radio-button>
                <el-radio-button label="1">禁用</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="department.entity.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编号" prop="govCode">
              <el-input v-model="department.entity.govCode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人" prop="linkUserName">
              <el-input v-model="department.entity.linkUserName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="linkUserPhone">
              <el-input v-model="department.entity.linkUserPhone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="type">
              <el-select v-model="department.entity.type" class="filter-item" placeholder="请选择" style="width:100%;">
                <el-option v-for="item in commonData.orgTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sort">
              <el-input v-model.number="department.entity.sort" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="department.entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="department.dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="department.dialogStatus==='create'?createDepartment():updateDepartment()">
          保存
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :title="company.dialogHeaderMap[company.dialogStatus]" :visible.sync="company.dialogVisible" width="70%">
      <el-form ref="companyForm" :rules="company.rules" :model="company.entity" size="small" status-icon label-position="right" label-width="90px" style="width: 98%; margin-left:10px;">
        <el-card class="box-card" style="margin-top: -20px; margin-bottom: 10px;">
          <div slot="header" class="clearfix">
            <span>单位基本信息</span>
            <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
          </div>
          <div class="text item">
            <el-row>
              <el-col :span="12">
                <el-form-item label="上级单位" prop="orgName">
                  <el-input v-model="company.entity.orgName" :disabled="true" placeholder="请选择上级单位">
                    <el-button slot="append" icon="el-icon-search" :disabled="true" />
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="company.entity.status" size="small">
                    <el-radio-button label="0">正常</el-radio-button>
                    <el-radio-button label="1">禁用</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="单位名称" prop="name">
                  <el-input v-model="company.entity.name" placeholder="请输入单位名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="编号" prop="govCode">
                  <el-input v-model="company.entity.govCode" placeholder="请输入单位编号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="电话" prop="phone">
                  <el-input v-model="company.entity.phone" placeholder="请输入单位联系电话" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="传真" prop="fax">
                  <el-input v-model="company.entity.fax" placeholder="请输入单位传真号码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="所在区域" prop="areaName">
                  <el-input v-model="company.entity.areaName" :disabled="true" placeholder="请选择所在区域">
                    <el-popover
                      slot="append"
                      placement="bottom"
                      title="区域选择"
                      width="500"
                      trigger="click"
                    >
                      <div style="height:500px;overflow-y:auto">
                        <el-tree
                          ref="areaTree"
                          :data="areaTree.data"
                          :props="areaTree.defaultProps"
                          node-key="id"
                          :expand-on-click-node="false"
                          :check-on-click-node="true"
                          show-checkbox
                          :check-strictly="true"
                          @node-click="handleNodeClick"
                          @check-change="handleCheckChange"
                        />
                      </div>
                      <el-button slot="reference" icon="el-icon-search" />
                    </el-popover>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="单位网址" prop="url">
                  <el-input v-model="company.entity.url" placeholder="请输入单位网址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="单位地址" prop="address">
                  <el-input v-model.number="company.entity.address" placeholder="请输入单位地址" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="排序号" prop="sort">
                  <el-input v-model.number="company.entity.sort" placeholder="请输入排序号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="company.khTypeCode === 'kh_qy'">
              <el-col :span="12">
                <el-form-item label="单位规模" prop="scale">
                  <el-input v-model="company.entity.scale" placeholder="请输入人员规模" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务开通" prop="businessStart">
                  <el-date-picker v-model="company.entity.businessStart" type="datetime" placeholder="请选择" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="company.khTypeCode === 'kh_qy'">
              <el-col :span="12">
                <el-form-item label="证照类型" prop="certificateType">
                  <el-select v-model="company.entity.certificateType" class="filter-item" placeholder="请选择" style="width:100%;">
                    <el-option v-for="item in commonData.certificateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证照号码" prop="certificateNo">
                  <el-input v-model="company.entity.certificateNo" placeholder="请输入证照号码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="company.khTypeCode === 'kh_qy'">
              <el-col :span="24">
                <el-form-item label="经营范围" prop="business">
                  <el-input v-model="company.entity.business" :autosize="{ minRows: 2, maxRows: 5}" type="textarea" placeholder="请输入经营范围" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="company.khTypeCode === 'kh_qy'">
              <el-col :span="24">
                <el-form-item label="单位资质" prop="credentials">
                  <el-input v-model="company.entity.credentials" :autosize="{ minRows: 2, maxRows: 5}" type="textarea" placeholder="请输入经营范围" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="company.entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>单位联系人信息</span>
            <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
          </div>
          <div class="text item">
            <el-row>
              <el-col :span="12">
                <el-form-item label="联系人/法人" prop="juridical">
                  <el-input v-model="company.entity.juridical" placeholder="请输入联系人或法人名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="jphone">
                  <el-input v-model="company.entity.jphone" placeholder="请输入联系人或法人联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="证照类型" prop="jType">
                  <el-select v-model="company.entity.jType" class="filter-item" placeholder="请选择" style="width:100%;">
                    <el-option v-for="item in commonData.personCertificateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证照号码" prop="jNo">
                  <el-input v-model="company.entity.jNo" placeholder="请输入证照号码" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="company.dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="company.dialogStatus==='create'?createCompany():updateCompany()">
          保存
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import Vue from 'vue'
import ZkTable from 'vue-table-with-tree-grid'
Vue.use(ZkTable)
import { getOrgTree, fetchKhDepartment, createKhDepartment, updateKhDepartment, deleteKhDepartment, createKhCompany, fetchKhCompany, updateKhCompany, deleteKhCompany } from '@/api/organization'
// import { loadAreaTree } from '@/api/area'
import { loadCachedMyCompanyAreaTree, loadOrgCertificateType, loadPersonCertificateType, loadOrgType } from '@/utils/laccache'

const statusOptions = [
  { key: 0, display_name: '正常' },
  { key: 1, display_name: '禁用' }
]

// arr to obj, such as { 0 : "正常", 1 : "禁用" }
const statusKeyValue = statusOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'CompanyTree',
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
      const typeMap = {
        'Company': '单位',
        'Department': '部门'
      }
      return typeMap[type]
    }
  },
  data() {
    return {
      props: {
        stripe: true,
        border: true,
        showHeader: true,
        showSummary: false,
        showRowHover: true,
        showIndex: false,
        treeType: true,
        isFold: false,
        expandType: false,
        selectionType: false
      },
      currentNode: undefined,
      data: [],
      columns: [
        {
          label: '名称',
          prop: 'name',
          minWidth: '250px'
        },
        {
          label: '编号',
          prop: 'govCode',
          minWidth: '150px'
        },
        {
          label: '类型',
          prop: 'typeTemplate',
          width: '90px',
          type: 'template',
          template: 'typeTemplate'
        },
        {
          label: '排序号',
          prop: 'sort',
          width: '90px'
        },
        {
          label: '状态',
          prop: 'statusTemplate',
          width: '50px',
          type: 'template',
          template: 'statusTemplate'
        },
        {
          label: '操作',
          prop: 'operations',
          width: '150px',
          type: 'template',
          template: 'operations'
        }
      ],
      statusOptions,
      commonData: {
        certificateTypeOptions: [],
        personCertificateTypeOptions: [],
        orgTypeOptions: []
      },
      department: {
        entity: {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          companyId: '',
          parentId: '',
          parentClass: '',
          orgName: '',
          name: '',
          govCode: '',
          linkUserName: '',
          linkUserPhone: '',
          type: '0',
          sort: 1,
          status: 0,
          remark: ''
        },
        rules: {
          orgName: [{ required: true, message: '请选择上级机构', trigger: 'blur' }],
          name: [{ required: true, message: '部门名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
          govCode: [{ required: true, message: '部门编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到 64 个字符', trigger: 'blur' }],
          linkUserName: [{ min: 2, max: 32, message: '联系人长度在 2 到 32 个字符', trigger: 'blur' }],
          linkUserPhone: [{ min: 2, max: 32, message: '联系方式长度在 2 到 32 个字符', trigger: 'blur' }],
          sort: [{ type: 'number', message: '排序号必须为数字值' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        },
        dialogVisible: false,
        dialogStatus: '',
        dialogHeaderMap: {
          update: '编辑部门',
          create: '新增部门'
        }
      },
      company: {
        khTypeCode: '',
        entity: {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          parentId: undefined,
          parentClass: 'KhCompany',
          khTypeCode: '',
          orgName: '',
          name: '',
          govCode: '',
          phone: '',
          fax: '',
          areaId: undefined,
          areaName: '',
          url: '',
          address: '',
          scale: 10,
          businessStart: new Date(),
          certificateType: 'certificate_type_org-yy',
          certificateNo: '',
          business: '',
          credentials: '',
          juridical: '',
          jphone: '',
          jType: 'certificate_type_person-id',
          jNo: '',
          sort: 1,
          status: 0,
          remark: ''
        },
        rules: {
          name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
          govCode: [{ required: true, message: '编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到 64 个字符', trigger: 'blur' }],
          phone: [{ min: 8, max: 32, message: '联系方式长度在 8 到 32 个字符', trigger: 'blur' }],
          fax: [{ min: 8, max: 32, message: '传真长度在 8 到 32 个字符', trigger: 'blur' }],
          areaName: [{ required: true, message: '区域不能为空', trigger: 'blur' }],
          url: [{ min: 10, max: 250, message: '联系方式长度在 10 到 250 个字符', trigger: 'blur' }],
          address: [{ min: 2, max: 120, message: '地址长度在 2 到 120 个字符', trigger: 'blur' }],
          scale: [{ type: 'number', message: '单位规模必须为数字值' }],
          certificateNo: [{ min: 2, max: 64, message: '证件号码长度在 2 到 64 个字符', trigger: 'blur' }],
          business: [{ min: 5, max: 500, message: '经营范围长度在 5 到 250 个字符', trigger: 'blur' }],
          credentials: [{ min: 2, max: 120, message: '单位资质长度在 2 到 120 个字符', trigger: 'blur' }],
          juridical: [{ min: 2, max: 60, message: '法人/联系人长度在 2 到 60 个字符', trigger: 'blur' }],
          jphone: [{ min: 2, max: 30, message: '联系方式长度在 2 到 30 个字符', trigger: 'blur' }],
          jNo: [{ min: 2, max: 60, message: '证照号码长度在 2 到 60 个字符', trigger: 'blur' }],
          sort: [{ type: 'number', message: '排序号必须为数字值' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        },
        dialogVisible: false,
        dialogStatus: '',
        dialogHeaderMap: {
          update: '编辑单位',
          create: '新增单位'
        }
      },
      areaTree: {
        checkedId: undefined,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      }
    }
  },
  computed: {
    propList() {
      return Object.keys(this.props).map(item => ({
        name: item
      }))
    }
  },
  created() {
    this.getTree()
    this.getAreaTree()
    this.loadCommonData()
  },
  methods: {
    loadCommonData() {
      loadOrgCertificateType().then(ct => {
        this.commonData.certificateTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            this.commonData.certificateTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(() => {
      })

      loadPersonCertificateType().then(ct => {
        this.commonData.personCertificateTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            this.commonData.personCertificateTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(() => {
      })

      loadOrgType().then(ct => {
        this.commonData.orgTypeOptions = []
        if (ct && ct.length > 0) {
          for (let i = 0; i < ct.length; i++) {
            this.commonData.orgTypeOptions.push({ key: ct[i].govCode, display_name: ct[i].name })
          }
        }
      }).catch(() => {
      })
    },
    handleNodeClick(data, checked, node) {
      this.areaTree.checkedId = data.id
      this.$refs.areaTree.setCheckedNodes([data])
    },
    handleCheckChange(data, checked, node) {
      if (checked === true) {
        this.areaTree.checkedId = data.id
        this.$refs.areaTree.setCheckedNodes([data])
        this.company.entity.areaId = data.id
        this.company.entity.areaName = data.name
      }
    },
    pickTreeNode(items, id) {
      let find
      for (const item of items) {
        if (item.id === id) {
          return item
        }
        if (item.children && item.children.length > 0) {
          find = this.pickTreeNode(item.children, id)
          if (find) {
            return find
          }
        }
      }
      return find
    },
    autoChildren(items) {
      for (const item of items) {
        if (!item.children) {
          item.children = []
        } else if (item.children.length > 0) {
          this.autoChildren(item.children)
        }
      }
    },
    getTree() {
      getOrgTree().then(response => {
        console.log('Company Tree Data', response.data)
        const items = response.data
        this.autoChildren(items)
        this.data = items
        this.company.khTypeCode = this.data[0].attributes.khTypeCode
      })
    },
    getAreaTree() {
      loadCachedMyCompanyAreaTree().then(response => {
        // console.log('Area Tree Data', response.data)
        this.areaTree.data = response
      }).catch(() => {
        // 取消
      })
      /*
      loadAreaTree().then(response => {
        // console.log('Area Tree Data', response.data)
        this.areaTree.data = response.data
      })
      */
    },
    resetDepartmentEntity(parent, me) {
      this.department.entity = {
        dataType: 'Object',
        id: (me && me.id) ? me.id : undefined,
        uuid: (me && me.uuid) ? me.uuid : '',
        parentId: me ? me.parentId : (parent ? parent.id : 0),
        parentClass: (parent && parent.id > 0) ? 'KhDepartment' : 'KhCompany',
        orgName: parent ? parent.name : '',
        name: (me && me.name) ? me.name : '',
        govCode: (me && me.govCode) ? me.govCode : '',
        linkUserName: (me && me.linkUserName) ? me.linkUserName : '',
        linkUserPhone: (me && me.linkUserPhone) ? me.linkUserPhone : '',
        type: (me && me.type) ? (me.type + '') : '0',
        sort: (me && me.sort) ? me.sort : 1,
        status: (me && me.status) ? me.status : 0,
        remark: (me && me.remark) ? me.remark : ''
      }
    },
    resetCompanyEntity(parent, me) {
      this.company.entity = {
        dataType: 'Object',
        id: (me && me.id) ? me.id : undefined,
        uuid: (me && me.uuid) ? me.uuid : '',
        khTypeCode: parent ? parent.attributes.khTypeCode : (me ? me.khTypeCode : this.company.khTypeCode),
        parentId: me ? me.parentId : (parent ? parent.id : 0),
        parentClass: 'KhCompany',
        orgName: parent ? parent.name : '',
        name: (me && me.name) ? me.name : '',
        govCode: (me && me.govCode) ? me.govCode : '',
        phone: (me && me.phone) ? me.phone : '',
        fax: (me && me.fax) ? me.fax : '',
        areaId: (me && me.areaId) ? me.areaId : undefined,
        areaName: (me && me.areaName) ? me.areaName : '',
        url: (me && me.url) ? me.url : '',
        address: (me && me.address) ? me.address : '',
        scale: (me && me.scale) ? me.scale : 10,
        businessStart: (me && me.businessStart) ? me.businessStart : new Date(),
        certificateType: (me && me.certificateType) ? me.certificateType : 'certificate_type_org-yy',
        certificateNo: (me && me.certificateNo) ? me.certificateNo : '',
        business: (me && me.business) ? me.business : '',
        credentials: (me && me.credentials) ? me.credentials : '',
        juridical: (me && me.juridical) ? me.juridical : '',
        jphone: (me && me.jphone) ? me.jphone : '',
        jType: (me && me.jType) ? me.jType : 'certificate_type_person-id',
        jNo: (me && me.jNo) ? me.jNo : '',
        sort: (me && me.sort) ? me.sort : 1,
        status: (me && me.status) ? me.status : 0,
        remark: (me && me.remark) ? me.remark : ''
      }
    },
    handleAddDepartment(row) {
      this.currentNode = row
      this.resetDepartmentEntity(row)
      this.department.dialogStatus = 'create'
      this.department.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['depForm'].clearValidate()
      })
    },
    createDepartment() {
      this.$refs['depForm'].validate((valid) => {
        if (valid) {
          createKhDepartment(this.department.entity).then((response) => {
            debugger
            const tempData = Object.assign({ dataType: 'Object', children: [] }, response.data)
            tempData.pId = this.currentNode.id
            if (!this.currentNode.children) {
              this.$set(this.currentNode, 'children', [])
              this.$set(this.currentNode.children, 0, tempData)
            } else {
              this.currentNode.children.push(tempData)
            }
            this.department.dialogVisible = false
            this.$notify({ title: '提示', message: '部门创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleAddCompany(row) {
      this.currentNode = row
      this.resetCompanyEntity(row)
      this.company.dialogStatus = 'create'
      this.company.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['companyForm'].clearValidate()
      })
    },
    createCompany(row) {
      this.$refs['companyForm'].validate((valid) => {
        if (valid) {
          createKhCompany(this.company.entity).then((response) => {
            debugger
            const tempData = Object.assign({ dataType: 'Object', children: [] }, response.data)
            if (!this.currentNode.children) {
              this.$set(this.currentNode, 'children', [])
              this.$set(this.currentNode.children, 0, tempData)
            } else {
              this.currentNode.children.push(tempData)
            }
            this.company.dialogVisible = false
            this.$notify({ title: '提示', message: '单位创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleEdit(row) {
      this.currentNode = row
      if (row.id > 0) {
        this.editDepartment(row)
      } else {
        this.editCompany(row)
      }
    },
    editDepartment(department) {
      const { id, uuid } = department
      fetchKhDepartment({ id, uuid }).then((response) => {
        const dep = response.data
        const parentId = this.currentNode.pId
        const parent = this.pickTreeNode(this.data, parentId)
        this.resetDepartmentEntity(parent, dep)
        console.log('this.department.entity', this.department.entity)

        this.department.dialogStatus = 'update'
        this.department.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['depForm'].clearValidate()
        })
      })
    },
    updateDepartment() {
      this.$refs['depForm'].validate((valid) => {
        if (valid) {
          const parent = this.pickTreeNode(this.data, this.currentNode.pId)
          const tempData = Object.assign({ dataType: 'Object' }, this.department.entity)
          // console.log('updateDepartment tempData', tempData)
          updateKhDepartment(tempData).then((response) => {
            const dep = response.data
            dep.pId = parent.id
            dep.children = this.currentNode.children
            for (let i = 0; i < parent.children.length; i++) {
              if (parent.children[i].id === dep.id) {
                parent.children.splice(i, 1, dep)
                break
              }
            }
            this.department.dialogVisible = false
            this.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    editCompany(company) {
      const data = { id: company.id.substring(1), uuid: company.uuid }
      fetchKhCompany(data).then((response) => {
        const com = response.data
        const parentId = '-' + com.parentId
        const parent = this.pickTreeNode(this.data, parentId)
        this.resetCompanyEntity(parent, com)
        console.log('this.company.entity', this.company.entity)

        this.company.dialogStatus = 'update'
        this.company.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['companyForm'].clearValidate()
        })
      })
    },
    updateCompany() {
      this.$refs['companyForm'].validate((valid) => {
        if (valid) {
          debugger
          const parentId = '-' + this.company.entity.parentId
          const parent = this.pickTreeNode(this.data, parentId)
          const tempData = Object.assign({ dataType: 'Object' }, this.company.entity)
          console.log('updateCompany tempData', tempData)
          updateKhCompany(tempData).then((response) => {
            const com = response.data
            for (let i = 0; i < parent.children.length; i++) {
              if (parent.children[i].id === com.id) {
                parent.children.splice(i, 1, com)
                break
              }
            }
            this.company.dialogVisible = false
            this.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('您确定要执行删除操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (row.id > 0) {
          this.deleteDepartment(row)
        } else {
          this.deleteCompany(row)
        }
      }).catch(() => {
        // 取消
      })
    },
    deleteDepartment(dep) {
      debugger
      if (dep.children && dep.children.length > 0) {
        this.$notify({ title: '提醒', message: '此机构节点下有子节点，删除所有子节点后再才能删除！', type: 'warning', duration: 4000 })
        return
      }
      const parent = this.pickTreeNode(this.data, dep.pId)
      if (parent && parent.children) {
        const { id, uuid } = dep
        deleteKhDepartment({ id, uuid }).then(() => {
          for (let i = 0; i < parent.children.length; i++) {
            if (parent.children[i].id === dep.id) {
              parent.children.splice(i, 1)
              this.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
              break
            }
          }
        }).catch(() => {
          this.$notify({ title: '错误', message: '删除失败！', type: 'error', duration: 4000 })
        })
      }
    },
    deleteCompany(com) {
      debugger
      const parent = this.pickTreeNode(this.data, com.pId)
      if (parent && parent.children) {
        const { id, uuid } = com
        deleteKhCompany({ id: id * -1, uuid }).then(() => {
          for (let i = 0; i < parent.children.length; i++) {
            if (parent.children[i].id === com.id) {
              parent.children.splice(i, 1)
              this.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
              break
            }
          }
        }).catch(() => {
          this.$notify({ title: '错误', message: '删除失败！', type: 'error', duration: 4000 })
        })
      }
    }
  }
}
</script>

