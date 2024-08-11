<template>
    <div class="main-body">
        <!--工具栏-->
        <div class="toolbar">
            <el-form :inline="true" :model="filters">
                <#list data as var>
               <#if  (var.dataType=='char' || var.dataType=='varchar' || var.dataType=='text' || var.dataType=='tinytext') && var.attrName!='id'  >
                <el-form-item>
                    <el-input v-model="filters.${var.attrName}" placeholder="${var.remarks}"></el-input>
                </el-form-item>
               </#if>
                </#list>
                <el-form-item>
                    <el-button icon="search" type="primary" v-if="$hasBP('bnt.${modelName2}.list') " @click="doSearch">{{ t('action.search') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button  icon="refresh" type="primary" v-if="$hasBP('bnt.${modelName2}.list')"  @click="refreshForm">{{ t('action.reset') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button icon="plus" type="primary" v-if="$hasBP('bnt.${modelName2}.add')"  @click="doAdd">{{ t('action.add') }}</el-button>
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
        <el-form ref="formRef" :model="form" label-width="auto" :rules="rules" style="font-weight: bold;">
          <#list data as var>
           <#if var.attrName!='isDeleted' && var.attrName!='parentId' && var.attrName!='createTime' && var.attrName!='updateTime' && var.attrName!='id' >
            <el-form-item label="${var.remarks}" prop="${var.attrName}">
                <#if  var.typeName =='Integer' || var.typeName =='java.math.BigDecimal' >
                    <el-input-number :min="0" v-model="form.${var.attrName}" controls-position="right"  clearable/>
                <#elseif var.typeName=='java.util.Date' || var.typeName=='Date'>
                    <el-date-picker
                            v-model="form.${var.attrName}"
                            type="datetime"
                            value-format="YYYY-MM-DD HH:mm:ss"
                            placeholder="选择日期">
                    </el-date-picker>
                <#else>
                    <el-input v-model="form.${var.attrName}" clearable/>
                </#if>
            </el-form-item>
            </#if>
          </#list>
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
    import api from "@/apis/${packageSub}/${modelName2}";
    import useTableHandlers from '@/apis/use-table-handlers'

    const filters = reactive({
        <#list data as var>
        <#if  (var.dataType=='char' || var.dataType=='varchar' || var.dataType=='text' || var.dataType=='tinytext') && var.attrName!='id'  >
        ${var.attrName}: '',
        </#if>
        </#list>
    });
    const form = reactive({
        id: "",
        <#list data as var>
        <#if var.attrName!='isDeleted' && var.attrName!='parentId' && var.attrName!='createTime' && var.attrName!='updateTime' && var.attrName!='id' >
        ${var.attrName}: '',
        </#if>
        </#list>
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
        <#list data as var>
        <#if var.attrName!='isDeleted' && var.attrName!='id'>
        {prop: "${var.attrName}", label: "${var.remarks}", minWidth: 120, showOverflowTooltip: true},
        </#if>
        </#list>
    ]);
    const operations = computed(() => [
        {
            type: 'edit',
            perm: 'bnt.${modelName2}.update'
        },
        {
            type: 'delete',
            perm: 'bnt.${modelName2}.remove'
        }
    ])
    const rules = computed(() => {
        return {
            <#list requireList as var>
            ${var.attrName}: [{required: true, message: '请输入${var.remarks}', trigger: "blur"}],
            </#list>
        }
    });

    function refreshForm(){
        <#list data as var>
        <#if  (var.dataType=='char' || var.dataType=='varchar' || var.dataType=='text' || var.dataType=='tinytext') && var.attrName!='id'  >
        filters.${var.attrName} = ''
        </#if>
        </#list>
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
