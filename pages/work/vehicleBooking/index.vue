<template>
  <view class="vehicle-booking-container">
    <!-- 页面标题 -->
    <uni-nav-bar left-icon="left" title="车辆预约" @clickLeft="goBack" background-color="#007AFF" color="#fff"></uni-nav-bar>
    
    <!-- 预约表单 -->
    <view class="booking-form">
      <uni-forms ref="form" :modelValue="formData" :rules="rules">
        <!-- 预约类型 -->
        <uni-forms-item label="预约类型" name="bookingType" required>
          <uni-data-select 
            v-model="formData.bookingType" 
            :localdata="bookingTypes"
            placeholder="请选择预约类型"
          ></uni-data-select>
        </uni-forms-item>
        
        <!-- 用车时间 -->
        <uni-forms-item label="出发时间" name="useTime" required>
          <uni-datetime-picker 
            v-model="formData.useTime" 
            type="datetime" 
            :clear-icon="false"
            placeholder="请选择出发时间"
          ></uni-datetime-picker>
        </uni-forms-item>
        
        <!-- 预计归还时间 -->
        <uni-forms-item label="回程时间" name="returnTime" required>
          <uni-datetime-picker 
            v-model="formData.returnTime" 
            type="datetime" 
            :clear-icon="false"
            placeholder="请选择回程时间"
          ></uni-datetime-picker>
        </uni-forms-item>
        
        <!-- 出发地点 -->
        <uni-forms-item label="出发地点" name="startLocation" required>
          <uni-easyinput 
            v-model="formData.startLocation" 
            placeholder="请输入出发地点"
          ></uni-easyinput>
        </uni-forms-item>
        
        <!-- 目的地 -->
        <uni-forms-item label="目的地" name="destination" required>
          <uni-easyinput 
            v-model="formData.destination" 
            placeholder="请输入目的地"
          ></uni-easyinput>
        </uni-forms-item>
        
        <!-- 乘车人数 -->
        <uni-forms-item label="乘车人数" name="passengerCount" required>
          <uni-number-box 
            v-model="formData.passengerCount" 
            :min="1" 
            :max="8"
          ></uni-number-box>
        </uni-forms-item>
        
        <!-- 预约事由 -->
        <uni-forms-item label="预约事由" name="reason" required>
          <uni-easyinput 
            v-model="formData.reason" 
            type="textarea" 
            placeholder="请详细说明用车事由，如：公务出差、会议接送、培训学习等"
            :maxlength="200"
          ></uni-easyinput>
        </uni-forms-item>
        
        <!-- 紧急联系人 -->
        <uni-forms-item label="紧急联系人" name="emergencyContact">
          <uni-easyinput 
            v-model="formData.emergencyContact" 
            placeholder="请输入紧急联系人姓名，如：张三"
          ></uni-easyinput>
        </uni-forms-item>
        
        <!-- 联系人电话 -->
        <uni-forms-item label="联系电话" name="emergencyPhone">
          <uni-easyinput 
            v-model="formData.emergencyPhone" 
            placeholder="请输入联系人电话，如：13800138000"
            type="number"
          ></uni-easyinput>
        </uni-forms-item>
      </uni-forms>
      
      <!-- 提交按钮 -->
      <view class="submit-section">
        <button class="submit-btn" @click="submitBooking" :disabled="isSubmitting">
          {{ isSubmitting ? '提交中...' : '提交' }}
        </button>
        <button class="cancel-btn" @click="resetForm">重置</button>
      </view>
    </view>
    
    <!-- 预约须知 -->
    <view class="notice-section">
      <uni-section title="预约须知" type="line">
        <view class="notice-content">
          <view class="notice-item">
            <text class="notice-number">1.</text>
            <text class="notice-text">请提前至少2小时进行车辆预约，以便我们安排车辆</text>
          </view>
          <view class="notice-item">
            <text class="notice-number">2.</text>
            <text class="notice-text">预约成功后请按时用车，如需取消请提前联系管理员，以便我们安排其他车辆</text>
          </view>
          <view class="notice-item">
            <text class="notice-number">3.</text>
            <text class="notice-text">使用车辆时请遵守交通规则，确保安全驾驶，如有违反交通规则，将承担相应的法律责任</text>
          </view>
          <view class="notice-item">
            <text class="notice-number">4.</text>
            <text class="notice-text">车辆使用完毕后请及时归还并做好清洁工作，如有损坏请及时通知管理员</text>
          </view>
        </view>
      </uni-section>
    </view>
  </view>
</template>

<script>
import UniNavBar from "@/uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.vue";
import UniForms from "@/uni_modules/uni-forms/components/uni-forms/uni-forms.vue";
import UniFormsItem from "@/uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue";
import UniDataSelect from "@/uni_modules/uni-data-select/components/uni-data-select/uni-data-select.vue";
import UniDatetimePicker from "@/uni_modules/uni-datetime-picker/components/uni-datetime-picker/uni-datetime-picker.vue";
import UniEasyinput from "@/uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue";
import UniNumberBox from "@/uni_modules/uni-number-box/components/uni-number-box/uni-number-box.vue";
import UniSection from "@/components/uni-section/uni-section.vue";

export default {
  components: {
    UniNavBar,
    UniForms,
    UniFormsItem,
    UniDataSelect,
    UniDatetimePicker,
    UniEasyinput,
    UniNumberBox,
    UniSection
  },
  data() {
    return {
      isSubmitting: false,
      formData: {
        bookingType: '',
        useTime: '',
        returnTime: '',
        startLocation: '',
        destination: '',
        passengerCount: 1,
        reason: '',
        emergencyContact: '',
        emergencyPhone: ''
      },
      bookingTypes: [
        { value: 'business', text: '公务出行' },
        { value: 'meeting', text: '会议接送' },
        { value: 'airport', text: '机场接送' },
        { value: 'training', text: '培训学习' },
        { value: 'other', text: '其他' }
      ],
      rules: {
        bookingType: {
          rules: [{ required: true, errorMessage: '请选择预约类型' }]
        },
        useTime: {
          rules: [{ required: true, errorMessage: '请选择出发时间' }]
        },
        returnTime: {
          rules: [{ required: true, errorMessage: '请选择回程时间' }]
        },
        startLocation: {
          rules: [{ required: true, errorMessage: '请输入出发地点' }]
        },
        destination: {
          rules: [{ required: true, errorMessage: '请输入目的地' }]
        },
        passengerCount: {
          rules: [{ required: true, errorMessage: '请选择乘车人数' }]
        },
        reason: {
          rules: [{ required: true, errorMessage: '请填写预约事由' }]
        }
      }
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },
    
    async submitBooking() {
      try {
        // 表单验证
        const isValid = await this.$refs.form.validate();
        if (!isValid) {
          return;
        }
        
        // 时间校验
        if (new Date(this.formData.useTime) >= new Date(this.formData.returnTime)) {
          uni.showToast({
            title: '回程时间必须晚于出发时间',
            icon: 'none'
          });
          return;
        }
        
        // 提前时间校验
        const now = new Date();
        const useTime = new Date(this.formData.useTime);
        const timeDiff = (useTime - now) / (1000 * 60 * 60); // 小时差
        
        if (timeDiff < 2) {
          uni.showToast({
            title: '请至少提前2小时预约，以便我们安排车辆',
            icon: 'none'
          });
          return;
        }
        
        this.isSubmitting = true;
        
        // 模拟提交请求
        await new Promise(resolve => setTimeout(resolve, 2000));
        
        uni.showToast({
          title: '预约提交成功，请等待管理员审核',
          icon: 'success'
        });
        
        // 延迟返回上一页
        setTimeout(() => {
          uni.navigateBack();
        }, 1500);
        
      } catch (error) {
        console.error('提交失败:', error);
        uni.showToast({
          title: '提交失败，请重试，如有问题请联系管理员',
          icon: 'none'
        });
      } finally {
        this.isSubmitting = false;
      }
    },
    
    resetForm() {
      this.formData = {
        bookingType: '',
        useTime: '',
        returnTime: '',
        startLocation: '',
        destination: '',
        passengerCount: 1,
        reason: '',
        emergencyContact: '',
        emergencyPhone: ''
      };
      
      // 清除表单验证状态
      this.$refs.form.clearValidate();
    }
  }
}
</script>

<style lang="scss" scoped>
.vehicle-booking-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f0f8ff 0%, #f8faff 100%);
  padding-bottom: 40rpx;
}

.booking-form {
  background-color: #fff;
  margin: 20rpx 20rpx 0;
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 122, 255, 0.1);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 6rpx;
    background: linear-gradient(90deg, #007AFF, #5856D6, #AF52DE);
  }
}

.submit-section {
  margin-top: 80rpx;
  display: flex;
  gap: 30rpx;
  
  .submit-btn {
    flex: 2;
    background: linear-gradient(135deg, #007AFF 0%, #5856D6 50%, #AF52DE 100%);
    color: #fff;
    border: none;
    border-radius: 24rpx;
    height: 96rpx;
    font-size: 34rpx;
    font-weight: 600;
    box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.3);
    transition: all 0.3s ease;
    
    &:active {
      transform: translateY(2rpx);
      box-shadow: 0 4rpx 16rpx rgba(0, 122, 255, 0.2);
    }
    
    &:disabled {
      background: linear-gradient(135deg, #ccc, #999);
      box-shadow: none;
      transform: none;
    }
  }
  
  .cancel-btn {
    flex: 1;
    background-color: #f8f9fa;
    color: #6c757d;
    border: 2rpx solid #e9ecef;
    border-radius: 24rpx;
    height: 96rpx;
    font-size: 30rpx;
    font-weight: 500;
    transition: all 0.3s ease;
    
    &:active {
      background-color: #e9ecef;
      border-color: #dee2e6;
    }
  }
}

.notice-section {
  background-color: #fff;
  margin: 30rpx 20rpx 0;
  border-radius: 24rpx;
  padding: 40rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  
  .notice-content {
    padding-top: 20rpx;
    
    .notice-item {
      display: flex;
      align-items: flex-start;
      margin-bottom: 30rpx;
      padding: 20rpx;
      background: linear-gradient(135deg, #f8f9ff 0%, #fff 100%);
      border-radius: 16rpx;
      border-left: 6rpx solid #007AFF;
      
      .notice-number {
        color: #007AFF;
        font-weight: 700;
        width: 48rpx;
        height: 48rpx;
        line-height: 48rpx;
        text-align: center;
        background: rgba(0, 122, 255, 0.1);
        border-radius: 50%;
        flex-shrink: 0;
        font-size: 26rpx;
      }
      
      .notice-text {
        color: #495057;
        font-size: 28rpx;
        line-height: 1.8;
        margin-left: 24rpx;
        flex: 1;
      }
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

/* 表单样式优化 */
/deep/ .uni-forms-item {
  margin-bottom: 40rpx;
  position: relative;
  
  .uni-forms-item__label {
    font-weight: 600;
    color: #2c3e50;
    font-size: 30rpx;
    margin-bottom: 16rpx;
    position: relative;
    
    &::before {
      content: '';
      position: absolute;
      left: -16rpx;
      top: 50%;
      transform: translateY(-50%);
      width: 6rpx;
      height: 32rpx;
      background: linear-gradient(180deg, #007AFF, #5856D6);
      border-radius: 3rpx;
    }
  }
  
  .uni-forms-item__content {
    position: relative;
  }
}

/deep/ .uni-easyinput {
  .uni-easyinput__content {
    border: 2rpx solid #e9ecef;
    border-radius: 16rpx;
    background-color: #fff;
    transition: all 0.3s ease;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
    
    &.is-focused {
      border-color: #007AFF;
      box-shadow: 0 0 0 6rpx rgba(0, 122, 255, 0.1);
    }
  }
  
  .uni-easyinput__content-input,
  .uni-easyinput__content-textarea {
    font-size: 30rpx;
    color: #2c3e50;
    
    &::placeholder {
      color: #adb5bd;
      font-size: 28rpx;
    }
  }
}

/deep/ .uni-data-select {
  .uni-data-select__input-box {
    border: 2rpx solid #e9ecef;
    border-radius: 16rpx;
    background-color: #fff;
    transition: all 0.3s ease;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
    
    &:focus-within {
      border-color: #007AFF;
      box-shadow: 0 0 0 6rpx rgba(0, 122, 255, 0.1);
    }
  }
  
  .uni-data-select__input {
    font-size: 30rpx;
    color: #2c3e50;
    
    &.uni-data-select__input--placeholder {
      color: #adb5bd;
      font-size: 28rpx;
    }
  }
}

/deep/ .uni-datetime-picker {
  .uni-datetime-picker--btn {
    border: 2rpx solid #e9ecef;
    border-radius: 16rpx;
    background-color: #fff;
    transition: all 0.3s ease;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
    padding: 24rpx 32rpx;
    
    &:active {
      border-color: #007AFF;
      box-shadow: 0 0 0 6rpx rgba(0, 122, 255, 0.1);
    }
  }
  
  .uni-datetime-picker-text {
    font-size: 30rpx;
    color: #2c3e50;
  }
  
  .uni-datetime-picker-placeholder {
    color: #adb5bd;
    font-size: 28rpx;
  }
}

/deep/ .uni-number-box {
  .uni-number-box__plus,
  .uni-number-box__minus {
    width: 64rpx;
    height: 64rpx;
    border-radius: 16rpx;
    background: linear-gradient(135deg, #007AFF, #5856D6);
    color: #fff;
    border: none;
    font-size: 32rpx;
    font-weight: 600;
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.95);
    }
  }
  
  .uni-number-box__num {
    margin: 0 24rpx;
    padding: 16rpx 32rpx;
    border: 2rpx solid #e9ecef;
    border-radius: 16rpx;
    background-color: #fff;
    font-size: 30rpx;
    font-weight: 600;
    color: #2c3e50;
    text-align: center;
    min-width: 120rpx;
  }
}

/* 导航栏样式 */
/deep/ .uni-navbar {
  box-shadow: 0 2rpx 16rpx rgba(0, 122, 255, 0.15);
}

/* 段落标题样式 */
/deep/ .uni-section {
  .uni-section__head {
    .uni-section__head-tag {
      background: linear-gradient(135deg, #007AFF, #5856D6);
    }
    
    .uni-section__head-content {
      font-size: 32rpx;
      font-weight: 700;
      color: #2c3e50;
    }
  }
}

/* 全局动画 */
.vehicle-booking-container * {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
</style>
