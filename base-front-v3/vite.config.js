import { defineConfig, loadEnv } from 'vite'
import path from 'path'
import createVitePlugins from './vite/plugins'  // 从本地路径导入 createVitePlugins 函数，通常用于创建 Vite 插件

// https://vitejs.dev/config/
export default defineConfig(({ mode, command }) => {
  // 使用 loadEnv 方法加载环境变量，mode 是当前环境模式，process.cwd() 是当前工作目录
  const env = loadEnv(mode, process.cwd())
  return {
    // 部署生产环境和开发环境下的URL。
    // 默认情况下，vite 会假设你的应用是被部署在一个域名的根路径上
    base: '/',
    plugins: createVitePlugins(env, command === 'build'),
    resolve: {
      // https://cn.vitejs.dev/config/#resolve-alias
      alias: {
        // '~' 别名指向项目根目录
        '~': path.resolve(__dirname, './'),
        // '@' 别名指向 src 目录
        '@': path.resolve(__dirname, './src')
      },
      // https://cn.vitejs.dev/config/#resolve-extensions
      // extensions 配置项用于自动解析指定后缀名的文件，在引入模块时可以省略这些后缀名
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    // vite 相关配置
    server: {
      port: 8420  //指定端口号
    }
  }
})
