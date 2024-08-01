<template>
  <div class="main-body">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <el-input v-model="filters.name" :placeholder="t('thead.name')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button icon="search" type="primary" :disabled="$hasBP('bnt.sysDic.list')  === false" @click="doSearch">{{ t('action.search') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="$hasBP('bnt.sysDic.list')  === false" @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="plus" type="primary" :disabled="$hasBP('bnt.sysDic.add')  === false" @click="doAdd">{{ t('action.add') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <cm-table
        ref="tableRef"
        :get-page="api.listPage"
        :filters="filters"
        :columns="columns"
        :operations="operations"
        @handleEdit="handleEdit"
        @handleDelete="handleDelete"
    ></cm-table>
  </div>
  <!-- 新增/编辑 -->
  <el-dialog
      :title="isEdit ? t('action.edit') : t('action.add')"
      width="40%"
      draggable
      v-model="dialogVisible"
      :close-on-click-modal="false"
      @close="doClose"
  >
    <el-form ref="formRef" :model="form" label-width="80px" :rules="rules">
      <el-form-item :label="t('thead.code')" prop="code">
        <el-input v-model="form.code"></el-input>
      </el-form-item>
      <el-form-item :label="t('thead.name')" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="doClose">{{ t("action.cancel") }}</el-button>
        <el-button
            type="primary"
            @click="handleSubmit"
            :loading="formLoading"
        >{{ t("action.submit") }}
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import api from "@/apis/dic";
import useTableHandlers from '@/apis/use-table-handlers'

const filters = reactive({
  name: ''
});
const form = reactive({
  id: "",
  name: "",
  code: "",
})
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
// computed
const columns = computed(() => [
  {type: 'selection'},
  /*{ prop: "id", label: t("thead.ID"), minWidth: 50 },*/
  {prop: "code", label: t("thead.code"), minWidth: 120, showOverflowTooltip: true},
  {prop: "name", label: t("thead.name"), minWidth: 120},
  {prop: "createTime", label: t("thead.createdTime"), minWidth: 160},
  {prop: "updateTime", label: t("thead.updatedTime"), minWidth: 160}
]);
const operations = computed(() => [
  {
    type: 'edit',
    perm: 'bnt.sysDic.update'
  },
  {
    type: 'delete',
    perm: 'bnt.sysDic.remove'
  }
])
const rules = computed(() => {
  return {
    name: [{required: true, message: t("form.nameRequired"), trigger: "blur"}],
    code: [{required: true, message: t("form.codeRequired"), trigger: "blur"}],
  }
});

function refreshForm(){
  filters.name = ''
  doSearch()
}

// methods
function handleDelete(ids, callback) {
  doRemove(api.remove, ids, callback)
}

function handleEdit(row) {
  doEdit(api.getById, row.id)
}

function handleSubmit() {
  doSubmit({save: api.save, update: api.update});
}

</script>
