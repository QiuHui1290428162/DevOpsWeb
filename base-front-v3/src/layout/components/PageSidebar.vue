<template>
  <!-- 侧边导航栏的容器 -->
  <div class="page-sidebar">
    <!-- 折叠控制条 -->
    <div class="collape-bar">
      <!-- 折叠控制图标，点击切换折叠状态 -->
      <el-icon class="cursor" @click="isCollapse = !isCollapse">
        <!-- 根据折叠状态显示不同图标，折叠时显示展开图标，否则显示折叠图标 -->
        <expand v-if="isCollapse" />
        <fold v-else />
      </el-icon>
    </div>
    <!-- 侧边菜单，绑定默认激活的菜单项和折叠状态 -->
    <el-menu :default-active="defaultActive" router class="sidemenu" :collapse="isCollapse">
      <!-- 循环遍历传入的菜单数据 -->
      <template v-for="(item, i) in treeData" :key="i">
        <!-- 为每一个菜单项渲染一个 SidebarMenuItem 组件 -->
        <sidebar-menu-item :item="item" :t="t" />
      </template>
    </el-menu>
  </div>
</template>

<script  setup>
//导入 SidebarMenuItem 组件
import SidebarMenuItem from "./SidebarMenuItem.vue";
//使用 Vue Router 的 useRoute 钩子获取当前路由信息
const route = useRoute();
//使用 Vuex 的 useStore 钩子访问 Vuex 状态
const store = useStore();
//使用 Vue I18n 的 useI18n 钩子获取翻译函数 t
const { t } = useI18n();
//从 Vuex 状态获取菜单树数据
const treeData = computed(() =>  store.state.menuTree);
//计算默认激活的菜单项，基于当前路由路径或者菜单树的第一个元素的路径
const defaultActive = computed(() => route.path || treeData.value[0].path)
//折叠状态的响应式引用，初始为不折叠
const isCollapse = ref(false)
</script>

<style lang="scss">
$side-width: 200px;
.page-sidebar {
  .sidemenu.el-menu,
  .sidemenu .el-sub-menu > .el-menu {
    --el-menu-text-color: #ccc;
    --el-menu-hover-bg-color: #060251;
    --el-menu-border-color: transparent;
    --el-menu-bg-color: #000;
    .el-menu-item {
      &.is-active {
        background-color: var(--el-menu-hover-bg-color);
      }
    }
  }
  .sidemenu.el-menu:not(.el-menu--collapse) {
    width: $side-width;
  }
  .collape-bar {
    color: #fff;
    font-size: 16px;
    line-height: 36px;
    text-align: right;

    .c-icon {
      cursor: pointer;
    }
  }
}
</style>
