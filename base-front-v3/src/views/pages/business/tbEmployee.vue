<template>
    <div class="main-body">
        <!--工具栏-->
        <div class="toolbar">
            <el-form :inline="true" :model="filters">
                                <el-form-item>
                    <el-input v-model="filters.realname" placeholder="姓名"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button icon="search" type="primary" :disabled="$hasBP('bnt.tbEmployee.list')  === false" @click="doSearch">{{ t('action.search') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" :disabled="$hasBP('bnt.tbEmployee.list')  === false"  @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button icon="plus" type="primary" :disabled="$hasBP('bnt.tbEmployee.add')  === false"  @click="doAdd">{{ t('action.add') }}</el-button>
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
            <el-form-item label="姓名" prop="realname">
                    <el-input v-model="form.realname" clearable/>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
                    <el-input-number :min="0" v-model="form.age" controls-position="right"  clearable/>
            </el-form-item>
            <el-form-item label="出生日期" prop="bornDate">
                    <el-date-picker
                            v-model="form.bornDate"
                            type="datetime"
                            value-format="YYYY-MM-DD HH:mm:ss"
                            placeholder="选择日期">
                    </el-date-picker>
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
    import api from "@/apis/business/tbEmployee";
    import useTableHandlers from '@/apis/use-table-handlers'

    const filters = reactive({
        realname: '',
    });
    const form = reactive({
        id: "",
        realname: '',
        age: '',
        bornDate: '',
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
        {prop: "realname", label: "姓名", minWidth: 120, showOverflowTooltip: true},
        {prop: "age", label: "年龄", minWidth: 120, showOverflowTooltip: true},
        {prop: "bornDate", label: "出生日期", minWidth: 120, showOverflowTooltip: true},
        {prop: "createTime", label: "创建时间", minWidth: 120, showOverflowTooltip: true},
        {prop: "updateTime", label: "修改时间", minWidth: 120, showOverflowTooltip: true},
    ]);
    const operations = computed(() => [
        {
            type: 'edit',
            perm: 'bnt.tbEmployee.update'
        },
        {
            type: 'delete',
            perm: 'bnt.tbEmployee.remove'
        }
    ])
    const rules = computed(() => {
        return {
            realname: [{required: true, message: '请输入姓名', trigger: "blur"}],
            age: [{required: true, message: '请输入年龄', trigger: "blur"}],
            bornDate: [{required: true, message: '请输入出生日期', trigger: "blur"}],
        }
    });

    function refreshForm(){
        filters.realname = ''
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
