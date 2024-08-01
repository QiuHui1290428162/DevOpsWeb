<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" label-position="left">
    <el-form-item :label="t('thead.username')">
      <label>{{ form.username }}</label>
    </el-form-item>
    <el-form-item :label="t('thead.name')">
      <label>{{ form.name }}</label>
    </el-form-item>
    <el-form-item :label="t('thead.mobile')">
      <label>{{ form.mobile }}</label>
    </el-form-item>
    <el-form-item :label="t('thead.email')">
      <label>{{ form.email }}</label>
    </el-form-item>
    <el-form-item :label="t('thead.dept')">
      <label>{{ form.deptName }}</label>
    </el-form-item>

  </el-form>
</template>
<script setup>
const { t } = useI18n();
const store = useStore();
const form = reactive({
  name: '',
  username: '',
  mobile: '',
  email: '',
  deptName: ''
});
const profile = computed(() => store.state.user.userInfo)

watch(profile, () => {
  updateForm();
}, { immediate: true })

function updateForm() {
  form.name = profile.value.name;
  form.username = profile.value.username;
  form.mobile = profile.value.mobile;
  form.email = profile.value.email;
  form.deptName = profile.value.deptName;
}

</script>
