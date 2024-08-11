// import { ref, getCurrentInstance } from "vue";
// import { useI18n } from "vue-i18n";
// import { ElMessage } from 'element-plus'
export default function useTableHandlers(form) {
    // 数据部分
    const {t} = useI18n(); // 使用国际化
    const tableRef = ref(); // 表格引用
    const dialogVisible = ref(false); // 对话框可见性
    const isEdit = ref(false); // 是否编辑模式
    const formLoading = ref(false); // 表单加载状态
    const formRef = ref(); // 表单引用
    const __formOld__ = {...form}; // 原始表单数据，用于重置

    // 方法部分

    // 搜索方法，触发表格重新加载
    const doSearch = () => {
        tableRef.value.reload();
    };

    // 添加方法，打开对话框并清除表单验证
    const doAdd = () => {
        dialogVisible.value = true;
        isEdit.value = false;
        formRef.value && formRef.value.clearValidate();
    };

    // 编辑方法，根据ID获取数据并填充表单
    const doEdit = (api, id) => {
        if (!form) return;
        api(id).then(response => {
            const row = response.data;
            if (row) {
                for (const k in form) {
                    if (k in row) {
                        form[k] = row[k];
                    }
                }
                isEdit.value = true;
                dialogVisible.value = true;
            }
        });
    };


    // 删除方法，调用API删除数据并执行回调
    const doRemove = (api, ids, callback) => {
        api(ids).then(() => {
            callback && callback();
        });
    };

    // 获取表单参数，如果不是编辑模式，删除ID字段
    const getParams = () => {
        const params = {...form};
        if (!isEdit.value) {
            delete params.id;
        }
        return params;
    };

    // 提交方法，验证表单并调用API保存或更新数据
    const doSubmit = (apis, callback) => {
        if (!form || !apis) return;
        // valid 表示表单验证是否通过
        formRef.value.validate((valid) => {
            if (valid) {
                //启用按钮加载
                formLoading.value = true;
                let promise;
                //获取表单参数, 判断api是否有getParams()
                const params = apis.getParams ? apis.getParams() : getParams();
                if (isEdit.value) {
                    promise = apis.update(params);
                } else {
                    promise = apis.save(params);
                }
                //处理API响应
                promise.then((res) => {
                        //如果提供了 callback，则调用它并传入响应数据；否则显示一个成功的消息提示。
                        if (callback) {
                            callback(res);
                        } else {
                            ElMessage({
                                message: t("tips.success"),
                                type: "success",
                                showClose: true,
                            });
                        }
                        //关闭表单对话框，并根据编辑状态刷新或重载表格数据。
                        doClose();
                        if (isEdit.value) {
                            tableRef.value.refresh();
                        } else {
                            tableRef.value.reload();
                        }
                    })
                    //取消按钮加载
                    .finally(() => {
                        formLoading.value = false;
                    });
            }
        });
    };

    // 重置表单方法，将表单重置为原始数据
    const resetForm = () => {
        if (!form) return;
        for (const k in __formOld__) {
            form[k] = __formOld__[k];
        }
    };

    // 关闭对话框并重置表单
    function doClose() {
        dialogVisible.value = false;
        resetForm();
    }

    // 返回方法和数据
    return {
        t,
        tableRef,
        dialogVisible,
        isEdit,
        formLoading,
        formRef,
        doSearch,
        doAdd,
        doEdit,
        doSubmit,
        doClose,
        doRemove,
    };
}
