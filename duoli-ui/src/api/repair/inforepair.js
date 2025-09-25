import request from '@/utils/request'

// 查询设备信息列表
export function listInforepair(query) {
  return request({
    url: '/repair/inforepair/list',
    method: 'get',
    params: query
  })
}

// 查询设备信息详细
export function getInforepair(equipmentId) {
  return request({
    url: '/repair/inforepair/' + equipmentId,
    method: 'get'
  })
}

// 新增设备信息
export function addInforepair(data) {
  return request({
    url: '/repair/inforepair',
    method: 'post',
    data: data
  })
}

// 修改设备信息
export function updateInforepair(data) {
  return request({
    url: '/repair/inforepair',
    method: 'put',
    data: data
  })
}

// 删除设备信息
export function delInforepair(equipmentId) {
  return request({
    url: '/repair/inforepair/' + equipmentId,
    method: 'delete'
  })
}
