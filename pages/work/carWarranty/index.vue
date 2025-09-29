<script>
import { listRepairData, addRepairData, getRepairByCode, updateRepairData } from "@/api/repair/repair";
import { convertFormToApiData, convertApiDataToForm, validateRequiredFields, imagesToJson, jsonToImages, addImageBaseUrl, handleImageUpload } from "@/utils/dataConverter";
import config from '@/config';

export default {
  name: "CarWarrantyForm",
  data() {
    return {
      mode: 'add', // add: 新增模式, edit: 编辑模式
      editId: null,
      form: {
        // 报修人
        reporterName: '',
        // 报修人联系方式
        reporterPhone: '',
        // 部门/车间
        department: '',
        // 报修地点
        location: '',
        // 设备型号
        equipmentModel: '',
        // 故障等级
        faultLevel: '',
        //其他故障等级
        faultLevelDesc: '',
        //故障图片
        faultImages: [],
        // 上传的图片URL（服务器返回的）
        uploadedImages: [],
        // 上传状态
        uploading: false,
        //故障描述
        faultDescription: '',
      },
      // 故障等级索引
      severityIndex: -1,
      //故障等级
      faultLevel: [ '紧急', '较急', '一般', '其他' ],
      // uview picker相关
      showFaultLevelPicker: false,
      faultLevelColumns: [
        [ '紧急', '较急', '一般', '其他' ]
      ]
    }
  },
  onLoad(options) {
    console.info('options:', options)
    // 检查是否为编辑模式
    if (options.mode === 'edit' && options.orderId) {
      this.mode = 'edit'
      this.editId = options.orderId
      this.getRepairByCodeFun(options.orderId)
    }
  },
  methods: {
    /*
     * 新增报修申请
     */
     addRepairOrderInfo() {
      // 设置故障等级索引
      if (this.severityIndex >= 0 && this.severityIndex < this.faultLevel.length) {
        this.form.faultLevel = this.severityIndex.toString()
      }

      // 准备提交数据，将图片URL转换为相对路径后再转换为字符串
      const relativePaths = this.form.uploadedImages.map(url => {
        if (url && url.startsWith(config.baseUrl)) {
          // 如果是完整URL，提取相对路径
          return url.replace(config.baseUrl, '')
        }
        return url
      })

      const submitData = {
        ...this.form,
        faultImages: imagesToJson(relativePaths),
        // 如果故障等级不是"其他"（索引3），确保faultLevelDesc为空
        faultLevelDesc: this.severityIndex === 3 ? this.form.faultLevelDesc : ''
      }

      // 如果是编辑模式，添加ID并调用更新接口
      if (this.mode === 'edit' && this.editId) {
        submitData.orderId = this.editId
        this.updateRepairOrder(submitData)
      } else {
        this.createRepairOrder(submitData)
      }
    },

    // 创建新工单
    createRepairOrder(submitData) {
      addRepairData(submitData).then(res => {
        console.info('提交成功', res)
        uni.showToast({
          title: '提交成功',
          icon: 'success',
          complete: () => {
            setTimeout(() => {
              uni.navigateBack()
            }, 1500)
          }
        })
      }).catch(err => {
        console.error('提交失败:', err)
        uni.showToast({
          title: '提交失败，请重试',
          icon: 'none'
        })
      })
    },

    // 更新工单
    updateRepairOrder(submitData) {
      updateRepairData(submitData).then(res => {
        console.info('更新成功', res)
        uni.showToast({
          title: '更新成功',
          icon: 'success',
          complete: () => {
            setTimeout(() => {
              uni.navigateBack()
            }, 1500)
          }
        })
      }).catch(err => {
        console.error('更新失败:', err)
        uni.showToast({
          title: '更新失败，请重试',
          icon: 'none'
        })
      })
    },
    /*
     * 根据工单ID获取工单数据
     */
    getRepairByCodeFun(orderId) {
      console.info('获取工单数据，ID:', orderId)

      getRepairByCode(orderId).then(res => {
        console.info('获取到的工单数据:', res)

        // 将API返回的数据映射到表单
        const data = res.data || res
        this.form = {
          ...this.form,
          reporterName: data.reporterName || '',
          reporterPhone: data.reporterPhone || '',
          department: data.department || '',
          location: data.location || '',
          equipmentModel: data.equipmentModel || '',
          faultDescription: data.faultDescription || '',
          faultLevelDesc: data.faultLevelDesc || ''
        }

        // 如果返回的数据中有faultLevel，需要转换为severityIndex
        if (data.faultLevel !== undefined && data.faultLevel !== null) {
          const faultLevelIndex = parseInt(data.faultLevel)
          // 确保索引在有效范围内
          if (faultLevelIndex >= 0 && faultLevelIndex < this.faultLevel.length) {
            this.severityIndex = faultLevelIndex
          } else {
            this.severityIndex = -1 // 如果数据异常，保持未选择状态
          }
          console.info('设置故障等级索引:', this.severityIndex)
        }

        // 将故障图片字符串转换为数组，并添加完整URL
        if (data.faultImages) {
          const imagePaths = jsonToImages(data.faultImages)
          // 为图片路径添加完整URL
          this.form.uploadedImages = addImageBaseUrl(imagePaths, config.baseUrl)
          // 同时设置本地预览图片
          this.form.faultImages = [...this.form.uploadedImages]
        }
      }).catch(err => {
        console.error('获取工单数据失败:', err)
        uni.showToast({
          title: '获取工单数据失败',
          icon: 'none'
        })
      })
    },
    // 提交申请
    submitApplication() {
      // 验证表单
      if (!this.validateForm()) {
        return
      }
      const actionText = this.mode === 'edit' ? '更新' : '提交'
      uni.showModal({
        title: '确认' + actionText,
        content: `确定要${actionText}这个报修申请吗？`,
        success: (res) => {
          if (res.confirm) {
            this.addRepairOrderInfo()
          }
        }
      })
    },
    // 验证表单
    validateForm() {
      // 使用工具类验证必填字段
      const requiredFields = ['reporterName', 'reporterPhone', 'department', 'location', 'equipmentModel', 'faultDescription']
      const validation = validateRequiredFields(this.form, requiredFields)

      if (!validation.isValid) {
        uni.showToast({ title: validation.message, icon: 'none' })
        return false
      }

      // 验证故障等级
      if (this.severityIndex === -1) {
        uni.showToast({ title: '请选择故障等级', icon: 'none' })
        return false
      }

      // 如果选择了"其他"，需要填写说明
      if (this.severityIndex === 3 && !this.form.faultLevelDesc.trim()) {
        uni.showToast({ title: '请描述其他故障情况', icon: 'none' })
        return false
      }
      return true
    },
    chooseImages() {
      if (this.form.uploading) {
        uni.showToast({
          title: '正在上传中，请稍候',
          icon: 'none'
        })
        return
      }

      handleImageUpload({
        maxCount: 6,
        currentImages: this.form.faultImages || [],
        onSuccess: (successUrls) => {
          // 添加上传成功的图片
          this.form.uploadedImages = (this.form.uploadedImages || []).concat(successUrls)
          this.form.faultImages = (this.form.faultImages || []).concat(successUrls)
          this.form.uploading = false
        },
        onProgress: (completed, total) => {
          // 可以在这里处理进度更新
          console.log(`上传进度: ${completed}/${total}`)
        },
        onError: (error) => {
          console.error('图片上传错误:', error)
          this.form.uploading = false
        }
      })

      this.form.uploading = true
    },
    previewImage(current) {
      if (!this.form.faultImages || this.form.faultImages.length === 0) return
      uni.previewImage({
        current,
        urls: this.form.faultImages
      })
    },

    // 删除图片
    removeImage(index) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除这张图片吗？',
        success: (res) => {
          if (res.confirm) {
            // 从两个数组中同时删除
            this.form.faultImages.splice(index, 1)
            this.form.uploadedImages.splice(index, 1)

            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
          }
        }
      })
    },
    // 打开故障等级选择器
    openFaultLevelPicker() {
      this.showFaultLevelPicker = true
    },
    // 故障等级选择确认
    onFaultLevelConfirm(e) {
      console.info('选择的故障等级:', e)
      // e.indexs 是选中项的索引数组，e.indexs[0] 是第一列的索引
      if (e.indexs && e.indexs.length > 0) {
        this.severityIndex = e.indexs[0]
        console.info('故障等级变更为:', this.faultLevel[this.severityIndex])
        
        // 如果选择的不是"其他"（索引3），则清空其他故障等级描述
        if (this.severityIndex !== 3) {
          this.form.faultLevelDesc = ''
          console.info('已清空其他故障等级描述')
        }
      }
      this.showFaultLevelPicker = false
    },
    // 故障等级选择取消
    onFaultLevelCancel() {
      this.showFaultLevelPicker = false
    },
    // 故障等级选择器关闭（点击遮罩或其他方式关闭）
    onFaultLevelClose() {
      this.showFaultLevelPicker = false
    }
  }
}
</script>

<template>
  <view class="page">
    <view class="card">
      <view class="title">{{ mode === 'edit' ? '编辑报修申请' : '衣车报修申请' }}</view>
      <view class="subtitle">{{ mode === 'edit' ? '修改申请信息后点击更新' : '请填写以下信息，我们将尽快处理' }}</view>

      <view class="form">
        <view class="form-item">
          <text class="label required">报修人</text>
          <input v-model="form.reporterName" class="input" placeholder="请输入报修人姓名" placeholder-class="ph"/>
        </view>

        <view class="form-item">
          <text class="label required">联系电话</text>
          <input v-model="form.reporterPhone" class="input" placeholder="请输入手机号" placeholder-class="ph" type="number"/>
        </view>

        <view class="form-item">
          <text class="label required">部门/车间</text>
          <input v-model="form.department" class="input" placeholder="如：一车间缝制线A组" placeholder-class="ph"/>
        </view>

        <view class="form-item">
          <text class="label required">报修地点</text>
          <input v-model="form.location" class="input" placeholder="如：3号厂房 2楼 设备区" placeholder-class="ph"/>
        </view>

        <view class="form-item">
          <text class="label required">设备型号</text>
          <input v-model="form.equipmentModel" class="input" placeholder="如：JUKI DDL-8700" placeholder-class="ph"/>
        </view>

        <view class="form-item">
          <text class="label required">故障等级</text>
          <view class="picker-inner" @click="openFaultLevelPicker">
            {{ severityIndex === -1 ? '请选择故障等级' : faultLevel[severityIndex] }}
          </view>
        </view>

        <view v-if="severityIndex === 3" class="form-item">
          <text class="label required">其他情况说明</text>
          <input v-model="form.faultLevelDesc" class="input" placeholder="请描述具体的故障等级或特殊情况"
                 placeholder-class="ph"/>
        </view>

        <view class="form-item textarea-item">
          <text class="label required">故障描述</text>
          <textarea v-model="form.faultDescription" auto-height class="textarea" placeholder="请简要描述问题现象、发生时间等"
                    placeholder-class="ph"/>
        </view>

        <view class="form-item">
          <text class="label">现场图片</text>
          <view class="uploader">
            <view class="upload-box" @click="chooseImages" :class="{ 'uploading': form.uploading }">
              <uni-icons v-if="!form.uploading" color="#666" size="28" type="camera"></uni-icons>
              <uni-icons v-else color="#666" size="28" type="spinner-cycle" class="rotating"></uni-icons>
              <text class="upload-text">{{ form.uploading ? '上传中...' : '点击上传' }}</text>
            </view>

            <view v-if="form.faultImages && form.faultImages.length" class="thumbs">
              <view v-for="(img, idx) in form.faultImages" :key="idx" class="thumb">
                <image :src="img" mode="aspectFill" @click="previewImage(img)"/>
                <view class="thumb-delete" @click.stop="removeImage(idx)">
                  <uni-icons color="#fff" size="16" type="close"></uni-icons>
                </view>
              </view>
            </view>
            <text class="hint">支持上传问题现场、设备铭牌等图片，最多6张</text>
          </view>
        </view>

        <button class="submit-btn" @click="submitApplication">{{ mode === 'edit' ? '更新申请' : '提交申请' }}</button>
      </view>
    </view>

    <!-- 故障等级选择器 -->
    <u-picker
      :show="showFaultLevelPicker"
      :columns="faultLevelColumns"
      @confirm="onFaultLevelConfirm"
      @cancel="onFaultLevelCancel"
      @close="onFaultLevelClose"
      :closeOnClickOverlay="true"
      :immediateChange="true"
    ></u-picker>
  </view>
</template>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  background: #f6f7fb;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 16px;
  box-sizing: border-box;
}

.card {
  width: 100%;
  max-width: 720rpx;
  background: #fff;
  border-radius: 16rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.06);
  padding: 28rpx;
}

.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #222;
}

.subtitle {
  margin-top: 8rpx;
  font-size: 24rpx;
  color: #888;
}

.form {
  margin-top: 24rpx;
}

.form-item {
  margin-bottom: 24rpx;
}

.label {
  display: block;
  font-size: 26rpx;
  color: #333;
  margin-bottom: 12rpx;
  position: relative;
}

.label.required::before {
  content: '*';
  color: #ff4757;
  font-size: 28rpx;
  position: absolute;
  left: -16rpx;
  top: 0;
  font-weight: bold;
}

.input {
  width: 100%;
  height: 80rpx;
  border: 1px solid #eee;
  border-radius: 12rpx;
  padding: 0 24rpx;
  box-sizing: border-box;
  background: #fafafa;
}

.ph {
  color: #bbb;
}

.textarea-item .textarea {
  width: 100%;
  min-height: 160rpx;
  border: 1px solid #eee;
  border-radius: 12rpx;
  padding: 16rpx 24rpx;
  box-sizing: border-box;
  background: #fafafa;
}

.picker {
  width: 100%;
}

.picker-inner {
  height: 80rpx;
  display: flex;
  align-items: center;
  padding: 0 24rpx;
  border: 1px solid #eee;
  border-radius: 12rpx;
  background: #fafafa;
  color: #666;
  position: relative;
  cursor: pointer;
}

.picker-inner::after {
  content: '';
  position: absolute;
  right: 24rpx;
  top: 50%;
  transform: translateY(-50%) rotate(45deg);
  width: 12rpx;
  height: 12rpx;
  border-right: 2px solid #999;
  border-bottom: 2px solid #999;
}

.uploader .upload-box {
  height: 160rpx;
  border: 1px dashed #ddd;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  color: #666;
  transition: all 0.3s ease;
}

.uploader .upload-box.uploading {
  background: #f0f0f0;
  pointer-events: none;
}

.rotating {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.uploader .hint {
  display: block;
  margin-top: 8rpx;
  font-size: 22rpx;
  color: #999;
}

.thumbs {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-top: 16rpx;
}

.thumb {
  position: relative;
  width: 200rpx;
  height: 200rpx;
  border-radius: 12rpx;
  overflow: hidden;
}

.thumb image {
  width: 100%;
  height: 100%;
}

.thumb-delete {
  position: absolute;
  top: 8rpx;
  right: 8rpx;
  width: 32rpx;
  height: 32rpx;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.submit-btn {
  margin-top: 32rpx;
  height: 88rpx;
  line-height: 88rpx;
  background: #007AFF;
  color: #fff;
  border-radius: 12rpx;
  font-size: 30rpx;
  border: none;
  width: 100%;
  transition: background-color 0.3s ease;

  &:active {
    background: #0056CC;
  }
}
</style>
