<template>
  <view class="detail-container">
    <!-- 加载状态 -->
    <view class="loading-container" v-if="loading">
      <uni-icons type="spinner-cycle" size="40" color="#007AFF" class="rotating"></uni-icons>
      <text class="loading-text">加载中...</text>
    </view>

    <view class="order-card" v-else>
      <!-- 工单头部 -->
      <view class="order-header">
        <view class="order-id">工单 #{{ order.orderNo || order.orderId }}</view>
        <view class="order-status" :class="'status-' + order.status">{{ getStatusText(order.status) }}</view>
      </view>

      <!-- 基本信息 -->
      <view class="info-section">
        <view class="section-title">基本信息</view>
        <view class="info-grid">
          <view class="info-item">
            <text class="label">报修人</text>
            <text class="value">{{ order.reporterName }}</text>
          </view>
          <view class="info-item">
            <text class="label">联系电话</text>
            <text class="value">{{ order.reporterPhone }}</text>
          </view>
          <view class="info-item">
            <text class="label">部门/车间</text>
            <text class="value">{{ order.department }}</text>
          </view>
          <view class="info-item">
            <text class="label">报修地点</text>
            <text class="value">{{ order.location }}</text>
          </view>
          <view class="info-item">
            <text class="label">设备型号</text>
            <text class="value">{{ order.equipmentModel }}</text>
          </view>
          <view class="info-item">
            <text class="label">故障等级</text>
            <text class="value" :style="{ color: getFaultLevelColor(order.faultLevel) }">{{ getFaultLevelText(order.faultLevel) }}</text>
          </view>
          <!-- 当故障等级为其他时，显示其他故障描述 -->
          <view class="info-item" v-if="order.faultLevel == 3 && order.faultLevelDesc">
            <text class="label">其他描述</text>
            <text class="value" style="color: #FF9500; font-style: italic;">{{ order.faultLevelDesc }}</text>
          </view>
        </view>
      </view>

      <!-- 故障描述 -->
      <view class="desc-section">
        <view class="section-title">故障描述</view>
        <text class="desc-text">{{ order.faultDescription }}</text>
      </view>

      <!-- 现场图片 -->
      <view class="images-section" v-if="getFaultImages().length > 0">
        <view class="section-title">现场图片</view>
        <view class="images-grid">
          <image
            v-for="(img, index) in getFaultImages()"
            :key="index"
            :src="img"
            class="detail-img"
            @click="previewImages(getFaultImages(), index)"
          />
        </view>
      </view>

      <!-- 创建时间 -->
      <view class="time-section" v-if="order.createTime">
        <view class="section-title">工单信息</view>
        <view class="info-grid">
          <view class="info-item">
            <text class="label">创建时间</text>
            <text class="value">{{ order.createTime }}</text>
          </view>
          <view class="info-item" v-if="order.updateTime">
            <text class="label">更新时间</text>
            <text class="value">{{ order.updateTime }}</text>
          </view>
          <!--TODO 后台日期控件改成时间控件。-->
          <!--<view class="info-item" v-if="order.repairStartTime">-->
          <!--  <text class="label">开始维修时间</text>-->
          <!--  <text class="value">{{ order.repairStartTime }}</text>-->
          <!--</view>-->
          <!--<view class="info-item" v-if="order.repairEndTime">-->
          <!--  <text class="label">完成维修时间</text>-->
          <!--  <text class="value">{{ order.repairEndTime }}</text>-->
          <!--</view>-->
          <!--<view class="info-item" v-if="order.repairDuration !== null && order.repairDuration !== undefined">-->
          <!--  <text class="label">维修用时</text>-->
          <!--  <text class="value">{{ formatDuration(order.repairDuration) }}</text>-->
          <!--</view>-->
        </view>
      </view>

      <!-- 维修结果 -->
      <view class="repair-result-section" v-if="order.status == 2 && order.repairResult">
        <view class="section-title">维修结果</view>
        
        <!-- 维修结果描述 -->
        <view class="result-desc">
          <text class="desc-text">{{ order.repairResult }}</text>
        </view>
        
        <!-- 维修结果图片 -->
        <view class="result-images" v-if="getRepairResultImages().length > 0">
          <view class="images-title">维修结果图片</view>
          <view class="images-grid">
            <image
              v-for="(img, index) in getRepairResultImages()"
              :key="index"
              :src="img"
              class="result-img"
              @click="previewImages(getRepairResultImages(), index)"
            />
          </view>
        </view>
        
        <!-- 维修人员信息 -->
        <view class="maintainer-info" v-if="order.mainterName || order.mainterPhone">
          <view class="info-grid">
            <view class="info-item" v-if="order.mainterName">
              <text class="label">维修人员</text>
              <text class="value">{{ order.mainterName }}</text>
            </view>
            <view class="info-item" v-if="order.mainterPhone">
              <text class="label">联系电话</text>
              <text class="value">{{ order.mainterPhone }}</text>
            </view>
            <view class="info-item" v-if="order.repairEndTime">
              <text class="label">完成时间</text>
              <text class="value">{{ order.repairEndTime }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 操作按钮 -->
      <view class="action-section" v-if="!fromMyOrders">
        <!-- 待处理状态：开始维修 + 无法处理 -->
        <view class="btn-group" v-if="order.status == 0">
          <button
            class="action-btn primary"
            :class="{ 'disabled': updating }"
            @click="startRepair"
            :disabled="updating"
          >
            {{ updating ? '更新中...' : '开始维修' }}
          </button>
          <button
            class="action-btn cancel"
            :class="{ 'disabled': updating }"
            @click="cancelOrder"
            :disabled="updating"
          >
            {{ updating ? '更新中...' : '无法处理' }}
          </button>
        </view>
        
        <!-- 处理中状态：完成维修 + 取消维修 -->
        <view class="btn-group" v-if="order.status == 1">
          <button
            class="action-btn success"
            :class="{ 'disabled': updating }"
            @click="completeRepair"
            :disabled="updating"
          >
            {{ updating ? '更新中...' : '完成维修' }}
          </button>
          <button
            class="action-btn cancel"
            :class="{ 'disabled': updating }"
            @click="cancelOrder"
            :disabled="updating"
          >
            {{ updating ? '更新中...' : '取消维修' }}
          </button>
        </view>
        
        <!-- 已完成或已取消状态：返回按钮 -->
        <button
          class="action-btn default"
          v-if="order.status == 2 || order.status == 3"
          @click="goBack"
        >
          返回列表
        </button>
      </view>
      
      <!-- 从个人工单页面进入时显示返回按钮 -->
      <view class="action-section" v-if="fromMyOrders">
        <button class="action-btn default" @click="goBack">
          返回我的工单
        </button>
      </view>
    </view>

    <!-- 维修结果填写模态框 -->
    <view class="modal-overlay" v-if="showRepairResultModal" @click="cancelRepairResult">
      <view class="repair-result-modal" @click.stop>
        <view class="modal-header">
          <text class="modal-title">填写维修结果</text>
          <uni-icons type="close" size="20" color="#999" @click="cancelRepairResult" class="close-btn"></uni-icons>
        </view>

        <view class="modal-content">
          <!-- 维修结果描述 -->
          <view class="form-item">
            <text class="label required">维修结果描述</text>
            <textarea
              v-model="repairResultForm.description"
              class="textarea"
              placeholder="请详细描述维修过程、更换的配件、解决的问题等"
              placeholder-class="ph"
              :maxlength="500"
              show-confirm-bar
              auto-height
            />
            <text class="char-count">{{ repairResultForm.description.length }}/500</text>
          </view>

          <!-- 维修结果图片 -->
          <view class="form-item">
            <text class="label">维修结果图片（可选）</text>
            <view class="uploader">
              <view class="upload-box" @click="chooseRepairResultImages" :class="{ 'uploading': uploading }">
                <uni-icons v-if="!uploading" color="#666" size="28" type="camera"></uni-icons>
                <uni-icons v-else color="#666" size="28" type="spinner-cycle" class="rotating"></uni-icons>
                <text class="upload-text">{{ uploading ? '上传中...' : '点击上传' }}</text>
              </view>

              <view v-if="repairResultForm.images && repairResultForm.images.length" class="thumbs">
                <view v-for="(img, idx) in repairResultForm.images" :key="idx" class="thumb">
                  <image :src="img" mode="aspectFill" @click="previewRepairResultImage(img)"/>
                  <view class="thumb-delete" @click.stop="removeRepairResultImage(idx)">
                    <uni-icons color="#fff" size="16" type="close"></uni-icons>
                  </view>
                </view>
              </view>
              <text class="hint">支持上传维修前后对比、更换配件等图片，最多6张</text>
            </view>
          </view>
        </view>

        <view class="modal-footer">
          <button class="cancel-btn" @click="cancelRepairResult">取消</button>
          <button
            class="submit-btn"
            :class="{ 'disabled': updating || !repairResultForm.description.trim() }"
            :disabled="updating || !repairResultForm.description.trim()"
            @click="submitRepairResult"
          >
            {{ updating ? '提交中...' : '完成维修' }}
          </button>
        </view>
      </view>
    </view>

    <!-- 取消工单填写模态框 -->
    <view class="modal-overlay" v-if="showCancelModal" @click="cancelCancelOrder">
      <view class="cancel-modal" @click.stop>
        <view class="modal-header">
          <text class="modal-title">{{ order.status == 0 ? '填写无法处理原因' : '填写取消维修原因' }}</text>
          <uni-icons type="close" size="20" color="#999" @click="cancelCancelOrder" class="close-btn"></uni-icons>
        </view>

        <view class="modal-content">
          <!-- 取消理由描述 -->
          <view class="form-item">
            <text class="label required">{{ order.status == 0 ? '无法处理原因' : '取消维修原因' }}</text>
            <textarea
              v-model="cancelForm.reason"
              class="textarea"
              :placeholder="order.status == 0 ? '请详细说明无法处理的原因，如：缺少配件、超出处理范围、需要专业技术支持等' : '请详细说明取消维修的原因，如：设备报废、配件无法采购、问题复杂度超出预期等'"
              placeholder-class="ph"
              :maxlength="500"
              show-confirm-bar
              auto-height
            />
            <text class="char-count">{{ cancelForm.reason.length }}/500</text>
          </view>

          <!-- 相关图片 -->
          <view class="form-item">
            <text class="label">相关图片（可选）</text>
            <view class="uploader">
              <view class="upload-box" @click="chooseCancelImages" :class="{ 'uploading': uploading }">
                <uni-icons v-if="!uploading" color="#666" size="28" type="camera"></uni-icons>
                <uni-icons v-else color="#666" size="28" type="spinner-cycle" class="rotating"></uni-icons>
                <text class="upload-text">{{ uploading ? '上传中...' : '点击上传' }}</text>
              </view>

              <view v-if="cancelForm.images && cancelForm.images.length" class="thumbs">
                <view v-for="(img, idx) in cancelForm.images" :key="idx" class="thumb">
                  <image :src="img" mode="aspectFill" @click="previewCancelImage(img)"/>
                  <view class="thumb-delete" @click.stop="removeCancelImage(idx)">
                    <uni-icons color="#fff" size="16" type="close"></uni-icons>
                  </view>
                </view>
              </view>
              <text class="hint">支持上传相关说明图片，如现场情况、设备状态等，最多6张</text>
            </view>
          </view>
        </view>

        <view class="modal-footer">
          <button class="cancel-btn" @click="cancelCancelOrder">取消</button>
          <button
            class="submit-btn cancel-submit"
            :class="{ 'disabled': updating || !cancelForm.reason.trim() }"
            :disabled="updating || !cancelForm.reason.trim()"
            @click="submitCancelOrder"
          >
            {{ updating ? '提交中...' : (order.status == 0 ? '确认无法处理' : '确认取消维修') }}
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getRepairByCode, updateRepairData } from "@/api/repair/repair";
import { processFaultImages, handleImageUpload } from "@/utils/dataConverter";
import { getUserProfile } from "@/api/system/user";
import config from '@/config';

export default {
  name: "OrderDetail",
  data() {
    return {
      orderId: '',
      fromMyOrders: false, // 是否从个人工单页面进入
      loading: true,
      updating: false, // 添加更新状态标志
      showRepairResultModal: false, // 维修结果模态框显示状态
      showCancelModal: false, // 取消工单模态框显示状态
      repairResultForm: {
        description: '', // 维修结果描述
        images: [], // 维修结果图片
        uploadedImages: [] // 上传成功的图片URL
      },
      cancelForm: {
        reason: '', // 取消理由
        images: [], // 取消相关图片
        uploadedImages: [] // 上传成功的图片URL
      },
      uploading: false, // 图片上传状态
      order: {
        orderId: '',
        orderNo: '',
        reporterName: '',
        reporterPhone: '',
        department: '',
        location: '',
        equipmentModel: '',
        faultLevel: '',
        faultLevelDesc: '',
        faultDescription: '',
        faultImages: '',
        status: '',
        createTime: '',
        updateTime: '',
        repairStartTime: '',
        repairEndTime: '',
        repairDuration: null,
        repairResult: '', // 维修结果描述
        repairResultImages: '', // 维修结果图片
        mainterName: '', // 维修人员姓名
        mainterPhone: '' // 维修人员电话
      }
    }
  },
  onLoad(options) {
    if (options.id) {
      this.orderId = options.id
      // 检查是否从个人工单页面进入
      this.fromMyOrders = options.from === 'myOrders'
      this.loadOrderDetail()
    }
  },
  methods: {
    loadOrderDetail() {
      console.log('加载工单详情:', this.orderId)
      this.loading = true

      getRepairByCode(this.orderId).then(res => {
        console.log('获取工单详情成功:', res)
        const data = res.data || res

        // 映射API数据到order对象
        this.order = {
          orderId: data.orderId || '',
          orderNo: data.orderNo || '',
          reporterName: data.reporterName || '',
          reporterPhone: data.reporterPhone || '',
          department: data.department || '',
          location: data.location || '',
          equipmentModel: data.equipmentModel || '',
          faultLevel: data.faultLevel || '',
          faultLevelDesc: data.faultLevelDesc || '',
          faultDescription: data.faultDescription || '',
          faultImages: data.faultImages || '',
          status: data.status || '',
          createTime: data.createTime || '',
          updateTime: data.updateTime || '',
          repairStartTime: data.repairStartTime || '',
          repairEndTime: data.repairEndTime || '',
          repairDuration: data.repairDuration || null,
          repairResult: data.repairResult || '', // 维修结果描述
          repairResultImages: data.repairResultImages || '', // 维修结果图片
          mainterName: data.mainterName || '', // 维修人员姓名
          mainterPhone: data.mainterPhone || '' // 维修人员电话
        }

        this.loading = false
      }).catch(err => {
        console.error('获取工单详情失败:', err)
        this.loading = false
        uni.showToast({
          title: '获取工单详情失败',
          icon: 'none'
        })
      })
    },
    startRepair() {
      uni.showModal({
        title: '开始维修',
        content: `确定开始维修工单 #${this.order.orderNo || this.order.orderId} 吗？`,
        success: (res) => {
          if (res.confirm) {
            // 防止重复点击
            if (this.updating) return
            this.updating = true

            // 显示加载状态
            uni.showLoading({
              title: '更新中...'
            })

            // 调用API更新状态为处理中（状态1）
            const now = new Date()
            const repairStartTime = now.getFullYear() + '-' +
              String(now.getMonth() + 1).padStart(2, '0') + '-' +
              String(now.getDate()).padStart(2, '0') + ' ' +
              String(now.getHours()).padStart(2, '0') + ':' +
              String(now.getMinutes()).padStart(2, '0') + ':' +
              String(now.getSeconds()).padStart(2, '0')

            const updateData = {
              orderId: this.order.orderId,
              status: 1,
              repairStartTime: repairStartTime
            }

            console.log('开始维修时间:', repairStartTime)

            updateRepairData(updateData).then(response => {
              console.log('开始维修成功:', response)
              // 更新本地状态
              this.order.status = 1
              this.order.repairStartTime = repairStartTime
              this.updating = false
              uni.hideLoading()
              uni.showToast({
                title: '已开始维修',
                icon: 'success'
              })
              // 重新加载数据以确保状态同步
              setTimeout(() => {
                this.loadOrderDetail()
              }, 1000)
            }).catch(error => {
              console.error('开始维修失败:', error)
              this.updating = false
              uni.hideLoading()
              uni.showToast({
                title: '操作失败，请重试',
                icon: 'none'
              })
            })
          }
        }
      })
    },
    completeRepair() {
      // 显示维修结果填写模态框
      this.showRepairResultModal = true
    },
    
    // 取消工单
    cancelOrder() {
      // 显示取消工单填写模态框
      this.showCancelModal = true
    },

    // 提交维修结果
    submitRepairResult() {
      // 验证维修结果描述是否填写
      if (!this.repairResultForm.description.trim()) {
        uni.showToast({
          title: '请填写维修结果描述',
          icon: 'none'
        })
        return
      }

      // 防止重复提交
      if (this.updating) return
      this.updating = true

      // 显示加载状态
      uni.showLoading({
        title: '提交中...'
      })

      // 首先获取当前用户信息
      getUserProfile().then(userRes => {
        const userData = userRes.data || userRes
        
        // 准备提交数据
        const now = new Date()
        const repairEndTime = now.getFullYear() + '-' +
          String(now.getMonth() + 1).padStart(2, '0') + '-' +
          String(now.getDate()).padStart(2, '0') + ' ' +
          String(now.getHours()).padStart(2, '0') + ':' +
          String(now.getMinutes()).padStart(2, '0') + ':' +
          String(now.getSeconds()).padStart(2, '0')

        const updateData = {
          orderId: this.order.orderId,
          status: 2,
          repairResult: this.repairResultForm.description,
          repairEndTime: repairEndTime,
          assignedTo: userData.userId,
          mainterName: userData.nickName || '', // 维修人员姓名
          mainterPhone: userData.phonenumber || '' // 维修人员电话
        }

        // 如果有开始时间，计算维修用时（秒）
        if (this.order.repairStartTime) {
          const startTime = new Date(this.order.repairStartTime)
          const endTime = new Date(repairEndTime)
          const durationMs = endTime.getTime() - startTime.getTime()
          const durationSeconds = Math.round(durationMs / 1000) // 转换为秒并四舍五入
          updateData.repairDuration = durationSeconds
          console.log(`维修用时: ${durationSeconds} 秒`)
        }

        // 如果有上传图片，添加到提交数据中
        if (this.repairResultForm.uploadedImages && this.repairResultForm.uploadedImages.length > 0) {
          // 将完整URL转换为相对路径后再提交
          const relativePaths = this.repairResultForm.uploadedImages.map(url => {
            if (url && url.startsWith(config.baseUrl)) {
              // 如果是完整URL，提取相对路径
              return url.replace(config.baseUrl, '')
            }
            return url
          })
          updateData.repairResultImages = relativePaths.join(',')
          console.log('维修结果图片相对路径:', relativePaths)
        }

        console.log('提交的维修结果数据:', updateData)
        return updateRepairData(updateData)
      }).then(response => {
        console.log('完成维修成功:', response)
        // 更新本地状态
        this.order.status = 2
        this.updating = false
        this.showRepairResultModal = false
        // 清空表单
        this.resetRepairResultForm()
        uni.hideLoading()
        uni.showToast({
          title: '维修完成',
          icon: 'success'
        })
        // 重新加载数据以确保状态同步
        setTimeout(() => {
          this.loadOrderDetail()
        }, 1000)
      }).catch(error => {
        console.error('完成维修失败:', error)
        this.updating = false
        uni.hideLoading()
        uni.showToast({
          title: '提交失败，请重试',
          icon: 'none'
        })
      })
    },

    // 取消维修结果填写
    cancelRepairResult() {
      this.showRepairResultModal = false
      this.resetRepairResultForm()
    },

    // 重置维修结果表单
    resetRepairResultForm() {
      this.repairResultForm = {
        description: '',
        images: [],
        uploadedImages: []
      }
    },

    // 提交取消工单
    submitCancelOrder() {
      // 验证取消理由是否填写
      if (!this.cancelForm.reason.trim()) {
        uni.showToast({
          title: '请填写取消理由',
          icon: 'none'
        })
        return
      }

      // 防止重复提交
      if (this.updating) return
      this.updating = true

      // 显示加载状态
      uni.showLoading({
        title: '提交中...'
      })

      // 准备提交数据
      const statusText = this.order.status == 0 ? '无法处理' : '取消维修'
      const updateData = {
        orderId: this.order.orderId,
        status: 3,
        repairResult: this.cancelForm.reason // 将取消理由存储在维修结果字段
      }

      // 如果有上传图片，添加到提交数据中
      if (this.cancelForm.uploadedImages && this.cancelForm.uploadedImages.length > 0) {
        // 将完整URL转换为相对路径后再提交
        const relativePaths = this.cancelForm.uploadedImages.map(url => {
          if (url && url.startsWith(config.baseUrl)) {
            // 如果是完整URL，提取相对路径
            return url.replace(config.baseUrl, '')
          }
          return url
        })
        updateData.repairResultImages = relativePaths.join(',') // 将取消相关图片存储在维修结果图片字段
        console.log('取消相关图片相对路径:', relativePaths)
      }

      console.log('提交的取消工单数据:', updateData)

      updateRepairData(updateData).then(response => {
        console.log('取消工单成功:', response)
        // 更新本地状态
        this.order.status = 3
        this.updating = false
        this.showCancelModal = false
        // 清空表单
        this.resetCancelForm()
        uni.hideLoading()
        uni.showToast({
          title: statusText + '成功',
          icon: 'success'
        })
        // 重新加载数据以确保状态同步
        setTimeout(() => {
          this.loadOrderDetail()
        }, 1000)
      }).catch(error => {
        console.error('取消工单失败:', error)
        this.updating = false
        uni.hideLoading()
        uni.showToast({
          title: '提交失败，请重试',
          icon: 'none'
        })
      })
    },

    // 取消取消工单填写
    cancelCancelOrder() {
      this.showCancelModal = false
      this.resetCancelForm()
    },

    // 重置取消工单表单
    resetCancelForm() {
      this.cancelForm = {
        reason: '',
        images: [],
        uploadedImages: []
      }
    },

    // 选择取消相关图片
    chooseCancelImages() {
      if (this.uploading) {
        uni.showToast({
          title: '正在上传中，请稍候',
          icon: 'none'
        })
        return
      }

      handleImageUpload({
        maxCount: 6,
        currentImages: this.cancelForm.images || [],
        onSuccess: (successUrls) => {
          // 添加上传成功的图片
          this.cancelForm.uploadedImages = (this.cancelForm.uploadedImages || []).concat(successUrls)
          this.cancelForm.images = (this.cancelForm.images || []).concat(successUrls)
          this.uploading = false
        },
        onProgress: (completed, total) => {
          console.log(`上传进度: ${completed}/${total}`)
        },
        onError: (error) => {
          console.error('图片上传错误:', error)
          this.uploading = false
        }
      })

      this.uploading = true
    },

    // 预览取消相关图片
    previewCancelImage(current) {
      if (!this.cancelForm.images || this.cancelForm.images.length === 0) return
      uni.previewImage({
        current,
        urls: this.cancelForm.images
      })
    },

    // 删除取消相关图片
    removeCancelImage(index) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除这张图片吗？',
        success: (res) => {
          if (res.confirm) {
            // 从两个数组中同时删除
            this.cancelForm.images.splice(index, 1)
            this.cancelForm.uploadedImages.splice(index, 1)

            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
          }
        }
      })
    },

    // 选择维修结果图片
    chooseRepairResultImages() {
      if (this.uploading) {
        uni.showToast({
          title: '正在上传中，请稍候',
          icon: 'none'
        })
        return
      }

      handleImageUpload({
        maxCount: 6,
        currentImages: this.repairResultForm.images || [],
        onSuccess: (successUrls) => {
          // 添加上传成功的图片
          this.repairResultForm.uploadedImages = (this.repairResultForm.uploadedImages || []).concat(successUrls)
          this.repairResultForm.images = (this.repairResultForm.images || []).concat(successUrls)
          this.uploading = false
        },
        onProgress: (completed, total) => {
          console.log(`上传进度: ${completed}/${total}`)
        },
        onError: (error) => {
          console.error('图片上传错误:', error)
          this.uploading = false
        }
      })

      this.uploading = true
    },

    // 预览维修结果图片
    previewRepairResultImage(current) {
      if (!this.repairResultForm.images || this.repairResultForm.images.length === 0) return
      uni.previewImage({
        current,
        urls: this.repairResultForm.images
      })
    },

    // 删除维修结果图片
    removeRepairResultImage(index) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除这张图片吗？',
        success: (res) => {
          if (res.confirm) {
            // 从两个数组中同时删除
            this.repairResultForm.images.splice(index, 1)
            this.repairResultForm.uploadedImages.splice(index, 1)

            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
          }
        }
      })
    },
    previewImages(images, current) {
      uni.previewImage({
        current,
        urls: images
      })
    },
    goBack() {
      uni.navigateBack()
    },
    getStatusText(status) {
      // 支持数字状态和字符串状态
      const statusMap = {
        // 数字状态
        0: '待处理',
        1: '处理中',
        2: '已完成',
        3: '已取消',
        // 字符串状态
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成',
        'cancelled': '已取消'
      }
      return statusMap[status] || '未知'
    },

    // 获取故障等级文本
    getFaultLevelText(level) {
      const textMap = {
        0: '严重',
        1: '较急',
        2: '一般',
        3: '其他'
      }
      return textMap[level] || '未知'
    },

    // 获取故障等级颜色
    getFaultLevelColor(level) {
      const colorMap = {
        0: '#FF3B30', // 严重 - 红色
        1: '#FF9500', // 较急 - 橙色
        2: '#34C759', // 一般 - 绿色
        3: '#8E8E93'  // 其他 - 灰色
      }
      return colorMap[level] || '#666'
    },

    // 处理故障图片数据
    getFaultImages() {
      return processFaultImages(this.order.faultImages)
    },

    // 处理维修结果图片数据
    getRepairResultImages() {
      return processFaultImages(this.order.repairResultImages)
    },

    // 格式化维修用时显示
    formatDuration(seconds) {
      if (seconds === null || seconds === undefined || seconds < 0) {
        return '未知'
      }

      if (seconds < 60) {
        return `${seconds} 秒`
      } else if (seconds < 3600) {
        const minutes = Math.floor(seconds / 60)
        const remainingSeconds = seconds % 60
        if (remainingSeconds === 0) {
          return `${minutes} 分钟`
        } else {
          return `${minutes} 分钟 ${remainingSeconds} 秒`
        }
      } else {
        const hours = Math.floor(seconds / 3600)
        const remainingMinutes = Math.floor((seconds % 3600) / 60)
        const remainingSeconds = seconds % 60

        let result = `${hours} 小时`
        if (remainingMinutes > 0) {
          result += ` ${remainingMinutes} 分钟`
        }
        if (remainingSeconds > 0) {
          result += ` ${remainingSeconds} 秒`
        }
        return result
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 20rpx;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400rpx;

  .loading-text {
    margin-top: 20rpx;
    font-size: 28rpx;
    color: #666;
  }
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

.order-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
  padding-bottom: 24rpx;
  border-bottom: 1px solid #f0f0f0;

  .order-id {
    font-size: 36rpx;
    font-weight: 700;
    color: #333;
  }

  .order-status {
    padding: 12rpx 24rpx;
    border-radius: 24rpx;
    font-size: 24rpx;
    color: #fff;
    font-weight: 600;

    // 数字状态样式
    &.status-0 {
      background: #FF9500; // 待处理 - 橙色
    }

    &.status-1 {
      background: #007AFF; // 处理中 - 蓝色
    }

    &.status-2 {
      background: #34C759; // 已完成 - 绿色
    }

    &.status-3 {
      background: #8E8E93; // 已取消 - 灰色
    }

    // 兼容字符串状态
    &.pending {
      background: #FF9500;
    }

    &.processing {
      background: #007AFF;
    }

    &.completed {
      background: #34C759;
    }

    &.cancelled {
      background: #8E8E93;
    }
  }
}

.info-section, .desc-section, .images-section, .time-section, .repair-result-section {
  margin-bottom: 32rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 20rpx;
    padding-bottom: 12rpx;
    border-bottom: 2rpx solid #f0f0f0;
  }
}

// 维修结果区域特殊样式
.repair-result-section {
  .result-desc {
    margin-bottom: 24rpx;
    
    .desc-text {
      font-size: 28rpx;
      color: #333;
      line-height: 1.6;
      background: #f8f9fa;
      padding: 20rpx;
      border-radius: 12rpx;
      border-left: 4rpx solid #34C759;
    }
  }
  
  .result-images {
    margin-bottom: 24rpx;
    
    .images-title {
      font-size: 26rpx;
      color: #666;
      margin-bottom: 16rpx;
      font-weight: 500;
    }
    
    .images-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 16rpx;
      
      .result-img {
        width: 200rpx;
        height: 200rpx;
        border-radius: 12rpx;
        border: 2rpx solid #e0e0e0;
      }
    }
  }
  
  .maintainer-info {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 12rpx;
    padding: 20rpx;
    border: 1px solid #dee2e6;
  }
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 20rpx;

  .info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 0;

    .label {
      font-size: 26rpx;
      color: #666;
      min-width: 160rpx;
    }

    .value {
      font-size: 26rpx;
      color: #333;
      flex: 1;
      text-align: right;
    }
  }
}

.desc-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
}

.images-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;

  .detail-img {
    width: 200rpx;
    height: 200rpx;
    border-radius: 12rpx;
  }
}

.repair-timeline {
  .timeline-item {
    display: flex;
    margin-bottom: 24rpx;
    position: relative;

    &:not(:last-child)::after {
      content: '';
      position: absolute;
      left: 12rpx;
      top: 24rpx;
      width: 2rpx;
      height: calc(100% + 24rpx);
      background: #e0e0e0;
    }

    .timeline-dot {
      width: 24rpx;
      height: 24rpx;
      border-radius: 50%;
      background: #007AFF;
      margin-right: 20rpx;
      flex-shrink: 0;
    }

    .timeline-content {
      flex: 1;

      .record-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8rpx;

        .record-action {
          font-size: 26rpx;
          font-weight: 600;
          color: #333;
        }

        .record-time {
          font-size: 22rpx;
          color: #999;
        }
      }

      .record-desc {
        font-size: 24rpx;
        color: #666;
        line-height: 1.5;
      }
    }
  }
}

.action-section {
  margin-top: 32rpx;
  padding-top: 24rpx;
  border-top: 1px solid #f0f0f0;

  .btn-group {
    display: flex;
    gap: 20rpx;
    
    .action-btn {
      flex: 1;
      height: 88rpx;
      border-radius: 44rpx;
      font-size: 30rpx;
      font-weight: 600;
      border: none;
    }
  }

  .action-btn {
    width: 100%;
    height: 88rpx;
    border-radius: 44rpx;
    font-size: 30rpx;
    font-weight: 600;
    border: none;

    &.primary {
      background: #007AFF;
      color: #fff;
    }

    &.success {
      background: #34C759;
      color: #fff;
    }

    &.cancel {
      background: #FF3B30;
      color: #fff;
      
      &:hover {
        background: #E6342A;
      }
    }

    &.default {
      background: #f0f0f0;
      color: #666;
    }

    &.disabled {
      opacity: 0.6;
      background: #ccc !important;
      color: #999 !important;
      cursor: not-allowed;
    }
  }
}

// 模态框遮罩层
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

// 维修结果模态框样式
.repair-result-modal {
  width: 680rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  max-height: 80vh;

  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx 32rpx 16rpx;
    border-bottom: 1px solid #f0f0f0;

    .modal-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
    }

    .close-btn {
      padding: 8rpx;
      cursor: pointer;
    }
  }

  .modal-content {
    padding: 24rpx 32rpx;
    max-height: 60vh;
    overflow-y: auto;

    .form-item {
      margin-bottom: 32rpx;

      .label {
        display: block;
        font-size: 26rpx;
        color: #333;
        margin-bottom: 12rpx;
        position: relative;

        &.required::before {
          content: '*';
          color: #ff4757;
          font-size: 28rpx;
          position: absolute;
          left: -16rpx;
          top: 0;
          font-weight: bold;
        }
      }

      .textarea {
        width: 100%;
        min-height: 200rpx;
        border: 1px solid #eee;
        border-radius: 12rpx;
        padding: 16rpx 20rpx;
        box-sizing: border-box;
        background: #fafafa;
        font-size: 28rpx;
        color: #333;
        line-height: 1.5;

        &.ph {
          color: #bbb;
        }
      }

      .char-count {
        display: block;
        text-align: right;
        font-size: 22rpx;
        color: #999;
        margin-top: 8rpx;
      }

      .uploader {
        .upload-box {
          height: 160rpx;
          border: 1px dashed #ddd;
          border-radius: 12rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 12rpx;
          color: #666;
          transition: all 0.3s ease;

          &.uploading {
            background: #f0f0f0;
            pointer-events: none;
          }

          .upload-text {
            font-size: 26rpx;
          }
        }

        .thumbs {
          display: flex;
          flex-wrap: wrap;
          gap: 16rpx;
          margin-top: 16rpx;

          .thumb {
            position: relative;
            width: 160rpx;
            height: 160rpx;
            border-radius: 12rpx;
            overflow: hidden;

            image {
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
          }
        }

        .hint {
          display: block;
          margin-top: 8rpx;
          font-size: 22rpx;
          color: #999;
        }
      }
    }
  }

  .modal-footer {
    display: flex;
    gap: 20rpx;
    padding: 24rpx 32rpx 32rpx;
    border-top: 1px solid #f0f0f0;

    .cancel-btn {
      flex: 1;
      height: 80rpx;
      line-height: 80rpx;
      background: #f5f6fa;
      color: #666;
      border-radius: 12rpx;
      font-size: 28rpx;
      border: none;
      text-align: center;
    }

    .submit-btn {
      flex: 2;
      height: 80rpx;
      line-height: 80rpx;
      background: #34C759;
      color: #fff;
      border-radius: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      border: none;
      text-align: center;

      &.disabled {
        opacity: 0.6;
        background: #ccc !important;
        color: #999 !important;
        cursor: not-allowed;
      }
    }
  }
}

// 取消工单模态框样式（复用维修结果模态框的样式）
.cancel-modal {
  width: 680rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  max-height: 80vh;

  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx 32rpx 16rpx;
    border-bottom: 1px solid #f0f0f0;

    .modal-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
    }

    .close-btn {
      padding: 8rpx;
      cursor: pointer;
    }
  }

  .modal-content {
    padding: 24rpx 32rpx;
    max-height: 60vh;
    overflow-y: auto;

    .form-item {
      margin-bottom: 32rpx;

      .label {
        display: block;
        font-size: 26rpx;
        color: #333;
        margin-bottom: 12rpx;
        position: relative;

        &.required::before {
          content: '*';
          color: #ff4757;
          font-size: 28rpx;
          position: absolute;
          left: -16rpx;
          top: 0;
          font-weight: bold;
        }
      }

      .textarea {
        width: 100%;
        min-height: 200rpx;
        border: 1px solid #eee;
        border-radius: 12rpx;
        padding: 16rpx 20rpx;
        box-sizing: border-box;
        background: #fafafa;
        font-size: 28rpx;
        color: #333;
        line-height: 1.5;

        &.ph {
          color: #bbb;
        }
      }

      .char-count {
        display: block;
        text-align: right;
        font-size: 22rpx;
        color: #999;
        margin-top: 8rpx;
      }

      .uploader {
        .upload-box {
          height: 160rpx;
          border: 1px dashed #ddd;
          border-radius: 12rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 12rpx;
          color: #666;
          transition: all 0.3s ease;

          &.uploading {
            background: #f0f0f0;
            pointer-events: none;
          }

          .upload-text {
            font-size: 26rpx;
          }
        }

        .thumbs {
          display: flex;
          flex-wrap: wrap;
          gap: 16rpx;
          margin-top: 16rpx;

          .thumb {
            position: relative;
            width: 160rpx;
            height: 160rpx;
            border-radius: 12rpx;
            overflow: hidden;

            image {
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
          }
        }

        .hint {
          display: block;
          margin-top: 8rpx;
          font-size: 22rpx;
          color: #999;
        }
      }
    }
  }

  .modal-footer {
    display: flex;
    gap: 20rpx;
    padding: 24rpx 32rpx 32rpx;
    border-top: 1px solid #f0f0f0;

    .cancel-btn {
      flex: 1;
      height: 80rpx;
      line-height: 80rpx;
      background: #f5f6fa;
      color: #666;
      border-radius: 12rpx;
      font-size: 28rpx;
      border: none;
      text-align: center;
    }

    .submit-btn {
      flex: 2;
      height: 80rpx;
      line-height: 80rpx;
      border-radius: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      border: none;
      text-align: center;

      &.cancel-submit {
        background: #FF3B30;
        color: #fff;
      }

      &.disabled {
        opacity: 0.6;
        background: #ccc !important;
        color: #999 !important;
        cursor: not-allowed;
      }
    }
  }
}

.ph {
  color: #bbb;
}
</style>
