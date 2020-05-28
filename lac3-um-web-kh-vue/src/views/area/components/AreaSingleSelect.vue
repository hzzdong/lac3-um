<template>
  <div class="app-container">
    <el-card class="box-card" shadow="never">
      <div class="text item">
        <el-tree
          ref="tree"
          :data="tree.data"
          :props="tree.defaultProps"
          node-key="id"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          show-checkbox
          :check-strictly="true"
          :default-expanded-keys="['0']"
          @node-click="handleNodeClick"
          @check-change="handleCheckChange"
        />
      </div>
      <div class="bottom clearfix" style="padding-top:10px;">
        <el-button style="float: right;" type="primary" icon="el-icon-circle-check" @click="onSelect">确认选择</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { loadTree, loadAreaTree } from '@/api/area'
import waves from '@/directive/waves' // waves directive
import { statusOptions } from '@/filters'

export default {
  name: 'LacAreaSingleSelect',
  directives: { waves },
  props: {
    treeType: {
      type: String,
      default: 'FullTree'
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
      statusOptions: statusOptions()
    }
  },
  created() {
    this.getTree()
  },
  methods: {
    getTree() {
      const that = this
      if (this.treeType === 'FullTree') {
        loadTree({}).then(response => {
          that.tree.data = response.data
          if (that.tree.data && that.tree.data.length > 0) {
            const root = that.tree.data[0]
            // that.tree.checkedNode = root
            // that.$refs.tree.setCheckedKeys([that.tree.checkedNode.id])
            if (root.id === '0') {
              root.disabled = true
            }
          }
        })
      } else {
        loadAreaTree({}).then(response => {
          that.tree.data = response.data
          if (that.tree.data && that.tree.data.length > 0) {
            const root = that.tree.data[0]
            // that.tree.checkedNode = root
            // that.$refs.tree.setCheckedKeys([that.tree.checkedNode.id])
            if (root.id === '0') {
              root.disabled = true
            }
          }
        })
      }
    },
    handleNodeClick(data, checked, node) {
      if (data.id !== '0') {
        this.tree.checkedNode = data
        this.$refs.tree.setCheckedNodes([data])
      } else {
        this.$notify({ title: '错误', message: '虚拟根节点无法选择，请选择其它节点！', type: 'warning', duration: 3000 })
      }
    },
    handleCheckChange(data, checked, node) {
      if (checked === true) {
        this.tree.checkedNode = data
        this.$refs.tree.setCheckedNodes([data])
      }
    },
    onSelect() {
      const checkedNodes = this.$refs.tree.getCheckedNodes()
      if (!checkedNodes || checkedNodes.length <= 0) {
        this.$notify({ title: '错误', message: '请先选择一个节点，然后再进行确认！', type: 'error', duration: 3000 })
      } else {
        this.$emit('onAreaSingleSelected', this.tree.checkedNode)
      }
    }
  }
}
</script>
