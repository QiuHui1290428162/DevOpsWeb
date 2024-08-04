<template>
  <!-- 使用 Element Plus 的配置提供器，动态设置语言 -->
  <el-config-provider :locale="elementLocales[locale]">
    <!-- 渲染当前匹配的路由组件 -->
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup>
import { elementLocales } from '@/i18n' // 导入自定义的 Element Plus 语言包配置
const { locale } = useI18n(); // 使用 useI18n 钩子获取 locale 变量
// 从 localStorage 获取存储的语言设置，如果没有则默认为 'zh-cn'
locale.value = localStorage.getItem('locale') || 'zh-cn';
</script>

<style>
/* 设置 html 和 body 的高度为 100% */
html,
body {
  height: 100%;
}
/* 设置 #app 元素的高度为 100%，并隐藏溢出的内容 */
#app {
  height: 100%;
  overflow: hidden;
}
/* flex-center 类，用于设置弹性布局，使子元素居中对齐 */
.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}
/* cursor 类，用于设置鼠标指针样式为指针 */
.cursor {
  cursor: pointer;
}
/* txt-c 类，用于设置文本居中对齐 */
.txt-c {
  text-align: center;
}
/* w100p 类，用于设置宽度为 100% */
.w100p {
  width: 100%;
}
</style>
