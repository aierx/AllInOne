<template>
  <div class="hello">
    <el-tree :data="data" node-key="id" accordion :highlight-current="true" @node-click="handleNodeClick">
      <span slot-scope="{ node, data }" style="width: 100%;">
        <div class="custom-tree-node ">
          <div>
            <span> {{ node.label }}</span>
          </div>
          <div>
            <span v-if="data.level==2">
              <el-button type="text">
                <el-tag size="mini">{{ data.slimline }}</el-tag>
              </el-button>
            </span>
            <span v-if="data.level==3">
              <el-tag size="mini" style=" color:#f56c6c;    background-color: #fef0f0;  border-color:#fef0f0;">{{ data.protocolType }}</el-tag>
            </span>
            <span v-if="data.level==4">
              <el-button type="text">
                <el-tag size="mini" style="color: #909399;    background-color: #f4f4f5;  border-color: #f4f4f5;">{{ data.requestType }}</el-tag>

              </el-button>
            </span>
          </div>
        </div>
      </span>
    </el-tree>
    <InvokePage :data=curData></InvokePage>
  </div>
</template>

<script>

import InvokePage from './InvokePage.vue'
export default {
  name: 'HelloWorld',
  data() {
    return {
      data: [],
      curData: {}
    }
  },
  props: {
    msg: String,
  },
  components: {
    InvokePage
  },
  /* eslint-disable */
  methods: {
    fetchData() {
      fetch("http://127.0.0.1:8080/services.json")
        .then(response => response.json())
        .then(data => {
          this.data = data
          for (let i = 0; i < data.length; i++) {
            const app = data[i];
            app.children = app.machineModels
            app.label = app.appkey
            app.level = 1
            let machines = app.machineModels
            for (let j = 0; j < machines.length; j++) {
              const machine = machines[j]
              machine.label = machine.ip + ":" + machine.port
              machine.children = machine.serviceModelList
              machine.level = 2
              let services = machine.serviceModelList
              for (let k = 0; k < services.length; k++) {
                const service = services[k];
                service.label = service.className
                service.children = service.methodModels
                service.level = 3
                for (let l = 0; l < service.children.length; l++) {
                  const method = service.children[l];
                  method.className = service.className
                  method.appkey = app.appkey
                  method.host = machine.label
                  method.slimline = machine.slimline
                  method.level = 4
                  method.label = method.methodName.replace(service.className + ".", "")
                }
              }
            }
          }
        })
    },
    handleNodeClick(data) {
      if (data.hasOwnProperty("methodName")) {
        this.curData = data
      }
    }
  },
  created() {
    this.fetchData()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style  scoped>
.custom-tree-node {
  width: 100%;
  display: flex;
  justify-content: space-between
}

.el-button {
  padding: 0;
}

::v-deep .el-tree-node__content {
  height: 40px;
  background-color: rgba(red, 0, 0, 50);
  border-color: rgba(255, 0, 0, 0.2);
}
</style>
