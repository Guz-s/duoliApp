<template>
  <view class="work-container">
     <!--轮播图 -->
    <uni-swiper-dot class="uni-swiper-dot-box" :info="data" :current="current" field="content">
      <swiper class="swiper-box" :current="swiperDotIndex" @change="changeSwiper">
        <swiper-item v-for="(item, index) in data" :key="index">
          <view class="swiper-item" @click="clickBannerItem(item)">
            <image :src="item.image" mode="aspectFill" :draggable="false" />
          </view>
        </swiper-item>
      </swiper>
    </uni-swiper-dot>

    <!-- 纵向导航栏 -->
    <view class="scroll-view-container">
      <scroll-view class="left-scroll-view" scroll-y="true">
        <view
          v-for="(item, index) in navList"
          :key="index"
          class="left-scroll-view-item"
          :class="{ active: activeNavIndex === index }"
          @click="changeNav(index)"
        >
          {{ item.name }}
        </view>
      </scroll-view>

      <!-- 内容区域 -->
      <scroll-view class="right-content" scroll-y="true" :scroll-top="scrollTop" :scroll-into-view="scrollIntoView" scroll-with-animation @scroll="onRightScroll">
        <view
          v-for="(item, index) in navList"
          :key="index"
          class="content-item"
          :id="'content'+index"
        >
          <uni-section :title="item.name" type="line"></uni-section>

          <!-- 宫格组件 -->
          <view v-if="index === 0" class="grid-body">
            <uni-grid :column="4" :showBorder="false" @change="changeGrid">

              <uni-grid-item>
                <view class="grid-item-box">
                  <!--<uni-icons type="bars" size="30"></uni-icons>-->
                  <u-icon name="chat-fill" size="30"></u-icon>
                  <text class="text">饭堂资讯</text>
                </view>
              </uni-grid-item>

            </uni-grid>
          </view>
          <view v-else-if="index === 1" class="grid-body">
            <uni-grid :column="4" :showBorder="false" @change="changeGrid">
              <uni-grid-item>
                <view class="grid-item-box">
                  <uni-icons type="auth-filled" size="30"></uni-icons>
                  <text class="text">来访预约</text>
                </view>
              </uni-grid-item>
              <uni-grid-item>
                <view class="grid-item-box">
                  <uni-icons type="staff-filled" size="30"></uni-icons>
                  <text class="text">招聘信息</text>
                </view>
              </uni-grid-item>
            </uni-grid>
          </view>
          <view v-else-if="index === 2" class="grid-body">
            <uni-grid :column="4" :showBorder="false" @change="changeGrid">
              <uni-grid-item>
                <view class="grid-item-box" @click="goThree('/pages/work/vehicleBooking/index')">
                  <uni-icons type="calendar-filled" size="30"></uni-icons>
                  <text class="text">车辆预约</text>
                </view>
              </uni-grid-item>
            </uni-grid>
          </view>
          <view v-else-if="index === 3" class="grid-body">
            <uni-grid :column="4" :showBorder="false">
              <uni-grid-item >
                <view class="grid-item-box"  @click="goThree('/pages/work/carWarranty/index')">
                  <u-icon name="error-circle-fill" size="30"></u-icon>
                  <text class="text">衣车报修</text>
                </view>
              </uni-grid-item>
              <uni-grid-item @change="changeGrid">
                <view class="grid-item-box">
                  <uni-icons type="gear-filled" size="30"></uni-icons>
                  <text class="text">宿舍管理</text>
                </view>
              </uni-grid-item>
            </uni-grid>
          </view>
          <view v-else-if="index === 4" class="grid-body">
            <uni-grid :column="4" :showBorder="false" @change="changeGrid">
              <uni-grid-item>
                <view class="grid-item-box">
                  <u-icon name="bell-fill" size="30"></u-icon>
                  <text class="text">公司公告</text>
                </view>
              </uni-grid-item>
              <uni-grid-item>
                <view class="grid-item-box">
                  <uni-icons type="fire-filled" size="30"></uni-icons>
                  <text class="text">工会活动</text>
                </view>
              </uni-grid-item>
            </uni-grid>
          </view>
          <view v-else-if="index === 5" class="grid-body">
            <uni-grid :column="4" :showBorder="false" @change="changeGrid">
              <uni-grid-item>
                <view class="grid-item-box">
                  <uni-icons type="list" size="30"></uni-icons>
                  <text class="text">其它</text>
                </view>
              </uni-grid-item>
            </uni-grid>
          </view>
          <!--<view class="content-box" v-else>-->
          <!--  <text>{{ item.content }}</text>-->
          <!--</view>-->
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
  import UniSection from "@/components/uni-section/uni-section.vue";
  import UniGridItem from "@/uni_modules/uni-grid/components/uni-grid-item/uni-grid-item.vue";
  import UniIcons from "@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue";
  import UniGrid from "@/uni_modules/uni-grid/components/uni-grid/uni-grid.vue";
  import { getUserProfile } from "@/api/system/user";
  import { listRepairData } from "@/api/repair/repair";

  export default {
    components: { UniGrid, UniIcons, UniGridItem, UniSection },
    data() {
      return {
        current: 0,
        swiperDotIndex: 0,
        data: [{
            image: '/static/images/banner/duolidamen.png'
          },
          {
            image: '/static/images/banner/TORAYLogo.jpg'
          }
        ],
        // 添加纵向导航相关数据
        activeNavIndex: 0,
        scrollTop: 0,
        scrollIntoView: '',
        scrollAnimation: false,
        // 右侧各内容块顶部位置（相对右侧容器）缓存
        sectionTops: [],
        // 内部节流用，避免频繁计算
        _isMeasuring: false,
        // 是否正在手动切换导航，避免滚动联动干扰
        _isManualNav: false,
        // 记录上次手动切换的目标索引，用于更精确的判断
        _lastManualIndex: -1,
        // 记录上次手动切换的时间戳
        _lastManualTime: 0,
        navList: [
          { name: '资讯管理', content: '' },
          { name: '人事管理', content: '' },
          { name: '预约服务', content: '' },
          { name: '运维管理', content: '' },
          { name: '公告管理',   content: '' },
          { name: '其他', content: '' }
        ]
      }
    },
    onLoad() {},
    mounted() {
      // 确保初始状态正确
      this.activeNavIndex = 0;
      // 首次渲染完成后测量一次
      this.measureSectionTops();
      // 图片或字体等异步资源加载后可能影响布局，再测一次
      setTimeout(() => {
        this.measureSectionTops();
        // 确保测量完成后，如果还在顶部，激活第一个导航项
        this.$nextTick(() => {
          if (this.activeNavIndex !== 0) {
            this.activeNavIndex = 0;
          }
        });
      }, 300);
    },
    methods: {
      clickBannerItem(item) {
        console.info(item)
      },
      changeSwiper(e) {
        this.current = e.detail.current
      },
      changeGrid(e) {
        this.$modal.showToast('模块建设中~')
      },
      goThree(url) {
        uni.navigateTo({
          url: url
        })
      },
      // 添加纵向导航切换方法
      changeNav(index) {
        this.activeNavIndex = index;
        // 记录手动切换的目标索引和时间戳
        this._lastManualIndex = index;
        this._lastManualTime = Date.now();
        // 标记为手动切换，避免滚动联动干扰
        this._isManualNav = true;
        // 清空之前的滚动目标，避免冲突
        this.scrollIntoView = '';
        // 下一帧再设置滚动目标
        this.$nextTick(() => {
          this.scrollIntoView = 'content' + index;
          // 滚动完成后恢复联动，增加延时确保滚动完全结束
          setTimeout(() => {
            this._isManualNav = false;
            // 滚动完成后，重新测量位置以确保准确性
            this.measureSectionTops();
          }, 1000);
        });
      },
      // 右侧滚动时联动左侧导航
      onRightScroll(e) {
        // 如果正在手动切换导航，忽略滚动联动
        if (this._isManualNav) return;

        // e.detail.scrollTop: 右侧容器已滚动的距离
        const scrollTop = e.detail.scrollTop || 0;
        if (!this.sectionTops || this.sectionTops.length === 0) {
          // 尚未测量，延迟测量后再联动
          this.measureSectionTops(() => this.updateActiveByScroll(scrollTop));
          return;
        }
        this.updateActiveByScroll(scrollTop);
      },
      updateActiveByScroll(scrollTop) {
        // 如果刚刚完成手动切换，给一个缓冲期，避免立即被覆盖
        if (this._lastManualIndex >= 0 && Date.now() - this._lastManualTime < 1500) {
          return;
        }

        // 找到当前在顶部的内容块
        let newIndex = 0;

        // 从前往后遍历，找到最后一个顶部位置小于等于当前滚动位置的区块
        for (let i = 0; i < this.sectionTops.length; i++) {
          const blockTop = this.sectionTops[i];
          // 如果当前滚动位置已经超过或等于这个区块的顶部
          if (scrollTop >= blockTop - 20) {
            newIndex = i;
          } else {
            // 如果还没到达这个区块，就停止查找
            break;
          }
        }

        // 如果找到了更合适的索引，就更新
        if (newIndex !== this.activeNavIndex) {
          this.activeNavIndex = newIndex;
        }
      },
      // 计算每个内容块相对右侧容器顶部的位置
      measureSectionTops(cb) {
        if (this._isMeasuring) return;
        this._isMeasuring = true;
        // 下一帧再测，确保DOM稳定
        this.$nextTick(() => {
          const query = uni.createSelectorQuery().in(this);
          query.select('.right-content').boundingClientRect(container => {
            if (!container) {
              this._isMeasuring = false;
              cb && cb();
              return;
            }
            const q2 = uni.createSelectorQuery().in(this);
            // 批量选取所有 section
            this.navList.forEach((_, idx) => {
              q2.select('#content' + idx).boundingClientRect();
            });
            q2.exec(rects => {
              // rects 是所有 select 的结果数组
              if (Array.isArray(rects)) {
                // 计算每个区块相对容器的顶部位置
                this.sectionTops = rects.map((r, idx) => {
                  if (r) {
                    const top = r.top - container.top;
                    return Math.max(0, top); // 确保不为负数
                  }
                  return 0;
                });
              }
              this._isMeasuring = false;
              cb && cb();
            });
          }).exec();
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  /* #ifndef APP-NVUE */
  page {
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    background-color: #fff;
    min-height: 100%;
    height: auto;
  }

  view {
    font-size: 14px;
    line-height: inherit;
  }
  /* #endif */

  .text {
    text-align: center;
    font-size: 26rpx;
    margin-top: 10rpx;
  }

  .grid-item-box {
    flex: 1;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 15px 0;
  }

  .uni-margin-wrap {
    width: 690rpx;
    width: 100%;
    ;
  }

  .swiper {
    height: 300rpx;
  }

  .swiper-box {
    height: 150px;
  }

  .swiper-item {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #fff;
    height: 300rpx;
    line-height: 300rpx;
  }

  @media screen and (min-width: 500px) {
    .uni-swiper-dot-box {
      width: 400px;
      /* #ifndef APP-NVUE */
      margin: 0 auto;
      /* #endif */
      margin-top: 8px;
    }

    .image {
      width: 100%;
    }
  }
  .scroll-view-container {
    display: flex;
    height: 500px;
    margin-top: 20px;

    .left-scroll-view {
      width: 120px;
      height: 100%;
      background-color: #f5f5f5;

      .left-scroll-view-item {
        background-color: #f5f5f5;
        line-height: 60px;
        text-align: center;
        font-size: 14px;
        border-bottom: 1px solid #e0e0e0;
        transition: all 0.3s;

        &.active {
          background-color: #007AFF;
          color: #fff;
        }
      }
    }

    .right-content {
      flex: 1;
      height: 100%;
      padding: 10px;
      box-sizing: border-box;
      /* 添加平滑滚动动画 */
      scroll-behavior: smooth;

      .content-item {
        margin-bottom: 20px;

        .content-box {
          padding: 15px;
          background-color: #f9f9f9;
          border-radius: 5px;
          min-height: 100px;
        }
      }
    }
  }
</style>
