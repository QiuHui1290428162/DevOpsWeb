<template>
  <div class="main-body">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <el-input v-model="filters.name" :placeholder="t('thead.name')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select
              v-model="filters.type"
              :placeholder="t('thead.sort')">
            <el-option
                v-for="l in typeList"
                :key="l.code"
                :label="l.name"
                :value="l.code"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="search" type="primary" :disabled="$hasBP('bnt.sysI18n.list')  === false" @click="doSearch">{{ t('action.search') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="$hasBP('bnt.sysI18n.list')  === false" @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="plus" type="primary" :disabled="$hasBP('bnt.sysI18n.add')  === false" @click="doAdd">{{ t('action.add') }}</el-button>
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
      <el-form-item :label="t('thead.name')" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item :label="t('thead.sort')" prop="type">
        <el-select
            v-model="form.type"
            :placeholder="t('thead.sort')">
          <el-option
              v-for="l in typeList"
              :key="l.code"
              :label="l.name"
              :value="l.code"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="t('thead.val')" prop="val">
        <el-input v-model="form.val"></el-input>
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
import api from "@/apis/i18n";
import dicItemApi from "@/apis/dic-item";
import useTableHandlers from '@/apis/use-table-handlers'

const filters = reactive({
  name: '',
  type:''
});
const form = reactive({
  id: "",
  name: "",
  type:"",
  val: "",
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
  {prop: "name", label: t("thead.name"), minWidth: 120},
  {prop: "val", label: t("thead.val"), minWidth: 120, showOverflowTooltip: true},
  {prop: "createTime", label: t("thead.createdTime"), minWidth: 160},
  {prop: "updateTime", label: t("thead.updatedTime"), minWidth: 160}
]);
const operations = computed(() => [
  {
    type: 'edit',
    perm: 'bnt.sysI18n.update'
  },
  {
    type: 'delete',
    perm: 'bnt.sysI18n.remove'
  }
])
const rules = computed(() => {
  return {
    name: [{required: true, message: t("form.nameRequired"), trigger: "blur"}],
    val: [{required: true, message: t("form.valRequired"), trigger: "blur"}],
  }
});
const typeList = ref([])
function loadTypeList(){
  dicItemApi.getAllDicItem('3000').then(res =>{
    typeList.value = res.data
  })
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

function refreshForm(){
  filters.name = ''
  filters.type = ''
  doSearch()
}

onMounted(function (){
  loadTypeList()
})

</script>
