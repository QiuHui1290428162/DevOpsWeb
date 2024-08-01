<template>
  <div class="page-sidebar">
    <div class="collape-bar">
      <el-icon class="cursor" @click="isCollapse = !isCollapse">
        <expand v-if="isCollapse" />
        <fold v-else />
      </el-icon>
    </div>
    <el-menu :default-active="defaultActive" router class="sidemenu" :collapse="isCollapse">
      <template v-for="(item, i) in treeData" :key="i">
        <sidebar-menu-item :item="item" :t="t" />
      </template>
    </el-menu>
  </div>
</template>

<script  setup>
import SidebarMenuItem from "./SidebarMenuItem.vue";
const route = useRoute();
const store = useStore();
const { t } = useI18n();
const treeData = computed(() =>  store.state.menuTree);
const defaultActive = computed(() => route.path || treeData.value[0].path)
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
