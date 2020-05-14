<template>
  <div class="app-container">
    <el-row>
      <el-col :span="10">
        <el-table
          ref="dictTreeTable"
          :data="dictTree.list"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          default-expand-all
          highlight-current-row
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
          @current-change="handleDictTypeChange"
        >
          <el-table-column prop="name" label="字典分类名称" min-width="100" />
          <el-table-column prop="govCode" label="编号" width="180" />
          <el-table-column label="操作" width="140">
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" icon="el-icon-plus" title="新增字典分类" @click="handleAddDictType(row)" />
              <el-button v-if="row.id !== '0'" type="primary" size="mini" icon="el-icon-edit" title="编辑字典分类" @click="handleEditDictType(row)" />
              <el-button v-if="row.id !== '0'" type="danger" size="mini" icon="el-icon-delete" title="删除字典分类" @click="handleDeleteDictType(row)" />
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="14">
        <div class="filter-container" style="margin-left:10px;">
          <el-input v-model="dicts.listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleDictFilter" />
          <el-input v-model="dicts.listQuery.rules.govCode.fv" placeholder="编号 精确匹配" style="width: 150px;" class="filter-item" />
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleDictFilter" />
          <el-button
            class="filter-item"
            style="margin-left: 10px; float: right;"
            type="primary"
            icon="el-icon-plus"
            :disabled="dicts.btnAddDict"
            @click="handleCreateDict"
          >
            添加字典项
          </el-button>
        </div>
        <el-table
          ref="dictTable"
          :key="dicts.tableKey"
          v-loading="dicts.listLoading"
          :data="dicts.list"
          border
          fit
          style="width: 100%;margin-left:10px;"
        >
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column prop="parentName" label="字典分类" width="150" />
          <el-table-column prop="name" label="字典名称" min-width="100" />
          <el-table-column prop="govCode" label="编号" width="210" />
          <el-table-column label="操作" width="100">
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" icon="el-icon-edit" title="编辑字典分类" @click="handleEditDict(row)" />
              <el-button type="danger" size="mini" icon="el-icon-delete" title="删除字典分类" @click="handleDeleteDictType(row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="dicts.total>0" :total="dicts.total" :page.sync="dicts.listQuery.page" :limit.sync="dicts.listQuery.limit" @pagination="getDicts" />
      </el-col>
    </el-row>

    <el-dialog :title="dictTree.dialogHeaderMap[dictTree.dialogStatus]" :visible.sync="dictTree.dialogVisible" width="50%">
      <el-form ref="dictTypeForm" :rules="dictTree.rules" :model="dictTree.entity" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="dictTree.entity.name" />
        </el-form-item>
        <el-form-item label="编号" prop="govCode">
          <el-input v-model="dictTree.entity.govCode" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dictTree.entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dictTree.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dictTree.dialogStatus==='create'?createDictType():updateDictType()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dicts.dialogHeaderMap[dicts.dialogStatus]" :visible.sync="dicts.dialogVisible" width="50%">
      <el-form ref="dictForm" :rules="dicts.rules" :model="dicts.entity" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-form-item label="分类名称" prop="parentName">
          <el-input v-model="dicts.entity.parentName" :disabled="true" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="dicts.entity.name" />
        </el-form-item>
        <el-form-item label="编号" prop="govCode">
          <el-input v-model="dicts.entity.govCode" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dicts.entity.status" size="small">
            <el-radio-button v-for="item in dicts.statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model.number="dicts.entity.sort" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dicts.entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dicts.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dicts.dialogStatus==='create'?createDict():updateDict()">保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { loadDictTree, saveDictTree, deleteDictType, getDictPage, saveDict } from '@/api/dict'
import { statusOptions } from '@/filters'
import { pickTreeNode, autoChildren } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'Dict',
  components: { Pagination },
  directives: { waves, permission },
  data() {
    return {
      dictTree: {
        currentNode: undefined,
        tableKey: 0,
        list: [],
        listLoading: true,
        rules: {
          name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
          govCode: [{ required: true, message: '编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到 64 个字符', trigger: 'blur' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        },
        dialogVisible: false,
        dialogStatus: '',
        dialogHeaderMap: {
          update: '编辑字典类型',
          create: '新增字典类型'
        },
        entity: {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          parentId: '',
          name: '',
          govCode: '',
          remark: ''
        }
      },
      dicts: {
        tableKey: 0,
        list: [],
        loaded: false,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            parentId: { fv: undefined, oper: 'eq', stype: 'L' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            govCode: { fv: undefined, oper: 'eq', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        btnAddDict: false,
        statusOptions: statusOptions(),
        dialogVisible: false,
        dialogStatus: '',
        dialogHeaderMap: {
          update: '编辑字典',
          create: '新增字典'
        },
        entity: {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          parentId: '',
          parentName: '',
          name: '',
          govCode: '',
          status: 0,
          sort: 0,
          remark: ''
        },
        rules: {
          name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
          govCode: [{ required: true, message: '编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到 64 个字符', trigger: 'blur' }],
          sort: [{ required: true, message: '排序号不能为空', trigger: 'blur' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        }
      }

    }
  },
  created() {
    this.getDictTree()
  },
  methods: {
    checkPermission,
    handleDictTypeChange(val) {
      const that = this
      that.dictTree.currentNode = val
      console.log(val)
      that.getDicts()
      if (val.id === '0') {
        that.dicts.btnAddDict = true
      } else {
        that.dicts.btnAddDict = false
      }
    },
    getDictTree() {
      const that = this
      that.dictTree.listLoading = true
      loadDictTree().then(response => {
        const items = response.data
        if (items && items.length > 0) {
          autoChildren(items)
          that.dictTree.list = items
          that.$refs.dictTreeTable.setCurrentRow(items[0])
        }
        that.dictTree.listLoading = false
      }).catch((err) => console.log(err))
    },
    resetDictType(parentId, me) {
      if (me) {
        this.dictTree.entity = Object.assign({ dataType: 'Object' }, me)
        this.dictTree.entity.parentId = me.pId
      } else {
        this.dictTree.entity = {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          parentId: parentId,
          name: '',
          govCode: '',
          remark: ''
        }
      }
    },
    handleAddDictType(row) {
      this.dictTree.currentNode = row
      this.resetDictType(row.id)
      this.dictTree.dialogStatus = 'create'
      this.dictTree.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dictTypeForm'].clearValidate()
      })
    },
    createDictType() {
      const that = this
      that.$refs['dictTypeForm'].validate((valid) => {
        if (valid) {
          saveDictTree(that.dictTree.entity).then((response) => {
            const tempData = Object.assign({ dataType: 'Object', children: [] }, response.data)
            debugger
            if (!that.dictTree.currentNode.children) {
              that.$set(that.dictTree.currentNode, 'children', [])
              that.$set(that.dictTree.currentNode.children, 0, tempData)
            } else {
              that.dictTree.currentNode.children.push(tempData)
            }
            that.dictTree.dialogVisible = false
            that.$notify({ title: '提示', message: '字典类型创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleEditDictType(row) {
      this.dictTree.currentNode = row
      this.resetDictType('', row)
      this.dictTree.dialogStatus = 'update'
      this.dictTree.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dictTypeForm'].clearValidate()
      })
    },
    updateDictType() {
      const that = this
      this.$refs['dictTypeForm'].validate((valid) => {
        if (valid) {
          debugger
          const parentId = that.dictTree.entity.parentId
          const parent = pickTreeNode(that.dictTree.list, parentId)
          const tempData = Object.assign({ dataType: 'Object' }, that.dictTree.entity)
          saveDictTree(tempData).then((response) => {
            const com = response.data
            for (let i = 0; i < parent.children.length; i++) {
              if (parent.children[i].id === com.id) {
                parent.children.splice(i, 1, com)
                break
              }
            }
            that.dictTree.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleDeleteDictType(row) {
      const that = this
      that.$confirm('您确定要执行删除操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const parent = pickTreeNode(that.dictTree.list, row.pId)
        if (parent && parent.children) {
          const { id, uuid } = row
          deleteDictType({ id, uuid }).then(() => {
            for (let i = 0; i < parent.children.length; i++) {
              if (parent.children[i].id === row.id) {
                parent.children.splice(i, 1)
                that.$refs.dictTreeTable.setCurrentRow(parent)
                that.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
                break
              }
            }
          }).catch((err) => console.log(err))
        }
      }).catch((err) => console.log(err))
    },
    /* DIct */
    getDicts() {
      const that = this
      if (that.dictTree.currentNode.id !== '0') {
        that.dicts.listQuery.rules.parentId.fv = that.dictTree.currentNode.id
      } else {
        that.dicts.listQuery.rules.parentId.fv = ''
      }

      that.dicts.listLoading = true
      getDictPage(that.dicts.listQuery).then(response => {
        console.log(response.data)
        that.dicts.list = response.data.data
        that.dicts.total = response.data.recordsTotal
        that.dicts.listLoading = false
      })
    },
    handleDictFilter() {
      this.dicts.listQuery.page = 1
      this.getDicts()
    },
    resetDict(parent, me) {
      const that = this
      if (me) {
        that.dicts.entity = Object.assign({ dataType: 'Object' }, me)
        that.dicts.entity.parentId = me.pId
        if (!parent) {
          parent = pickTreeNode(that.dictTree.list, me.pId)
        }
        if (parent) {
          that.dicts.entity.parentName = parent.name
        }
      } else {
        that.dicts.entity = {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          parentId: parent.id,
          parentName: parent.name,
          name: '',
          govCode: '',
          status: 0,
          sort: 0,
          remark: ''
        }
      }
    },
    handleCreateDict() {
      const that = this
      that.resetDict(that.dictTree.currentNode)
      that.dicts.dialogStatus = 'create'
      that.dicts.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['dictForm'].clearValidate()
      })
    },
    createDict() {
      const that = this
      that.$refs['dictForm'].validate((valid) => {
        if (valid) {
          saveDict(that.dicts.entity).then((response) => {
            const tempData = Object.assign({ }, response.data)
            that.dicts.list.unshift(tempData)
            that.dicts.dialogVisible = false
            that.$notify({ title: '提示', message: '字典创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleEditDict(row) {
      const that = this
      that.resetDict(that.dictTree.currentNode, row)
      that.dicts.dialogStatus = 'update'
      that.dicts.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['dictForm'].clearValidate()
      })
    },
    updateDict() {
      const that = this
      this.$refs['dictForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({ dataType: 'Object' }, that.dicts.entity)
          saveDict(tempData).then((response) => {
            const dict = response.data
            const index = that.dicts.list.findIndex(v => v.id === dict.id)
            that.dicts.list.splice(index, 1, dict)
            that.dicts.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    }

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.mimi {
    padding: 5px 5px;
    font-size: 12px;
    border-radius: 3px;
}
</style>
