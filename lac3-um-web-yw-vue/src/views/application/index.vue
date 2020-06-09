<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.rules.code.fv" placeholder="编码 精确匹配" style="width: 150px;" class="filter-item" />
      <el-select v-model="listQuery.rules.clazz.fv" placeholder="类别" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in appClazz" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.rules.type.fv" placeholder="内外类型" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in appTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.rules.mappingType.fv" placeholder="账号映射" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in accountMappringTypes" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.rules.screenType.fv" placeholder="适用屏幕" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in screenTypes" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter" />
      <el-button
        v-permission="['yw_app_add']"
        class="filter-item"
        style="margin-left: 10px; float: right;"
        type="primary"
        icon="el-icon-plus"
        @click="handleAddApp"
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
      <el-table-column label="应用名称" width="300px" prop="name" sortable>
        <template slot-scope="{row}">
          <span v-if="checkPermission(['yw_app_edit','yw_app_menu']) === false">{{ row.name }}</span>
          <router-link v-if="checkPermission(['yw_app_edit','yw_app_menu']) === true" :to="'/application/app-view/'+row.id+'/'+row.uuid" class="link-type">
            <span>{{ row.name }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="应用编码" width="200px" prop="code" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="clazz" label="类别" class-name="status-col" width="95">
        <template slot-scope="{row}">
          <el-tag size="small" :type="row.clazz | statusTypeFilter">{{ row.clazz | appClazzFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" class-name="status-col" width="70">
        <template slot-scope="{row}">
          <el-tag size="small" :type="row.type | statusTypeFilter">{{ row.type | appTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="mappingType" label="账号映射" class-name="status-col" width="90">
        <template slot-scope="{row}">
          <el-tag size="small" :type="row.mappingType | statusTypeFilter">{{ row.mappingType | accountMappringTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="screenType" label="适用屏幕" class-name="status-col" width="80">
        <template slot-scope="{row}">
          <el-tag size="small" :type="row.screenType | statusTypeFilter">{{ row.screenType | screenTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注说明" min-width="100px" prop="remark">
        <template slot-scope="scope">
          <span>{{ scope.row.remark }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getAppPage" />

    <el-dialog title="新增应用" :visible.sync="dialogVisible">
      <el-form ref="entityForm" :rules="rules" :inline="false" :model="entity" size="small" status-icon label-position="right" label-width="120px" style="width: 90%; margin-left:30px;">
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="entity.name" />
        </el-form-item>
        <el-form-item label="应用编号" prop="code">
          <el-input v-model="entity.code" />
        </el-form-item>
        <el-form-item label="应用类别" prop="clazz">
          <el-radio-group v-model="entity.clazz" size="small">
            <el-radio-button v-for="item in appClazz" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内外类型" prop="type">
          <el-radio-group v-model="entity.type" size="small">
            <el-radio-button v-for="item in appTypeOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号映射类型" prop="mappingType">
          <el-radio-group v-model="entity.mappingType" size="small">
            <el-radio-button v-for="item in accountMappringTypes" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="适用屏幕" prop="screenType">
          <el-radio-group v-model="entity.screenType" size="small">
            <el-radio-button v-for="item in screenTypes" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="entity.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="首页地址" prop="url">
          <el-input v-model="entity.url" />
        </el-form-item>
        <el-form-item label="登出地址" prop="logout">
          <el-input v-model="entity.logout" />
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model.number="entity.sort" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createApp()">保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { findAppPage, saveApp } from '@/api/application'
import { validURL } from '@/utils/validate'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { statusOptions, appTypeOptions, appClazz, accountMappringTypes, screenTypes } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'Appliction',
  components: { Pagination },
  directives: { waves, permission },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        rules: {
          name: { fv: undefined, oper: 'cn', stype: 'S' },
          code: { fv: undefined, oper: 'eq', stype: 'S' },
          type: { fv: undefined, oper: 'eq', stype: 'I' },
          clazz: { fv: undefined, oper: 'eq', stype: 'I' },
          screenType: { fv: undefined, oper: 'eq', stype: 'I' },
          mappingType: { fv: undefined, oper: 'eq', stype: 'I' },
          status: { fv: undefined, oper: 'eq', stype: 'I' }
        },
        orderby: { orderby: 'sort', order: 'asc' }
      },
      statusOptions: statusOptions(),
      appTypeOptions: appTypeOptions(),
      appClazz: appClazz(),
      accountMappringTypes: accountMappringTypes(),
      screenTypes: screenTypes(),
      entity: {},
      dialogVisible: false,
      rules: {
        code: [{ required: true, message: '账号不能为空', trigger: 'blur' }, { min: 6, max: 64, message: '账号长度在 2 到 64 个字符', trigger: 'blur' }],
        name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
        sort: [{ required: true, message: '排序号不能为空' }, { type: 'number', message: '排序号必须为数字值' }],
        url: [
          { required: true, message: '应用首页地址不能为空', trigger: 'blur' },
          { validator: (rule, value, callback) => {
            if (value && value !== '') {
              if (!validURL(value)) {
                callback(new Error('请输入有效的应用首页地址'))
                return
              }
            }
            callback()
          }, trigger: 'blur' }
        ],
        logout: [
          { required: true, message: '应用登出地址不能为空', trigger: 'blur' },
          { validator: (rule, value, callback) => {
            if (value && value !== '') {
              if (!validURL(value)) {
                callback(new Error('请输入有效的应用登出地址'))
                return
              }
            }
            callback()
          }, trigger: 'blur' }
        ],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
      }

    }
  },
  created() {
    this.getAppPage()
  },
  methods: {
    checkPermission,
    getAppPage() {
      const that = this
      that.listLoading = true
      findAppPage(that.listQuery).then(response => {
        console.log(response.data)
        that.list = response.data.data
        that.total = response.data.recordsTotal
        that.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getAppPage()
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
    resetEntity() {
      this.entity = {
        dataType: 'Object',
        id: undefined,
        name: '',
        govCode: '',
        type: 0,
        clazz: 0,
        screenType: 1,
        mappingType: 1,
        status: 0,
        sort: 1,
        remark: ''
      }
    },
    handleAddApp() {
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
          saveApp(that.entity).then((response) => {
            const tempData = Object.assign({ }, response.data)
            that.list.unshift(tempData)
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '应用创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    }

  }
}
</script>
