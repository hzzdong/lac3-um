<template>
  <div class="app-container">
    <el-table
      ref="areaTable"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      style="width: 100%;margin-bottom: 20px;"
      row-key="id"
      border
      fit
      highlight-current-row
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      @current-change="handleAreaChange"
    >
      <el-table-column prop="name" label="区域名称" min-width="100" />
      <el-table-column prop="govCode" label="编号" width="180" />
      <el-table-column prop="status" label="状态" class-name="status-col" width="60">
        <template slot-scope="{row}">
          <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" icon="el-icon-plus" title="新增字典分类" @click="handleAddArea(row)" />
          <el-button v-if="row.id !== '0'" type="primary" size="mini" icon="el-icon-edit" title="编辑字典分类" @click="handleEditArea(row)" />
          <el-button v-if="row.id !== '0'" type="danger" size="mini" icon="el-icon-delete" title="删除字典分类" @click="handleDeleteArea(row)" />
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogHeaderMap[dialogStatus]" :visible.sync="dialogVisible" width="50%">
      <el-form ref="areaForm" :rules="rules" :model="entity" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-form-item label="父区域" prop="parentName">
          <el-input v-model="entity.parentName" :disabled="true" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="entity.name" />
        </el-form-item>
        <el-form-item label="编号" prop="govCode">
          <el-input v-model="entity.govCode" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="entity.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model.number="entity.sort" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createArea():updateArea()">保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { loadTree, saveArea, deleteArea } from '@/api/area'
import { statusOptions } from '@/filters'
import { pickTreeNode } from '@/utils'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'Area',
  directives: { waves, permission },
  data() {
    return {
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
      statusOptions: statusOptions(),
      currentNode: undefined,
      entity: {
        dataType: 'Object',
        id: undefined,
        uuid: '',
        parentId: '',
        parentName: '',
        name: '',
        govCode: '',
        level: 0,
        status: 0,
        sort: 1,
        remark: ''
      }
    }
  },
  created() {
    this.getAreaTree()
  },
  methods: {
    checkPermission,
    handleAreaChange(val) {
      this.currentNode = val
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
    getAreaTree() {
      const that = this
      that.listLoading = true
      loadTree().then(response => {
        const items = response.data
        if (items && items.length > 0) {
          that.autoChildren(items)
          that.list = items
          setTimeout(() => {
            that.$refs.areaTable.toggleRowExpansion(that.list[0], true)
          }, 100)
        }
        that.listLoading = false
      }).catch((err) => console.log(err))
    },
    resetArea(parent, me) {
      const that = this
      if (me) {
        that.entity = Object.assign({ dataType: 'Object' }, me)
        that.entity.parentId = me.pId
        if (!parent) {
          parent = pickTreeNode(that.list, me.pId)
        }
        that.entity.parentName = parent.name
      } else {
        that.entity = {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          parentId: parent.id,
          parentName: parent.name,
          name: '',
          govCode: '',
          level: 0,
          status: 0,
          sort: 1,
          remark: ''
        }
      }
    },
    handleAddArea(row) {
      this.currentNode = row
      this.resetArea(row)
      this.dialogStatus = 'create'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['areaForm'].clearValidate()
      })
    },
    createArea() {
      const that = this
      that.$refs['areaForm'].validate((valid) => {
        if (valid) {
          saveArea(that.entity).then((response) => {
            const tempData = Object.assign({ dataType: 'Object', children: [] }, response.data)
            if (!that.currentNode.children) {
              that.$set(that.currentNode, 'children', [])
              that.$set(that.currentNode.children, 0, tempData)
            } else {
              that.currentNode.children.push(tempData)
            }
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '区域创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleEditArea(row) {
      this.currentNode = row
      this.resetArea('', row)
      this.dialogStatus = 'update'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['areaForm'].clearValidate()
      })
    },
    updateArea() {
      const that = this
      this.$refs['areaForm'].validate((valid) => {
        if (valid) {
          debugger
          const parentId = that.entity.parentId
          const parent = pickTreeNode(that.list, parentId)
          const tempData = Object.assign({ dataType: 'Object' }, that.entity)
          saveArea(tempData).then((response) => {
            for (let i = 0; i < parent.children.length; i++) {
              if (parent.children[i].id === tempData.id) {
                parent.children.splice(i, 1, tempData)
                break
              }
            }
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    handleDeleteArea(row) {
      const that = this
      that.$confirm('您确定要执行删除操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const parent = pickTreeNode(that.list, row.pId)
        if (parent && parent.children) {
          const { id, uuid } = row
          deleteArea({ id, uuid }).then(() => {
            for (let i = 0; i < parent.children.length; i++) {
              if (parent.children[i].id === row.id) {
                parent.children.splice(i, 1)
                that.$refs.areaTable.setCurrentRow(parent)
                that.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
                break
              }
            }
          }).catch((err) => console.log(err))
        }
      }).catch((err) => console.log(err))
    }

  }
}
</script>
