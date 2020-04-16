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
        <el-tag :type="row.status | statusTypeFilter">{{ row.status | statusFilter }}</el-tag>
      </template>
    </zk-table>

  </div>
</template>

<script>
import Vue from 'vue'
import ZkTable from 'vue-table-with-tree-grid'
Vue.use(ZkTable)
import { getFullOrgTree } from '@/api/organization'

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
          width: '90px',
          type: 'template',
          template: 'statusTemplate'
        }
      ],
      statusOptions
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
  },
  methods: {
    getTree() {
      getFullOrgTree().then(response => {
        debugger
        this.data = response.data
      })
    }
  }
}
</script>
