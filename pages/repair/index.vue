<template>
  <view class="repair-container">
    <!-- 头部信息 -->
    <view class="header">
      <view class="user-info">
        <image class="avatar" :src="avatar" mode="aspectFill" @click="goToAvatar"></image>
        <view class="info">
          <text class="name">{{ userInfo.name || '维修人员' }}</text>
          <text class="role">维修工程师</text>
        </view>
      </view>
      <view class="logout-btn" @click="handleLogout">
        <uni-icons type="logout" size="20" color="#666"></uni-icons>
      </view>
    </view>

    <!-- 统计卡片 -->
    <view class="stats-cards">
      <view class="stat-card" @click="goToOrders('all')">
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
        <view class="order-item" v-for="order in recentOrders" :key="order.id" @click="goToOrderDetail(order.id)">
          <view class="order-header">
            <text class="order-id">#{{ order.id }}</text>
            <view class="order-status" :class="order.status">{{ getStatusText(order.status) }}</view>
          </view>
          <view class="order-content">
            <text class="order-title">{{ order.title }}</text>
            <text class="order-location">{{ order.location }}</text>
          </view>
          <view class="order-footer">
            <text class="order-time">{{ order.createTime }}</text>
            <text class="order-reporter">报修人：{{ order.reporter }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "RepairDashboard",
  computed: {
    avatar() {
      return this.$store.getters.avatar
    }
  },
  data() {
    return {
      userInfo: {
        name: '张师傅'
      },
      orderStats: {
        total: 25,
        pending: 8,
        processing: 5,
        completed: 12
      },
      recentOrders: [
        {
          id: 'R2024001',
          title: 'JUKI DDL-8700 断线故障',
          location: '3号厂房 2楼 设备区',
          reporter: '李小明',
          status: 'pending',
          createTime: '2024-01-15 14:30'
        },
        {
          id: 'R2024002',
          title: '兄弟牌缝纫机异响',
          location: '1号厂房 1楼 A组',
          reporter: '王小红',
          status: 'processing',
          createTime: '2024-01-15 10:20'
        },
        {
          id: 'R2024003',
          title: '重机缝纫机卡线',
          location: '2号厂房 3楼 B组',
          reporter: '赵小刚',
          status: 'completed',
          createTime: '2024-01-14 16:45'
        }
      ]
    }
  },
  methods: {
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
        url: '/pages/repair/info/index'
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
    getStatusText(status) {
      const statusMap = {
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成'
      }
      return statusMap[status] || '未知'
    },
    computed:{
      avatar() {
        return this.$store.state.user.avatar
      },
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
  gap: 16rpx;
  margin-bottom: 32rpx;

  .stat-card {
    flex: 1;
    background: #fff;
    border-radius: 16rpx;
    padding: 32rpx 24rpx;
    text-align: center;
    box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

    &.pending {
      border-top: 6rpx solid #FF9500;
    }

    &.processing {
      border-top: 6rpx solid #007AFF;
    }

    &.completed {
      border-top: 6rpx solid #34C759;
    }

    .stat-number {
      font-size: 48rpx;
      font-weight: 700;
      color: #333;
      margin-bottom: 8rpx;
    }

    .stat-label {
      font-size: 24rpx;
      color: #666;
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
        }
      }

      .order-content {
        margin-bottom: 16rpx;

        .order-title {
          display: block;
          font-size: 26rpx;
          color: #333;
          margin-bottom: 8rpx;
        }

        .order-location {
          font-size: 22rpx;
          color: #666;
        }
      }

      .order-footer {
        display: flex;
        justify-content: space-between;
        font-size: 20rpx;
        color: #999;
      }
    }
  }
}
</style>
