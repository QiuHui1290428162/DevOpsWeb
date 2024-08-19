<template>
    <div class="main-body">
        <!--定时邮件发送参数页面-->
        <div class="toolbar">
            <el-form :inline="true" :model="filters">
              <!-- 搜索关键词输入框 -->
              <el-form-item>
                <el-input v-model="filters.taskName" :placeholder="t('thead.emailName')"></el-input>
              </el-form-item>
              <el-form-item>
                <el-input v-model="filters.recipientEmail" :placeholder="t('thead.recipientEmail')"></el-input>
              </el-form-item>
              <el-form-item>
                <el-input v-model="filters.ccEmail" :placeholder="t('thead.ccEmail')"></el-input>
              </el-form-item>
              <!-- 下拉框 -->
              <el-form-item>
                <el-select
                    v-model="filters.databaseName"
                    :placeholder="t('thead.databaseName')">
                  <el-option
                      :label="''"
                      :value="''"/>
                  <el-option
                      v-for="l in databaseList"
                      :key="l.code"
                      :label="l.name"
                      :value="l.name"/>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                    v-model="filters.status"
                    :placeholder="t('thead.status')">
                  <el-option
                      v-for="l in typeList"
                      :key="l.code"
                      :label="l.name"
                      :value="l.code"/>
                </el-select>
              </el-form-item>
              <br>
                <el-form-item>
                    <el-button icon="search" type="primary" v-if="$hasBP('bnt.taskScheduledEmail.list')" @click="doSearch">{{ t('action.search') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-if="$hasBP('bnt.taskScheduledEmail.list')"  @click="refreshForm" icon="refresh">{{ t('action.reset') }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button icon="plus" type="primary" v-if="$hasBP('bnt.taskScheduledEmail.add')"  @click="doAdd">{{ t('action.add') }}</el-button>
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
        <el-form
            ref="formRef"
            :model="form"
            label-width="auto"
            :rules="rules"
            label-position="top"
            style="font-weight: bold;">
            <!-- prop必须与form里的值一致-->
            <el-form-item :label="t('from.taskName')" prop="taskName" >
              <el-input v-model="form.taskName" clearable/>
            </el-form-item>
            <el-form-item :label="t('from.recipientEmail')" prop="recipientEmail">
              <el-input v-model="form.recipientEmail" clearable autosize type="textarea"/>
            </el-form-item>
            <el-form-item :label="t('form.ccEmail')" prop="ccEmail">
                    <el-input v-model="form.ccEmail" clearable autosize type="textarea"/>
            </el-form-item>
            <el-form-item :label="t('from.subject')" prop="subject">
                    <el-input v-model="form.subject" clearable/>
            </el-form-item>
            <el-form-item :label="t('form.scheduledTime')" prop="scheduledTime">
                <el-row :gutter="10" type="flex" align="middle">
                  <el-col :span="50">
                    <el-input v-model="form.scheduledTime" clearable/>
                  </el-col>
                  <el-col :span="4">
                    <el-button type="primary" @click="showCronHelp">{{ t('action.help') }}</el-button>
                  </el-col>
                </el-row>
            </el-form-item>
            <el-form-item :label="t('form.remarks')" prop="remarks">
                    <el-input v-model="form.remarks" clearable  autosize type="textarea"/>
            </el-form-item>
            <el-form-item :label="t('form.status')">
              <el-switch
                  v-model="form.status"
                  :active-value="1"
                  :inactive-value="0"
              ></el-switch>
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

    <!-- 编辑邮件正文 -->
    <el-dialog
        :title="t('action.editEmailBody')"
        width="40%"
        draggable
        v-model="emailBodyDialogVisible"
        :close-on-click-modal="false"
        @close="handleClose"
    >
      <el-form
          ref="formRef"
          :model="form"
          label-width="auto"
          :rules="rules"
          label-position="top"
          style="font-weight: bold;">
        <el-form-item :label="t('form.emailBodyType')" prop="bodyType">
          <el-select
              v-model="form.bodyType">
            <!--:key节点唯一标识,防止排序错乱; parseInt(l.code, 10)转换为int类型-->
            <el-option
                v-for="l in bodyList"
                :key="l.code"
                :label="l.name"
                :value="parseInt(l.code, 10)"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="t('form.emailBody')" prop="body">
                <el-input v-model="form.body" clearable autosize type="textarea"/>
        </el-form-item>
        <el-form-item :label="t('form.attachmentType')" prop="attachmentType">
          <el-select
              v-model="form.attachmentType">
            <el-option
                v-for="l in attachmentList"
                :key="l.code"
                :label="l.name"
                :value="parseInt(l.code, 10)"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="t('form.attachment')" prop="attachment">
                <el-input v-model="form.attachment" clearable  autosize type="textarea"/>
        </el-form-item>
        <el-form-item :label="t('form.databaseName')" prop="databaseName">
          <el-select
              v-model="form.databaseName">
            <el-option
                v-for="l in databaseList"
                :key="l.code"
                :label="l.name"
                :value="l.name"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleClose">{{ t("action.cancel") }}</el-button>
          <el-button
              type="primary"
              @click="handleSubmit"
              :loading="formLoading"
          >{{ t("action.submit") }}
          </el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 弹出框，用于显示Cron表达式帮助 -->
    <el-dialog title="Cron表达式规则" v-model="cronHelpVisible" width="40%">
      <p>字段解释：</p>
      <p>第1位: 秒 (秒 0-59 | * )。</p>
      <p>第2位: 分钟 (分 0-59 | * )，支持间隔指定，例如 0/20。</p>
      <p>第3位: 小时 (小时 0-23 | * )。</p>
      <p>第4位: 日期 (日期 1-31 | * | ?)，? 表示不指定，与星期二选一,若星期有数值则日期必须为 "?”</p>
      <p>第5位: 月份 (月份 1-12 | * )。</p>
      <p>第6位: 星期 (星期 1-7 | * | ? )，与日期二选一,若日期有数值则星期必须为 "?”</p>
      <br>
      <p>特殊字符解释：</p>
      <p>*：表示任意值。例如，* 在分钟字段表示每分钟。</p>
      <p>?：仅在日期和星期字段中使用，表示不指定值。</p>
      <p>/：表示步长。例如，0/20 在分钟字段表示每 20 分钟执行一次。</p>
      <br>
      <p>简单案例：</p>
      <p>0 * * * * ? : 每小时的第 0 分钟执行</p>
      <p>0 0 9 * * ? : 每天早上 9:00 执行</p>
      <p>0 0 9 1 * ? : 每月1号早上 9:00 执行</p>
      <p>0 0/20 9 * * ? : 每天早上 9:00从第0分开始, 每20分执行</p>
      <p>0 0 9 ? * 1: 每周一早上 9:00 执行</p>
    </el-dialog>

</template>

<script setup>
import api, {sendMail} from "@/apis/task/taskScheduledEmail";
    import useTableHandlers from '@/apis/use-table-handlers'
    import dicItemApi from "@/apis/dic-item";
    import {getById} from "@/apis/task/taskScheduledEmail";


    const filters = reactive({
      taskName: '',
      recipientEmail: '',
      ccEmail: '',
      databaseName: '',
      status:'1'
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
        scheduledTime: '0 0 0 0 0 0',
        remarks: '',
        status: ''
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
    // computed
    const columns = computed(() => [
        {type: 'selection'},
        {prop: "taskName", label: "任务名称", minWidth: 200, showOverflowTooltip: true},
        {prop: "recipientEmail", label: "收件人邮箱号", minWidth: 120, showOverflowTooltip: true},
        {prop: "ccEmail", label: "抄送人邮箱号", minWidth: 120, showOverflowTooltip: true},
        {prop: "subject", label: "邮件主题", minWidth: 120, showOverflowTooltip: true},
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
          label: t('action.editEmailBody'),
          onClick: handleEditEmailBody,
          perm: 'bnt.taskScheduledEmail.editEmailBody'
        },
        {
          label: t('action.sendEmail'),
          onClick: handleSendEmail,
          perm: 'bnt.taskScheduledEmail.sendEmail',
          isDisabled: (row) => row.status !== "正常"   // 该行 status 不为 正常 时禁用按钮
        },
        {
            type: 'delete',
            perm: 'bnt.taskScheduledEmail.remove'
        },

    ])

    const emailRegex1 = /([\w.%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/;
    const emailRegex2 = /^(([\w.%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,};[\r\n]?)+[\w.%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/;
    /*
     * 时间格式验证器
      秒、分钟 (秒 0-59 | *)，支持间隔指定，例如 0/20。
      小时 (小时 0-23 | *)。
      日期 (日期 1-31 | * | ? | L | W)，L 和 W 是 Cron 特有的表示法，可视需要添加。 与星期二选一,若星期有数值则日期必须为 "?”
      月份 (月份 1-12 | JAN-DEC | *)。
      星期 (星期 1-7 | MON-SUN | * | ? | L)，L 表示最后一天，? 表示不指定。与日期二选一,若日期有数值则星期必须为 "?”
      年份 (年份 1970-2099 | *)，可视需要调整年份范围。
     */
    const cronRegex = /^(\*\/?\d*|\d+(\/\d+)?|\?)\s+(\*\/?\d*|\d+(\/\d+)?|\?)\s+(\*\/?\d*|\d+(\/\d+)?|\?)\s+(\*\/?\d*|\d+(\/\d+)?|\?|L|LW|W|\d+W|\d+L)\s+(\*\/?\d*|\d+(\/\d+)?|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\s+(\*\/?\d*|\d+(\/\d+)?|\?|L|1L|2L|3L|4L|5L|6L|7L|SUN|MON|TUE|WED|THU|FRI|SAT)$/
    //const cronRegex = /^(\d{1,2}|\*)(\s+(\d{1,2}|\*)(\s+(\d{1,2}|\*)(\s+(\d{1,2}|\*|\?|\d{1,2}-\d{1,2})(\s+(\d{1,2}|\*|\?|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(\s+(\d{1,2}|\*|\?|MON|TUE|WED|THU|FRI|SAT|SUN)(\s+(\d{4}|\*|\d{4}-\d{4}))?)?)?)?)?)?$/

    // 验证规则
    const rules = computed(() => {
        return {
            taskName: [{required: true, message: '请输入任务名称', trigger: "blur"}],
            recipientEmail: [
              { required: true, message: '请输入收件人邮箱号', trigger: "blur" },
              // 自定义验证器，仅在非空时验证正则表达式
              {
                validator: (rule, value, callback) => {
                  // 邮箱正则表达式
                  if (!emailRegex1.test(value)) {
                    if ( emailRegex2.test(value) ) {
                      callback();
                    }
                    callback(new Error('输入邮箱格式有误，多个邮箱请用分号分隔, 邮箱前后不要有空格, 最后一个邮箱号后不要有分号'));
                  } else {
                    callback();  // 符合正则表达式
                  }
                },
                trigger: "blur"}],
            ccEmail: [
              {
                validator: (rule, value, callback) => {
                  if (value === '' || value === null) {
                    callback();  // 如果为空，直接通过验证
                  } else if (!emailRegex1.test(value)) {
                    if ( emailRegex2.test(value) ) {
                      callback();
                    }
                    callback(new Error('输入邮箱格式有误，多个邮箱请用分号分隔, 邮箱前后不要有空格, 最后一个邮箱号后不要有分号'));
                  } else {
                    callback();
                  }
                },
                trigger: "blur"}],
            subject: [{required: true, message: '请输入邮件主题', trigger: "blur"}],
            scheduledTime: [
              { required: true, message: '请输入定时发送时间', trigger: "blur" },
              { pattern: cronRegex, message: '时间格式不正确, 例如: 0 0 9 * * ?(每天早上9点)', trigger: "blur" }
            ],
            body: [{required: true, message: '请输入邮件正文', trigger: "blur"}],
            bodyType: [
              {
              validator: (rule, value, callback) => {
                // 转换 bodyList 中的 code 为整数，以确保类型匹配
                const validCodes = bodyList.value.map(item => parseInt(item.code, 10));
                if (validCodes.includes(value)) {
                  callback();  // 如果匹配，验证通过
                } else {
                  callback(new Error('必须选择有效的邮件类型'));  // 如果不匹配，验证失败
                }
              },
              trigger: 'blur' }],
            attachmentType: [
            {
              validator: (rule, value, callback) => {
                if(form.attachment) {
                  const validCodes = attachmentList.value.map(item => parseInt(item.code, 10));
                  if (validCodes.includes(value)) {
                    callback();  // 如果匹配，验证通过
                  } else {
                    callback(new Error('必须选择有效的附件类型'));  // 如果不匹配，验证失败
                  }
                }else {
                    callback();
                }
              },
              trigger: 'blur' }],
            databaseName: [{required: true, message: '请选择数据库', trigger: "blur"}],
        }
    });

    //帮助弹出窗
    const cronHelpVisible = ref(false);
    function showCronHelp() {
      cronHelpVisible.value = true;
    }

    function refreshForm(){
      filters.taskName = '';
      filters.recipientEmail = '';
      filters.ccEmail = '';
      filters.databaseName = '默认';
      filters.status = '1';
      doSearch();
    }

    function handleSearch(value) {
      filters.status = '-1';
      filters.recipientEmail = '';
      filters.ccEmail = '';
      filters.databaseName = '默认';
      filters.taskName = value;
      doSearch();

    }

    // methods
    function handleDelete(ids, callback) {
        doRemove(api.remove, ids, callback)
    }

    function handleEdit(row) {
        doEdit(api.getById, row.id)
    }

    const emailBodyDialogVisible = ref(false); // 对话框可见性
    //编辑邮件正文
    function handleEditEmailBody(row) {
      const id = row.id;

      if (!form) return;
      getById(id).then(response => {
        const row = response.data;
        if (row) {
          for (const k in form) {
            if (k in row) {
              form[k] = row[k];
            }
          }
          //开启编辑模式
          isEdit.value = true;
          //打开对话框
          emailBodyDialogVisible.value = true;
        }
      });
    }

const sendingMail = ref(false);
    //发送邮件
function handleSendEmail(row) {
  if (sendingMail.value) {
    return; // 如果正在发送，则直接返回，防止重复发送
  }
  ElMessageBox.confirm(
      t('tips.sendMailConfirm'),
      t('tips.warnTitle'), {
        confirmButtonText: t('action.confirm'),
        cancelButtonText: t('action.cancel'),
        type: "warning",
        draggable: true,
      }
  ).then(() => {
    ElMessage({
      message: t("tips.sendEmail"),
      type: "success",
      showClose: true,
    });

    sendingMail.value = true; // 设置发送状态为真, 防止重复发送
    //调用接口
    sendMail(row.id).then(response => {
      ElMessage({
        message: t("tips.sendEmailSuccess"),
        type: "success",
        showClose: true,
      });
    }).finally(() => {
      sendingMail.value = false; // 无论成功还是失败，都重置发送状态
    });
  }).catch(() => {
    // 如果用户取消操作，可以选择不执行任何操作或给出提示
  });
}


    //使用回调函数确保顺序
    function handleSubmit() {
      doSubmit({save: api.save, update: api.update}, () => {
        // 搜索新增的记录
        handleSearch(form.taskName);
        // 关闭对话框
        emailBodyDialogVisible.value = false;
        // 提示成功
        ElMessage({
          message: t("tips.success"),
          type: "success",
          showClose: true,
        });
      });
    }


    function handleClose() {
      emailBodyDialogVisible.value = false;
      doClose();
    }

    // 初始化下拉列表
    const typeList = ref([])
    const databaseList = ref([])
    const bodyList = ref([])
    const attachmentList = ref([])
    function loadTypeList(){
      dicItemApi.getAllDicItem('1').then(res =>{
        typeList.value = res.data
      })
      dicItemApi.getAllDicItem('2000').then(res =>{
        databaseList.value = res.data
      })
      dicItemApi.getAllDicItem('4000').then(res =>{
        bodyList.value = res.data
      })
      dicItemApi.getAllDicItem('5000').then(res =>{
        attachmentList.value = res.data
      })
    }
    onMounted(function (){
      loadTypeList()
    })
</script>

