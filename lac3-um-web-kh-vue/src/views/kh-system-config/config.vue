<template>
  <div class="app-container">
    <el-table
      ref="configTable"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      style="width: 100%;"
    >
      <el-table-column prop="key" label="编号" width="200px" />
      <el-table-column prop="name" label="名称" width="200px" />
      <el-table-column prop="value" label="值" width="350px">
        <template slot-scope="{row}">
          <el-switch
            v-if="row.key === 'enable_org_permission'"
            v-model="entity.enable_org_permission"
            active-text="启用"
            inactive-text="禁用"
            @change="haddleChangeOrgPermission($event, row)"
          />
          <el-switch
            v-if="row.key === 'enable_area_permission'"
            v-model="entity.enable_area_permission"
            active-text="启用"
            inactive-text="禁用"
            @change="haddleChangeAreaPermission($event, row)"
          />
          <span v-if="row.key === 'area_roots'">
            <el-table
              ref="companyAreaTable"
              v-loading="listLoading"
              :data="entity.companyAreas"
              tooltip-effect="dark"
              style="width: 100%"
              border
              fit
            >
              <el-table-column label="区域名称" min-width="100px">
                <template slot-scope="scope">
                  <span>{{ scope.row.value }}</span>
                </template>
              </el-table-column>
            </el-table>
            <el-button type="primary" icon="el-icon-edit" circle @click="toSelectAreas" />
          </span>
          <span v-if="row.key === 'logo'"><el-button type="primary" icon="el-icon-upload" circle /> {{ row.value }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注说明" min-width="100px" />
    </el-table>

    <el-dialog title="单位根区域设置" :visible.sync="areaTree.dialogFormVisible">
      <el-tree
        ref="tree_area"
        :data="areaTree.data"
        :props="areaTree.defaultProps"
        node-key="id"
        :expand-on-click-node="false"
        :check-on-click-node="true"
        show-checkbox
        :check-strictly="true"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="areaTree.dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onSaveAreas()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { findCompanyConfigs, saveCompanyConfig } from '@/api/config'
import { loadCompanyAreaFullTree, getConfigCompanyAreaRootIds } from '@/api/organization'
import { parseTreeNodeChecked } from '@/utils'
import waves from '@/directive/waves' // waves directive

export default {
  name: 'KhSystemConfig',
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      entity: {
        enable_org_permission: false,
        enable_area_permission: false,
        // area_roots: [],
        companyAreas: [],
        logo: ''
      },
      areaTree: {
        dialogFormVisible: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      }

    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      findCompanyConfigs().then(response => {
        this.list = response.data
        // console.log(this.list)
        this.listLoading = false
        if (this.list && this.list.length > 0) {
          for (const item of this.list) {
            if (item.key === 'enable_org_permission') {
              this.entity.enable_org_permission = item.value === 'yes'
            } else if (item.key === 'enable_area_permission') {
              this.entity.enable_area_permission = item.value === 'yes'
            } else if (item.key === 'area_roots') {
              // this.entity.area_roots = item.value
              this.entity.companyAreas = JSON.parse(item.value)
            } else if (item.key === 'logo') {
              this.entity.logo = item.value
            }
          }
        }
      })
    },
    handleFilter() {
      this.getList()
    },
    haddleChangeOrgPermission(val, row) {
      const fv = val === true ? 'yes' : 'no'
      const req = { key: row.key, value: fv }
      saveCompanyConfig(req).then(() => {
        this.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
      }).catch(() => {
      })
    },
    haddleChangeAreaPermission(val, row) {
      const fv = val === true ? 'yes' : 'no'
      const req = { key: row.key, value: fv }
      saveCompanyConfig(req).then(() => {
        this.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
      }).catch(() => {
      })
    },
    getAreaTree() {
      loadCompanyAreaFullTree().then(response => {
        this.areaTree.data = response.data
      })
    },
    getCompanyAreaFullTreeAndChecded() {
      const p1 = loadCompanyAreaFullTree()
      const p2 = getConfigCompanyAreaRootIds()
      return Promise.all([p1, p2])
    },
    toSelectAreas() {
      this.getCompanyAreaFullTreeAndChecded().then(response => {
        debugger
        this.areaTree.data = response[0].data
        this.areaTree.dialogFormVisible = true
        const companyAreaIds = response[1].data
        this.$nextTick(() => {
          const checkedIds = []
          if (companyAreaIds && companyAreaIds.length > 0) {
            for (const checkedAreaId of companyAreaIds) {
              parseTreeNodeChecked(checkedIds, checkedAreaId + '', this.areaTree.data)
            }
          }
          if (checkedIds.length > 0) {
            this.$refs.tree_area.setCheckedKeys(checkedIds)
          }
        })
      }).catch((e) => {
        console.log(e)
      })
    },
    onSaveAreas() {
      const items = this.$refs.tree_area.getCheckedNodes()
      const areas = []
      if (items && items.length > 0) {
        for (const item of items) {
          areas.push({ key: item.id, value: item.name })
        }
      }
      // console.log(JSON.stringify(areas))
      const req = { key: 'area_roots', value: areas }
      saveCompanyConfig(req).then(() => {
        for (const config of this.list) {
          if (config.key === 'area_roots') {
            // config.value = JSON.stringify(areas)
            this.entity.companyAreas = []
            for (const area of areas) {
              this.entity.companyAreas.push(area)
            }
          }
        }
        this.areaTree.dialogFormVisible = false
        this.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
      }).catch(() => {
      })
    }

  }
}
</script>
