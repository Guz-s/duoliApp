<template>
  <view class="info-container">
    <!-- 头像区域 -->
    <view class="avatar-section">
      <view class="avatar-wrapper" @click="goToAvatar">
        <image class="avatar" :src="avatar" mode="aspectFill"></image>
        <view class="avatar-edit">
          <uni-icons type="camera" size="20" color="#fff"></uni-icons>
        </view>
      </view>
      <text class="avatar-tip">点击修改头像</text>
    </view>

    <!-- 个人信息列表 -->
    <view class="info-list">
      <view class="info-item">
        <view class="info-label">
          <uni-icons type="person-filled" size="20" color="#007AFF"></uni-icons>
          <text>姓名</text>
        </view>
        <text class="info-value">{{ userInfo.name || '张师傅' }}</text>
      </view>

      <view class="info-item">
        <view class="info-label">
          <uni-icons type="phone-filled" size="20" color="#007AFF"></uni-icons>
          <text>手机号码</text>
        </view>
        <text class="info-value">{{ userInfo.phone || '138****1234' }}</text>
      </view>

      <view class="info-item">
        <view class="info-label">
          <uni-icons type="email-filled" size="20" color="#007AFF"></uni-icons>
          <text>邮箱</text>
        </view>
        <text class="info-value">{{ userInfo.email || 'zhang@example.com' }}</text>
      </view>

      <view class="info-item">
        <view class="info-label">
          <uni-icons type="auth-filled" size="20" color="#007AFF"></uni-icons>
          <text>工号</text>
        </view>
        <text class="info-value">{{ userInfo.workId || 'R001' }}</text>
      </view>

      <view class="info-item">
        <view class="info-label">
          <uni-icons type="staff-filled" size="20" color="#007AFF"></uni-icons>
          <text>部门</text>
        </view>
        <text class="info-value">{{ userInfo.department || '维修部' }}</text>
      </view>

      <view class="info-item">
        <view class="info-label">
          <uni-icons type="gear-filled" size="20" color="#007AFF"></uni-icons>
          <text>职位</text>
        </view>
        <text class="info-value">{{ userInfo.position || '维修工程师' }}</text>
      </view>

      <view class="info-item">
        <view class="info-label">
          <uni-icons type="calendar-filled" size="20" color="#007AFF"></uni-icons>
          <text>入职日期</text>
        </view>
        <text class="info-value">{{ userInfo.joinDate || '2020-01-15' }}</text>
      </view>

      <view class="info-item">
        <view class="info-label">
          <uni-icons type="star-filled" size="20" color="#007AFF"></uni-icons>
          <text>技能等级</text>
        </view>
        <text class="info-value">{{ userInfo.skillLevel || '高级技师' }}</text>
      </view>
    </view>

    <!-- 统计信息 -->
    <view class="stats-section">
      <view class="section-title">工作统计</view>
      <view class="stats-grid">
        <view class="stat-item">
          <text class="stat-number">{{ workStats.totalRepairs }}</text>
          <text class="stat-label">总维修数</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ workStats.completedRepairs }}</text>
          <text class="stat-label">已完成</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ workStats.avgTime }}</text>
          <text class="stat-label">平均用时(小时)</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ workStats.rating }}</text>
          <text class="stat-label">用户评分</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section">
      <button class="action-btn primary" @click="goToAvatar">
        <uni-icons type="camera" size="18" color="#fff"></uni-icons>
        <text>修改头像</text>
      </button>
      <button class="action-btn secondary" @click="goToPassword">
        <uni-icons type="locked" size="18" color="#007AFF"></uni-icons>
        <text>修改密码</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  name: "RepairInfo",
  computed: {
    avatar() {
      return this.$store.getters.avatar
    }
  },
  data() {
    return {
      userInfo: {
        name: '张师傅',
        phone: '138****1234',
        email: 'zhang@example.com',
        workId: 'R001',
        department: '维修部',
        position: '维修工程师',
        joinDate: '2020-01-15',
        skillLevel: '高级技师',
        avatar: '/static/images/profile.jpg'
      },
      workStats: {
        totalRepairs: 156,
        completedRepairs: 142,
        avgTime: 2.5,
        rating: 4.8
      }
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      // 这里应该从API获取用户信息
      // 现在使用模拟数据
      console.log('加载维修人员信息')
    },
    goToAvatar() {
      uni.navigateTo({
        url: '/pages/mine/avatar/index'
      })
    },
    goToPassword() {
      uni.navigateTo({
        url: '/pages/mine/pwd/index'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.info-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 20rpx;
}

.avatar-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
  text-align: center;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

  .avatar-wrapper {
    position: relative;
    display: inline-block;
    margin-bottom: 16rpx;

    .avatar {
      width: 160rpx;
      height: 160rpx;
      border-radius: 50%;
      border: 4rpx solid #f0f0f0;
    }

    .avatar-edit {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 48rpx;
      height: 48rpx;
      background: #007AFF;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 4rpx solid #fff;
    }
  }

  .avatar-tip {
    font-size: 24rpx;
    color: #666;
  }
}

.info-list {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

  .info-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 32rpx;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .info-label {
      display: flex;
      align-items: center;
      font-size: 28rpx;
      color: #333;

      text {
        margin-left: 16rpx;
      }
    }

    .info-value {
      font-size: 28rpx;
      color: #666;
      text-align: right;
    }
  }
}

.stats-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);

  .section-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 24rpx;
  }

  .stats-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 24rpx;

    .stat-item {
      flex: 1;
      min-width: 140rpx;
      text-align: center;
      padding: 24rpx 16rpx;
      background: #f8f9fa;
      border-radius: 12rpx;

      .stat-number {
        display: block;
        font-size: 36rpx;
        font-weight: 700;
        color: #007AFF;
        margin-bottom: 8rpx;
      }

      .stat-label {
        font-size: 22rpx;
        color: #666;
      }
    }
  }
}

.action-section {
  display: flex;
  gap: 24rpx;

  .action-btn {
    flex: 1;
    height: 88rpx;
    border-radius: 44rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28rpx;
    font-weight: 600;
    border: none;

    &.primary {
      background: #007AFF;
      color: #fff;

      text {
        margin-left: 12rpx;
      }
    }

    &.secondary {
      background: #f0f0f0;
      color: #007AFF;

      text {
        margin-left: 12rpx;
      }
    }
  }
}
</style>
