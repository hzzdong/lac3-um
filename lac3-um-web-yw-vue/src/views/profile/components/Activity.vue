<template>
  <div class="app-container">
    <el-form ref="userForm" :rules="rules" :inline="false" :model="user" size="small" status-icon label-position="right" label-width="100px" style="width: 90%; margin-left:10px;">
      <el-form-item label="头像:" prop="ico">
        <my-upload
          v-model="show"
          field="file"
          :width="300"
          :height="300"
          url="/fss/pub/Archive/upload"
          :params="params"
          :headers="headers"
          img-format="png"
          @src-file-set="srcFileSet"
          @crop-success="cropSuccess"
          @crop-upload-success="cropUploadSuccess"
          @crop-upload-fail="cropUploadFail"
        />
        <el-avatar :size="100" :src="user.ico" />
        <el-button @click="toggleShow()">更换头像</el-button>
      </el-form-item>
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
  </div>
</template>

<script>
import { validateMobile } from '@/utils/validate'
import { updateMe, updateHeaderImage } from '@/api/user'
import { sheetClose } from '@/utils'
import 'babel-polyfill' // es6 shim
import myUpload from 'vue-image-crop-upload'

export default {
  components: { 'my-upload': myUpload },
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
          ico: '',
          remark: '',
          roleEnabled: false
        }
      }
    }
  },
  data() {
    return {
      show: false,
      params: {
        appId: '16',
        appName: '用户中心',
        objectId: '',
        objectType: '10001'
      },
      headers: {
        smail: '*_~'
      },
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
          userObj.ico = ''
          updateMe(userObj).then(() => {
            this.$store.dispatch('user/refreshUserInfo', this.user)
            this.$notify({ title: '提示', message: '用户信息保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    toggleShow() {
      this.show = true
    },
    srcFileSet(fileName, fileType, fileSize) {
      this.params.objectId = this.user.uuid
    },
    /**
			 * crop success
			 *
			 * [param] imgDataUrl
			 * [param] field
			 */
    cropSuccess(imgDataUrl, field) {
      console.log('-------- crop success --------')
      this.user.ico = imgDataUrl
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
        const icoReq = { dataType: 'Object', key: 'ico', value: jsonData.data.path }
        updateHeaderImage(icoReq).then((response) => {
          that.$store.dispatch('user/updateHeaderImage', icoReq.value)
          that.show = false
          that.$notify({ title: '提示', message: '头像上传成功！', type: 'success', duration: 2000 })
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
