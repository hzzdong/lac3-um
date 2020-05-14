<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
<style>
.el-button--mini {
    padding: 7px 8px;
    font-size: 12px;
    border-radius: 3px;
}
.tool-bar {
    border-bottom:1px solid #DCDFE6;
    margin-bottom: 20px;
}
.el-input_view input {
    border-radius: 0px;
    border-top-width: 0px;
    border-left-width: 0px;
    border-right-width: 0px;
    border-bottom-width: 1px;
    /*outline: medium;*/
}
.el-textarea_view textarea {
    border-radius: 0px;
    border-top-width: 0px;
    border-left-width: 0px;
    border-right-width: 0px;
    border-bottom-width: 1px;
}
.el-span_view {
    width: 100%;
    padding: 0 15px;
    display: inline-block;
    border-bottom:1px solid #DCDFE6;
}
</style>
