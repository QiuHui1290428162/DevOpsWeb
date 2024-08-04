// 从 'vue' 包中导入 createApp 方法
import { createApp } from "vue";
// 导入根组件 App.vue
import App from "./App.vue";
// 导入路由配置
import router from "./router";
// 导入 Vuex 状态管理
import store from "./store";
// 从 'vue-i18n' 包中导入 createI18n 方法，用于国际化配置
import { createI18n } from 'vue-i18n';
// 导入 Element Plus 组件库
import ElementPlus from 'element-plus';
// 导入 Element Plus 的样式文件
import 'element-plus/dist/index.css';
// 导入项目的图标 CSS 文件
import '@/assets/aliicon/iconfont.css';
// 导入 Element Plus 的中文语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn';
// 导入获取语言信息的 API 函数
import { getLanguageInfo } from "@/apis/language";
// 导入标准化 CSS 文件
import 'normalize.css/normalize.css';

// 导入 Element Plus 的图标组件
import * as ElIcons from "@element-plus/icons-vue";
// 导入自定义组件 CmTable.vue
import CmTable from "@/components/CmTable.vue";
// 导入按钮权限检查工具函数
import hasBtnPermission from '@/utils/btn-permission';

// 创建 Vue 应用实例
const app = createApp(App);

// 全局注册 Element Plus 的图标组件
for (const name in ElIcons) {
  app.component(name, ElIcons[name]);
}
// 注册自定义组件 CmTable
app.component('CmTable', CmTable);

// 异步初始化 i18n 国际化配置
const initializeI18n = async () => {
  // 异步获取语言信息
  const { data } = await getLanguageInfo();
  // 创建 i18n 实例
  const i18n = createI18n({
    legacy: false,
    locale: 'zh-cn', // 设置默认语言为中文
    fallbackLocale: 'zh-cn', // 设置回退语言为中文
    messages: {
      'zh-cn': JSON.parse(data.cn), // 解析并设置中文语言包
      'en': JSON.parse(data.en) // 解析并设置英文语言包
    }
  });
  // 在应用中使用 i18n
  app.use(i18n);
  // 使用 Element Plus 并设置其语言为中文
  app.use(ElementPlus, {
    locale: zhCn,
    // 设置组件的默认大小，可选值有 large、default、small
    size: 'default'
  });
  // 使用路由和状态管理，并挂载应用到 id 为 "app" 的元素上
  app.use(router).use(store).mount("#app");
  // 将按钮权限检查工具函数添加到全局属性
  app.config.globalProperties.$hasBP = hasBtnPermission;
};

// 调用异步初始化函数
initializeI18n();
