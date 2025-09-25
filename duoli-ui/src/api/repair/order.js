import request from '@/utils/request'

// 查询报修工单列表
export function listOrder(query) {
  return request({
    url: '/repair/order/list',
    method: 'get',
    params: query
  })
}

// 查询报修工单详细
export function getOrder(orderId) {
  return request({
    url: '/repair/order/' + orderId,
    method: 'get'
  })
}

// 新增报修工单
export function addOrder(data) {
  return request({
    url: '/repair/order',
    method: 'post',
    data: data
  })
}

// 修改报修工单
export function updateOrder(data) {
  return request({
    url: '/repair/order',
    method: 'put',
    data: data
  })
}

// 删除报修工单
export function delOrder(orderId) {
  return request({
    url: '/repair/order/' + orderId,
    method: 'delete'
  })
}
