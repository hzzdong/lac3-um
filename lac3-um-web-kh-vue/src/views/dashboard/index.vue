<template>
  <div class="dashboard-container">
    <el-image
      style="width: 40%; margin-left: 300px;margin-top: 100px;"
      :src="welcomeSrc"
      fit="contain"
    />
  </div>
</template>

<script>
import welcomeSrc from '@/assets/welcome.png'

export default {
  name: 'Dashboard',
  data() {
    return {
      welcomeSrc: welcomeSrc
    }
  }
}
</script>
