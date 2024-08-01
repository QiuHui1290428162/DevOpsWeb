<template>
  <el-sub-menu :index="item.path">
    <template #title>
      <i v-if="item.icon" :class="'iconfont '+item.icon">&nbsp;&nbsp;</i>
      <span> {{ t(`menu.${item.name}`) }}</span>
    </template>
    <template v-for="(child, ci) in item.children" :key="ci">
      <sidebar-menu-item v-if="child.children && child.children.length > 0" :item="child" :t="t" />
      <el-menu-item v-else :index="child.path">
        <i v-if="child.icon" :class="'iconfont '+child.icon">&nbsp;&nbsp;</i>
        {{ t(`menu.${child.name}`) }}
      </el-menu-item>
    </template>
  </el-sub-menu>
</template>

<script>

export default {
  props: {
    item: {
      type: Object,
      required: true,
    },
    t: {
      type: Function,
      required: true,
    },
  },
};
</script>
