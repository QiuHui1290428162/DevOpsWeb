<template>
  <!-- 主体区域 -->
  <div class="main-body">
    <!-- 工具栏区域，包含表单元素用于过滤和操作数据 -->
    <div class="toolbar">
      <!-- 表单用于输入搜索条件和展示操作按钮 -->
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <!-- 输入框绑定角色名称过滤器 -->
          <el-input v-model="filters.roleName" :placeholder="t('thead.roleName')"></el-input>
        </el-form-item>
        <el-form-item>
          <!-- 搜索按钮，权限控制是否可用 -->
          <el-button icon="search" type="primary" :disabled="$hasBP('bnt.sysRole.list') === false" @click="doSearch">{{ t('action.search') }}</el-button>
        </el-form-item>
        <el-form-item>
          <!-- 重置按钮，权限控制显示 -->
          <el-button type="primary" @click="refreshForm" v-if="$hasBP('bnt.sysRole.list')" icon="refresh">{{ t('action.reset') }}</el-button>
        </el-form-item>
        <el-form-item>
          <!-- 添加按钮，权限控制显示 -->
          <el-button icon="plus" type="primary" v-if="$hasBP('bnt.sysRole.add')" @click="doAdd">{{ t('action.add') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 表格内容区域 -->
    <cm-table
        ref="tableRef"
        :get-page="listPage"
        :filters="filters"
        :columns="columns"
        :operations="operations"
        @handleEdit="handleEdit"
        @handleDelete="handleDelete"
    ></cm-table>
  </div>
  <!-- 新增/编辑对话框 -->
  <el-dialog
      :title="isEdit ? t('action.edit') : t('action.add')"
      width="40%"
      draggable
      v-model="dialogVisible"
      :close-on-click-modal="false"
      @close="doClose"
  >
    <!-- 表单用于输入和提交数据 -->
    <el-form ref="formRef" :model="form" label-width="80px" :rules="rules">
      <el-form-item :label="t('thead.roleName')" prop="roleName">
        <el-input v-model="form.roleName"></el-input>
      </el-form-item>
      <el-form-item :label="t('thead.remark')" prop="description">
        <el-input v-model="form.description" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <!-- 对话框底部，包括取消和提交按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="doClose">{{ t("action.cancel") }}</el-button>
        <el-button
            type="primary"
            @click="handleSubmit"
            :loading="formLoading"
        >{{ t("action.submit") }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 绑定资源对话框 -->
  <el-dialog
      :title="t('action.bindResource')"
      width="40%"
      draggable
      v-model="bindDlgVisible"
      :close-on-click-modal="false"
  >
    <!-- 对话框内的按钮和树形结构用于资源选择 -->
    <div style="margin-top:-35px">
      <el-button @click="selectAll">{{ t("action.selectAll") }}</el-button>
      <el-button @click="deselectAll">{{ t('action.invertSelect') }}</el-button>
    </div>
    <el-tree
        ref="treeRef"
        :data="resourceTreeData"
        show-checkbox
        node-key="id"
        :check-strictly="true"
        :default-checked-keys="defaultCheckedKeys"
        :props="{label: 'val'}"
    />
    <!-- 对话框底部，包括取消和提交按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="bindDlgVisible = false">{{ t("action.cancel") }}</el-button>
        <el-button
            type="primary"
            @click="onBindResouceConfirm"
            :loading="bindLoading"
        >{{ t("action.submit") }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>


<script setup>
// 导入 API 函数和自定义钩子
import { listPage, save, update, remove, bindResouce, getById } from "@/apis/app-role";
import { listTree, getResourceByRoleId } from "@/apis/app-resource";
import useTableHandlers from '@/apis/use-table-handlers'

// 定义表单和筛选器的响应式数据对象
const filters = reactive({
  roleName: ''
});
const form = reactive({
  id: "",
  roleName: "",
  description: "",
});

// 使用自定义钩子来处理表单和表格操作
const {
  t,
  tableRef,
  dialogVisible,
  isEdit,
  formLoading,
  formRef,
  doSearch,
  doAdd,
  doEdit,
  doRemove,
  doSubmit,
  doClose
} = useTableHandlers(form);

// 资源树数据、绑定对话框可见性、默认勾选的键和绑定加载状态
const resourceTreeData = ref([]);
const bindDlgVisible = ref(false);
const defaultCheckedKeys = ref([]);
const bindLoading = ref(false);
const treeRef = ref();
const bindRoleId = ref();

// 定义表格列和操作按钮的配置
const columns = computed(() => [
  { type: 'selection' },
  { prop: "roleName", label: t("thead.roleName"), minWidth: 120 },
  { prop: "description", label: t("thead.remark"), minWidth: 120, showOverflowTooltip: true },
  { prop: "createTime", label: t("thead.createdTime"), minWidth: 160 },
  { prop: "updateTime", label: t("thead.updatedTime"), minWidth: 160 }
]);
const operations = computed(() => [
  { type: 'edit', perm: 'bnt.sysRole.update' },
  { label: t('action.bindResource'), onClick: handleBindResource, perm: 'bnt.sysRole.assignAuth' },
  { type: 'delete', perm: 'bnt.sysRole.remove' }
]);
const rules = computed(() => {
  return {
    roleName: [{ required: true, message: t('form.nameRequired'), trigger: "blur" }],
  }
});

// 全选功能
function selectAll() {
  const checkedKeys = [];
  traverseTreeData(resourceTreeData.value, node => {
    checkedKeys.push(node.id);
  });
  defaultCheckedKeys.value = checkedKeys;
}

// 反选功能
function deselectAll(){
  defaultCheckedKeys.value = [];
  treeRef.value.setCheckedKeys(defaultCheckedKeys.value);
};

// 递归遍历树数据辅助函数
function traverseTreeData(data, callback) {
  for (const node of data) {
    callback(node);
    if (node.children && node.children.length > 0) {
      traverseTreeData(node.children, callback);
    }
  }
}

// 删除、编辑、提交、刷新、绑定资源处理函数
function handleDelete(ids, callback) {
  doRemove(remove, ids, callback);
}

function handleEdit(row) {
  doEdit(getById, row.id);
}

function handleSubmit() {
  doSubmit({ save, update });
}

function refreshForm() {
  filters.roleName = '';
  doSearch();
}

function handleBindResource(row) {
  bindDlgVisible.value = true;
  bindRoleId.value = row.id;
  getResourceByRoleId(row.id).then(res => {
    defaultCheckedKeys.value = res.data;
    nextTick(() => {
      treeRef.value.setCheckedKeys(defaultCheckedKeys.value);
    });
  });
}

// 获取资源树数据
function getResourceTree() {
  listTree().then(res => {
    resourceTreeData.value = res.data;
  });
}

// 确认绑定资源
function onBindResouceConfirm() {
  bindLoading.value = true;
  bindResouce({ roleId: bindRoleId.value, menuIdList: treeRef.value.getCheckedKeys() }).then(() => {
    ElMessage({ message: t('tips.success'), type: 'success', showClose: true });
    bindDlgVisible.value = false;
  }).finally(() => {
    bindLoading.value = false;
  });
}

// 组件挂载时加载资源树
onMounted(() => {
  getResourceTree();
});
</script>

