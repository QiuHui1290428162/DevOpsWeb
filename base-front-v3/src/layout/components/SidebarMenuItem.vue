<template>
  <!-- 使用 el-sub-menu 组件来创建一个嵌套的子菜单，:index 属性绑定到 item 的路径 -->
  <el-sub-menu :index="item.path">
    <!-- 定义子菜单的标题区域，可以包含图标和文字 -->
    <template #title>
      <!-- 如果菜单项有图标，显示图标 -->
      <i v-if="item.icon" :class="'iconfont '+item.icon">&nbsp;&nbsp;</i>
      <!-- 显示菜单项的名称，名称通过国际化函数 t 转换得到 -->
      <span> {{ t(`menu.${item.name}`) }}</span>
    </template>
    <!-- 遍历子菜单项 -->
    <template v-for="(child, ci) in item.children" :key="ci">
      <!-- 如果子项还有更多子菜单，递归使用 SidebarMenuItem 组件渲染 -->
      <sidebar-menu-item v-if="child.children && child.children.length > 0" :item="child" :t="t"/>
      <!-- 如果没有更多子菜单，使用 el-menu-item 组件渲染普通菜单项 -->
      <el-menu-item v-else :index="child.path" @click="onMenuClick(child)">
        <!-- 如果菜单项有图标，显示图标 -->
        <i v-if="child.icon" :class="'iconfont '+child.icon">&nbsp;&nbsp;</i>
        <!-- 显示菜单项的名称 -->
        {{ t(`menu.${child.name}`) }}
      </el-menu-item>
    </template>
  </el-sub-menu>
</template>

<script>
export default {
  // 定义组件的 props
  props: {
    item: {
      type: Object,  // item 是一个对象，包含菜单项的详细信息
      required: true,
    },
    t: {
      type: Function,  // t 是一个函数，用于国际化文本的翻译
      required: true,
    },
  },
  methods: {
    // 检查URL是否为HTTP(S)网址
    isHttpUrl(url) {
      return /^(http|https):\/\//.test(url);
    },
    // 菜单项点击事件
    onMenuClick(item) {
      // 判断 菜单路径 是否存在并且是一个 http/https 网址
      if (item.component && this.isHttpUrl(item.component)) {
        // 触发事件，将符合条件的 item.component 传递给父组件
        this.$emit('menu-click', item.component);
      } else {
        // 否则，传递 null，表示使用本地组件
        this.$emit('menu-click', null);
      }
    }
  }
};
</script>
