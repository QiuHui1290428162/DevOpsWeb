<template>
    <div class="main-body">
        <!--工具栏-->
        <div class="toolbar">
            <el-form :inline="true" :model="filters">
              <!-- 搜索关键词输入框 -->
              <el-form-item>
                <el-input v-model="filters.keyword" ></el-input>
              </el-form-item>
                <el-form-item>
                    <el-button icon="search" type="primary" :disabled="$hasBP('bnt.taskScheduledEmail.list')  === false" @click="doSearch">{{ t('action.search') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" :disabled="$hasBP('bnt.taskScheduledEmail.list')  === false"  @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button icon="plus" type="primary" :disabled="$hasBP('bnt.taskScheduledEmail.add')  === false"  @click="doAdd">{{ t('action.add') }}</el-button>
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
            <el-form-item label="任务名称" prop="taskName">
                    <el-input v-model="form.taskName" clearable/>
            </el-form-item>
            <el-form-item label="收件人邮箱号" prop="recipientEmail">
                    <el-input v-model="form.recipientEmail" clearable/>
            </el-form-item>
            <el-form-item label="抄送人邮箱号" prop="ccEmail">
                    <el-input v-model="form.ccEmail" clearable/>
            </el-form-item>
            <el-form-item label="邮件主题" prop="subject">
                    <el-input v-model="form.subject" clearable/>
            </el-form-item>
            <el-form-item label="邮件内容" prop="body">
                    <el-input v-model="form.body" clearable/>
            </el-form-item>
            <el-form-item label="邮件内容类型" prop="bodyType">
                    <el-input-number :min="0" v-model="form.bodyType" controls-position="right"  clearable/>
            </el-form-item>
            <el-form-item label="附件" prop="attachment">
                    <el-input v-model="form.attachment" clearable/>
            </el-form-item>
            <el-form-item label="附件类型" prop="attachmentType">
                    <el-input-number :min="0" v-model="form.attachmentType" controls-position="right"  clearable/>
            </el-form-item>
            <el-form-item label="数据库名称" prop="databaseName">
                    <el-input v-model="form.databaseName" clearable/>
            </el-form-item>
            <el-form-item label="定时发送时间" prop="scheduledTime">
                    <el-input v-model="form.scheduledTime" clearable/>
            </el-form-item>
            <el-form-item label="备注" prop="remarks">
                    <el-input v-model="form.remarks" clearable/>
            </el-form-item>
            <el-form-item label="任务状态" prop="status">
                    <el-input-number :min="0" v-model="form.status" controls-position="right"  clearable/>
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
    import api from "@/apis/task/taskScheduledEmail";
    import useTableHandlers from '@/apis/use-table-handlers'

    const filters = reactive({
      keyword: ''
    })
    const form = reactive({
        id: "",
        taskName: '',
        recipientEmail: '',
        ccEmail: '',
        subject: '',
        body: '',
        bodyType: '',
        attachment: '',
        attachmentType: '',
        databaseName: '',
        scheduledTime: '',
        remarks: '',
        status: '',
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
        {prop: "taskName", label: "任务名称", minWidth: 120, showOverflowTooltip: true},
        {prop: "recipientEmail", label: "收件人邮箱号", minWidth: 120, showOverflowTooltip: true},
        {prop: "ccEmail", label: "抄送人邮箱号", minWidth: 120, showOverflowTooltip: true},
        {prop: "subject", label: "邮件主题", minWidth: 120, showOverflowTooltip: true},
        {prop: "body", label: "邮件内容", minWidth: 120, showOverflowTooltip: true},
        {prop: "bodyType", label: "邮件内容类型", minWidth: 120, showOverflowTooltip: true},
        {prop: "attachment", label: "附件", minWidth: 120, showOverflowTooltip: true},
        {prop: "attachmentType", label: "附件类型", minWidth: 120, showOverflowTooltip: true},
        {prop: "databaseName", label: "数据库名称", minWidth: 120, showOverflowTooltip: true},
        {prop: "scheduledTime", label: "定时发送时间", minWidth: 120, showOverflowTooltip: true},
        {prop: "remarks", label: "备注", minWidth: 120, showOverflowTooltip: true},
        {prop: "status", label: "任务状态", minWidth: 120, showOverflowTooltip: true},
        {prop: "createTime", label: "创建时间", minWidth: 120, showOverflowTooltip: true},
        {prop: "updateTime", label: "修改时间", minWidth: 120, showOverflowTooltip: true},
    ]);
    const operations = computed(() => [
        {
            type: 'edit',
            perm: 'bnt.taskScheduledEmail.update'
        },
        {
            type: 'delete',
            perm: 'bnt.taskScheduledEmail.remove'
        }
    ])
    const rules = computed(() => {
        return {
            taskName: [{required: true, message: '请输入任务名称', trigger: "blur"}],
            recipientEmail: [{required: true, message: '请输入收件人邮箱号', trigger: "blur"}],
            subject: [{required: true, message: '请输入邮件主题', trigger: "blur"}],
        }
    });

    function refreshForm(){
        filters.keyword = ''
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
