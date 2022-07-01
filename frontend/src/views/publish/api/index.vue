<template>
  <layout-content>
    <el-table
      v-loading="loading"
      :data="tableData"
      style="width: 100%"
    >
      <el-table-column
        v-if="false"
        prop="pubId"
        label="ID"
        width="180"
      >

      </el-table-column>
      <el-table-column
        v-if="false"
        prop="datasetId"
        label="数据集ID"
        width="180"
      >
      </el-table-column>
      <el-table-column
        prop="datasetName"
        label="数据集"
        width="180"
      >
        <template v-slot="slotProps">
          <DatasetSelector>{{ slotProps.row.datasetName }}</DatasetSelector>
        </template>
      </el-table-column>
      <el-table-column
        prop="successWrapper"
        label="成功返回值包裹体"
        width="180"
      >
        <template v-slot="slotProps">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="slotProps.row.successWrapper"
          >
          </el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="failureWrapper"
        label="失败返回值包裹体"
        width="180"
      >
        <template v-slot="slotProps">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="slotProps.row.failureWrapper"
          >
          </el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="pubUrlPrefix"
        label="发布路径前缀"
        width="180"
      >
        <template v-slot="slotProps">
          <el-input
            placeholder="请输入内容"
            v-model="slotProps.row.pubUrlPrefix"
          >
          </el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="pageable"
        label="是否分页"
        width="180"
      >
        <template v-slot="slotProps">
          <el-switch
            v-model="slotProps.row.pageable"
            active-color="#13ce66"
            inactive-color="#ff4949"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        prop="publishEnabled"
        label="是否发布"
        width="180"
      >
        <template v-slot="slotProps">
          <el-switch
            v-model="slotProps.row.publishEnabled"
            active-color="#13ce66"
            inactive-color="#ff4949"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作">

      </el-table-column>
    </el-table>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="index"
        :page-sizes="[10, 30, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </layout-content>
</template>

<script>

import { list } from '@/api/publish/publish'
import LayoutContent from '@/components/business/LayoutContent'
import DatasetSelector from '@/views/publish/api/DatasetSelector'

export default {
  name: 'publishApi',
  components: { LayoutContent, DatasetSelector },
  mounted() {
    this.loadData()
  },
  data() {
    return {
      tableData: [],
      index: 1,
      total: 0,
      pageSize: 10,
      loading: false
    }
  },
  methods: {
    loadData() {
      this.loading = true
      list(this.pageSize, this.index).then(response => {
        console.log(response)
        if (response.success) {
          this.total = response.data.total
          this.tableData = response.data.records
        }
        this.loading = false
      }).catch((e) => {
        this.loading = false
      })
    },
    handleCurrentChange(val) {
      console.log(`变更到第： ${val} 页`)
      this.index = val
      this.loadData()
    },
    handleSizeChange(val) {
      console.log(`当前变更的页面显示每页的大小为： ${val}`)
      this.pageSize = val
      this.loadData()
    },
    handleEdit(index, row) {
      console.log(index, row)
    },
    handleDelete(index, row) {
      console.log(index, row)
    }
  }
}
</script>

<style scoped>

</style>
