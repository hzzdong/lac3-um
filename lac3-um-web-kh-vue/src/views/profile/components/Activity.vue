<template>
  <el-form ref="userForm" :rules="rules" :inline="false" :model="user" size="small" status-icon label-position="right" label-width="100px" style="width: 90%; margin-left:10px;">
    <el-form-item label="姓名：" prop="name">
      <el-input v-model="user.name" />
    </el-form-item>
    <el-form-item label="手机：" prop="mobile">
      <el-input v-model="user.mobile" />
    </el-form-item>
    <el-form-item label="账号：" prop="account">
      <span class="el-span_view">{{ user.account }}</span>
    </el-form-item>
    <el-form-item label="工号：" prop="govCode">
      <el-input v-model="user.govCode" />
    </el-form-item>
    <el-form-item label="昵称：" prop="nickName">
      <el-input v-model="user.nickName" />
    </el-form-item>
    <el-form-item label="备注：">
      <el-input v-model="user.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSaveMe">确定</el-button>
      <el-button @click="onClose()">关闭</el-button>
    </el-form-item>

  </el-form>
</template>

<script>
import { validateMobile } from '@/utils/validate'
import { updateMe } from '@/api/user'
import { sheetClose } from '@/utils'

export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          dataType: 'Object',
          id: undefined,
          uuid: '',
          parentId: '',
          parentClass: '',
          name: '',
          account: '',
          govCode: '',
          mobile: '',
          nickName: '',
          remark: '',
          roleEnabled: false
        }
      }
    }
  },
  data() {
    return {
      rules: {
        account: [{ required: true, message: '账号不能为空', trigger: 'blur' }, { min: 6, max: 64, message: '账号长度在 2 到 64 个字符', trigger: 'blur' }],
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }],
        mobile: [{ validator: (rule, value, callback) => {
          if (value && value !== '') {
            if (!validateMobile(value)) {
              callback(new Error('请输入有效的手机号码'))
              return
            }
          }
          callback()
        }, trigger: 'blur' }],
        govCode: [{ min: 2, max: 64, message: '工号长度在 2 到 64 个字符', trigger: 'blur' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
      }
    }
  },
  methods: {
    onClose() {
      sheetClose(this)
    },
    onSaveMe() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          const userObj = Object.assign({ dataType: 'Object' }, this.user)
          userObj.roleEnabled = false
          updateMe(userObj).then(() => {
            this.$store.dispatch('user/refreshUserInfo', this.user)
            this.$notify({ title: '提示', message: '用户信息保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    }
  }
}
</script>
