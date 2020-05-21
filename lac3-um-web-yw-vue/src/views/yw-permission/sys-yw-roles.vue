<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.rules.name.fv" placeholder="角色名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.rules.govCode.fv" placeholder="角色编号 模糊匹配" style="width: 150px;" class="filter-item" />
      <el-select v-model="listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" plain @click="handleFilter" />
      <el-button
        v-permission="['yw_sys_role']"
        class="filter-item"
        style="float: right;"
        type="primary"
        icon="el-icon-plus"
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
      <el-table-column label="" class-name="status-col" width="40" prop="status" sortable>
        <template slot-scope="{row}">
          <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
        </template>
      </el-table-column>
      <el-table-column label="角色名称" width="250px" prop="name" sortable>
        <template slot-scope="{row}">
          <span v-if="checkPermission(['yw_sys_role_m']) === false">{{ row.name }}</span>
          <router-link v-if="checkPermission(['yw_sys_role_m']) === true" :to="'/role/yw-role-view/'+row.id+'/'+row.uuid" class="link-type">
            <span>{{ row.name }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="角色编号" width="250px" prop="govCode" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.govCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排序号" width="100px" align="center" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.sort }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注说明" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.remark }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog title="角色新增" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :inline="false" :model="temp" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="角色编号" prop="govCode">
          <el-input v-model="temp.govCode" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="temp.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="等级" prop="level">
          <el-radio-group v-model="temp.level" size="small">
            <el-radio-button v-for="item in levelOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
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
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="createData()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { findCompanyRolePage, createSysYwRole } from '@/api/sysywrole'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数
import { statusOptions, levelOptions } from '@/filters'

export default {
  name: 'SysYwRole',
  components: { Pagination },
  directives: { waves, permission },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        rules: {
          name: { fv: undefined, oper: 'cn', stype: 'S' },
          govCode: { fv: undefined, oper: 'eq', stype: 'S' },
          status: { fv: undefined, oper: 'eq', stype: 'I' }
        },
        orderby: { orderby: 'sort', order: 'asc' }
      },
      statusOptions: statusOptions(),
      levelOptions: levelOptions(),
      temp: {
        id: undefined,
        name: '',
        govCode: '',
        status: 0,
        remark: ''
      },
      dialogFormVisible: false,
      rules: {
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
        govCode: [{ required: true, message: '角色编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到32 个字符', trigger: 'blur' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    getList() {
      this.listLoading = true
      findCompanyRolePage(this.listQuery).then(response => {
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
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
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
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        govCode: '',
        status: 0,
        level: 0,
        remark: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({ dataType: 'Object' }, this.temp)
          createSysYwRole(tempData).then((response) => {
            const tempData = Object.assign({}, response.data)
            this.list.unshift(tempData)
            this.dialogFormVisible = false
            this.$notify({ title: '提示', message: '创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    }
  }
}
</script>
