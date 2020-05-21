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
      <el-table-column prop="name" label="名称" width="250px" />
      <el-table-column prop="key" label="编号" width="250px" />
      <el-table-column prop="value" label="值" width="350px">
        <template slot-scope="{row}">
          <el-switch
            v-if="row.key === 'enable_org_permission'"
            v-model="entity.enable_org_permission"
            active-text="启用"
            inactive-text="禁用"
            @change="haddleChangeStatusSwitch($event, row)"
          />
          <el-switch
            v-if="row.key === 'enable_area_permission'"
            v-model="entity.enable_area_permission"
            active-text="启用"
            inactive-text="禁用"
            @change="haddleChangeStatusSwitch($event, row)"
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
          <span v-if="row.key === 'logo'">
            <my-upload
              v-model="show"
              field="file"
              :width="300"
              :height="300"
              url="/fss/pub/Archive/upload"
              :params="params"
              :headers="headers"
              img-format="png"
              @crop-success="cropSuccess"
              @crop-upload-success="cropUploadSuccess"
              @crop-upload-fail="cropUploadFail"
            />
            <el-avatar :size="100" :src="entity.logo" />
            <el-button type="primary" icon="el-icon-upload" circle title="公司LOGO上传" @click="toggleShow()" />
          </span>
          <el-switch
            v-if="row.key === 'enable_m_dep'"
            v-model="entity.enable_m_dep"
            active-text="启用"
            inactive-text="禁用"
            @change="haddleChangeStatusSwitch($event, row)"
          />
          <el-switch
            v-if="row.key === 'enable_zzd'"
            v-model="entity.enable_zzd"
            active-text="启用"
            inactive-text="禁用"
            @change="haddleChangeStatusSwitch($event, row)"
          />
          <!--
          <el-select
            v-if="row.key === 'company_class'"
            v-model="entity.company_class"
            placeholder="请选择单位类型"
            clearable
            class="filter-item"
            style="width: 100%"
            @change="haddleChangeComanyClass"
          >
            <el-option v-for="item in companyClasses" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          -->

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
import { loadCompanyAreaFullTree, getConfigCompanyAreaRootIds, fetchMyCompany } from '@/api/organization'
import { parseTreeNodeChecked } from '@/utils'
import waves from '@/directive/waves' // waves directive
import myUpload from 'vue-image-crop-upload'

const companyClasses = [
  { key: 'kh_xx', display_name: '普通单位' },
  { key: 'kh_qy', display_name: '企业单位' }
]

export default {
  name: 'YwSystemConfig',
  directives: { waves },
  components: { 'my-upload': myUpload },
  data() {
    return {
      companyClasses: companyClasses,
      tableKey: 0,
      list: null,
      listLoading: true,
      entity: {
        enable_org_permission: false,
        enable_area_permission: false,
        // area_roots: [],
        companyAreas: [],
        logo: '',
        enable_m_dep: false,
        enable_zzd: false,
        company_class: ''
      },
      areaTree: {
        dialogFormVisible: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      /* LOGO */
      company: {},
      show: false,
      params: {
        appId: '1',
        appName: '用户中心',
        objectId: '',
        objectType: '10002'
      },
      headers: {
        smail: '*_~'
      }

    }
  },
  created() {
    this.getList()
    this.getMyCompany()
  },
  methods: {
    getList() {
      this.listLoading = true
      findCompanyConfigs().then(response => {
        this.list = response.data
        this.listLoading = false
        if (this.list && this.list.length > 0) {
          for (const item of this.list) {
            if (item.key === 'enable_org_permission') {
              this.entity.enable_org_permission = item.value === 'yes'
            } else if (item.key === 'enable_area_permission') {
              this.entity.enable_area_permission = item.value === 'yes'
            } else if (item.key === 'area_roots') {
              if (item.value && item.value.length > 0) {
                this.entity.companyAreas = JSON.parse(item.value)
              }
            } else if (item.key === 'logo') {
              if (item.value && item.value.length > 0) {
                this.entity.logo = this.$store.state.settings.fssBaseUrl + item.value
              }
            } else if (item.key === 'enable_m_dep') {
              this.entity.enable_m_dep = item.value === 'yes'
            } else if (item.key === 'enable_zzd') {
              this.entity.enable_zzd = item.value === 'yes'
            } else if (item.key === 'company_class') {
              this.entity.company_class = item.value
            }
          }
        }
      })
    },
    getMyCompany() {
      fetchMyCompany().then((response) => {
        this.company = response.data
        this.params.objectId = this.company.uuid
      }).catch((err) => {
        console.log(err)
      })
    },
    handleFilter() {
      this.getList()
    },
    haddleChangeStatusSwitch(val, row) {
      const fv = val === true ? 'yes' : 'no'
      const req = { key: row.key, value: fv }
      saveCompanyConfig(req).then(() => {
        this.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
      }).catch((err) => console.log(err))
    },
    /*
    haddleChangeComanyClass(val) {
      const req = { key: 'company_class', value: val }
      saveCompanyConfig(req).then(() => {
        this.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
      }).catch((err) => console.log(err))
    },
    */
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
    },
    /* LOGO */
    toggleShow() {
      this.show = true
    },
    /**
			 * crop success
			 *
			 * [param] imgDataUrl
			 * [param] field
			 */
    cropSuccess(imgDataUrl, field) {
      console.log('-------- crop success --------')
      this.entity.logo = imgDataUrl
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
        const req = { key: 'logo', value: jsonData.data.path }
        saveCompanyConfig(req).then(() => {
          that.show = false
          that.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
        }).catch(() => {
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
