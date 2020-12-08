<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;">
      <el-tab-pane label="工作日管理" name="tabHoliday">
        <el-calendar id="calendar" v-model="curDay">
          <template
            slot="dateCell"
            slot-scope="{date, data}"
          >
            <div slot="reference" class="calendar" @click="dayOnClick(date,data)">
              <span>{{ data.day.split("-").slice(1).join("-") }}</span>
              <span class="calendar-day" :class="'day-'+holiday(data.day)">
                {{ holidayText(data.day) }}
              </span>
            </div>
          </template>
        </el-calendar>
        <el-form :inline="true">
          <el-form-item>
            <el-switch v-model="mode" active-text="我要编辑" inactive-text="查看" />
          </el-form-item>
          <el-form-item v-if="mode === true" label="切换到编辑模式，请先选择日期类型，再点击日期设置。">
            <el-radio-group v-model="dayType">
              <el-radio-button label="0">工作日</el-radio-button>
              <el-radio-button label="1">周末</el-radio-button>
              <el-radio-button label="2">补班</el-radio-button>
              <el-radio-button label="3">放假</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="工作时间管理" name="tabWorkTime">
        <el-form ref="workTimeForm" :model="workTime" label-width="120px">
          <el-form-item label="上午上班：">
            <el-col :span="5">
              <el-select v-model="workTime.amGoToWorkHour" placeholder="请选择上午几点上班" style="width:100%;">
                <el-option v-for="index of 24" :key="index-1" :label="(index - 1)+'点'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（点）</el-col>
            <el-col :span="5">
              <el-select v-model="workTime.amGoToWorkMinute" placeholder="请选择上午几分上班" style="width:100%;">
                <el-option v-for="index of 60" :key="index-1" :label="(index - 1)+'分'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（分）</el-col>
            <el-col class="line" :span="12" />
          </el-form-item>
          <el-form-item label="上午下班：">
            <el-col :span="5">
              <el-select v-model="workTime.amGoOffWorkHour" placeholder="请选择上午几点下班" style="width:100%;">
                <el-option v-for="index of 24" :key="index-1" :label="(index - 1)+'点'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（点）</el-col>
            <el-col :span="5">
              <el-select v-model="workTime.amGoOffWorkMinute" placeholder="请选择上午几分下班" style="width:100%;">
                <el-option v-for="index of 60" :key="index-1" :label="(index - 1)+'分'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（分）</el-col>
            <el-col class="line" :span="12" />
          </el-form-item>
          <el-form-item label="下午上班：">
            <el-col :span="5">
              <el-select v-model="workTime.pmGoToWorkHour" placeholder="请选择下午几点上班" style="width:100%;">
                <el-option v-for="index of 24" :key="index-1" :label="(index - 1)+'点'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（点）</el-col>
            <el-col :span="5">
              <el-select v-model="workTime.pmGoToWorkMinute" placeholder="请选择下午几分上班" style="width:100%;">
                <el-option v-for="index of 60" :key="index-1" :label="(index - 1)+'分'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（分）</el-col>
            <el-col class="line" :span="12" />
          </el-form-item>
          <el-form-item label="下午下班：">
            <el-col :span="5">
              <el-select v-model="workTime.pmGoOffWorkHour" placeholder="请选择下午几点下班" style="width:100%;">
                <el-option v-for="index of 24" :key="index-1" :label="(index - 1)+'点'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（点）</el-col>
            <el-col :span="5">
              <el-select v-model="workTime.pmGoOffWorkMinute" placeholder="请选择下午几分下班" style="width:100%;">
                <el-option v-for="index of 60" :key="index-1" :label="(index - 1)+'分'" :value="index-1" />
              </el-select>
            </el-col>
            <el-col class="line" :span="1">（分）</el-col>
            <el-col class="line" :span="12" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSaveWorkTime">保存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

    </el-tabs></div>
</template>

<script>
import { getCompanyHolidays4Month, setCompanyHoliday, getCompanyWorkTime, initCompanyWorkTime, setCompanyWorkTime } from '@/api/holiday'
import { sheetClose, sheetRefresh } from '@/utils'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'Holiday',
  directives: { waves, permission },
  data() {
    return {
      tabPosition: 'right',
      activeTab: 'tabHoliday',
      mode: false,
      dayType: 3,
      curDay: new Date(),
      calendarArr: [],
      workTime: {
        companyId: '',
        amGoToWorkHour: 8,
        amGoToWorkMinute: 30,
        amGoOffWorkHour: 12,
        amGoOffWorkMinute: 0,
        pmGoToWorkHour: 13,
        pmGoToWorkMinute: 30,
        pmGoOffWorkHour: 17,
        pmGoOffWorkMinute: 30
      }
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
    this.getWorkTime()
    this.getHolidays()
  },
  mounted() {
    this.$nextTick(() => {
      const prevBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:nth-child(1)')
      prevBtn.addEventListener('click', () => {
        console.log(this.curDay)
        this.getHolidays()
      })
    })

    this.$nextTick(() => {
      const prevBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:nth-child(2)')
      prevBtn.addEventListener('click', () => {
        console.log(this.curDay)
        this.getHolidays()
      })
    })

    this.$nextTick(() => {
      const prevBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:last-child')
      prevBtn.addEventListener('click', () => {
        console.log(this.curDay)
        this.getHolidays()
      })
    })
  },
  methods: {
    checkPermission,
    getWorkTime() {
      const that = this
      getCompanyWorkTime().then(response => {
        if (response.message === 'no') {
          that.$confirm('您所在公司未启用工作时间设置（系统会自动继承父公司设置），您是否启用本公司工作时间设置？', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            initCompanyWorkTime().then(response => {
              sheetRefresh(that)
            }).catch((err) => console.log(err))
          }).catch((e) => {
            sheetClose(that)
          })
        } else {
          this.workTime = response.data
        }
      }).catch((err) => console.log(err))
    },
    getHolidays() {
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
      return 9
    },
    holidayText(day) {
      for (let i = 0; i < this.calendarArr.length; i++) {
        if (this.calendarArr[i].date === day) {
          const dType = this.calendarArr[i].status
          if (dType === 0 || dType === '0') {
            return '班'
          } else if (dType === 1 || dType === '1') {
            return '休'
          } else if (dType === 2 || dType === '2') {
            return '补'
          } else if (dType === 3 || dType === '3') {
            return '假'
          }
        }
      }
      return ''
    },
    dayOnClick(date, data) {
      const that = this
      const da = data.day.split('-')
      debugger
      if (da[0] !== (that.year + '') || da[1] !== (that.month + '')) {
        const req = { year: da[0], month: da[1] }
        getCompanyHolidays4Month(req).then(response => {
          that.calendarArr = response.data
        }).catch((err) => console.log(err))
      } else {
        if (that.mode === true) {
          let index = -1
          let calendarDate
          for (let i = 0; i < that.calendarArr.length; i++) {
            if (that.calendarArr[i].date === data.day) {
              index = i
              calendarDate = Object.assign({}, that.calendarArr[i])
              break
            }
          }
          if (!calendarDate || !calendarDate.date || !calendarDate.year) {
            that.$notify({ title: '提示', message: '更新失败: ' + data.day + ' 尚未在系统中自动生成节假日信息，请迟些时间再进行设置！', type: 'error', duration: 2000 })
          } else if (index !== -1 && (calendarDate.status + '') !== (that.dayType + '')) {
            calendarDate.status = that.dayType
            setCompanyHoliday(calendarDate).then(response => {
              that.calendarArr[index] = calendarDate
              that.calendarArr.splice(index, 1, calendarDate)
              that.$notify({ title: '提示', message: data.day + '被成功设置为' + that.holidayText(data.day), type: 'success', duration: 2000 })
            }).catch((err) => console.log(err))
          }
        }
      }
      // this.curDay = date
      // console.log(date)
    },
    onSaveWorkTime() {
      const entity = Object.assign({ dataType: 'Object' }, this.workTime)
      setCompanyWorkTime(entity).then(response => {
        this.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
      }).catch((err) => console.log(err))
    }
  }
}
</script>

<style lang="scss" scoped>
  .calendar {
    position: relative;
    width: 100%;
    height: 100%;
  }
  .calendar-day {
    width:40px;
    height:40px;
    padding:10px 10px;
    float: right;
    border: 1px solid #d7dae2;
    border-radius: 20px;
  }
  .day-0 {
    background-color: #82d2f7;
  }
  .day-1 {
    background-color: #92fca2;
  }
  .day-2 {
    background-color: #00a0e9;
  }
  .day-3 {
    background-color: #00ff22;
  }
  .day-9 {
    display: none;
  }
</style>
