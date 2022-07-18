<template>
  <el-select :value="valueTitle" :clearable="clearable" @clear="clearHandle">
    <el-option :key="valueId" :value="valueId" :label="valueTitle" class="options">
      <el-tree id="tree-option" ref="selectTree" :accordion="accordion" :data="options" :props="props"
        :node-key="props.value" :default-expanded-keys="defaultExpandedKey" @node-click="handleNodeClick">
      </el-tree>
    </el-option>
  </el-select>
</template>

<script>
export default {
  name: "TreeInputSelector",
  props: {
    // 配置项
    props: {
      type: Object,
      default: {
        value: 'id',             // ID字段名
        label: 'title',         // 显示名称
        children: 'children'    // 子级字段名
      }
    },

    // 选项列表数据(树形结构的对象数组)
    options: { type: Array, default: [] },

    // 初始值
    value: { type: String, default: null },

    // 初始内容
    label: {type: String, default: null},

    // 可清空选项
    clearable: { type: Boolean, default: true },

    // 自动收起
    accordion: { type: Boolean, default: false }
  },
  data() {
    console.log(1)
    return {
      valueId: null,
      valueTitle: '',
      defaultExpandedKey: []
    }
  },
  mounted() {
    console.log(2);
    this.initHandle();
  },
  methods: {
    // 初始化值
    initHandle() {
      console.log(3)
      if (this.value) {
        console.log(4)
        this.clearHandle();
        this.valueId = this.value;    // 初始值
        this.valueTitle = this.lable; // 内容
        this.$refs.selectTree.setCurrentKey(this.valueId);       // 设置默认选中
        this.defaultExpandedKey = [this.valueId];      // 设置默认展开
        console.log(this.$refs.selectTree);
        console.log(this.$refs.selectTree);
      }
      this.initScroll();
      this.$forceUpdate();
    },
    // 初始化滚动条
    initScroll() {
      this.$nextTick(() => {
        let scrollWrap = document.querySelectorAll('.el-scrollbar .el-select-dropdown__wrap')[0]
        let scrollBar = document.querySelectorAll('.el-scrollbar .el-scrollbar__bar')
        scrollWrap.style.cssText = 'margin: 0px; max-height: none; overflow: hidden;'
        scrollBar.forEach(ele => ele.style.width = 0)
      })
    },
    // 切换选项
    handleNodeClick(node) {
      console.log(5);
      console.log(node);
      if (node.forbiddenSelect) {
        // this.clearHandle();
        return;
      }
      this.valueTitle = node[this.props.label]
      this.valueId = node[this.props.value]
      this.$emit('getValue', this.valueId)
      this.defaultExpandedKey = []
    },
    // 清除选中
    clearHandle() {
      this.valueTitle = ''
      this.valueId = null
      this.defaultExpandedKey = []
      this.clearSelected()
      this.$emit('getValue', null)
    },
    // 清空选中样式
    clearSelected() {
      let allNode = document.querySelectorAll('#tree-option .el-tree-node')
      allNode.forEach((element) => element.classList.remove('is-current'))
    }
  }
}
</script>

<style scoped>
.el-scrollbar .el-scrollbar__view .el-select-dropdown__item {
  height: auto;
  max-height: 274px;
  padding: 0;
  overflow: hidden;
  overflow-y: auto;
}

.el-select-dropdown__item.selected {
  font-weight: normal;
}

ul li>>>.el-tree .el-tree-node__content {
  height: auto;
  padding: 0 20px;
}

.el-tree-node__label {
  font-weight: normal;
}

.el-tree>>>.is-current .el-tree-node__label {
  color: #409EFF;
  font-weight: 700;
}

.el-tree>>>.is-current .el-tree-node__children .el-tree-node__label {
  color: #606266;
  font-weight: normal;
}
</style>
