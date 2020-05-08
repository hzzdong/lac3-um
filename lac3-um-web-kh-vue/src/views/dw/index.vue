<template>
  <div class="app-container">
    <el-table
      ref="companyTreeViewTable"
      :data="companyTree"
      style="width: 100%;margin-bottom: 20px;"
      row-key="id"
      border
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="name" label="名称" width="250">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="govCode" label="编号" width="150" />
      <el-table-column label="联系人" width="120">
        <template slot-scope="{row}">
          <span>{{ row.attributes.linkUserName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系方式" width="110">
        <template slot-scope="{row}">
          <span>{{ row.attributes.linkUserPhone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状" class-name="status-col" width="40" prop="status">
        <template slot-scope="{row}">
          <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注说明" min-width="100" />
      <el-table-column label="操作" width="60">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" icon="el-icon-user" title="代维" @click="handleDw(row)" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getFullTreeOfCompany } from '@/api/organization'
import { sheetClose, sheetRefresh } from '@/utils'
import waves from '@/directive/waves' // waves directive

export default {
  name: 'CompanyView',
  directives: { waves },
  data() {
    return {
      companyTree: []
    }
  },
  computed: {
    isDwMode: function() {
      const cuser = this.$store.getters.currentUser
      if (this.company && this.company.id) {
        return this.company.id === cuser.companyId
      }
      return false
    }
  },
  created() {
    this.getCompanyTree()
  },
  methods: {
    getCompanyTree() {
      getFullTreeOfCompany().then(response => {
        this.companyTree = response.data
      })
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    handleDw(row) {
      this.$confirm('您确定要执行删除操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('user/logout').then(() => {
          // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
          window.location = `/api4umkh/dwLogin?id=${row.id.substring(1)}&uuid=${row.uuid}`
        }).catch(err => console.log(err))
      }).catch(err => console.log(err))
    }

  }
}
</script>
