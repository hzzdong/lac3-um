<template>
  <div class="app-container">
    <el-calendar id="calendar" v-model="curDay">
      <template
        slot="dateCell"
        slot-scope="{date, data}"
      >
        <div style="position: relative">
          <span>{{ data.day.split("-").slice(1).join("-") }}</span>
          <div v-if="holiday(data.day) === 0 " class="calendar-day workday">班</div>
          <div v-if="holiday(data.day) === 1 " class="calendar-day weekend">休</div>
          <div v-if="holiday(data.day) === 2 " class="calendar-day workday2">补</div>
          <div v-if="holiday(data.day) === 3 " class="calendar-day holiday">假</div>
        </div>
      </template>
    </el-calendar>

  </div>
</template>

<script>
import { getCompanyHolidays4Month } from '@/api/holiday'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'Holiday',
  directives: { waves, permission },
  data() {
    return {
      curDay: new Date(),
      calendarArr: []
    }
  },
  computed: {
    year: function() {
      return this.curDay.getFullYear()
    },
    month: function() {
      let m = this.curDay.getMonth() + 1
      if (m < 10) {
        m = '0' + m
      }
      return m
    }
  },
  created() {
    this.getCompanyHolidays4Month()
  },
  mounted() {
    this.$nextTick(() => {
      const prevBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:nth-child(1)')
      prevBtn.addEventListener('click', () => {
        console.log(this.curDay)
        this.getCompanyHolidays4Month()
      })
    })

    this.$nextTick(() => {
      const prevBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:nth-child(2)')
      prevBtn.addEventListener('click', () => {
        console.log(this.curDay)
        this.getCompanyHolidays4Month()
      })
    })

    this.$nextTick(() => {
      const prevBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:last-child')
      prevBtn.addEventListener('click', () => {
        console.log(this.curDay)
        this.getCompanyHolidays4Month()
      })
    })
  },
  methods: {
    checkPermission,
    getCompanyHolidays4Month() {
      const year = this.curDay.getFullYear()
      const month = this.curDay.getMonth() + 1
      const req = { year: year, month: month }
      console.log(req)
      getCompanyHolidays4Month(req).then(response => {
        this.calendarArr = response.data
        // console.log(this.calendarArr)
      }).catch((err) => console.log(err))
    },
    holiday(day) {
      for (let i = 0; i < this.calendarArr.length; i++) {
        if (this.calendarArr[i].date === day) {
          return this.calendarArr[i].status
        }
      }
    }

  }
}
</script>

<style lang="scss" scoped>
  .calendar-day {
    width:40px;
    height:40px;
    padding:10px 10px;
    float: right;
    border: 1px solid #d7dae2;
    border-radius: 20px;
  }
  .weekend {
    background-color: #92fca2;
  }
  .workday {
    background-color: #82d2f7;
  }
  .workday2 {
    background-color: #00a0e9;
  }
  .holiday {
    background-color: #00ff22;
  }
</style>
