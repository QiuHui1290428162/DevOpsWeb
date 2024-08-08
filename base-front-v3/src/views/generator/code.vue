<template>
  <el-form ref="form2" label-width="150px" size="small" :model="form" style="margin-left:5px;margin-right:500px">
    <el-form-item label="选择生成文件">
      <el-checkbox-group v-model="form.genList">
        <el-checkbox label="api" checked>前端api</el-checkbox>
        <el-checkbox label="vue" checked>前端页面</el-checkbox>
        <el-checkbox label="po" checked>后端Po实体类</el-checkbox>
        <el-checkbox label="vo" checked>后端Vo实体类</el-checkbox>
        <el-checkbox label="mapper" checked>后端Mapper生成</el-checkbox>
        <el-checkbox label="xml" checked>后端Mapper XML生成</el-checkbox>
        <el-checkbox label="service" checked>后端Service生成</el-checkbox>
        <el-checkbox label="impl" checked>后端Service实现生成</el-checkbox>
        <el-checkbox label="controller" checked>后端Controller生成</el-checkbox>
        <el-checkbox label="main" :checked="false">启动类</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <!--
    <el-form-item label="激活码">
      <el-input v-model="form.authCode" placeholder=""></el-input>
    </el-form-item>
    -->
    <el-form-item label="数据库表名">
      <el-input v-model="form.tabName"></el-input>
    </el-form-item>
    <el-form-item label="生成代码包路径">
      <el-input v-model="form.packageName"></el-input>
    </el-form-item>
    <el-form-item label="生成代码后端目录">
      <el-input v-model="form.filePath"></el-input>
    </el-form-item>
    <el-form-item label="生成代码前端目录">
      <el-input v-model="form.frontPath"></el-input>
    </el-form-item>
    <el-form-item label="创建人">
      <el-input v-model="form.author"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit" v-loading.fullscreen.lock="loading">生成代码</el-button>
    </el-form-item>
  </el-form>
</template>
<script setup>
import api from "@/apis/code"
const loading = ref(false)
const form = reactive({
  genList:[],
  //authCode:'',
  tabName:'',
  packageName:'com.lanf',
  filePath:'D:\\code\\code\\src\\main',
  frontPath:'D:\\code\\code\\base-front-v3\\src',
  author:''
})
function onSubmit(){
  let tabName = form.tabName;
  if (tabName == '') {
    ElMessage({message: '请填写表名', type: "warnning"});
    return;
  }
  loading.value = true
  api.save(form).then(response => {
    loading.value = false
    if (response.code == '200') {
     ElMessage({message: '代码生成成功，请将生成的代码拷贝至项目中，重启系统', type: "success"});
    }
  }).catch(() => {
    loading.value = false
  })
  console.log('submit!');
}
</script>