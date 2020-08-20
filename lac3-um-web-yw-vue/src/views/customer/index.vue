<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.rules.phone.fv" placeholder="电话 模糊匹配" style="width: 150px;" class="filter-item" />
      <el-select v-model="listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter" />
      <el-button
        v-permission="['kh_company_m']"
        class="filter-item"
        style="margin-left: 10px; float: right;"
        type="primary"
        icon="el-icon-plus"
        @click="handleAddCustomer"
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
          <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
        </template>
      </el-table-column>
      <el-table-column label="单位名称" width="300px" prop="name" sortable>
        <template slot-scope="{row}">
          <span v-if="checkPermission(['kh_company_m','kh_company_user_m','kh_company_app']) === false">{{ row.name }}</span>
          <router-link v-if="checkPermission(['kh_company_m','kh_company_user_m','kh_company_app']) === true" :to="'/customer/customer-view/'+row.id+'/'+row.uuid" class="link-type">
            <span>{{ row.name }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="所在区域" width="200px" prop="areaName">
        <template slot-scope="scope">
          <span>{{ scope.row.areaName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电话" width="120px" prop="phone" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地址" width="300px" prop="address" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.address }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注说明" min-width="100px" prop="remark">
        <template slot-scope="scope">
          <span>{{ scope.row.remark }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getPage" />

    <el-dialog title="新增客户单位" :visible.sync="dialogVisible" width="70%">
      <el-form ref="entityForm" :rules="rules" :inline="false" :model="entity" size="small" status-icon label-position="right" label-width="120px" style="width: 90%; margin-left:30px;">
        <el-card class="box-card" style="margin-top: -20px; margin-bottom: 10px;">
          <div slot="header" class="clearfix">
            <span>单位基本信息</span>
            <el-radio-group v-model="entity.typeCode" style="float: right;" size="small">
              <el-radio-button v-for="item in commonData.companyClasses" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
            </el-radio-group>
          </div>
          <div class="text item">
            <el-row>
              <el-col :span="12">
                <el-form-item label="单位名称" prop="name">
                  <el-input v-model="entity.name" placeholder="请输入单位名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="编号" prop="govCode">
                  <el-input v-model="entity.govCode" placeholder="请输入单位编号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="电话" prop="phone">
                  <el-input v-model="entity.phone" placeholder="请输入单位联系电话" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="传真" prop="fax">
                  <el-input v-model="entity.fax" placeholder="请输入单位传真号码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="所在区域" prop="areaName">
                  <el-input v-model="entity.areaName" :disabled="true" placeholder="请选择所在区域">
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
                  <el-input v-model="entity.url" placeholder="请输入单位网址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="排序号" prop="sort">
                  <el-input v-model.number="entity.sort" placeholder="请输入排序号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="entity.status" size="small">
                    <el-radio-button label="0">正常</el-radio-button>
                    <el-radio-button label="1">禁用</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="单位地址" prop="address">
                  <el-input v-model.number="entity.address" placeholder="请输入单位地址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="12">
                <el-form-item label="单位规模" prop="scale">
                  <el-input v-model="entity.scale" placeholder="请输入人员规模" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务开通" prop="businessStart">
                  <el-date-picker v-model="entity.businessStart" type="datetime" placeholder="请选择" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="12">
                <el-form-item label="证照类型" prop="certificateType">
                  <el-select v-model="entity.certificateType" class="filter-item" placeholder="请选择" style="width:100%;">
                    <el-option v-for="item in commonData.certificateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证照号码" prop="certificateNo">
                  <el-input v-model="entity.certificateNo" placeholder="请输入证照号码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="24">
                <el-form-item label="经营范围" prop="business">
                  <el-input v-model="entity.business" :autosize="{ minRows: 2, maxRows: 5}" type="textarea" placeholder="请输入经营范围" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="24">
                <el-form-item label="单位资质" prop="credentials">
                  <el-input v-model="entity.credentials" :autosize="{ minRows: 2, maxRows: 5}" type="textarea" placeholder="请输入经营范围" />
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
                  <el-input v-model="entity.juridical" placeholder="请输入联系人或法人名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="jphone">
                  <el-input v-model="entity.jphone" placeholder="请输入联系人或法人联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="证照类型" prop="jType">
                  <el-select v-model="entity.jType" class="filter-item" placeholder="请选择" style="width:100%;">
                    <el-option v-for="item in commonData.personCertificateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证照号码" prop="jNo">
                  <el-input v-model="entity.jNo" placeholder="请输入证照号码" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createApp()">保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { findCustomerPage, saveKhCompany } from '@/api/customer'
import { loadOrgCertificateType, loadPersonCertificateType, loadOrgType } from '@/utils/laccache'
import { loadTree } from '@/api/area'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { statusOptions, companyClasses } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'Appliction',
  components: { Pagination },
  directives: { waves, permission },
  data() {
    return {
      commonData: {
        companyClasses: companyClasses(),
        certificateTypeOptions: [],
        personCertificateTypeOptions: [],
        orgTypeOptions: []
      },
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        rules: {
          name: { fv: undefined, oper: 'cn', stype: 'S' },
          phone: { fv: undefined, oper: 'cn', stype: 'S' },
          status: { fv: undefined, oper: 'eq', stype: 'I' }
        },
        orderby: { orderby: 'sort', order: 'asc' }
      },
      statusOptions: statusOptions(),
      entity: {},
      dialogVisible: false,
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
        jphone: [{ required: true, message: '联系方式不能为空', trigger: 'blur' }, { min: 2, max: 30, message: '联系方式长度在 2 到 30 个字符', trigger: 'blur' }],
        jNo: [{ min: 2, max: 60, message: '证照号码长度在 2 到 60 个字符', trigger: 'blur' }],
        sort: [{ required: true, message: '排序号不能为空', trigger: 'blur' }, { type: 'number', message: '排序号必须为数字值' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
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
  created() {
    this.resetEntity()
    this.loadCommonData()
    this.getPage()
    this.getAreaTree()
  },
  methods: {
    checkPermission,
    loadCommonData() {
      loadOrgCertificateType().then(res => {
        this.commonData.certificateTypeOptions = (res && res.length > 0) ? res : []
      }).catch((err) => console.log(err))

      loadPersonCertificateType().then(res => {
        this.commonData.personCertificateTypeOptions = (res && res.length > 0) ? res : []
      }).catch((err) => console.log(err))

      loadOrgType().then(res => {
        this.commonData.orgTypeOptions = (res && res.length > 0) ? res : []
      }).catch((err) => console.log(err))
    },
    getPage() {
      const that = this
      that.listLoading = true
      findCustomerPage(that.listQuery).then(response => {
        that.list = response.data.data
        that.total = response.data.recordsTotal
        that.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getPage()
    },
    sortChange(data) {
      const that = this
      const { prop, order } = data
      that.listQuery.orderby.orderby = prop
      if (order === 'ascending') {
        that.listQuery.orderby.order = 'asc'
      } else {
        that.listQuery.orderby.order = 'desc'
      }
      that.handleFilter()
    },
    getAreaTree() {
      /*
      loadCachedMyCompanyAreaTree().then(response => {
        this.areaTree.data = response
      }).catch((err) => console.log(err))
      */
      loadTree().then(response => {
        this.areaTree.data = response.data
      }).catch((err) => console.log(err))
    },
    handleNodeClick(data, checked, node) {
      this.areaTree.checkedId = data.id
      this.$refs.areaTree.setCheckedNodes([data])
    },
    handleCheckChange(data, checked, node) {
      if (checked === true) {
        this.areaTree.checkedId = data.id
        this.$refs.areaTree.setCheckedNodes([data])
        this.entity.areaId = data.id
        this.entity.areaName = data.name
      }
    },
    resetEntity() {
      this.entity = {
        dataType: 'Object',
        typeCode: 'kh_xx',
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
      }
    },
    handleAddCustomer() {
      const that = this
      that.resetEntity()
      that.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['entityForm'].clearValidate()
      })
    },
    createApp() {
      const that = this
      that.$refs['entityForm'].validate((valid) => {
        if (valid) {
          saveKhCompany(that.entity).then((response) => {
            const tempData = Object.assign({ }, response.data)
            that.list.unshift(tempData)
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '客户单位创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    }

  }
}
</script>
