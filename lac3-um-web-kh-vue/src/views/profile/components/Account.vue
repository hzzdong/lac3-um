<template>
  <el-form ref="passForm" :rules="rules" :inline="false" :model="user" size="small" status-icon label-position="right" label-width="100px" style="width: 90%; margin-left:10px;">
    <el-form-item label="姓名：">
      <span class="el-span_view">{{ user.name }}</span>
    </el-form-item>
    <el-form-item label="账号：">
      <span class="el-span_view">{{ user.account }}</span>
    </el-form-item>
    <el-form-item label="旧密码" prop="oldpass">
      <el-input v-model="user.oldpass" type="password" autocomplete="off" placeholder="请输入旧密码" />
    </el-form-item>
    <el-form-item label="新密码" prop="password">
      <el-input v-model="user.password" type="password" autocomplete="off" placeholder="请输入新密码" />
    </el-form-item>
    <el-form-item label="确认密码" prop="checkPass">
      <el-input v-model="user.checkPass" type="password" autocomplete="off" placeholder="请输入确认密码" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onUpdatePass">确定</el-button>
      <el-button @click="onClose()">关闭</el-button>
    </el-form-item>

  </el-form>
</template>

<script>
import { sheetClose } from '@/utils'
import { updatePass } from '@/api/user'
import md5 from 'js-md5'

export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          dataType: 'Object',
          oldpass: '',
          password: '',
          checkPass: '',
          name: '',
          account: '',
          roleEnabled: false
        }
      }
    }
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.user.checkPass !== '') {
          this.$refs.passForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.user.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      rules: {
        oldpass: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
        password: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { validator: validatePass, trigger: 'blur' }],
        checkPass: [{ required: true, message: '请输入确认密码', trigger: 'blur' }, { validator: validatePass2, trigger: 'blur' }]
      }
    }
  },
  methods: {
    onClose() {
      sheetClose(this)
    },
    onUpdatePass() {
      this.$refs['passForm'].validate((valid) => {
        if (valid) {
          const userObj = { oldpass: md5(this.user.oldpass), password: md5(this.user.password) }
          updatePass(userObj).then((response) => {
            console.log(response)
            if (response.data === true) {
              this.$notify({ title: '提示', message: '密码更新成功', type: 'success', duration: 2000 })
            } else {
              this.$message({ message: '修改密码失败，可能是您输入的旧密码错误，请重试。', type: 'error' })
            }
          })
        }
      })
    }
  }
}
</script>
