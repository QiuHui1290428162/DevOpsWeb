<template>
  <div class="main-body">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <el-input v-model="filters.title" :placeholder='t("thead.title")'></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            icon="search"
            type="primary"
            @click="doSearch"
            :disabled="$hasBP('bnt.sysOperLog.list')  === false"
          >{{ t('action.search') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="$hasBP('bnt.sysOperLog.list')  === false" @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <cm-table
      ref="tableRef"
      :get-page="listPage"
      :filters="filters"
      :columns="columns"
      :showOperation="false"
      :showBatchDelete="false"
    ></cm-table>
  </div>
</template>

<script setup>
import { listPage } from '@/apis/logs-operation'
import useTableHandlers from '@/apis/use-table-handlers'
const filters = reactive({
  title: ''
});
const {
  t,
  tableRef,
  doSearch,
} = useTableHandlers();
const columns = computed(() => [
  { prop: "title", label: t("thead.title"), minWidth: 100 },
  { prop: "method", label: t("thead.method"), minWidth: 100 },
  { prop: "requestMethod", label: t("thead.requestMethod"), minWidth: 100 },
  { prop: "operUrl", label: t("thead.operUrl"), minWidth: 100 },
  { prop: "operName", label: t("thead.operator"), minWidth: 100 },
  { prop: "operIp", label: t("thead.IP"), minWidth: 120 },
  { prop: "operTime", label: t("thead.operateTime"), minWidth: 120 }
])
function refreshForm(){
  filters.title = ''
  doSearch()
}
</script>
