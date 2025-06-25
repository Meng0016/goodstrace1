import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus' // 导入ElementPlus
import 'element-plus/dist/index.css' // 导入样式
import zhCn from 'element-plus/dist/locale/zh-cn.mjs' // 中文语言包

const app = createApp(App)
app.use(ElementPlus, { locale: zhCn })
app.mount('#app')
