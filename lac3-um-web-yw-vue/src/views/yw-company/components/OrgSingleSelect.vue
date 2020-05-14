<template>
  <div class="app-container">
    <el-card class="box-card" shadow="never">
      <div class="text item">
        <el-tree
          ref="tree"
          :data="tree.data"
          :props="tree.defaultProps"
          node-key="id"
          :default-expand-all="true"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          show-checkbox
          :check-strictly="true"
          @node-click="handleNodeClick"
          @check-change="handleCheckChange"
        />
      </div>
      <div class="bottom clearfix" style="padding-top:10px;">
        <el-button style="float: right;" type="primary" icon="el-icon-circle-check" @click="onOrgSelect">确认选择</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getCompanyTree } from '@/api/organization'
import waves from '@/directive/waves' // waves directive
import { userStatusOptions, userTypeOptions } from '@/filters'

export default {
  name: 'LacOrgSelect',
  directives: { waves },
  props: {
    treeType: {
      type: String,
      default: 'SelfTree'
    },
    companyId: {
      type: Number,
      default: -1
    },
    companyUuid: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      tree: {
        checkedNode: undefined,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      statusOptions: userStatusOptions(),
      typeOptions: userTypeOptions()
    }
  },
  created() {
    this.getTree()
  },
  methods: {
    getTree() {
      const req = { id: this.companyId, uuid: this.companyUuid, type: this.treeType }
      getCompanyTree(req).then(response => {
        this.tree.data = response.data
        if (this.tree.data[0]) {
          this.tree.checkedNode = this.tree.data[0]
          this.$refs.tree.setCheckedKeys([this.tree.checkedNode.id])
        }
      })
    },
    handleNodeClick(data, checked, node) {
      this.tree.checkedNode = data
      this.$refs.tree.setCheckedNodes([data])
    },
    handleCheckChange(data, checked, node) {
      if (checked === true) {
        this.tree.checkedNode = data
        this.$refs.tree.setCheckedNodes([data])
      }
    },
    onOrgSelect() {
      this.$emit('onOrgSingleSelected', this.tree.checkedNode)
    }
  }
}
</script>
