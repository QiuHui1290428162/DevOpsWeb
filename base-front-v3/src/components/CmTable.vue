<template>
  <!-- 主体容器，绑定了加载状态 -->
  <div v-loading="loading" class="cm-table">
    <!--表格栏-->
    <!-- :data  绑定表格数据
     v-bind 绑定所有父组件传递的属性
     @selection-change 行选择改变时触发的事件 -->
    <el-table
        :data="data.content"
        class="cm-table__tb"
        v-bind="$attrs"
        @selection-change="selectionChange"
    >
      <!-- 根据columns数组动态生成表格列 -->
      <el-table-column v-for="column in columns" :key="column.prop" v-bind="column">
        <!-- 对于有自定义插槽的列，使用插槽内容 -->
        <template v-if="$slots[`${column.prop}Slot`]" #default="scope">
          <slot :name="`${column.prop}Slot`" :scope="scope"/>
        </template>
      </el-table-column>
      <!-- 操作列，条件渲染，显示编辑和删除等操作按钮 -->
      <!-- v-if  只有当showOperation为true时显示操作列
      :label 列标题
      :width 列宽度
      -->
      <el-table-column
          v-if="showOperation"
          fixed="right"
          :label="t('action.operation')"
          :width="oprWidth"
      >
        <!-- 默认模板插槽，用于显示操作按钮 -->
        <template #default="{ row }">
          <!-- 遍历操作数组，为每种操作类型创建按钮 -->
          <template v-for="(opr, i) in operations" :key="i">
            <!-- 根据条件显示按钮 -->
            <template v-if="isShow(opr.show, row) && $hasBP(opr.perm)">
              <!-- 编辑按钮 -->
              <el-button
                  v-if="opr.type === 'edit'"
                  type="text"
                  @click="handleEdit(row)"
              >{{ t('action.edit') }}
              </el-button>
              <!-- 删除按钮 -->
              <el-button
                  v-else-if="opr.type === 'delete'"
                  type="text"
                  class="danger"
                  @click="handleDelete(row)"
              >{{ t('action.delete') }}
              </el-button>
              <!-- 自定义按钮
                如果 opr 对象有 isDisabled 函数，就调用它并传入当前行 row 作为参数。
                如果没有定义 isDisabled 函数，则默认不禁用按钮。
              -->
              <el-button
                  v-else
                  type="text"
                  :disabled="opr.isDisabled ? opr.isDisabled(row) : false"
                  @click="opr.onClick(row)"
              >{{ opr.label }}
              </el-button>
            </template>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <!--分页栏-->
    <div class="cm-table__toolbar">
      <!-- 条件渲染批量删除按钮 -->
      <template v-for="(opr, i) in operations" :key="i">
      <el-button
          v-if="showBatchDelete && opr.type === 'delete' && $hasBP(opr.perm)"
          type="danger"
          :disabled="selections.length === 0"
          @click="handleBatchDelete()"
      >{{ t('action.batchDelete') }}
      </el-button>
      </template>
      <!-- 分页组件 -->
      <!-- v-if  只有当showPagination为true时显示分页组件
      currentPage 绑定每页显示条数
      page-size 绑定每页显示条数
      :total 总条数
      :page-sizes 每页显示条数
      :page-size 每页显示条数
      :current-page 当前页
      @size-change 每页显示条数改变时触发
      @current-change 当前页改变时触发
      -->
      <el-pagination
          v-if="showPagination"
          class="cm-table__pagination"
          v-model:currentPage="pageRequest.pageNum"
          v-model:page-size="pageRequest.pageSize"
          :total="data.totalSize || 0"
          :page-sizes="[10, 20, 50, 100, 200]"
          layout="total, prev, pager, next, sizes, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
      ></el-pagination>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  getPage: Function, // 获取表格数据的方法，预期是一个异步函数
  filters: Object, // 外部传入的过滤条件对象
  showPagination: { // 是否显示分页组件
    type: Boolean,
    default: true
  },
  columns: Array, // 表格列的配置数组
  showOperation: { // 是否显示操作列
    type: Boolean,
    default: true,
  },
  operations: { // 操作列按钮的配置
    type: Array,
    default: () => [
      { type: 'edit', perm: 'bnt.default.update' },
      { type: 'delete', perm: 'bnt.default.remove' }
    ]
  },
  oprWidth: { // 操作列的宽度
    type: Number,
    default: 185
  },
  showBatchDelete: { // 是否显示批量删除按钮
    type: Boolean,
    default: true,
  },
})

// 用于接收父组件传入的参数
const emit = defineEmits(['handleEdit', 'handleDelete']);

const {t} = useI18n();  // 国际化，提取文本
const loading = ref(false) // 控制加载状态显示
const pageRequest = reactive({ // 分页请求参数
  pageNum: 1,
  pageSize: 20,
})
const data = ref({}); // 存储从后端加载的数据


// 分页查询
function findPage() {
  debugger;
  if (!props.getPage) {
    return;
  }
  loading.value = true;
  /*  const req = props.getPage({ ...pageRequest, ...(props.filters || {}), sortby: props.sortby });*/
  const req = props.getPage(pageRequest.pageNum, pageRequest.pageSize, props.filters);
  if (Object(req).constructor === Promise) {
    req.then(res => {
      if (res.data && res.data.records) {
        data.value = {
          content: res.data.records,
          totalSize: res.data.total
        }
      }else{
        data.value = {
          content: res.data
        }
      }
    }).catch(() => {
      data.value = {}
    }).finally(() => {
      loading.value = false;
    });
  }

}

// 刷新
function reload() {
  handlePageChange(1); // 重新加载第一页数据
}

// 更新每页显示条数,并重置第一页
function handleSizeChange(pageSize) {
  pageRequest.pageSize = pageSize; // 更新页大小
  pageRequest.pageNum = 1; // 重置为第一页
  findPage(); // 重新加载数据
}

// 换页刷新
function handlePageChange(pageNum) {
  pageRequest.pageNum = pageNum; // 更新当前页码
  findPage(); // 重新加载数据
}

function isShow(showFn, row) {
  if (showFn && typeof showFn === 'function') {
    return showFn(row) // 根据函数结果决定是否显示
  }
  return true;
}

function isDisabled(disabledFn, row) {
  if (disabledFn && typeof disabledFn === 'function') {
    return disabledFn(row) // 根据函数结果决定是否禁用
  }
  return false;
}


// 编辑
function handleEdit(row) {
  emit("handleEdit", row);
}

// 删除
function handleDelete(row) {
  onDelete([row.id]);
}

const selections = ref([]);

function selectionChange(slts) {
  selections.value = slts;
}

// 批量删除
function handleBatchDelete() {
  let ids = selections.value.map((item) => item.id);
  onDelete(ids);
}

// 删除操作
function onDelete(ids) {
  ElMessageBox.confirm(t('tips.deleteConfirm'), t('tips.deleteTitle'), {
    confirmButtonText: t('action.confirm'),
    cancelButtonText: t('action.cancel'),
    type: "warning",
    draggable: true,
  }).then(() => {
    const callback = () => {
      ElMessage({message: t('tips.success'), type: "success"});
      reload();
    };
    emit("handleDelete", ids, callback);
  }).catch(() => {
  });
}

reload();

defineExpose({
  refresh: findPage,
  reload,
})
</script>
<style lang="scss" scoped>
.cm-table__tb {
  border: 1px solid #eee;
  border-bottom: none;
  min-width: 100%;
}

.cm-table__toolbar {
  padding: 10px 5px;

  &:after {
    content: "";
    display: table;
    clear: both;
  }
}

.cm-table__pagination {
  float: right;
  padding-right: 0;
}

.danger {
  color: var(--el-color-danger) !important;
}
</style>
