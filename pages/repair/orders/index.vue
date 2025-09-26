<template>
  <view class="orders-container">
    <!-- 状态筛选 -->
    <view class="filter-tabs">
      <view 
        class="tab-item" 
        :class="{ active: currentStatus === item.value }"
        v-for="item in statusTabs" 
        :key="item.value"
        @click="switchStatus(item.value)"
      >
        {{ item.label }}
        <view class="count" v-if="item.count > 0">{{ item.count }}</view>
      </view>
    </view>

    <!-- 工单列表 -->
    <view class="order-list">
      <view class="order-item" v-for="order in filteredOrders" :key="order.id" @click="goToDetail(order.id)">
        <view class="order-header">
          <text class="order-id">#{{ order.id }}</text>
          <view class="order-status" :class="order.status">{{ getStatusText(order.status) }}</view>
        </view>
        
        <view class="order-content">
          <text class="order-title">{{ order.title }}</text>
          <view class="order-info">
            <text class="info-item">📍 {{ order.location }}</text>
            <text class="info-item">👤 {{ order.reporter }}</text>
            <text class="info-item">📞 {{ order.phone }}</text>
          </view>
          <text class="order-desc">{{ order.description }}</text>
        </view>

        <view class="order-footer">
          <text class="order-time">{{ order.createTime }}</text>
          <view class="order-actions">
            <button 
              class="action-btn" 
              :class="{
                primary: order.status === 'pending',
                success: order.status === 'processing', 
                default: order.status === 'completed'
              }"
              @click.stop="handleAction(order)"
            >
              {{ getActionText(order.status) }}
            </button>
          </view>
        </view>

        <!-- 图片预览 -->
        <view class="order-images" v-if="order.images && order.images.length > 0">
          <image 
            v-for="(img, index) in order.images.slice(0, 3)" 
            :key="index"
            :src="img" 
            class="preview-img"
            @click.stop="previewImages(order.images, index)"
          />
          <view class="more-images" v-if="order.images.length > 3">
            +{{ order.images.length - 3 }}
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="filteredOrders.length === 0">
      <uni-icons type="info" size="60" color="#ccc"></uni-icons>
      <text class="empty-text">暂无{{ getStatusText(currentStatus) }}工单</text>
    </view>
  </view>
</template>

<script>
export default {
  name: "RepairOrders",
  data() {
    return {
      currentStatus: 'all',
      statusTabs: [
        { label: '全部', value: 'all', count: 25 },
        { label: '待处理', value: 'pending', count: 8 },
        { label: '处理中', value: 'processing', count: 5 },
        { label: '已完成', value: 'completed', count: 12 }
      ],
      orders: [
        {
          id: 'R2024001',
          title: 'JUKI DDL-8700 断线故障',
          location: '3号厂房 2楼 设备区',
          reporter: '李小明',
          phone: '138****1234',
          description: '设备在运行过程中频繁断线，影响生产效率',
          status: 'pending',
          createTime: '2024-01-15 14:30',
          images: ['/static/images/banner/duolidamen.png']
        },
        {
          id: 'R2024002',
          title: '兄弟牌缝纫机异响',
          location: '1号厂房 1楼 A组',
          reporter: '王小红',
          phone: '139****5678',
          description: '机器运行时发出异常噪音，需要检查',
          status: 'processing',
          createTime: '2024-01-15 10:20',
          images: ['/static/images/banner/TORAYLogo.jpg']
        },
        {
          id: 'R2024003',
          title: '重机缝纫机卡线',
          location: '2号厂房 3楼 B组',
          reporter: '赵小刚',
          phone: '137****9012',
          description: '缝纫机经常卡线，已影响正常生产',
          status: 'completed',
          createTime: '2024-01-14 16:45',
          images: []
        },
        {
          id: 'R2024004',
          title: '电脑平车速度异常',
          location: '4号厂房 1楼 C组',
          reporter: '孙小丽',
          phone: '136****3456',
          description: '设备速度不稳定，时快时慢',
          status: 'pending',
          createTime: '2024-01-14 09:15',
          images: []
        },
        {
          id: 'R2024005',
          title: '包缝机断针',
          location: '2号厂房 2楼 D组',
          reporter: '周小强',
          phone: '135****7890',
          description: '包缝机频繁断针，需要更换配件',
          status: 'processing',
          createTime: '2024-01-13 15:30',
          images: []
        }
      ]
    }
  },
  computed: {
    filteredOrders() {
      if (this.currentStatus === 'all') {
        return this.orders
      }
      return this.orders.filter(order => order.status === this.currentStatus)
    }
  },
  onLoad(options) {
    if (options.status) {
      this.currentStatus = options.status
    }
  },
  methods: {
    switchStatus(status) {
      this.currentStatus = status
    },
    goToDetail(orderId) {
      uni.navigateTo({
        url: `/pages/repair/orders/detail?id=${orderId}`
      })
    },
    handleAction(order) {
      const actions = {
        'pending': () => this.startRepair(order),
        'processing': () => this.completeRepair(order),
        'completed': () => this.viewDetail(order)
      }
      
      if (actions[order.status]) {
        actions[order.status]()
      }
    },
    startRepair(order) {
      uni.showModal({
        title: '开始维修',
        content: `确定开始维修工单 #${order.id} 吗？`,
        success: (res) => {
          if (res.confirm) {
            // 更新状态为处理中
            order.status = 'processing'
            this.updateOrderCounts()
            uni.showToast({
              title: '已开始维修',
              icon: 'success'
            })
          }
        }
      })
    },
    completeRepair(order) {
      uni.showModal({
        title: '完成维修',
        content: `确定完成工单 #${order.id} 的维修吗？`,
        success: (res) => {
          if (res.confirm) {
            // 更新状态为已完成
            order.status = 'completed'
            this.updateOrderCounts()
            uni.showToast({
              title: '维修完成',
              icon: 'success'
            })
          }
        }
      })
    },
    viewDetail(order) {
      this.goToDetail(order.id)
    },
    updateOrderCounts() {
      const counts = {
        all: this.orders.length,
        pending: this.orders.filter(o => o.status === 'pending').length,
        processing: this.orders.filter(o => o.status === 'processing').length,
        completed: this.orders.filter(o => o.status === 'completed').length
      }
      
      this.statusTabs.forEach(tab => {
        tab.count = counts[tab.value]
      })
    },
    previewImages(images, current) {
      uni.previewImage({
        current,
        urls: images
      })
    },
    getStatusText(status) {
      const statusMap = {
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成'
      }
      return statusMap[status] || '未知'
    },
    getActionText(status) {
      const actionMap = {
        'pending': '开始维修',
        'processing': '完成维修',
        'completed': '查看详情'
      }
      return actionMap[status] || '操作'
    },
  }
}
</script>

<style lang="scss" scoped>
.orders-container {
  min-height: 100vh;
  background: #f5f6fa;
}

.filter-tabs {
  display: flex;
  background: #fff;
  padding: 0 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 32rpx 16rpx;
    position: relative;
    font-size: 28rpx;
    color: #666;
    border-bottom: 4rpx solid transparent;

    &.active {
      color: #007AFF;
      border-bottom-color: #007AFF;
      font-weight: 600;
    }

    .count {
      position: absolute;
      top: 16rpx;
      right: 16rpx;
      background: #FF3B30;
      color: #fff;
      font-size: 20rpx;
      padding: 4rpx 8rpx;
      border-radius: 20rpx;
      min-width: 32rpx;
      text-align: center;
    }
  }
}

.order-list {
  padding: 20rpx;

  .order-item {
    background: #fff;
    border-radius: 16rpx;
    padding: 32rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24rpx;

      .order-id {
        font-size: 32rpx;
        font-weight: 700;
        color: #333;
      }

      .order-status {
        padding: 12rpx 24rpx;
        border-radius: 24rpx;
        font-size: 22rpx;
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

    .order-content {
      margin-bottom: 24rpx;

      .order-title {
        display: block;
        font-size: 30rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 16rpx;
      }

      .order-info {
        margin-bottom: 16rpx;

        .info-item {
          display: block;
          font-size: 24rpx;
          color: #666;
          margin-bottom: 8rpx;
        }
      }

      .order-desc {
        font-size: 26rpx;
        color: #333;
        line-height: 1.5;
      }
    }

    .order-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .order-time {
        font-size: 22rpx;
        color: #999;
      }

      .order-actions {
        .action-btn {
          padding: 16rpx 32rpx;
          border-radius: 24rpx;
          font-size: 24rpx;
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
    }

    .order-images {
      display: flex;
      gap: 16rpx;
      margin-top: 24rpx;

      .preview-img {
        width: 120rpx;
        height: 120rpx;
        border-radius: 12rpx;
      }

      .more-images {
        width: 120rpx;
        height: 120rpx;
        background: #f0f0f0;
        border-radius: 12rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24rpx;
        color: #666;
      }
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;

  .empty-text {
    margin-top: 24rpx;
    font-size: 28rpx;
    color: #999;
  }
}
</style>
