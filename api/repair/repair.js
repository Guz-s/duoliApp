import upload from '@/utils/upload'
import request from '@/utils/request'


// 查询工单数据列表
export function listRepairData(query) {
  return request({
    url: '/repair/order/list',
    method: 'get',
    params: query
  })
}

// 根据工单ID查询工单数据信息
export function getRepairByCode(repairId) {
  return request({
    url: '/repair/order/' + repairId,
    method: 'get'
  })
}

// 新增工单申请
export function addRepairData(data) {
  return request({
    url: '/repair/order',
    method: 'post',
    data: data
  })
}


// 修改工单申请
export function updateRepairData(data) {
  return request({
    url: '/repair/order',
    method: 'put',
    data: data
  })
}

// 删除工单申请
export function delRepairData(data) {
  return request({
    url: '/repair/order/' + data,
    method: 'delete'
  })
}

// 故障图片上传 - 使用通用上传接口
export function uploadRepairImage(data) {
  return upload({
    url: '/common/upload',
    name: data.name || 'file', 
    filePath: data.filePath
  })
}
