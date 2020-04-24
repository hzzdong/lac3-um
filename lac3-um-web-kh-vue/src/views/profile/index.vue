<template>
  <div class="app-container">
    <el-card>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="个人信息修改" name="activity">
          <activity :user="user" />
        </el-tab-pane>
        <el-tab-pane label="密码修改" name="account">
          <account :user="user" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import Activity from './components/Activity'
import Account from './components/Account'
import { getLoginUser } from '@/api/user'

export default {
  name: 'Profile',
  components: { Activity, Account },
  data() {
    return {
      user: {},
      activeTab: 'activity'
    }
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      getLoginUser().then(response => {
        this.user = response.data
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>
