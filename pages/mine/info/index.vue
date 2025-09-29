<template>
  <view class="info-container">
    <!-- 头像区域 -->
    <view class="avatar-section">
      <view class="avatar-wrapper" @click="goToAvatar">
        <image class="avatar" :src="avatar" mode="aspectFill"></image>
        <view class="avatar-edit">
          <uni-icons type="camera" size="20" ></uni-icons>
        </view>
      </view>
      <text class="avatar-tip">点击修改头像</text>
      <text class="user-role">{{ user.nickName || '用户' }}</text>
    </view>

    <!-- 个人信息列表 -->
    <view class="info-list">
      <uni-list>
        <uni-list-item 
          showExtraIcon="true" 
          :extraIcon="{type: 'person-filled'}" 
          title="昵称" 
          :rightText="user.nickName || '未设置'" 
          clickable
          @click="goToEdit"
        />
        <uni-list-item 
          showExtraIcon="true" 
          :extraIcon="{type: 'phone-filled'}" 
          title="手机号码" 
          :rightText="user.phonenumber || '未设置'" 
          clickable
          @click="goToEdit"
        />
        <uni-list-item 
          showExtraIcon="true" 
          :extraIcon="{type: 'email-filled'}" 
          title="邮箱" 
          :rightText="user.email || '未设置'" 
          clickable
          @click="goToEdit"
        />
        <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled'}" title="岗位" :rightText="postGroup" />
        <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled'}" title="角色" :rightText="roleGroup" />
        <uni-list-item showExtraIcon="true" :extraIcon="{type: 'calendar-filled'}" title="创建日期" :rightText="user.createTime" />
      </uni-list>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section">
      <button class="action-btn primary" @click="goToEdit">
        <uni-icons type="compose" size="18" color="#fff"></uni-icons>
        <text>编辑资料</text>
      </button>
      <button class="action-btn secondary" @click="goToPassword">
        <uni-icons type="locked" size="18" color="#007AFF"></uni-icons>
        <text>修改密码</text>
      </button>
    </view>
  </view>
</template>

<script>
  import { getUserProfile } from "@/api/system/user"

  export default {
    computed: {
      avatar() {
        return this.$store.getters.avatar
      }
    },
    data() {
      return {
        user: {},
        roleGroup: "",
        postGroup: ""
      }
    },
    onLoad() {
      this.getUser()
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data
          this.roleGroup = response.roleGroup
          this.postGroup = response.postGroup
        })
      },
      goToEdit() {
        uni.navigateTo({
          url: '/pages/mine/info/edit'
        })
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

<style lang="scss">
  page {
    background-color: #f5f6fa;
  }

  .info-container {
    min-height: 100vh;
    background: #f5f6fa;
    padding: 20rpx;
  }

  // 头像区域
  .avatar-section {
    background: linear-gradient(135deg, #007AFF 0%, #5AC8FA 100%);
    border-radius: 16rpx;
    padding: 40rpx;
    text-align: center;
    margin-bottom: 24rpx;
    box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.15);

    .avatar-wrapper {
      position: relative;
      display: inline-block;
      margin-bottom: 16rpx;

      .avatar {
        width: 160rpx;
        height: 160rpx;
        border-radius: 50%;
        border: 4rpx solid rgba(255, 255, 255, 0.3);
      }

      .avatar-edit {
        position: absolute;
        bottom: 0;
        right: 0;
        width: 48rpx;
        height: 48rpx;
        background: rgba(255, 255, 255, 0.9);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 4rpx solid #007AFF;
      }
    }

    .avatar-tip {
      display: block;
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.8);
      margin-bottom: 8rpx;
    }

    .user-role {
      display: block;
      font-size: 28rpx;
      color: #fff;
      font-weight: 600;
    }
  }

  // 信息列表样式
  .info-list {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 24rpx;
    box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
  }

  // 操作按钮
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
        background: linear-gradient(135deg, #007AFF 0%, #5AC8FA 100%);
        color: #fff;
        box-shadow: 0 6rpx 20rpx rgba(0, 122, 255, 0.3);

        text {
          margin-left: 12rpx;
        }
      }

      &.secondary {
        background: #fff;
        color: #007AFF;
        border: 2rpx solid #007AFF;

        text {
          margin-left: 12rpx;
        }
      }
    }
  }
</style>
