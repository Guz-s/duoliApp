/**
 * 数据转换工具类
 * 用于处理前端和后端之间的数据格式转换
 */

import config from '@/config'
import { uploadRepairImage } from '@/api/repair/repair'

/**
 * 将前端表单数据转换为后端接口需要的格式
 * @param {Object} formData 前端表单数据
 * @returns {Object} 转换后的数据
 */
export function convertFormToApiData(formData) {
  const apiData = { ...formData }

  // 处理图片数组转JSON字符串
  if (Array.isArray(apiData.faultImages)) {
    apiData.faultImages = JSON.stringify(apiData.faultImages)
  }

  // 处理其他可能需要转换的数组字段
  if (Array.isArray(apiData.attachments)) {
    apiData.attachments = JSON.stringify(apiData.attachments)
  }

  // 处理日期格式
  if (apiData.createTime && apiData.createTime instanceof Date) {
    apiData.createTime = apiData.createTime.toISOString().slice(0, 19).replace('T', ' ')
  }

  // 处理空字符串转null（根据后端需要）
  Object.keys(apiData).forEach(key => {
    if (apiData[key] === '') {
      apiData[key] = null
    }
  })

  return apiData
}

/**
 * 将后端返回的数据转换为前端表单需要的格式
 * @param {Object} apiData 后端返回的数据
 * @returns {Object} 转换后的数据
 */
export function convertApiDataToForm(apiData) {
  const formData = { ...apiData }

  // 处理JSON字符串转图片数组
  if (typeof formData.faultImages === 'string' && formData.faultImages) {
    try {
      formData.faultImages = JSON.parse(formData.faultImages)
    } catch (e) {
      console.warn('解析faultImages失败:', e)
      formData.faultImages = []
    }
  } else if (!formData.faultImages) {
    formData.faultImages = []
  }

  // 处理其他可能的JSON字符串字段
  if (typeof formData.attachments === 'string' && formData.attachments) {
    try {
      formData.attachments = JSON.parse(formData.attachments)
    } catch (e) {
      console.warn('解析attachments失败:', e)
      formData.attachments = []
    }
  } else if (!formData.attachments) {
    formData.attachments = []
  }

  // 处理null值转空字符串（用于表单显示）
  Object.keys(formData).forEach(key => {
    if (formData[key] === null || formData[key] === undefined) {
      // 根据字段类型设置默认值
      if (['faultImages', 'attachments'].includes(key)) {
        formData[key] = []
      } else {
        formData[key] = ''
      }
    }
  })

  return formData
}

/**
 * 图片数组转逗号分隔字符串（适配后端存储格式）
 * @param {Array} imageArray 图片路径数组
 * @returns {String} 逗号分隔的字符串
 */
export function imagesToJson(imageArray) {
  if (!Array.isArray(imageArray) || imageArray.length === 0) {
    return ''
  }
  // 返回逗号分隔的字符串
  return imageArray.join(',')
}

/**
 * 逗号分隔字符串转图片数组（适配后端存储格式）
 * @param {String} imageString 逗号分隔的图片路径字符串
 * @returns {Array} 图片路径数组
 */
export function jsonToImages(imageString) {
  if (!imageString || typeof imageString !== 'string') {
    return []
  }

  // 如果是JSON格式，先尝试解析JSON
  if (imageString.startsWith('[') && imageString.endsWith(']')) {
    try {
      const parsed = JSON.parse(imageString)
      return Array.isArray(parsed) ? parsed : []
    } catch (e) {
      console.warn('解析图片JSON失败:', e)
      return []
    }
  }

  // 否则按逗号分隔处理
  return imageString.split(',').map(url => url.trim()).filter(url => url)
}

/**
 * 为图片路径添加完整URL前缀
 * @param {Array} imagePaths 图片路径数组
 * @param {String} baseUrl 基础URL
 * @returns {Array} 完整URL数组
 */
export function addImageBaseUrl(imagePaths, baseUrl = 'http://localhost:8080') {
  if (!Array.isArray(imagePaths)) {
    return []
  }

  return imagePaths.map(path => {
    if (!path) return ''
    // 如果已经是完整URL，直接返回
    if (path.startsWith('http') || path.startsWith('blob:')) {
      return path
    }
    // 添加baseUrl前缀
    return baseUrl + path
  })
}

/**
 * 处理故障图片数据，支持字符串和数组格式，自动添加baseUrl前缀
 * @param {String|Array} faultImages 故障图片数据（字符串或数组）
 * @param {String} baseUrl 基础URL，默认使用config中的baseUrl
 * @returns {Array} 完整URL数组
 */
export function processFaultImages(faultImages, baseUrl = config.baseUrl) {
  if (!faultImages) return []
  
  const images = typeof faultImages === 'string'
    ? faultImages.split(',').map(img => img.trim()).filter(img => img)
    : Array.isArray(faultImages) ? faultImages : []
  
  return images.map(img => {
    if (!img) return ''
    // 如果已经是完整URL，直接返回
    if (img.startsWith('http') || img.startsWith('blob:')) {
      return img
    }
    // 添加baseUrl前缀
    return baseUrl + img
  }).filter(img => img)
}

/**
 * 故障等级索引转换
 * @param {Number} index 故障等级索引
 * @param {Array} levelArray 故障等级数组
 * @returns {String} 故障等级字符串或索引字符串（根据后端需要）
 */
export function convertFaultLevel(index, levelArray = ['紧急', '较急', '一般', '其他']) {
  if (index >= 0 && index < levelArray.length) {
    // 如果后端需要文本，返回 levelArray[index]
    // 如果后端需要索引，返回 index.toString()
    return index.toString() // 根据你的需求，这里返回索引字符串
  }
  return ''
}

/**
 * 验证必填字段
 * @param {Object} formData 表单数据
 * @param {Array} requiredFields 必填字段列表
 * @returns {Object} 验证结果 {isValid: boolean, message: string}
 */
export function validateRequiredFields(formData, requiredFields) {
  for (const field of requiredFields) {
    if (!formData[field] || (typeof formData[field] === 'string' && !formData[field].trim())) {
      return {
        isValid: false,
        message: `请填写${getFieldLabel(field)}`
      }
    }
  }
  return { isValid: true, message: '' }
}

/**
 * 获取字段的中文标签
 * @param {String} fieldName 字段名
 * @returns {String} 中文标签
 */
function getFieldLabel(fieldName) {
  const labelMap = {
    reporterName: '报修人',
    reporterPhone: '联系电话',
    department: '部门/车间',
    location: '报修地点',
    equipmentModel: '设备型号',
    faultDescription: '故障描述'
  }
  return labelMap[fieldName] || fieldName
}

/**
 * 上传单张图片
 * @param {String} filePath 图片本地路径
 * @returns {Promise} 上传结果
 */
export function uploadSingleImage(filePath) {
  return new Promise((resolve) => {
    const data = {
      name: 'file',
      filePath: filePath
    }

    uploadRepairImage(data)
      .then(response => {
        console.log('上传响应:', response)
        // 根据后端返回的数据结构调整
        let imageUrl = response.data?.url || response.url || response.imgUrl || response.fileName

        // 如果返回的是相对路径，添加baseUrl前缀
        if (imageUrl && !imageUrl.startsWith('http') && !imageUrl.startsWith('blob:')) {
          imageUrl = config.baseUrl + imageUrl
        }

        resolve({
          success: true,
          url: imageUrl || filePath // 如果没有返回URL，使用本地路径
        })
      })
      .catch(error => {
        console.error('图片上传失败:', error)
        resolve({
          success: false,
          error: error,
          url: filePath // 失败时也返回本地路径用于预览
        })
      })
  })
}

/**
 * 批量上传图片
 * @param {Array} imagePaths 图片路径数组
 * @param {Function} onProgress 进度回调函数
 * @returns {Promise} 上传结果
 */
export function uploadMultipleImages(imagePaths, onProgress) {
  return new Promise(async (resolve) => {
    try {
      let completedCount = 0
      const results = []

      // 逐个上传图片以便显示进度
      for (const path of imagePaths) {
        try {
          const result = await uploadSingleImage(path)
          results.push(result)
          completedCount++

          // 调用进度回调
          if (onProgress) {
            onProgress(completedCount, imagePaths.length)
          }
        } catch (error) {
          results.push({ success: false, error })
          completedCount++

          // 调用进度回调
          if (onProgress) {
            onProgress(completedCount, imagePaths.length)
          }
        }
      }

      resolve(results)
    } catch (error) {
      console.error('批量上传失败:', error)
      resolve([])
    }
  })
}

/**
 * 处理图片选择和上传的完整流程
 * @param {Object} options 配置选项
 * @param {Number} options.maxCount 最大图片数量，默认6
 * @param {Array} options.currentImages 当前已有的图片数组
 * @param {Function} options.onSuccess 成功回调
 * @param {Function} options.onProgress 进度回调
 * @param {Function} options.onError 错误回调
 */
export function handleImageUpload(options = {}) {
  const {
    maxCount = 6,
    currentImages = [],
    onSuccess,
    onProgress,
    onError
  } = options

  const remainingCount = maxCount - currentImages.length

  if (remainingCount <= 0) {
    uni.showToast({
      title: `最多只能上传${maxCount}张图片`,
      icon: 'none'
    })
    return
  }

  uni.chooseImage({
    count: remainingCount,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: async (res) => {
      const paths = res.tempFilePaths || []
      if (paths.length === 0) return

      // 显示上传进度
      uni.showLoading({
        title: `上传中... (0/${paths.length})`,
        mask: true
      })

      try {
        const results = await uploadMultipleImages(paths, (completed, total) => {
          // 更新进度显示
          uni.showLoading({
            title: `上传中... (${completed}/${total})`,
            mask: true
          })

          // 调用外部进度回调
          if (onProgress) {
            onProgress(completed, total)
          }
        })

        // 将上传成功的图片URL添加到数组中
        const successUrls = results
          .filter(result => result.success)
          .map(result => result.url)

        const failedCount = results.filter(result => !result.success).length
        const successCount = results.filter(result => result.success).length

        uni.hideLoading()

        // 显示结果提示
        if (failedCount > 0) {
          uni.showToast({
            title: `${successCount}张成功，${failedCount}张失败`,
            icon: 'none',
            duration: 2000
          })
        } else {
          uni.showToast({
            title: '图片上传完成',
            icon: 'success'
          })
        }

        // 调用成功回调
        if (onSuccess) {
          onSuccess(successUrls, results)
        }

      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '上传失败',
          icon: 'none'
        })

        // 调用错误回调
        if (onError) {
          onError(error)
        }
      }
    },
    fail: (error) => {
      if (onError) {
        onError(error)
      }
    }
  })
}

export default {
  convertFormToApiData,
  convertApiDataToForm,
  imagesToJson,
  jsonToImages,
  addImageBaseUrl,
  processFaultImages,
  convertFaultLevel,
  validateRequiredFields,
  uploadSingleImage,
  uploadMultipleImages,
  handleImageUpload
}
