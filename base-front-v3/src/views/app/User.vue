<template>
  <div class="main-body">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="filters">
        <!-- 搜索关键词输入框 -->
        <el-form-item>
          <el-input v-model="filters.keyword" :placeholder="t('thead.username')"></el-input>
        </el-form-item>
        <!-- 搜索按钮 -->
        <el-form-item>
          <el-button icon="search" type="primary" :disabled="$hasBP('bnt.sysUser.list')  === false" @click="doSearch">{{ t('action.search') }}</el-button>
        </el-form-item>
        <!-- 重置按钮 -->
        <el-form-item>
        <el-button type="primary" @click="refreshForm" v-if="$hasBP('bnt.sysUser.list')" icon="refresh">{{ t('action.reset') }}</el-button>
        </el-form-item>
        <!-- 添加按钮 -->
        <el-form-item>
          <el-button icon="plus" type="primary" v-if="$hasBP('bnt.sysUser.add')" @click="handleAdd">{{ t('action.add') }}</el-button>
        </el-form-item>

      </el-form>
    </div>
    <!--表格内容栏-->
    <cm-table
        ref="tableRef"
        :get-page="listPage"
        :filters="filters"
        :columns="columns"
        :operations="operations"
        @handleEdit="handleEdit"
        @handleDelete="handleDelete"
    />
  </div>
  <!--新增编辑界面-->
  <el-dialog
      :title="isEdit ? t('action.edit') : t('action.add')"
      v-model="dialogVisible"
      draggable
      width="50%"
      :close-on-click-modal="false"
      @close="doClose"
  >
    <el-form ref="formRef" :model="form" label-width="80px" :rules="rules" label-position="right">
      <el-form-item :label="t('form.username')" prop="username">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item :label="t('form.name')" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item :label="t('form.dept')" prop="deptId">
        <el-cascader
            v-model="form.deptId"
            :props="{ label: 'name', value: 'id', checkStrictly: true, emitPath: false }"
            :options="deptData"
            clearable
            filterable
            class="w100p"
        ></el-cascader>
      </el-form-item>
      <el-form-item :label="t('form.email')" prop="email">
        <el-input v-model="form.email"></el-input>
      </el-form-item>
      <el-form-item :label="t('form.mobile')" prop="mobile">
        <el-input v-model="form.mobile"></el-input>
      </el-form-item>
      <el-form-item :label="t('form.role')" prop="roleList">
        <el-select
            v-model="form.roleList"
            multiple
            :placeholder="t('form.choose')"
            style="width: 100%"
        >
          <el-option v-for="item in roles" :key="item.id" :label="item.roleName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="t('status.on')">
        <el-switch v-model="form.status"></el-switch>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="doClose">
        {{
          t("action.cancel")
        }}
      </el-button>
      <el-button
          type="primary"
          @click="handleSubmit"
          :loading="formLoading"
      >{{ t("action.submit") }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {listPage, save, update, remove, getById, resetPassword} from "@/apis/app-user";
import {listSimple} from "@/apis/app-role";
import {listTree} from "@/apis/app-dept";
import useTableHandlers from '@/apis/use-table-handlers'

const filters = reactive({
  keyword: ''
})
const form = reactive({
  id: '',
  username: '',
  name: "",
  deptId: '',
  email: "",
  mobile: "",
  roleList: [],
  status: true,
});
const {
  t, // 国际化函数
  tableRef, // 表格引用
  dialogVisible, // 控制对话框可见性
  isEdit, // 是否为编辑模式
  formLoading, // 表单加载状态
  formRef, // 表单引用
  doSearch, // 搜索函数
  doAdd, // 添加函数
  doEdit, // 编辑函数
  doSubmit, // 提交函数
  doRemove, // 删除函数
  doResetPassword, // 重置密码
  doClose // 关闭对话框函数
} = useTableHandlers(form);

// 定义部门数据和角色数据
const deptData = ref([])
const roles = ref([]);

// 计算表格列配置
const columns = computed(() => [
  {type: "selection"},
  /* { prop: "id", label: t("thead.ID"), minWidth: 50 },*/
  {prop: "username", label: t("thead.username"), minWidth: 120, showOverflowTooltip: true},
  {prop: "name", label: t("thead.name"), minWidth: 120, showOverflowTooltip: true},
  {prop: "mobile", label: t("thead.mobile"), minWidth: 100, showOverflowTooltip: true},
  {prop: "email", label: t("thead.email"), minWidth: 100, showOverflowTooltip: true},
  {prop: "deptName", label: t("thead.dept"), minWidth: 120, showOverflowTooltip: true},
  /*  { prop: "roleNames", label: t("thead.role"), minWidth: 100, showOverflowTooltip: true },*/
  /*  { prop: "description", label: t("thead.description"), minWidth: 120, showOverflowTooltip: true },*/
  {
    prop: "status", label: t("thead.status"), minWidth: 70, formatter: (row) => {
      return row.status ? t('status.on') : t('status.off')
    }
  },
])
// 联系方式验证器
const contactValidator = (rule, value, callback) => {
  // 如果邮箱和手机号都为空，则触发错误回调，并显示错误信息
  // if (!form.email && !form.mobile) {
  //   callback(new Error(t('form.emailOrMobile')))
  // }
  // 如果邮箱或手机号其中一个为空，则清除另一个字段的验证
  //(!form.mobile || !form.email) && formRef.value.clearValidate(rule.field === 'email' ? 'mobile' : 'email')
  callback()
}

// 计算操作列配置
const operations = computed(() => [
  {
    type: 'edit',
    perm:'bnt.sysUser.update'
  },
  {
    type: 'delete',
    perm:'bnt.sysUser.remove'
  },
  {
    label: t('action.resetPassword'),
    onClick: handleResetPassword,
    perm:'bnt.sysUser.resetPassword'
  },
])
// 计算表单验证规则
const rules = computed(() => {
  return {
    username: [
      {required: true, message: t('form.usernameHolder'), trigger: ['change', 'blur']},
      {pattern: /^.{5,}$/, message: t('form.usernameError'), trigger: ['change', 'blur']}
    ],
    deptId: [
      {required: true, message: t('form.deptRequired'), trigger: ['change', 'blur']}
    ],
    email: [
      {type: 'email', message: t('form.emailError'), trigger: ['change', 'blur']},
      {
        validator: contactValidator, message: t('form.emailOrMobile'), trigger: ['change', 'blur']
      }
    ],
    mobile: [
      {pattern: /^1[3-9]\d{9}$/, message: t('form.mobileError'), trigger: ['change', 'blur']},
      {
        validator: contactValidator, message: t('form.emailOrMobile'), trigger: ['change', 'blur']
      }
    ],
    roleList: [
      {required: true, message: t('form.roleIdsRequired'), trigger: ['change', 'blur']},
    ]
  }
});

// 初始化部门数据和角色数据
function initFormRequest() {
  findDeptTree();
  findRoles();
}

// 刷新表单
function refreshForm(){
  filters.keyword = ''
  doSearch()
}

// 添加操作
function handleAdd(row) {
  doAdd(row);
}

// 编辑操作
function handleEdit(row) {
  initFormRequest();
  doEdit(getById, row.id);
  // form.roleList = row.roleIds.split(',')
}

// 获取部门树数据
function findDeptTree() {
  listTree().then(res => {
    deptData.value = res.data;
  })
}

// 获取角色数据
function findRoles() {
  listSimple().then(res => {
    roles.value = res.data;
  })
}

// 删除操作
function handleDelete(ids, callback) {
  doRemove(remove, ids, callback)
}

// 重置密码
function handleResetPassword(row) {
  doResetPassword(resetPassword, row.id);

}

// 提交表单
function handleSubmit() {
  doSubmit({save, update, getParams}, (res) => {
    ElMessage({message: t('tips.success'), type: "success"});
  });
}

// 获取表单参数
function getParams() {
  const params = {...form}
  if (!isEdit.value) {
    delete params.id;
  }
  return params
}

// 组件挂载时初始化
onMounted(() =>{
  initFormRequest();
});

</script>
