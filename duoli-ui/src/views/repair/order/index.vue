<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入工单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工单标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入工单标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报修人ID" prop="reporterId">
        <el-input
          v-model="queryParams.reporterId"
          placeholder="请输入报修人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报修人姓名" prop="reporterName">
        <el-input
          v-model="queryParams.reporterName"
          placeholder="请输入报修人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报修人电话" prop="reporterPhone">
        <el-input
          v-model="queryParams.reporterPhone"
          placeholder="请输入报修人电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报修部门ID" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入报修部门ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门/车间" prop="department">
        <el-input
          v-model="queryParams.department"
          placeholder="请输入部门/车间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报修地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入报修地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备ID" prop="equipmentId">
        <el-input
          v-model="queryParams.equipmentId"
          placeholder="请输入设备ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备型号" prop="equipmentModel">
        <el-input
          v-model="queryParams.equipmentModel"
          placeholder="请输入设备型号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="故障等级" prop="faultLevel">
        <el-input
          v-model="queryParams.faultLevel"
          placeholder="请输入故障等级"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="其他等级描述" prop="faultLevelDesc">
        <el-input
          v-model="queryParams.faultLevelDesc"
          placeholder="请输入其他等级描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-input
          v-model="queryParams.priority"
          placeholder="请输入优先级"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分配给的维修人员ID" prop="assignedTo">
        <el-input
          v-model="queryParams.assignedTo"
          placeholder="请输入分配给的维修人员ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="维修开始时间" prop="repairStartTime">
        <el-date-picker clearable
          v-model="queryParams.repairStartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择维修开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="维修结束时间" prop="repairEndTime">
        <el-date-picker clearable
          v-model="queryParams.repairEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择维修结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="维修用时" prop="repairDuration">
        <el-input
          v-model="queryParams.repairDuration"
          placeholder="请输入维修用时"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="维修费用" prop="repairCost">
        <el-input
          v-model="queryParams.repairCost"
          placeholder="请输入维修费用"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="满意度评分" prop="satisfactionRating">
        <el-input
          v-model="queryParams.satisfactionRating"
          placeholder="请输入满意度评分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['repair:order:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['repair:order:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['repair:order:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="el-icon-upload2"
            size="mini"
            @click="handleImport"
            v-hasPermi="['repair:order:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['repair:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单ID" align="center" prop="orderId" min-width="180" show-overflow-tooltip/>
      <el-table-column label="工单编号" align="center" prop="orderNo" />
      <el-table-column label="工单标题" align="center" prop="title" />
      <el-table-column label="报修人ID" align="center" prop="reporterId" />
      <el-table-column label="报修人姓名" align="center" prop="reporterName" />
      <el-table-column label="报修人电话" align="center" prop="reporterPhone" />
      <el-table-column label="报修部门ID" align="center" prop="deptId" />
      <el-table-column label="部门/车间" align="center" prop="department" />
      <el-table-column label="报修地点" align="center" prop="location" />
      <el-table-column label="设备ID" align="center" prop="equipmentId" />
      <el-table-column label="设备型号" align="center" prop="equipmentModel" />
      <el-table-column label="故障等级" align="center" prop="faultLevel" />
      <el-table-column label="其他等级描述" align="center" prop="faultLevelDesc" />
      <el-table-column label="故障描述" align="center" prop="faultDescription" />
      <el-table-column label="故障图片" align="center" prop="faultImages" />
      <el-table-column label="工单状态" align="center" prop="status" />
      <el-table-column label="优先级" align="center" prop="priority" />
      <el-table-column label="分配给的维修人员ID" align="center" prop="assignedTo" />
      <el-table-column label="维修开始时间" align="center" prop="repairStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.repairStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="维修结束时间" align="center" prop="repairEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.repairEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="维修用时" align="center" prop="repairDuration" />
      <el-table-column label="维修结果描述" align="center" prop="repairResult" />
      <el-table-column label="维修费用" align="center" prop="repairCost" />
      <el-table-column label="满意度评分" align="center" prop="satisfactionRating" />
      <el-table-column label="满意度评价" align="center" prop="satisfactionComment" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['repair:order:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['repair:order:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改报修工单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入工单编号" />
        </el-form-item>
        <el-form-item label="工单标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入工单标题" />
        </el-form-item>
        <el-form-item label="报修人ID" prop="reporterId">
          <el-input v-model="form.reporterId" placeholder="请输入报修人ID" />
        </el-form-item>
        <el-form-item label="报修人姓名" prop="reporterName">
          <el-input v-model="form.reporterName" placeholder="请输入报修人姓名" />
        </el-form-item>
        <el-form-item label="报修人电话" prop="reporterPhone">
          <el-input v-model="form.reporterPhone" placeholder="请输入报修人电话" />
        </el-form-item>
        <el-form-item label="报修部门ID" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入报修部门ID" />
        </el-form-item>
        <el-form-item label="部门/车间" prop="department">
          <el-input v-model="form.department" placeholder="请输入部门/车间" />
        </el-form-item>
        <el-form-item label="报修地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入报修地点" />
        </el-form-item>
        <el-form-item label="设备ID" prop="equipmentId">
          <el-input v-model="form.equipmentId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="设备型号" prop="equipmentModel">
          <el-input v-model="form.equipmentModel" placeholder="请输入设备型号" />
        </el-form-item>
        <el-form-item label="故障等级" prop="faultLevel">
          <el-input v-model="form.faultLevel" placeholder="请输入故障等级" />
        </el-form-item>
        <el-form-item label="其他等级描述" prop="faultLevelDesc">
          <el-input v-model="form.faultLevelDesc" placeholder="请输入其他等级描述" />
        </el-form-item>
        <el-form-item label="故障描述" prop="faultDescription">
          <el-input v-model="form.faultDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="故障图片" prop="faultImages">
          <el-input v-model="form.faultImages" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input v-model="form.priority" placeholder="请输入优先级" />
        </el-form-item>
        <el-form-item label="分配给的维修人员ID" prop="assignedTo">
          <el-input v-model="form.assignedTo" placeholder="请输入分配给的维修人员ID" />
        </el-form-item>
        <el-form-item label="维修开始时间" prop="repairStartTime">
          <el-date-picker clearable
            v-model="form.repairStartTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择维修开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="维修结束时间" prop="repairEndTime">
          <el-date-picker clearable
            v-model="form.repairEndTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择维修结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="维修用时" prop="repairDuration">
          <el-input v-model="form.repairDuration" placeholder="请输入维修用时" />
        </el-form-item>
        <el-form-item label="维修结果描述" prop="repairResult">
          <el-input v-model="form.repairResult" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="维修费用" prop="repairCost">
          <el-input v-model="form.repairCost" placeholder="请输入维修费用" />
        </el-form-item>
        <el-form-item label="满意度评分" prop="satisfactionRating">
          <el-input v-model="form.satisfactionRating" placeholder="请输入满意度评分" />
        </el-form-item>
        <el-form-item label="满意度评价" prop="satisfactionComment">
          <el-input v-model="form.satisfactionComment" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
          ref="upload"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url + '?updateSupport=' + upload.updateSupport"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from "@/api/repair/order"
import {getToken} from "@/utils/auth";
export default {
  name: "Order",
  data() {

    return {
      // 导入参数
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 弹出层标题（导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/repair/order/importData"
      },
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 报修工单表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        title: null,
        reporterId: null,
        reporterName: null,
        reporterPhone: null,
        deptId: null,
        department: null,
        location: null,
        equipmentId: null,
        equipmentModel: null,
        faultLevel: null,
        faultLevelDesc: null,
        faultDescription: null,
        faultImages: null,
        status: null,
        priority: null,
        assignedTo: null,
        repairStartTime: null,
        repairEndTime: null,
        repairDuration: null,
        repairResult: null,
        repairCost: null,
        satisfactionRating: null,
        satisfactionComment: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "工单编号不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "工单标题不能为空", trigger: "blur" }
        ],
        reporterId: [
          { required: true, message: "报修人ID不能为空", trigger: "blur" }
        ],
        reporterName: [
          { required: true, message: "报修人姓名不能为空", trigger: "blur" }
        ],
        reporterPhone: [
          { required: true, message: "报修人电话不能为空", trigger: "blur" }
        ],
        location: [
          { required: true, message: "报修地点不能为空", trigger: "blur" }
        ],
        faultLevel: [
          { required: true, message: "故障等级不能为空", trigger: "blur" }
        ],
        faultDescription: [
          { required: true, message: "故障描述不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询报修工单列表 */
    getList() {
      this.loading = true
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        title: null,
        reporterId: null,
        reporterName: null,
        reporterPhone: null,
        deptId: null,
        department: null,
        location: null,
        equipmentId: null,
        equipmentModel: null,
        faultLevel: null,
        faultLevelDesc: null,
        faultDescription: null,
        faultImages: null,
        status: null,
        priority: null,
        assignedTo: null,
        repairStartTime: null,
        repairEndTime: null,
        repairDuration: null,
        repairResult: null,
        repairCost: null,
        satisfactionRating: null,
        satisfactionComment: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加报修工单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      getOrder(orderId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改报修工单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderId != null) {
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = row.orderId || this.ids
      this.$modal.confirm('是否确认删除报修工单编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('repair/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    },
/** 下载模板操作 */
importTemplate() {
  this.download('repair/order/importTemplate', {
  }, `template_${new Date().getTime()}.xlsx`)
},
/** 导入按钮操作 */
handleImport() {
  this.upload.title = "报修工单导入";
  this.upload.open = true;
},
// 文件上传中处理
handleFileUploadProgress(event, file, fileList) {
  this.upload.isUploading = true;
},
// 文件上传成功处理
handleFileSuccess(response, file, fileList) {
  this.upload.open = false;
  this.upload.isUploading = false;
  this.$refs.upload.clearFiles();
  this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  this.getList();
},
// 提交上传文件
submitFileForm() {
  this.$refs.upload.submit();
}
  }
}
</script>
