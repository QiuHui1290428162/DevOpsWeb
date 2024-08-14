<template>
    <div class="main-body">
        <!--工具栏-->
        <div class="toolbar">
            <el-form :inline="true" :model="filters">
                                <el-form-item>
                    <el-button icon="search" type="primary" v-if="$hasBP('bnt.sysLog.list') " @click="doSearch">{{ t('action.search') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button  icon="refresh" type="primary" v-if="$hasBP('bnt.sysLog.list')"  @click="refreshForm">{{ t('action.reset') }}</el-button>
                </el-form-item>
            </el-form>
        </div>
        <!--表格内容栏-->
        <cm-table
                ref="tableRef"
                :get-page="api.listPage"
                :filters="filters"
                :columns="columns"
                :showOperation="false"
                :showBatchDelete="false"
        ></cm-table>
    </div>

</template>

<script setup>
    import api from "@/apis/logs-system";
    import useTableHandlers from '@/apis/use-table-handlers'

    const filters = reactive({
    });
    const form = reactive({
        id: "",
        module: '',
        functionName: '',
        class: '',
        operationDescription: '',
        result: '',
        userName: '',
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
        {prop: "module", label: "模块名称", minWidth: 120, showOverflowTooltip: true},
        {prop: "functionName", label: "功能名称", minWidth: 120, showOverflowTooltip: true},
        {prop: "class", label: "类名称", minWidth: 120, showOverflowTooltip: true},
        {prop: "operationDescription", label: "操作描述", minWidth: 120, showOverflowTooltip: true},
        {prop: "result", label: "操作结果", minWidth: 120, showOverflowTooltip: true},
        {prop: "userName", label: "用户名", minWidth: 120, showOverflowTooltip: true},
        {prop: "createTime", label: "创建时间", minWidth: 120, showOverflowTooltip: true},
    ]);

    const rules = computed(() => {
        return {
        }
    });

    function refreshForm(){
        doSearch()
    }


</script>
