<script setup>
export default {
  name: 'Header',
  props: ['login', 'user']
}
</script>

<template>
  <div class="header">
    <h3>商品溯源平台</h3>
    <span class="user-name" v-if="this.user">{{this.user}}</span>
  </div>
</template>

<style scoped>
html,body,div,p,ul,li,img,
h3{
  padding: 0;
  margin: 0;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
body,
body>section{
  height: 100%;
}
.header{
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(90deg,#4b6cb7 0%,#182848 100%);
  color: #fff;
  width: 100%;
  padding: 1rem 2rem;
  box-sizing: border-box;
}
div.header h3{
  margin: 0;
  color: #fff;
  font-size: 1.5rem;
  font-weight: bold;
}
.header>.user-name{
  font-size: 1rem;
  background-color: rgba(255,255,255,0.2);
  padding: 0.5rem 1rem;
  border-radius: 15px;
  font-weight: normal;
}
</style>
