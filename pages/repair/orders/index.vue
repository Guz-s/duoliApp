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
      <view class="order-item" v-for="order in filteredOrders" :key="order.orderId" @click="goToDetail(order.orderId || order.id)">
        <view class="order-header">
          <text class="order-id">#{{ order.orderNo || order.id }}</text>
          <view class="order-status" :class="'status-' + order.status">{{ getStatusText(order.status) }}</view>
        </view>

        <view class="order-content">
          <text class="order-title">{{ order.faultDescription || order.title }}</text>
          <view class="order-info">
            <text class="info-item">报修地点： {{ order.location }}</text>
            <text class="info-item">设备型号： {{ order.equipmentModel }}</text>
            <text class="info-item" :style="{ color: getFaultLevelColor(order.faultLevel) }">
              故障等级：{{ getFaultLevelText(order.faultLevel) }}
            </text>
            <!-- 当故障等级为其他时，显示其他故障描述 -->
            <text class="info-item fault-desc-other" v-if="order.faultLevel == 3 && order.faultLevelDesc">
              其他描述：{{ order.faultLevelDesc }}
            </text>
            <text class="info-item" v-if="order.reporterName">👤 {{ order.reporterName }}</text>
            <text class="info-item" v-if="order.reporterPhone">📞 {{ order.reporterPhone }}</text>
          </view>
        </view>

        <view class="order-footer">
          <text class="order-time">{{ order.createTime }}</text>
        </view>

        <!-- 图片预览 -->
        <view class="order-images" v-if="getFaultImages(order).length > 0">
          <image
            v-for="(img, index) in getFaultImages(order).slice(0, 3)"
            :key="index"
            :src="img"
            class="preview-img"
            @click.stop="previewImages(getFaultImages(order), index)"
          />
          <view class="more-images" v-if="getFaultImages(order).length > 3">
            +{{ getFaultImages(order).length - 3 }}
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
import { getRepairByCode, listRepairData } from "@/api/repair/repair";
import { processFaultImages } from "@/utils/dataConverter";

export default {
  name: "RepairOrders",
  data() {
    return {
      currentStatus: 'all',
      statusTabs: [
        { label: '全部', value: 'all', count: 0 },
        { label: '待处理', value: 'pending', count: 0 },
        { label: '处理中', value: 'processing', count: 0 },
        { label: '已完成', value: 'completed', count: 0 },
        { label: '已取消', value: 'cancelled', count: 0 }
      ],
      orders: []
    }
  },
  onLoad(options) {
    if (options.status) {
      this.currentStatus = options.status
    }
    // this.getRepairListFun();
  },
  onShow() {
    // 每次页面显示时刷新数据，包括从编辑页面返回时
    this.getRepairListFun();
  },
  computed: {
    filteredOrders() {
      // 确保 orders 是数组，如果不是则返回空数组
      if (!this.orders || !Array.isArray(this.orders)) {
        console.info('orders不是数组或为空:', this.orders)
        return []
      }

      if (this.currentStatus === 'all') {
        return this.orders
      }

      // 状态映射：数字转字符串
      const statusMapping = {
        0: 'pending',    // 待处理
        1: 'processing', // 处理中
        2: 'completed',  // 已完成
        3: 'cancelled'   // 已取消
      }

      return this.orders.filter(order => {
        const mappedStatus = statusMapping[parseInt(order.status)]
        return mappedStatus === this.currentStatus
      })
    }
  },
  methods: {
    // 获取工单列表
    getRepairListFun(){
      listRepairData().then(res => {
          this.orders = res.rows || []
          console.info('orders:', this.orders)
          this.updateOrderCounts()
        } ).catch(err => {
          this.orders = []
          console.error('获取工单列表失败:', err)
        })
    },

    // 处理故障图片数据
    getFaultImages(order) {
      return processFaultImages(order.faultImages)
    },

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
      // 状态映射：数字转字符串
      const statusMapping = {
        0: 'pending',    // 待处理
        1: 'processing', // 处理中
        2: 'completed',  // 已完成
        3: 'cancelled'   // 已取消
      }

      const counts = {
        all: this.orders.length,
        pending: this.orders.filter(o => statusMapping[parseInt(o.status)] === 'pending').length,
        processing: this.orders.filter(o => statusMapping[parseInt(o.status)] === 'processing').length,
        completed: this.orders.filter(o => statusMapping[parseInt(o.status)] === 'completed').length,
        cancelled: this.orders.filter(o => statusMapping[parseInt(o.status)] === 'cancelled').length
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
    getActionText(status) {
      // 状态映射：数字转字符串
      const statusMapping = {
        0: 'pending',
        1: 'processing',
        2: 'completed',
        3: 'cancelled'
      }

      const mappedStatus = statusMapping[parseInt(status)] || status

      const actionMap = {
        'pending': '开始维修',
        'processing': '完成维修',
        'completed': '查看详情',
        'cancelled': '已取消'
      }
      return actionMap[mappedStatus] || '操作'
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

        .fault-desc-other {
          color: #FF9500 !important;
          font-style: italic;
          background: #FFF8F0;
          padding: 8rpx 12rpx;
          border-radius: 6rpx;
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
        .action-text {
          font-size: 22rpx;
          color: #007AFF;
        }

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
