<template>
  <!-- 头部容器，包括左侧的标题和右侧的功能区 -->
  <div class="header-cont">
    <!-- 左侧标题，点击跳转到主页 -->
    <div class="left">
      <h1>
        <router-link to="/">{{ t('sitename') }}</router-link>
      </h1>
    </div>
    <!-- 右侧功能区，包括语言切换和用户相关操作 -->
    <div class="right flex-center">
      <!-- 语言切换部分 -->
      <div class="lang gap">
        <span
            class="item"
            :class="{ active: locale === 'zh-cn' }"
            @click="changeLanguage('zh-cn')"
        >简体中文</span>
        /
        <span
            class="item"
            :class="{ active: locale === 'en' }"
            @click="changeLanguage('en')"
        >EN</span>
      </div>
      <!-- 登录状态的下拉菜单 -->
      <template v-if="isLogin">
        <el-dropdown trigger="click" @command="handleCommand">
          <div class="flex-center cursor">
            {{ username }}
            <el-icon>
              <caret-bottom />
            </el-icon>
          </div>
          <!-- 下拉菜单内容 -->
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="toPersonal">{{ t('personalCenter') }}</el-dropdown-item>
              <el-dropdown-item command="toLogout">{{ t('logout') }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
      <!-- 如果未登录且当前路由不是登录页，显示登录链接 -->
      <template v-else-if="$route.name !== 'Login'">
        <router-link to="/login">{{ t('login') }}</router-link>
      </template>
    </div>
  </div>
</template>

<script setup>
import { logout } from '@/apis/login'
const { locale, t } = useI18n();
// 语言切换
function changeLanguage(lang) {
  locale.value = lang
  localStorage.setItem('locale', lang)
}

const store = useStore();
const isLogin = computed(() => store.getters['user/isLogin']);
const userInfo = computed(() => store.state.user.userInfo);
const username = computed(() => userInfo.value?.name)

// 更新用户信息
store.dispatch('user/refreshInfo');

const router = useRouter();
const commands = ({
  toPersonal: () => {
    router.push('/personal')
  },
  toLogout: () => {
    logout().then(res => {
      if (res.code == 200) {
        store.commit('user/clearToken');
        store.commit('user/clearUserInfo');
        // router.push('/login')
        window.location = '/';
      }
    })
  }
});

// 处理下拉菜单命令
function handleCommand(command) {
  commands[command] && commands[command]();
}
</script>


<style lang="scss">
.header-cont {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 100%;

  a {
    color: inherit;
    text-decoration: none;
  }
  h1 {
    margin: 0;
    font-size: 20px;
  }
  .gap {
    margin-right: 20px;
  }
  .right {
    .lang {
      font-size: 14px;
      .item {
        cursor: pointer;
        &.active {
          font-size: 18px;
          font-weight: bold;
        }
      }
    }
  }
  .el-dropdown {
    color: inherit;
  }
}
</style>
