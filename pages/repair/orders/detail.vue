<template>
  <view class="detail-container">
    <view class="order-card">
      <!-- 工单头部 -->
      <view class="order-header">
        <view class="order-id">工单 #{{ order.id }}</view>
        <view class="order-status" :class="order.status">{{ getStatusText(order.status) }}</view>
      </view>

      <!-- 基本信息 -->
      <view class="info-section">
        <view class="section-title">基本信息</view>
        <view class="info-grid">
          <view class="info-item">
            <text class="label">报修人</text>
            <text class="value">{{ order.reporter }}</text>
          </view>
          <view class="info-item">
            <text class="label">联系电话</text>
            <text class="value">{{ order.phone }}</text>
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
            <text class="value">{{ order.model }}</text>
          </view>
          <view class="info-item">
            <text class="label">故障等级</text>
            <text class="value">{{ order.severity }}</text>
          </view>
        </view>
      </view>

      <!-- 故障描述 -->
      <view class="desc-section">
        <view class="section-title">故障描述</view>
        <text class="desc-text">{{ order.description }}</text>
      </view>

      <!-- 现场图片 -->
      <view class="images-section" v-if="order.images && order.images.length > 0">
        <view class="section-title">现场图片</view>
        <view class="images-grid">
          <image 
            v-for="(img, index) in order.images" 
            :key="index"
            :src="img" 
            class="detail-img"
            @click="previewImages(order.images, index)"
          />
        </view>
      </view>

      <!-- 维修记录 -->
      <view class="repair-section" v-if="order.repairRecords && order.repairRecords.length > 0">
        <view class="section-title">维修记录</view>
        <view class="repair-timeline">
          <view class="timeline-item" v-for="(record, index) in order.repairRecords" :key="index">
            <view class="timeline-dot"></view>
            <view class="timeline-content">
              <view class="record-header">
                <text class="record-action">{{ record.action }}</text>
                <text class="record-time">{{ record.time }}</text>
              </view>
              <text class="record-desc" v-if="record.description">{{ record.description }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 操作按钮 -->
      <view class="action-section">
        <button 
          class="action-btn primary" 
          v-if="order.status === 'pending'"
          @click="startRepair"
        >
          开始维修
        </button>
        <button 
          class="action-btn success" 
          v-if="order.status === 'processing'"
          @click="completeRepair"
        >
          完成维修
        </button>
        <button 
          class="action-btn default" 
          v-if="order.status === 'completed'"
          @click="goBack"
        >
          返回列表
        </button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "OrderDetail",
  data() {
    return {
      orderId: '',
      order: {
        id: 'R2024001',
        title: 'JUKI DDL-8700 断线故障',
        reporter: '李小明',
        phone: '138****1234',
        department: '一车间缝制线A组',
        location: '3号厂房 2楼 设备区',
        model: 'JUKI DDL-8700',
        severity: '紧急',
        description: '设备在运行过程中频繁断线，影响生产效率。断线主要发生在缝制厚料时，怀疑是针板或压脚问题。',
        status: 'pending',
        createTime: '2024-01-15 14:30',
        images: [
          '/static/images/banner/duolidamen.png',
          '/static/images/banner/TORAYLogo.jpg'
        ],
        repairRecords: [
          {
            action: '工单创建',
            time: '2024-01-15 14:30',
            description: '用户提交报修申请'
          }
        ]
      }
    }
  },
  onLoad(options) {
    if (options.id) {
      this.orderId = options.id
      this.loadOrderDetail()
    }
  },
  methods: {
    loadOrderDetail() {
      // 这里应该根据orderId从API获取详情
      // 现在使用模拟数据
      console.log('加载工单详情:', this.orderId)
    },
    startRepair() {
      uni.showModal({
        title: '开始维修',
        content: `确定开始维修工单 #${this.order.id} 吗？`,
        success: (res) => {
          if (res.confirm) {
            this.order.status = 'processing'
            this.addRepairRecord('开始维修', '维修人员开始处理此工单')
            uni.showToast({
              title: '已开始维修',
              icon: 'success'
            })
          }
        }
      })
    },
    completeRepair() {
      uni.showModal({
        title: '完成维修',
        content: `确定完成工单 #${this.order.id} 的维修吗？`,
        success: (res) => {
          if (res.confirm) {
            this.order.status = 'completed'
            this.addRepairRecord('维修完成', '设备已修复，可正常使用')
            uni.showToast({
              title: '维修完成',
              icon: 'success'
            })
          }
        }
      })
    },
    addRepairRecord(action, description) {
      const now = new Date()
      const timeStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
      
      this.order.repairRecords.push({
        action,
        time: timeStr,
        description
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
      const statusMap = {
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成'
      }
      return statusMap[status] || '未知'
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

    &.pending {
      background: #FF9500;
    }

    &.processing {
      background: #007AFF;
    }

    &.completed {
      background: #34C759;
    }
  }
}

.info-section, .desc-section, .images-section, .repair-section {
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

    &.default {
      background: #f0f0f0;
      color: #666;
    }
  }
}
</style>
