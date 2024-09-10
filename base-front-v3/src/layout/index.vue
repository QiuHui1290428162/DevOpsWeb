<template>
  <!-- 页面容器，包含了整个页面的结构 -->
  <div class="page-container">
    <!-- 头部区域，使用 PageHeader 组件 -->
    <header>
      <page-header />
    </header>
    <!-- 主内容区域 -->
    <main>
      <!-- 左侧导航栏，条件渲染控制是否显示 -->
      <div v-if="showLeft" class="left">
        <page-sidebar @menu-click="onMenuClick"></page-sidebar>
      </div>
      <!-- 右侧内容区域，用于展示由路由控制的视图 -->
      <div class="right">
        <!-- 通过 iframe 加载外部 HTTP 页面 -->
        <iframe v-if="iframeSrc" :src="iframeSrc" frameborder="0" width="100%" height="100%"></iframe>
        <!-- 其他视图，当 iframe 不存在时展示 -->
        <router-view v-else></router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
//导入 PageHeader 和 PageSidebar 组件
import PageHeader from './components/PageHeader.vue'
import PageSidebar from './components/PageSidebar.vue'
//使用 Vue Router 的 useRoute 钩子获取当前路由信息
const route = useRoute();
//判断是否显示url网页
const iframeSrc = ref(null);
//使用 computed 创建一个响应式的 showLeft 属性，决定是否显示左侧导航
const showLeft = computed(() => {
  const routeName = route.name;
  //根据当前路由名称判断是否显示左侧导航
  //如果路由名称是 'Login' 或 'NotFound'，或者以 'Personal' 开头，则不显示左侧导航
  return !['Login', 'NotFound'].includes(routeName) && !/^Personal/.test(routeName);
});

// 菜单点击事件
const onMenuClick = (url) => {
  if (url) {
    // 若子菜单的组件路径是外部链接，则设置 iframeSrc
    iframeSrc.value = url;
  } else {
    // 如果没有外部链接，清空 iframeSrc 以显示 router-view
    iframeSrc.value = null;
  }
}
</script>

<style lang="scss">
.page-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
  > header {
    height: 54px;
    background: #000;
    color: #fff;
  }

  > main {
    display: flex;
    flex: 1;
    overflow: auto;

    > .left {
      height: 100%;
      background-color: #000;
      color: #fff;
    }
    > .right {
      flex: 1;
      overflow: hidden;
      background-color: #f5f7f9;
      > .main-body {
        padding: 16px 16px 30px;
        overflow: auto;
        height: 100%;
        box-sizing: border-box;
      }
    }
  }
}
</style>