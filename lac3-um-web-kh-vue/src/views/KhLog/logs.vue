<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.rules.tid.fv" placeholder="TID 精确匹配" style="width: 150px;" class="filter-item" />
      <el-input v-model="listQuery.rules.module.fv" placeholder="模块名称 模糊匹配" style="width: 150px;" class="filter-item" />
      <el-select v-model="listQuery.rules.operateResult.fv" placeholder="操作结果" clearable class="filter-item" style="width: 110px">
        <el-option v-for="item in orOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-input v-model="listQuery.rules.ip.fv" placeholder="IP地址 精确匹配" style="width: 150px;" class="filter-item" />
      <el-input v-model="listQuery.rules.operatorAccount.fv" placeholder="操作者账号 模糊匹配" style="width: 170px;" class="filter-item" />
      <div class="filter-item">
        <el-date-picker
          v-model="listQuery.rules.operateTime.fv"
          type="daterange"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptions"
        />
      </div>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" plain @click="handleFilter" />
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
      <el-table-column prop="module" label="模块" width="90px" />
      <el-table-column label="内容描述" min-width="250px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleView(row)">{{ row.operateDesc }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="operateTime" label="操作时间" width="160px" />
      <el-table-column label="结果" width="80px" class-name="status-col">
        <template slot-scope="scope">
          <el-tag effect="dark" size="small" :type="scope.row.operateResult | statusTypeFilter"><span>{{ scope.row.operateResult | operateResult }}</span></el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="costTime" label="耗时" width="70px" />
      <el-table-column prop="ip" label="IP" width="130px" />
      <el-table-column label="操作者" width="120px">
        <template slot-scope="scope">
          <span>{{ scope.row.operatorAccount }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog title="日志详情" :visible.sync="dialogFormVisible" width="75%">
      <el-form ref="dataForm" :inline="false" :model="temp" size="small" status-icon label-position="right" label-width="80px" style="width: 95%; margin-left:30px;">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作时间:">
              <span class="el-span_view">{{ temp.operateTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="TID:">
              <span class="el-span_view">{{ temp.tid }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="模块:">
              <span class="el-span_view">{{ temp.module }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作者:">
              <span class="el-span_view">{{ temp.operatorAccount }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="操作内容描述:">
              <el-input v-model="temp.operateDesc" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="操作内容描述" class="el-textarea_view" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作结果:">
              <span class="el-span_view">{{ temp.operateResult | operateResult }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="耗时:">
              <span class="el-span_view">{{ temp.costTime + ' ( 毫秒 )' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="temp.operateResult !== 0">
          <el-col :span="24">
            <el-form-item label="错误信息:">
              <el-input v-model="temp.errorMessage" :autosize="{ minRows: 5, maxRows: 10}" type="textarea" placeholder="错误信息描述" class="el-textarea_view" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="IP:">
              <span class="el-span_view">{{ temp.ip }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="URL:">
              <span class="el-span_view">{{ temp.url }}</span>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { findWebLogPage } from '@/api/weblog'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const orOptions = [
  { key: 0, display_name: '成功' },
  { key: 1, display_name: '失败' }
]

export default {
  name: 'KhRole',
  components: { Pagination },
  directives: { waves },
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
          tid: { fv: undefined, oper: 'eq', stype: 'S' },
          module: { fv: undefined, oper: 'cn', stype: 'S' },
          operateResult: { fv: undefined, oper: 'eq', stype: 'I' },
          ip: { fv: undefined, oper: 'eq', stype: 'S' },
          operatorAccount: { fv: undefined, oper: 'cn', stype: 'S' },
          operateTime: { fv: undefined, oper: 'eq', stype: 'DR' }
        },
        orderby: { orderby: 'id', order: 'desc' }
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      orOptions,
      temp: {},
      dialogFormVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      findWebLogPage(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.recordsTotal
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleView(row) {
      this.temp = row
      this.dialogFormVisible = true
    }
  }
}
</script>
