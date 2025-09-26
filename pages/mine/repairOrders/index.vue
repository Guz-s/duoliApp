<script>
export default {
  name: "RepairOrders",
  data() {
    return {
      currentTab: 0,
      tabList: [
        { name: '已申请(4)' },
        { name: '待处理(3)' },
        { name: '处理中(2)' },
        { name: '已完成(5)' },
        { name: '已取消(1)' }
      ],
      orders: {
        applied: [
          {
            id: 101,
            orderNo: 'A20241225001',
            title: 'JUKI DDL-8700 断线故障',
            reporter: '张三',
            phone: '13800138001',
            department: '一车间缝制线A组',
            location: '3号厂房 2楼 设备区',
            equipmentModel: 'JUKI DDL-8700',
            faultLevel: '紧急',
            faultDescription: '设备在运行过程中频繁断线，影响生产效率，需要尽快处理',
            createTime: '2024-12-25 14:30',
            images: ['/static/images/banner/duolidamen.png'],
            otherSeverity: ''
          },
          {
            id: 102,
            orderNo: 'A20241224002',
            title: '兄弟牌缝纫机异响',
            reporter: '李四',
            phone: '13800138002',
            department: '二车间缝制线B组',
            location: '1号厂房 1楼 A组',
            equipmentModel: '兄弟牌缝纫机',
            faultLevel: '较急',
            faultDescription: '机器运行时发出异常噪音，需要检查维修',
            createTime: '2024-12-24 10:20',
            images: ['/static/images/banner/TORAYLogo.jpg'],
            otherSeverity: ''
          },
          {
            id: 103,
            orderNo: 'A20241223003',
            title: '重机缝纫机卡线',
            reporter: '王五',
            phone: '13800138003',
            department: '三车间缝制线C组',
            location: '2号厂房 3楼 B组',
            equipmentModel: '重机缝纫机',
            faultLevel: '一般',
            faultDescription: '缝纫机经常卡线，已影响正常生产进度',
            createTime: '2024-12-23 16:45',
            images: [],
            otherSeverity: ''
          },
          {
            id: 104,
            orderNo: 'A20241222004',
            title: '飞马缝纫机跳线',
            reporter: '赵六',
            phone: '13800138004',
            department: '四车间缝制线D组',
            location: '3号厂房 1楼 C组',
            equipmentModel: '飞马缝纫机',
            faultLevel: '其他',
            faultDescription: '缝纫机跳线问题，需要专业技术人员检查',
            createTime: '2024-12-22 09:15',
            images: [],
            otherSeverity: '需要更换专用配件'
          }
        ],
        pending: [
          {
            id: 1,
            orderNo: 'R20241225001',
            title: 'JUKI DDL-8700 断线故障',
            location: '3号厂房 2楼 设备区',
            equipmentModel: 'JUKI DDL-8700',
            faultLevel: '紧急',
            faultDescription: '设备在运行过程中频繁断线，影响生产效率',
            createTime: '2024-12-25 14:30',
            images: ['/static/images/banner/duolidamen.png']
          },
          {
            id: 2,
            orderNo: 'R20241224002',
            title: '兄弟牌缝纫机异响',
            location: '1号厂房 1楼 A组',
            equipmentModel: '兄弟牌缝纫机',
            faultLevel: '较急',
            faultDescription: '机器运行时发出异常噪音，需要检查',
            createTime: '2024-12-24 10:20',
            images: ['/static/images/banner/TORAYLogo.jpg']
          },
          {
            id: 3,
            orderNo: 'R20241223003',
            title: '重机缝纫机卡线',
            location: '2号厂房 3楼 B组',
            equipmentModel: '重机缝纫机',
            faultLevel: '一般',
            faultDescription: '缝纫机经常卡线，已影响正常生产',
            createTime: '2024-12-23 16:45',
            images: []
          }
        ],
        processing: [
          {
            id: 4,
            orderNo: 'R20241222001',
            title: '飞马缝纫机跳线',
            location: '3号厂房 1楼 C组',
            equipmentModel: '飞马缝纫机',
            faultLevel: '紧急',
            faultDescription: '缝纫机跳线严重，无法正常缝制',
            createTime: '2024-12-22 09:15',
            images: []
          },
          {
            id: 5,
            orderNo: 'R20241221002',
            title: '杰克缝纫机噪音',
            location: '2号厂房 2楼 B组',
            equipmentModel: '杰克缝纫机',
            faultLevel: '较急',
            faultDescription: '设备运行时噪音异常，需要维修',
            createTime: '2024-12-21 15:30',
            images: []
          }
        ],
        completed: [
          {
            id: 6,
            orderNo: 'R20241220001',
            title: '重机锁眼机故障',
            location: '1号厂房 3楼 A组',
            equipmentModel: '重机锁眼机',
            faultLevel: '一般',
            faultDescription: '锁眼机无法正常工作',
            createTime: '2024-12-20 11:20',
            completeTime: '2024-12-20 16:45',
            images: []
          },
          {
            id: 7,
            orderNo: 'R20241219002',
            title: 'JUKI包缝机断线',
            location: '3号厂房 2楼 D组',
            equipmentModel: 'JUKI包缝机',
            faultLevel: '紧急',
            faultDescription: '包缝机频繁断线，影响生产',
            createTime: '2024-12-19 14:10',
            completeTime: '2024-12-19 18:30',
            images: []
          },
          {
            id: 8,
            orderNo: 'R20241218003',
            title: '兄弟牌拷边机异响',
            location: '2号厂房 1楼 C组',
            equipmentModel: '兄弟牌拷边机',
            faultLevel: '较急',
            faultDescription: '拷边机运行时发出异常声音',
            createTime: '2024-12-18 10:45',
            completeTime: '2024-12-18 15:20',
            images: []
          },
          {
            id: 9,
            orderNo: 'R20241217004',
            title: '飞马平缝机卡线',
            location: '1号厂房 2楼 B组',
            equipmentModel: '飞马平缝机',
            faultLevel: '一般',
            faultDescription: '平缝机经常卡线，需要调整',
            createTime: '2024-12-17 16:30',
            completeTime: '2024-12-17 20:15',
            images: []
          },
          {
            id: 10,
            orderNo: 'R20241216005',
            title: '杰克绷缝机跳线',
            location: '3号厂房 3楼 A组',
            equipmentModel: '杰克绷缝机',
            faultLevel: '较急',
            faultDescription: '绷缝机跳线问题，影响质量',
            createTime: '2024-12-16 13:25',
            completeTime: '2024-12-16 17:40',
            images: []
          }
        ],
        cancelled: [
          {
            id: 11,
            orderNo: 'R20241215001',
            title: '重机钉扣机故障',
            location: '2号厂房 3楼 D组',
            equipmentModel: '重机钉扣机',
            faultLevel: '一般',
            faultDescription: '钉扣机无法正常工作',
            createTime: '2024-12-15 09:30',
            cancelTime: '2024-12-15 10:15',
            cancelReason: '设备已更换，无需维修',
            images: []
          }
        ]
      }
    }
  },
  computed: {
    currentOrders() {
      const statusMap = ['applied', 'pending', 'processing', 'completed', 'cancelled']
      return this.orders[statusMap[this.currentTab]] || []
    },
    tabLineLeft() {
      // 5个标签页，每个标签页宽度为 100% / 5 = 20%
      // 以rpx为单位，屏幕宽度750rpx，每个标签宽度150rpx
      const tabWidth = 750 / 5 // 150rpx
      const lineWidth = 60 // 下划线宽度60rpx
      const offset = (tabWidth - lineWidth) / 2 // 居中偏移量
      return (this.currentTab * tabWidth) + offset
    },
    tabLineWidth() {
      return 60 // 下划线宽度
    }
  },
  methods: {
    // 选项卡切换
    tabChange(index) {
      this.currentTab = index
    },
    // 查看工单详情
    viewOrderDetail(order) {
      // 如果是已申请状态，跳转到编辑页面
      if (this.currentTab === 0) {
        this.editApplication(order)
        return
      }
      
      uni.showModal({
        title: '工单详情',
        content: `工单号：${order.orderNo}\n状态：${this.getStatusText(this.currentTab)}\n描述：${order.faultDescription}`,
        showCancel: false
      })
    },
    // 编辑申请
    editApplication(order) {
      // 将申请数据存储到全局变量或本地存储，供编辑页面使用
      uni.setStorageSync('editingOrder', order)
      
      // 跳转到报修申请页面进行编辑
      uni.navigateTo({
        url: '/pages/work/carWarranty/index?mode=edit&id=' + order.id
      })
    },
    // 获取状态文本
    getStatusText(statusIndex) {
      const statusMap = ['已申请', '待处理', '处理中', '已完成', '已取消']
      return statusMap[statusIndex] || '未知'
    },
    // 预览图片
    previewImages(images, current) {
      if (!images || images.length === 0) return
      uni.previewImage({
        current,
        urls: images
      })
    },
    // 获取故障等级颜色
    getFaultLevelColor(level) {
      const colorMap = {
        '紧急': '#ff4757',
        '较急': '#ffa502',
        '一般': '#2ed573'
      }
      return colorMap[level] || '#666'
    }
  }
}
</script>

<template>
  <view class="page">
    <!-- 状态选项卡 -->
    <view class="tabs-container">
      <view class="tabs-wrapper">
        <view 
          class="tab-item" 
          v-for="(tab, index) in tabList" 
          :key="index"
          :class="{ 'active': currentTab === index }"
          @click="tabChange(index)"
        >
          <text class="tab-text">{{ tab.name }}</text>
        </view>
      </view>
      <view class="tab-line" :style="{ left: tabLineLeft + 'rpx', width: tabLineWidth + 'rpx' }"></view>
    </view>

    <!-- 工单列表 -->
    <view class="orders-container">
      <view class="orders-list" v-if="currentOrders.length > 0">
        <view 
          class="order-item" 
          v-for="order in currentOrders" 
          :key="order.id" 
          @click="viewOrderDetail(order)"
        >
          <view class="order-header">
            <text class="order-id">#{{ order.orderNo }}</text>
            <view class="order-status" :class="'status-' + currentTab">
              {{ getStatusText(currentTab) }}
            </view>
          </view>
          
          <view class="order-content">
            <text class="order-title">{{ order.title }}</text>
            <view class="order-info">
              <text class="info-item">📍 {{ order.location }}</text>
              <text class="info-item">🔧 {{ order.equipmentModel }}</text>
              <text class="info-item" :style="{ color: getFaultLevelColor(order.faultLevel) }">
                ⚡ {{ order.faultLevel }}
              </text>
            </view>
            <text class="order-desc">{{ order.faultDescription }}</text>
          </view>

          <view class="order-footer">
            <text class="order-time">{{ order.createTime }}</text>
            <view class="order-actions">
              <text class="action-text" v-if="currentTab === 0">点击编辑</text>
              <text class="action-text" v-else-if="currentTab === 1">等待处理</text>
              <text class="action-text" v-else-if="currentTab === 2">维修中</text>
              <text class="action-text" v-else-if="currentTab === 3">已完成</text>
              <text class="action-text" v-else>已取消</text>
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
      <view class="empty-state" v-else>
        <u-empty 
          mode="data" 
          text="暂无数据"
          :show="true"
        ></u-empty>
        <text class="empty-hint">该状态下暂无报修记录</text>
      </view>
    </view>
  </view>
</template>

<style scoped lang="scss">
.page {
  min-height: 100vh;
  background: #f6f7fb;
}

.tabs-container {
  position: relative;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.tabs-wrapper {
  display: flex;
  position: relative;
}

.tab-item {
  flex: 1;
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: all 0.3s ease;

  .tab-text {
    font-size: 28rpx;
    color: #666;
    transition: all 0.3s ease;
  }

  &.active {
    .tab-text {
      color: #007AFF;
      font-weight: bold;
    }
  }
}

.tab-line {
  position: absolute;
  bottom: 0;
  height: 4rpx;
  background: #007AFF;
  border-radius: 2rpx;
  transition: all 0.3s ease;
}

.orders-container {
  padding: 20rpx;
  height: calc(100vh - 100rpx);
  overflow-y: auto;
}


.orders-list {
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
        font-size: 28rpx;
        font-weight: 600;
        color: #333;
      }

      .order-status {
        padding: 8rpx 16rpx;
        border-radius: 20rpx;
        font-size: 20rpx;
        color: #fff;

        &.status-0 {
          background: #5856D6;
        }

        &.status-1 {
          background: #FF9500;
        }

        &.status-2 {
          background: #007AFF;
        }

        &.status-3 {
          background: #34C759;
        }

        &.status-4 {
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
  text-align: center;

  .empty-hint {
    margin-top: 24rpx;
    font-size: 24rpx;
    color: #999;
  }
}
</style>
