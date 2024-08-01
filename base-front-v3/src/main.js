import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import { createI18n } from 'vue-i18n';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/aliicon/iconfont.css' // icon css
import zhCn from 'element-plus/es/locale/lang/zh-cn';
import { getLanguageInfo } from "@/apis/language";
import 'normalize.css/normalize.css';

import * as ElIcons from "@element-plus/icons-vue";
import CmTable from "@/components/CmTable.vue";
import hasBtnPermission from '@/utils/btn-permission';

const app = createApp(App);
for (const name in ElIcons) {
  app.component(name, ElIcons[name]);
}
app.component('CmTable', CmTable);
const initializeI18n = async () => {
  const { data } = await getLanguageInfo();
  const i18n = createI18n({
    legacy: false,
    locale: 'zh-cn',
    fallbackLocale: 'zh-cn',
    messages: {
      'zh-cn': JSON.parse(data.cn),
      'en': JSON.parse(data.en)
    }
  });
  app.use(i18n);
  app.use(ElementPlus, {
    locale: zhCn,
    // 支持 large、default、small
    size:'default'
  })
  app.use(router).use(store).mount("#app");
  app.config.globalProperties.$hasBP = hasBtnPermission;
};
initializeI18n();


