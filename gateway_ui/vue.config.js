const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack:e=>{
    e.devtool="source-map",
    e.optimization={
      minimize:false
    }
  }
})
