<template>
  <div class="main-body">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <el-input v-model="filters.name" :placeholder="t('thead.name')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button icon="search" type="primary" :disabled="$hasBP('bnt.sysDept.list')  === false" @click="doSearch">{{ t('action.search') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="$hasBP('bnt.sysDept.list')  === false" @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="plus" type="primary" :disabled="$hasBP('bnt.sysDept.add')  === false" @click="handleAdd">{{ t('action.add') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--表格树内容栏-->
    <cm-table
      rowKey="id"
      ref="tableRef"
      :get-page="listTree"
      :filters="filters"
      :columns="columns"
      :showBatchDelete="false"
      :showPagination="false"
      :operations="operations"
      @handleEdit="handleEdit"
      @handleDelete="handleDelete"
    ></cm-table>
  </div>
  <el-dialog
    :title="isEdit ? t('action.edit') : t('action.add')"
    width="40%"
    draggable
    v-model="dialogVisible"
    :close-on-click-modal="false"
    @close="doClose"
  >
    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      @keyup.enter="handleSubmit"
      label-width="80px"
    >
      <el-form-item :label="t('thead.name')" prop="name">
        <el-input v-model="form.name" :placeholder="t('thead.name')"></el-input>
      </el-form-item>
      <el-form-item :label="t('form.parent')" prop="parentId">
        <el-cascader
          v-model="form.parentId"
          :props="{ label: 'name', value: 'id', checkStrictly: true, emitPath: false }"
          :options="deptData"
          clearable
          filterable
          class="w100p"
        ></el-cascader>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="doClose">{{ t('action.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ t('action.confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { listTree, getDeptById, save, update, remove } from '@/apis/app-dept'
import useTableHandlers from '@/apis/use-table-handlers'
const filters = reactive({
  name: ''
});
const form = reactive({
  id: '',
  name: '',
  parentId: null,
});
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
const deptData = ref([])

const columns = computed(() => [
/*  { prop: "id", label: t("thead.ID") },*/
  { prop: "name", label: t("thead.name") },
  { prop: "createTime", label: t("thead.createdTime"), minWidth: 160 },
  { prop: "updateTime", label: t("thead.updatedTime"), minWidth: 160 },
])

const operations = computed(() => [
  {
    type: 'edit',
    perm:'bnt.sysDept.update'
  },
  {
    type: 'delete',
    perm:'bnt.sysDept.remove'
  }
])

const rules = computed(() => {
  return {
    name: [
      { required: true, message: t('form.usernameHolder'), trigger: ['change', 'blur'] }
    ]
  }
})

// methods
function initFormRequest(row) {
  listTree().then(res => {
    deptData.value = res.data;
  })
}
function handleAdd() {
  doAdd();
}
function refreshForm(){
  filters.name = ''
  doSearch()
}
function handleEdit(row) {
  doEdit(getDeptById, row.id)
}
function handleDelete(ids, callback) {
  doRemove(remove, ids, callback)
}
function handleSubmit() {
  if(form.id && form.parentId && form.id == form.parentId){
    ElMessage({message: t('tips.sameDept'), type: "error"});
    return;
  }
  doSubmit({ save, update });
}

onMounted(() =>{
  initFormRequest();
});
</script>
