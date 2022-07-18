<template>
  <TreeInputSelector 
  v-if="loading"
  :props="pp" 
  :options="list" 
  :value="value" 
  :label="label"
  :clearable="isClearable"
  :accordion="isAccordion" 
  @getValue="getValue($event)" 
  />
</template>

<script>
import { datasetTreeList } from '@/api/publish/publish'
import TreeInputSelector from '@/components/TreeInputSelector'

export default {
  name: 'DatasetSelector',
  components: { TreeInputSelector },
  props: {
    value: String,
    selected: Function,
    label: String,
  },
  mounted() {
    this.initValue();
    console.log("==组件装在后，获取到的props： ", this.value);
  },
  data() {
    return {
      isClearable: true,      // 可清空（可选）
      isAccordion: true,      // 可收起（可选）
      // 选项列表（必选）
      list: [],
      pp: {
        value:'id',             // ID字段名
        label: 'name',         // 显示名称
        children: 'children'    // 子级字段名
      },
      loading: false
    };
  },
  methods: {
    // 取值
    getValue(value) {
      this.valueId = value
      console.log(this.valueId);
    },
    initValue() {
      datasetTreeList().then(resp => {
        console.log(resp);
        if (resp.success) {
          const treeList = [];
          resp.data.forEach(d => {
            if (d.type === 'group') {
              treeList[treeList.length] = {
                id: d.id,
                parentId: d.pid,
                name: d.name,
                forbiddenSelect: true,
                rank: d.createTime
              }
            } else {
              treeList[treeList.length] = {
                id: d.id,
                parentId: d.sceneId,
                name: d.name,
                rank: d.createTime
              }
            }
          });
          console.log(treeList);
          
          let cloneData = JSON.parse(JSON.stringify(treeList))      // 对源数据深度克隆
          this.list =  cloneData.filter(father => {                      // 循环所有项，并添加children属性
            let branchArr = cloneData.filter(child => father.id == child.parentId);       // 返回每一项的子级数组
            branchArr.length > 0 ? father.children = branchArr : ''   //给父级添加一个children属性，并赋值
            return father.parentId == 0;      //返回第一层
          });
          this.loading = true;
        }
      });
    }
  }
}
</script>

<style scoped>
</style>
