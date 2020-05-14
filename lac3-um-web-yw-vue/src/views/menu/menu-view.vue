<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="菜单项基本信息" name="tabMenuItem">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
          <el-button v-permission="['yw_app_menu']" type="success" icon="el-icon-edit" circle @click="toMenuItemUpdate()" />
          <el-button v-if="menuItem.status === 0 && checkPermission(['yw_app_menu'])" type="warning" icon="el-icon-lock" title="禁用" circle @click="toChangeMenuStatus(1)" />
          <el-button v-if="menuItem.status === 1 && checkPermission(['yw_app_menu'])" type="warning" icon="el-icon-unlock" title="启用" circle @click="toChangeMenuStatus(0)" />
          <el-button v-permission="['yw_app_menu']" type="danger" icon="el-icon-delete" circle @click="onMenuItemDelete()" />
        </div>
        <aside style="margin-top:15px;">
          <i class="el-icon-info" /> 菜单视图。您可以通过选择本视图右侧的页签查看和编辑相关功能信息。
        </aside>
        <el-form
          ref="menuItemForm"
          :model="menuItem"
          size="small"
          status-icon
          label-position="right"
          label-width="120px"
          style="width: 98%;"
        >
          <el-row>
            <el-col :span="12">
              <el-form-item label="菜单名称:" prop="name">
                <span class="el-span_view">{{ menuItem.name }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="编号:" prop="govCode">
                <span class="el-span_view">{{ menuItem.govCode }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="类型:" prop="type">
                <span class="el-span_view">{{ menuItem.type | menuTypeFilter }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态:" prop="status">
                <span class="el-span_view">{{ menuItem.status | statusFilter }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="字体图标:" prop="ico">
                <span class="el-span_view">{{ menuItem.ico }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序号:" prop="sort">
                <span class="el-span_view">{{ menuItem.sort }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="URL:" prop="url">
                <span class="el-span_view">{{ menuItem.url }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="创建时间:" prop="createTime">
                <span class="el-span_view">{{ menuItem.createTime }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新:" prop="updateTime">
                <span class="el-span_view">{{ menuItem.updateTime }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="备注:">
            <el-input v-model="menuItem.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" class="el-textarea_view" />
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="关联操作管理" name="tabOperations">
        <aside>
          <i class="el-icon-info" /> 菜单项[ <span>{{ menuItem.name }}</span> ] 关联资源管理。
        </aside>
        <div class="filter-container">
          <el-input v-model="operations.listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleAppMenuOperationsFilter" />
          <el-input v-model="operations.listQuery.rules.uri.fv" placeholder="uri 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-select v-model="operations.listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleMenuOperationsFilter" />
          <el-button
            v-permission="['yw_app_menu']"
            class="filter-item"
            style="float: right;"
            type="primary"
            icon="el-icon-plus"
            @click="toCreateOperation"
          >
            添加操作
          </el-button>
        </div>
        <el-table
          ref="operationTable"
          :key="operations.tableKey"
          v-loading="operations.listLoading"
          :data="operations.list"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          fit
          highlight-current-row
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="归属菜单" width="200">
            <template slot-scope="{row}">
              <span>{{ row.menuName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作名称" width="300" prop="name">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="uri" label="URI" min-width="200">
            <template slot-scope="{row}">
              <span>{{ row.uri }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" icon="el-icon-edit" title="编辑操作" @click="toEditOperaton(row)" />
              <el-button type="danger" size="mini" icon="el-icon-close" title="移除操作" @click="onOperatonDelete(row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="operations.total>0" :total="operations.total" :page.sync="operations.listQuery.page" :limit.sync="operations.listQuery.limit" @pagination="findMenuOperations" />
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="菜单编辑" :visible.sync="dialogVisible" width="50%">
      <el-form ref="menuItemForm" :rules="rules" :model="entity" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="entity.name" />
        </el-form-item>
        <el-form-item label="编号" prop="govCode">
          <el-input v-model="entity.govCode" disabled="true" />
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="entity.url" />
        </el-form-item>
        <el-form-item label="字体图标" prop="ico">
          <el-input v-model="entity.ico" />
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model.number="entity.sort" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="entity.type" size="small">
            <el-radio-button v-for="item in menuTypes" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="entity.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onAppUpdate()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="operations.dialogHeaderMap[operations.dialogStatus]" :visible.sync="operations.dialogVisible" width="50%">
      <el-form ref="operationForm" :rules="operations.rules" :inline="false" :model="operations.entity" size="small" status-icon label-post="right" label-width="80px" style="width: 98%; margin-left:10px;">
        <el-form-item label="归属菜单" prop="menuName">
          <el-input v-model="operations.entity.menuName" :disabled="true" />
        </el-form-item>
        <el-form-item label="操作名称" prop="name">
          <el-input v-model="operations.entity.name" placeholder="请输入操作名称" />
        </el-form-item>
        <el-form-item label="URI" prop="uri">
          <el-input v-model="operations.entity.uri" placeholder="请输入操作URI" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="operations.entity.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="operations.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="operations.dialogStatus==='create'?onAddOperation():onUpdateOperation()">保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { fetchById, updateMenu, deleteMenu, changeMenuStatus, getMenuOperationPage, saveOperation, deleteOperation } from '@/api/menu'
import { sheetClose, sheetRefresh } from '@/utils'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数
import { statusOptions, menuTypes } from '@/filters'

export default {
  name: 'MenuDetail',
  components: { Pagination },
  directives: { waves, permission },
  data() {
    return {
      tabPosition: 'right',
      activeTab: 'tabMenuItem',
      tempRoute: {},
      menuItem: {},
      entity: {}, // for edit
      statusOptions: statusOptions(),
      menuTypes: menuTypes(),
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
        code: [{ required: true, message: '编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到32 个字符', trigger: 'blur' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }],
        timeout: [{ required: true, message: '超时时间不能为空' }, { type: 'number', message: '超时时间必须为数字值' }],
        signatureKey: [{ required: true, message: '签名秘钥不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '签名秘钥长度在 2 到 64 个字符', trigger: 'blur' }],
        host: [{ required: true, message: '白名单不能为空', trigger: 'blur' }, { min: 2, max: 512, message: '白名单长度在 2 到512 个字符', trigger: 'blur' }],
        authAddr: [{ required: true, message: '接口地址不能为空', trigger: 'blur' }, { min: 10, max: 512, message: '接口地址长度在 10 到512 个字符', trigger: 'blur' }],
        authSignKey: [{ required: true, message: '签名秘钥不能为空', trigger: 'blur' }, { min: 10, max: 64, message: '签名秘钥长度在 10 到64 个字符', trigger: 'blur' }]
      },
      dialogVisible: false,
      loading: false,
      /** operations */
      operations: {
        dialogVisible: false,
        dialogStatus: '',
        dialogHeaderMap: {
          update: '编辑操作',
          create: '新增操作'
        },
        loaded: false,
        tableKey: 0,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            appId: { fv: undefined, oper: 'eq', stype: 'L' },
            menuId: { fv: undefined, oper: 'eq', stype: 'L' },
            menuUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            uri: { fv: undefined, oper: 'cn', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        entity: {},
        rules: {
          menuName: [{ required: true, message: '归属菜单不能为空，请选择', trigger: 'blur' }],
          name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
          uri: [{ required: true, message: 'uri不能为空', trigger: 'blur' }, { max: 255, message: 'uri长度不能大于 255 个字符', trigger: 'blur' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        }
      }

    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    const uuid = this.$route.params && this.$route.params.uuid
    this.fetchData(id, uuid)
    this.tempRoute = Object.assign({}, this.$route)
  },
  methods: {
    checkPermission,
    fetchData(id, uuid) {
      const that = this
      fetchById({ id, uuid }).then(response => {
        that.menuItem = response.data
        that.entity = Object.assign({ dataType: 'Object' }, that.menuItem)
        that.setTagsViewTitle()
        that.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = '菜单视图'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.menuItem.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '菜单视图'
      document.title = `${title} - ${this.menuItem.name}`
    },
    handleTabClick(tab, event) {
      if (tab.name === 'tabOperations') {
        if (this.operations.loaded === false) {
          this.findMenuOperations()
          this.operations.loaded = true
        }
      }
    },
    toMenuItemUpdate() {
      const that = this
      that.entity = Object.assign({ dataType: 'Object' }, that.menuItem)
      that.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['menuItemForm'].clearValidate()
      })
    },
    onAppUpdate() {
      const that = this
      that.$refs['menuItemForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({ dataType: 'Object' }, that.entity)
          updateMenu(tempData).then(() => {
            that.menuItem = Object.assign({}, that.entity)
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    toChangeMenuStatus(status) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { id: this.menuItem.id, uuid: this.menuItem.uuid, status: status }
        changeMenuStatus(req).then(() => {
          this.menuItem.status = status
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((e) => console.log(e))
      }).catch((e) => console.log(e))
    },
    onMenuItemDelete() {
      const that = this
      that.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = that.menuItem
        deleteMenu({ id, uuid }).then(() => {
          that.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
          that.onClose()
        }).catch((err) => console.log(err))
      }).catch(() => {
      })
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    /** operations */
    findMenuOperations() {
      const that = this
      that.operations.listQuery.rules.appId.fv = that.menuItem.appId
      that.operations.listQuery.rules.menuId.fv = that.menuItem.id
      that.operations.listQuery.rules.menuUuid.fv = that.menuItem.uuid
      that.operations.listLoading = true
      getMenuOperationPage(that.operations.listQuery).then(response => {
        that.operations.list = response.data.data
        that.operations.total = response.data.recordsTotal
        that.operations.listLoading = false
      })
    },
    handleMenuOperationsFilter() {
      this.operations.listQuery.page = 1
      this.findMenuOperations()
    },
    resetOperationEntity(me) {
      const that = this
      if (me) {
        that.operations.entity = Object.assign({ dataType: 'Object' }, me)
      } else {
        that.operations.entity = {
          dataType: 'Object',
          appId: that.menuItem.appId,
          menuId: that.menuItem.id,
          menuName: that.menuItem.name,
          menuGovCode: that.menuItem.govCode,
          name: '',
          uri: '',
          status: 0,
          remark: ''
        }
      }
    },
    toCreateOperation() {
      this.operations.dialogVisible = true
      this.operations.dialogStatus = 'create'
      this.resetOperationEntity()
      this.$nextTick(() => {
        this.$refs['operationForm'].clearValidate()
      })
    },
    onAddOperation() {
      const that = this
      that.$refs['operationForm'].validate((valid) => {
        if (valid) {
          const operation = Object.assign({ dataType: 'Object' }, that.operations.entity)
          saveOperation(operation).then((response) => {
            const tempData = Object.assign({}, response.data)
            this.operations.list.unshift(tempData)
            this.operations.dialogVisible = false
            this.$notify({ title: '提示', message: '创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    toEditOperaton(row) {
      const that = this
      that.operations.dialogVisible = true
      that.operations.dialogStatus = 'update'
      that.resetOperationEntity(row)
      that.$nextTick(() => {
        that.$refs['operationForm'].clearValidate()
      })
    },
    onUpdateOperation() {
      const that = this
      that.$refs['operationForm'].validate((valid) => {
        if (valid) {
          const operation = Object.assign({ dataType: 'Object' }, that.operations.entity)
          saveOperation(operation).then(() => {
            const index = that.operations.list.findIndex(v => v.id === operation.id)
            that.operations.list.splice(index, 1, that.operations.entity)
            that.operations.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    onOperatonDelete(row) {
      const that = this
      that.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = row
        deleteOperation({ id, uuid }).then((response) => {
          const index = that.operations.list.indexOf(row)
          that.operations.list.splice(index, 1)
          that.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(err => console.log(err))
      }).catch(err => console.log(err))
    }

  }
}
</script>
