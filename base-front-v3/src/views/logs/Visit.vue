<template>
  <div class="main-body">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <el-input v-model="filters.username" :placeholder="t('thead.username')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            icon="search"
            type="primary"
            @click="doSearch"
            :disabled="$hasBP('bnt.sysLoginLog.list')  === false"
          >{{ t('action.search') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="$hasBP('bnt.sysLoginLog.list')  === false" @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
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
import { listPage } from '@/apis/logs-visit'
import useTableHandlers from '@/apis/use-table-handlers'
const filters = reactive({
  username: ''
});
const {
  t,
  tableRef,
  doSearch,
} = useTableHandlers();
const columns = computed(() => [
  { prop: "username", label: t("thead.username"), minWidth: 100 },
  {
    prop: "status", label: t("thead.status"), minWidth: 120, formatter: (row) => {
      return row.status ? t('status.success') : t('status.fail')
    }
  },
  { prop: "ipaddr", label: t("thead.IP"), minWidth: 120 },
  { prop: "accessTime", label: t("thead.visitTime"), minWidth: 120 },
])
function refreshForm(){
  filters.username = ''
  doSearch()
}
</script>
