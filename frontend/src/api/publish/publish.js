import request from '@/utils/request'


export function list(pageSize, index) {
  return request({
    url: `/publish/api/list/${pageSize}/${index}`,
    method: 'get',
    loading: true
  })
}

export function datasetTreeList() {
  return request({
    url: `/publish/api/list/dataset/treeSelect`,
    method: 'get',
    loading: true
  })
}
