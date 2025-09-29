<script>
import { getRepairByCode, listRepairData } from "@/api/repair/repair";
import { processFaultImages } from "@/utils/dataConverter";

export default {
  name: "RepairOrders",
  data() {
    return {
      repairList:[],
      currentTab: 0,
      tabList: [
        { name: '待处理' },
        { name: '处理中' },
        { name: '已完成' },
        { name: '已取消' }
      ],
    }
  },
  onShow() {
    // 每次页面显示时刷新数据，包括从编辑页面返回时
    this.getRepairListFun();
  },
  computed: {
    currentOrders() {
      // 根据 status 过滤数据：0-待处理，1-处理中，2-已完成，3-已取消
      // 确保 repairList 是数组，如果不是则返回空数组
      if (!this.repairList || !Array.isArray(this.repairList)) {
        console.info('repairList不是数组或为空:', this.repairList)
        return []
      }

      // 处理 status 可能是字符串的情况
      const filtered = this.repairList.filter(item => {
        const itemStatus = parseInt(item.status)
        const match = itemStatus === this.currentTab
        return match
      })
      return filtered
    },
    tabLineLeft() {
      // 4个标签页，每个标签页宽度为 100% / 4 = 25%
      // 以rpx为单位，屏幕宽度750rpx，每个标签宽度187.5rpx
      const tabWidth = 750 / 4 // 187.5rpx
      const lineWidth = 60 // 下划线宽度60rpx
      const offset = (tabWidth - lineWidth) / 2 // 居中偏移量
      return (this.currentTab * tabWidth) + offset
    },
    tabLineWidth() {
      return 60 // 下划线宽度
    }
  },
  methods: {
    // 处理故障图片数据
    getFaultImages(order) {
      return processFaultImages(order.faultImages)
    },

    // 获取工单列表
    getRepairListFun(){
      listRepairData().then(res => {
          this.repairList = res.rows
          console.info('repairList:', this.repairList)
        } ).catch(err => {
          this.repairList = []
          console.error('获取工单列表失败:', err)
        })
    },
    // 选项卡切换
    tabChange(index) {
      this.currentTab = index
    },
    // 查看工单详情
    viewOrderDetail(order) {
      console.info('查看工单详情:', order)
      // 如果是待处理状态，跳转到编辑页面
      if (order[0].status == 0) {
        this.editApplication(order[0])
        return
      }

      // 其他状态跳转到详情页面查看
      uni.navigateTo({
        url: `/pages/repair/orders/detail?id=${order[0].orderId}&from=myOrders`
      })
    },
    // 编辑申请
    editApplication(order) {
      console.info('编辑工单:', order)

      // 跳转到报修申请页面进行编辑，传递工单ID
      uni.navigateTo({
        url: `/pages/work/carWarranty/index?mode=edit&orderId=${order[0].orderId}`
      })
    },
    // 获取状态文本
    getStatusText(statusIndex) {
      const statusMap = ['待处理', '处理中', '已完成', '已取消']
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
          @click="viewOrderDetail(currentOrders)"
        >
          <view class="order-header">
            <text class="order-id">#{{ order.orderNo}}</text>
            <view class="order-status" :class="'status-' + order.status">
              {{ getStatusText(order.status) }}
            </view>
          </view>

          <view class="order-content">
            <text class="order-title">{{ order.faultDescription }}</text>
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
            </view>
            <!--<text class="order-desc">{{ order.faultDescription }}</text>-->
          </view>

          <view class="order-footer">
            <text class="order-time">{{ order.createTime }}</text>
            <view class="order-actions">
              <text class="action-text" v-if="order.status == 0">点击编辑</text>
              <text class="action-text" v-else-if="order.status == 1">维修中</text>
              <text class="action-text" v-else-if="order.status == 2">已完成</text>
              <text class="action-text" v-else-if="order.status == 3">已取消</text>
            </view>
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
        font-size: 35rpx;
        font-weight: 600;
        color: #333;
      }

      .order-status {
        padding: 8rpx 16rpx;
        border-radius: 20rpx;
        font-size: 20rpx;
        color: #fff;

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
