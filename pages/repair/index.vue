<template>
  <view class="repair-container">
    <!-- 头部信息 -->
    <view class="header">
      <view class="user-info">
        <image class="avatar" :src="avatar" mode="aspectFill" @click="goToAvatar"></image>
        <view class="info">
          <text class="name">{{ name || '维修人员' }}</text>
          <text class="role">{{ postGroup }}</text>
        </view>
      </view>
      <view class="logout-btn" @click="handleLogout">
        <uni-icons type="logout" size="20" color="#666"></uni-icons>
      </view>
    </view>

    <!-- 统计卡片 -->
    <view class="stats-cards">
      <view class="stat-card repairall" @click="goToOrders('all')">
        <view class="stat-number">{{ orderStats.total }}</view>
        <view class="stat-label">全部工单</view>
      </view>
      <view class="stat-card pending" @click="goToOrders('pending')">
        <view class="stat-number">{{ orderStats.pending }}</view>
        <view class="stat-label">待处理</view>
      </view>
      <view class="stat-card processing" @click="goToOrders('processing')">
        <view class="stat-number">{{ orderStats.processing }}</view>
        <view class="stat-label">处理中</view>
      </view>
      <view class="stat-card completed" @click="goToOrders('completed')">
        <view class="stat-number">{{ orderStats.completed }}</view>
        <view class="stat-label">已完成</view>
      </view>
      <view class="stat-card cancelled" @click="goToOrders('cancelled')">
        <view class="stat-number">{{ orderStats.cancelled }}</view>
        <view class="stat-label">已取消</view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="section-title">功能菜单</view>
      <view class="menu-grid">
        <view class="menu-item" @click="goToOrders('all')">
          <view class="menu-icon">
            <uni-icons type="list" size="24" color="#007AFF"></uni-icons>
          </view>
          <text class="menu-text">工单管理</text>
        </view>
        <view class="menu-item" @click="goToInfo">
          <view class="menu-icon">
            <uni-icons type="person-filled" size="24" color="#34C759"></uni-icons>
          </view>
          <text class="menu-text">个人信息</text>
        </view>
        <view class="menu-item" @click="goToPassword">
          <view class="menu-icon">
            <uni-icons type="locked" size="24" color="#FF9500"></uni-icons>
          </view>
          <text class="menu-text">修改密码</text>
        </view>
        <view class="menu-item" @click="handleLogout">
          <view class="menu-icon">
            <uni-icons type="close" size="24" color="#FF3B30"></uni-icons>
          </view>
          <text class="menu-text">退出登录</text>
        </view>
      </view>
    </view>

    <!-- 最近工单 -->
    <view class="recent-section">
      <view class="section-title">
        <text>最近工单</text>
        <text class="more" @click="goToOrders('all')">查看全部</text>
      </view>
      <view class="order-list">
        <!-- 加载状态 -->
        <view class="loading-state" v-if="loading">
          <uni-icons type="spinner-cycle" size="30" color="#007AFF"></uni-icons>
          <text class="loading-text">加载中...</text>
        </view>
        
        <!-- 工单列表 -->
        <view class="order-item" v-for="order in recentOrders" :key="order.id" @click="goToOrderDetail(order.id)">
          <view class="order-header">
            <text class="order-id">#{{ order.orderNo || order.id }}</text>
            <view class="order-status" :class="order.status">{{ getStatusText(order.status) }}</view>
          </view>
          <view class="order-content">
            <text class="order-title">{{ order.title }}</text>
            <view class="order-info">
              <text class="info-item">📍 {{ order.location }}</text>
              <text class="info-item" v-if="order.equipmentModel">🔧 {{ order.equipmentModel }}</text>
              <text class="info-item" :style="{ color: getFaultLevelColor(order.faultLevel) }" v-if="order.faultLevel !== undefined">
                ⚠️ {{ getFaultLevelText(order.faultLevel) }}
              </text>
              <!-- 当故障等级为其他时，显示其他故障描述 -->
            <text class="info-item fault-desc-other" v-if="order.faultLevel == 3 && order.faultLevelDesc">
              其他描述：{{ order.faultLevelDesc }}
            </text>
              <text class="info-item" v-if="order.reporterName">👤 {{ order.reporterName }}</text>
              <text class="info-item" v-if="order.reporterPhone">📞 {{ order.reporterPhone }}</text>
            </view>
          </view>
          <!-- 故障图片预览 -->
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
          <view class="order-footer">
            <text class="order-time">{{ order.createTime }}</text>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="!loading && recentOrders.length === 0">
          <uni-icons type="info" size="40" color="#ccc"></uni-icons>
          <text class="empty-text">暂无最近工单</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getUserProfile } from "@/api/system/user";
import { listRepairData } from "@/api/repair/repair";
import { processFaultImages } from "@/utils/dataConverter";

export default {
  name: "RepairDashboard",
  computed: {
    avatar() {
      return this.$store.getters.avatar
    },
    name() {
      return this.$store.state.user.name
    }
  },
  data() {
    return {
      user: {},
      roleGroup: "",
      postGroup: "",
      loading: false,
      orderStats: {
        total: 0,
        pending: 0,
        processing: 0,
        completed: 0,
        cancelled: 0
      },
      recentOrders: [],
      allOrders: [] // 存储所有工单数据
    }
  },
  onLoad() {
    this.getUser()
    this.getRepairData()
  },
  onShow() {
    // 每次页面显示时刷新数据
    this.getRepairData()
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data
        this.roleGroup = response.roleGroup
        this.postGroup = response.postGroup
      }).catch(err => {
        console.error('获取用户信息失败:', err)
      })
    },
    
    // 获取工单数据
    async getRepairData() {
      if (this.loading) return
      
      this.loading = true
      try {
        const response = await listRepairData()
        this.allOrders = response.rows || []
        
        // 计算统计数据
        this.calculateOrderStats()
        
        // 获取最近的工单（按创建时间排序，取前5条）
        this.getRecentOrders()
        
      } catch (error) {
        console.error('获取工单数据失败:', error)
        uni.showToast({
          title: '获取数据失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    // 计算工单统计数据
    calculateOrderStats() {
      // 状态映射：数字转字符串
      const statusMapping = {
        0: 'pending',    // 待处理
        1: 'processing', // 处理中
        2: 'completed',  // 已完成
        3: 'cancelled'   // 已取消
      }
      
      this.orderStats = {
        total: this.allOrders.length,
        pending: this.allOrders.filter(order => statusMapping[parseInt(order.status)] === 'pending').length,
        processing: this.allOrders.filter(order => statusMapping[parseInt(order.status)] === 'processing').length,
        completed: this.allOrders.filter(order => statusMapping[parseInt(order.status)] === 'completed').length,
        cancelled: this.allOrders.filter(order => statusMapping[parseInt(order.status)] === 'cancelled').length
      }
    },
    
    // 获取最近工单
    getRecentOrders() {
      // 按创建时间排序，取最新的5条
      const sortedOrders = [...this.allOrders].sort((a, b) => {
        return new Date(b.createTime) - new Date(a.createTime)
      })
      
      // 转换数据格式以适配模板显示
      this.recentOrders = sortedOrders.slice(0, 5).map(order => {
        const statusMapping = {
          0: 'pending',
          1: 'processing', 
          2: 'completed',
          3: 'cancelled'
        }
        
        return {
          id: order.orderId || order.id,
          orderNo: order.orderNo,
          title: order.faultDescription || '设备故障',
          location: order.location || '未指定位置',
          reporter: order.reporterName || '未知',
          reporterPhone: order.reporterPhone,
          equipmentModel: order.equipmentModel,
          faultLevel: order.faultLevel,
          faultLevelDesc: order.faultLevelDesc,
          status: statusMapping[parseInt(order.status)] || 'pending',
          createTime: this.formatDateTime(order.createTime),
          faultImages: order.faultImages
        }
      })
    },
    
    // 格式化日期时间
    formatDateTime(dateTimeStr) {
      if (!dateTimeStr) return ''
      
      const date = new Date(dateTimeStr)
      if (isNaN(date.getTime())) return dateTimeStr
      
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      
      return `${year}-${month}-${day} ${hours}:${minutes}`
    },
    
    // 处理故障图片
    getFaultImages(order) {
      return processFaultImages(order.faultImages)
    },
    goToOrders(status) {
      uni.navigateTo({
        url: `/pages/repair/orders/index?status=${status}`
      })
    },
    goToOrderDetail(orderId) {
      uni.navigateTo({
        url: `/pages/repair/orders/detail?id=${orderId}`
      })
    },
    goToInfo() {
      uni.navigateTo({
        url: '/pages/mine/info/index'
      })
    },
    goToPassword() {
      uni.navigateTo({
        url: '/pages/mine/pwd/index'
      })
    },
    goToAvatar() {
      uni.navigateTo({
        url: '/pages/mine/avatar/index'
      })
    },
    handleLogout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            // 清除登录信息
            uni.removeStorageSync('App-Token')
            uni.removeStorageSync('userInfo')
            // 跳转到登录页
            uni.reLaunch({
              url: '/pages/login'
            })
          }
        }
      })
    },
    
    // 预览图片
    previewImages(images, current) {
      uni.previewImage({
        current,
        urls: images
      })
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
    
    getStatusText(status) {
      const statusMap = {
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成',
        'cancelled': '已取消'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.repair-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 20rpx;
}

.header {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

  .user-info {
    display: flex;
    align-items: center;

    .avatar {
      width: 80rpx;
      height: 80rpx;
      border-radius: 50%;
      margin-right: 24rpx;
    }

    .info {
      .name {
        display: block;
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 8rpx;
      }

      .role {
        font-size: 24rpx;
        color: #666;
      }
    }
  }

  .logout-btn {
    padding: 16rpx;
  }
}

.stats-cards {
  display: flex;
  gap: 12rpx;
  margin-bottom: 32rpx;
  overflow-x: auto;

  .stat-card {
    flex: 1;
    min-width: 120rpx;
    background: #fff;
    border-radius: 12rpx;
    padding: 20rpx 12rpx;
    text-align: center;
    box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

    &.repairall {
      border-top: 6rpx solid #f7ff00;
    }

    &.pending {
      border-top: 6rpx solid #FF9500;
    }

    &.processing {
      border-top: 6rpx solid #007AFF;
    }

    &.completed {
      border-top: 6rpx solid #34C759;
    }

    &.cancelled {
      border-top: 6rpx solid #8E8E93;
    }

    .stat-number {
      font-size: 36rpx;
      font-weight: 700;
      color: #333;
      margin-bottom: 6rpx;
    }

    .stat-label {
      font-size: 20rpx;
      color: #666;
      line-height: 1.2;
    }
  }
}

.menu-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

  .section-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 24rpx;
  }

  .menu-grid {
    display: flex;
    gap: 32rpx;

    .menu-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 24rpx;
      border-radius: 12rpx;
      background: #f8f9fa;

      .menu-icon {
        margin-bottom: 16rpx;
      }

      .menu-text {
        font-size: 24rpx;
        color: #333;
      }
    }
  }
}

.recent-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

  .section-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 24rpx;

    .more {
      font-size: 24rpx;
      color: #007AFF;
      font-weight: normal;
    }
  }

  .order-list {
    .loading-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 60rpx 40rpx;
      
      .loading-text {
        margin-top: 16rpx;
        font-size: 24rpx;
        color: #666;
      }
    }
    
    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 60rpx 40rpx;
      
      .empty-text {
        margin-top: 16rpx;
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .order-item {
      padding: 24rpx;
      border: 1px solid #f0f0f0;
      border-radius: 12rpx;
      margin-bottom: 16rpx;

      .order-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16rpx;

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
        margin-bottom: 16rpx;

        .order-title {
          display: block;
          font-size: 26rpx;
          font-weight: 600;
          color: #333;
          margin-bottom: 12rpx;
          line-height: 1.4;
        }

        .order-info {
          .info-item {
            display: block;
            font-size: 22rpx;
            color: #666;
            margin-bottom: 6rpx;
            line-height: 1.3;
          }

          .fault-desc-other {
            color: #FF9500 !important;
            font-style: italic;
            background: #FFF8F0;
            padding: 8rpx 12rpx;
            border-radius: 6rpx;
          }
        }
      }

      .order-images {
        display: flex;
        gap: 12rpx;
        margin: 16rpx 0;

        .preview-img {
          width: 80rpx;
          height: 80rpx;
          border-radius: 8rpx;
          border: 1px solid #f0f0f0;
        }

        .more-images {
          width: 80rpx;
          height: 80rpx;
          background: #f8f9fa;
          border-radius: 8rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20rpx;
          color: #666;
          border: 1px solid #f0f0f0;
        }
      }

      .order-footer {
        display: flex;
        justify-content: flex-start;
        font-size: 20rpx;
        color: #999;
        margin-top: 12rpx;
      }
    }
  }
}
</style>
