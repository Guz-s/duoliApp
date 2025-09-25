package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报修工单对象 repair_order
 * 
 * @author duoli
 * @date 2025-09-25
 */
public class RepairOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工单ID */
    private String orderId;

    /** 工单编号（如：R2024001） */
    @Excel(name = "工单编号", readConverterExp = "如=：R2024001")
    private String orderNo;

    /** 工单标题 */
    @Excel(name = "工单标题")
    private String title;

    /** 报修人ID */
    @Excel(name = "报修人ID")
    private Long reporterId;

    /** 报修人姓名 */
    @Excel(name = "报修人姓名")
    private String reporterName;

    /** 报修人电话 */
    @Excel(name = "报修人电话")
    private String reporterPhone;

    /** 报修部门ID */
    @Excel(name = "报修部门ID")
    private Long deptId;

    /** 部门/车间 */
    @Excel(name = "部门/车间")
    private String department;

    /** 报修地点 */
    @Excel(name = "报修地点")
    private String location;

    /** 设备ID（可选） */
    @Excel(name = "设备ID", readConverterExp = "可=选")
    private Long equipmentId;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String equipmentModel;

    /** 故障等级（紧急/较急/一般/其他） */
    @Excel(name = "故障等级", readConverterExp = "紧=急/较急/一般/其他")
    private String faultLevel;

    /** 其他等级描述 */
    @Excel(name = "其他等级描述")
    private String faultLevelDesc;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String faultDescription;

    /** 故障图片（JSON格式存储图片路径数组） */
    @Excel(name = "故障图片", readConverterExp = "J=SON格式存储图片路径数组")
    private String faultImages;

    /** 工单状态（0待处理 1处理中 2已完成 3已取消） */
    @Excel(name = "工单状态", readConverterExp = "0=待处理,1=处理中,2=已完成,3=已取消")
    private String status;

    /** 优先级（1低 2中 3高 4紧急） */
    @Excel(name = "优先级", readConverterExp = "1=低,2=中,3=高,4=紧急")
    private Integer priority;

    /** 分配给的维修人员ID */
    @Excel(name = "分配给的维修人员ID")
    private Long assignedTo;

    /** 维修开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "维修开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date repairStartTime;

    /** 维修结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "维修结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date repairEndTime;

    /** 维修用时（分钟） */
    @Excel(name = "维修用时", readConverterExp = "分=钟")
    private Long repairDuration;

    /** 维修结果描述 */
    @Excel(name = "维修结果描述")
    private String repairResult;

    /** 维修费用 */
    @Excel(name = "维修费用")
    private BigDecimal repairCost;

    /** 满意度评分（1-5分） */
    @Excel(name = "满意度评分", readConverterExp = "1=-5分")
    private Integer satisfactionRating;

    /** 满意度评价 */
    @Excel(name = "满意度评价")
    private String satisfactionComment;

    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setReporterId(Long reporterId) 
    {
        this.reporterId = reporterId;
    }

    public Long getReporterId() 
    {
        return reporterId;
    }

    public void setReporterName(String reporterName) 
    {
        this.reporterName = reporterName;
    }

    public String getReporterName() 
    {
        return reporterName;
    }

    public void setReporterPhone(String reporterPhone) 
    {
        this.reporterPhone = reporterPhone;
    }

    public String getReporterPhone() 
    {
        return reporterPhone;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public String getDepartment() 
    {
        return department;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setEquipmentId(Long equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId() 
    {
        return equipmentId;
    }

    public void setEquipmentModel(String equipmentModel) 
    {
        this.equipmentModel = equipmentModel;
    }

    public String getEquipmentModel() 
    {
        return equipmentModel;
    }

    public void setFaultLevel(String faultLevel) 
    {
        this.faultLevel = faultLevel;
    }

    public String getFaultLevel() 
    {
        return faultLevel;
    }

    public void setFaultLevelDesc(String faultLevelDesc) 
    {
        this.faultLevelDesc = faultLevelDesc;
    }

    public String getFaultLevelDesc() 
    {
        return faultLevelDesc;
    }

    public void setFaultDescription(String faultDescription) 
    {
        this.faultDescription = faultDescription;
    }

    public String getFaultDescription() 
    {
        return faultDescription;
    }

    public void setFaultImages(String faultImages) 
    {
        this.faultImages = faultImages;
    }

    public String getFaultImages() 
    {
        return faultImages;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPriority(Integer priority) 
    {
        this.priority = priority;
    }

    public Integer getPriority() 
    {
        return priority;
    }

    public void setAssignedTo(Long assignedTo) 
    {
        this.assignedTo = assignedTo;
    }

    public Long getAssignedTo() 
    {
        return assignedTo;
    }

    public void setRepairStartTime(Date repairStartTime) 
    {
        this.repairStartTime = repairStartTime;
    }

    public Date getRepairStartTime() 
    {
        return repairStartTime;
    }

    public void setRepairEndTime(Date repairEndTime) 
    {
        this.repairEndTime = repairEndTime;
    }

    public Date getRepairEndTime() 
    {
        return repairEndTime;
    }

    public void setRepairDuration(Long repairDuration) 
    {
        this.repairDuration = repairDuration;
    }

    public Long getRepairDuration() 
    {
        return repairDuration;
    }

    public void setRepairResult(String repairResult) 
    {
        this.repairResult = repairResult;
    }

    public String getRepairResult() 
    {
        return repairResult;
    }

    public void setRepairCost(BigDecimal repairCost) 
    {
        this.repairCost = repairCost;
    }

    public BigDecimal getRepairCost() 
    {
        return repairCost;
    }

    public void setSatisfactionRating(Integer satisfactionRating) 
    {
        this.satisfactionRating = satisfactionRating;
    }

    public Integer getSatisfactionRating() 
    {
        return satisfactionRating;
    }

    public void setSatisfactionComment(String satisfactionComment) 
    {
        this.satisfactionComment = satisfactionComment;
    }

    public String getSatisfactionComment() 
    {
        return satisfactionComment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("title", getTitle())
            .append("reporterId", getReporterId())
            .append("reporterName", getReporterName())
            .append("reporterPhone", getReporterPhone())
            .append("deptId", getDeptId())
            .append("department", getDepartment())
            .append("location", getLocation())
            .append("equipmentId", getEquipmentId())
            .append("equipmentModel", getEquipmentModel())
            .append("faultLevel", getFaultLevel())
            .append("faultLevelDesc", getFaultLevelDesc())
            .append("faultDescription", getFaultDescription())
            .append("faultImages", getFaultImages())
            .append("status", getStatus())
            .append("priority", getPriority())
            .append("assignedTo", getAssignedTo())
            .append("repairStartTime", getRepairStartTime())
            .append("repairEndTime", getRepairEndTime())
            .append("repairDuration", getRepairDuration())
            .append("repairResult", getRepairResult())
            .append("repairCost", getRepairCost())
            .append("satisfactionRating", getSatisfactionRating())
            .append("satisfactionComment", getSatisfactionComment())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
