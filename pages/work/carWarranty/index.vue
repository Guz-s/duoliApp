<script>
import { listRepairData } from "@/api/repair/repair";

export default {
  name: "CarWarrantyForm",
  data() {
    return {
      mode: 'add', // add: 新增模式, edit: 编辑模式
      editId: null,
      form: {
        reporter: '',
        phone: '',
        department: '',
        location: '',
        model: '',
        severityIndex: -1,
        description: '',
        images: [],
        otherSeverity: ''
      },
      severityOptions: ['紧急','较急','一般','其他']
    }
  },
  onLoad(options) {
    this.getRepairFun()
    // 检查是否为编辑模式
    if (options.mode === 'edit' && options.id) {
      this.mode = 'edit'
      this.editId = options.id
      this.loadEditData()
    }
  },
  methods: {

    /*
      * 获取工单数据列表
      */
    getRepairFun() {
      listRepairData().then(res => {
        console.info(res)
      })
    },

    // 加载编辑数据
    loadEditData() {
      try {
        const orderData = uni.getStorageSync('editingOrder')
        if (orderData) {
          this.form.reporter = orderData.reporter || ''
          this.form.phone = orderData.phone || ''
          this.form.department = orderData.department || ''
          this.form.location = orderData.location || ''
          this.form.model = orderData.equipmentModel || ''
          this.form.description = orderData.faultDescription || ''
          this.form.images = orderData.images || []
          this.form.otherSeverity = orderData.otherSeverity || ''

          // 设置故障等级
          const levelIndex = this.severityOptions.indexOf(orderData.faultLevel)
          this.form.severityIndex = levelIndex >= 0 ? levelIndex : -1
        }
      } catch (e) {
        console.error('加载编辑数据失败:', e)
      }
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
            this.saveApplication()
          }
        }
      })
    },
    // 保存申请
    saveApplication() {
      const actionText = this.mode === 'edit' ? '更新成功' : '提交成功'

      // 这里可以调用API保存数据
      // 如果是编辑模式，可以更新本地存储的数据
      if (this.mode === 'edit') {
        // 更新存储的编辑数据
        const orderData = uni.getStorageSync('editingOrder')
        if (orderData) {
          orderData.reporter = this.form.reporter
          orderData.phone = this.form.phone
          orderData.department = this.form.department
          orderData.location = this.form.location
          orderData.equipmentModel = this.form.model
          orderData.faultDescription = this.form.description
          orderData.images = this.form.images
          orderData.otherSeverity = this.form.otherSeverity
          orderData.faultLevel = this.form.severityIndex >= 0 ? this.severityOptions[this.form.severityIndex] : ''

          uni.setStorageSync('editingOrder', orderData)
        }
      }

      uni.showToast({
        title: actionText,
        icon: 'success',
        duration: 2000,
        complete: () => {
          setTimeout(() => {
            // 返回上一页
            uni.navigateBack()
          }, 1500)
        }
      })
    },
    // 验证表单
    validateForm() {
      if (!this.form.reporter.trim()) {
        uni.showToast({ title: '请输入报修人', icon: 'none' })
        return false
      }
      if (!this.form.phone.trim()) {
        uni.showToast({ title: '请输入联系电话', icon: 'none' })
        return false
      }
      if (!this.form.department.trim()) {
        uni.showToast({ title: '请输入部门/车间', icon: 'none' })
        return false
      }
      if (!this.form.location.trim()) {
        uni.showToast({ title: '请输入报修地点', icon: 'none' })
        return false
      }
      if (!this.form.model.trim()) {
        uni.showToast({ title: '请输入设备型号', icon: 'none' })
        return false
      }
      if (this.form.severityIndex === -1) {
        uni.showToast({ title: '请选择故障等级', icon: 'none' })
        return false
      }
      if (this.form.severityIndex === 3 && !this.form.otherSeverity.trim()) {
        uni.showToast({ title: '请描述其他故障情况', icon: 'none' })
        return false
      }
      if (!this.form.description.trim()) {
        uni.showToast({ title: '请输入故障描述', icon: 'none' })
        return false
      }
      return true
    },
    chooseImages() {
      uni.chooseImage({
        count: 6,
        sizeType: ['compressed'],
        sourceType: ['album','camera'],
        success: (res) => {
          const paths = res.tempFilePaths || []
          // 仅前端预览，直接记录本地路径
          this.form.images = (this.form.images || []).concat(paths).slice(0, 6)
        }
      })
    },
    previewImage(current) {
      if (!this.form.images || this.form.images.length === 0) return
      uni.previewImage({
        current,
        urls: this.form.images
      })
    },
    onSeverityChange(e) {
      const idx = (e && e.detail && typeof e.detail.value !== 'undefined') ? Number(e.detail.value) : -1
      this.form.severityIndex = idx
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
          <text class="label">报修人</text>
          <input class="input" v-model="form.reporter" placeholder="请输入报修人姓名" placeholder-class="ph" />
        </view>

        <view class="form-item">
          <text class="label">联系电话</text>
          <input class="input" v-model="form.phone" type="number" placeholder="请输入手机号" placeholder-class="ph" />
        </view>

        <view class="form-item">
          <text class="label">部门/车间</text>
          <input class="input" v-model="form.department" placeholder="如：一车间缝制线A组" placeholder-class="ph" />
        </view>

        <view class="form-item">
          <text class="label">报修地点</text>
          <input class="input" v-model="form.location" placeholder="如：3号厂房 2楼 设备区" placeholder-class="ph" />
        </view>

        <view class="form-item">
          <text class="label">设备型号</text>
          <input class="input" v-model="form.model" placeholder="如：JUKI DDL-8700" placeholder-class="ph" />
        </view>

        <view class="form-item">
          <text class="label">故障等级</text>
          <picker class="picker" :range="severityOptions" :value="form.severityIndex" @change="onSeverityChange">
            <view class="picker-inner">
              {{ form.severityIndex === -1 ? '请选择' : severityOptions[form.severityIndex] }}
            </view>
          </picker>
        </view>

        <view class="form-item" v-if="form.severityIndex === 3">
          <text class="label">其他情况说明</text>
          <input class="input" v-model="form.otherSeverity" placeholder="请描述具体的故障等级或特殊情况" placeholder-class="ph" />
        </view>

        <view class="form-item textarea-item">
          <text class="label">故障描述</text>
          <textarea class="textarea" v-model="form.description" auto-height placeholder="请简要描述问题现象、发生时间等" placeholder-class="ph" />
        </view>

        <view class="form-item">
          <text class="label">现场图片</text>
          <view class="uploader">
            <view class="upload-box" @click="chooseImages">
              <uni-icons type="camera" size="28" color="#666"></uni-icons>
              <text class="upload-text">点击上传</text>
            </view>

            <view class="thumbs" v-if="form.images && form.images.length">
              <view class="thumb" v-for="(img, idx) in form.images" :key="idx" @click="previewImage(img)">
                <image :src="img" mode="aspectFill" />
              </view>
            </view>

            <text class="hint">支持上传问题现场、设备铭牌等图片（本地预览，不上传）</text>
          </view>
        </view>

        <button class="submit-btn" @click="submitApplication">{{ mode === 'edit' ? '更新申请' : '提交申请' }}</button>
      </view>
    </view>
  </view>
</template>

<style scoped lang="scss">
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
  box-shadow: 0 8rpx 24rpx rgba(0,0,0,0.06);
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
  width: 200rpx;
  height: 200rpx;
  border-radius: 12rpx;
  overflow: hidden;
}

.thumb image {
  width: 100%;
  height: 100%;
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
