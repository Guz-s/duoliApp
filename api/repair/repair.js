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

// 根据工单号查询工单数据信息
export function getRepairByCode(repairCode) {
  return request({
    url: '/repair/order' + repairCode,
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

// 删除字典数据
export function delRepairData(data) {
  return request({
    url: '/repair/order' + data,
    method: 'delete'
  })
}
