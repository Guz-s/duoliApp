package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备信息对象 equipment_info
 * 
 * @author duoli
 * @date 2025-09-25
 */
public class EquipmentInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备ID */
    private String equipmentId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String equipmentCode;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String model;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 设备位置 */
    @Excel(name = "设备位置")
    private String location;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private Long deptId;

    /** 采购日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采购日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    /** 保修到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "保修到期日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date warrantyExpire;

    /** 设备状态（0正常 1维修中 2停用） */
    @Excel(name = "设备状态", readConverterExp = "0=正常,1=维修中,2=停用")
    private String status;

    /** 设备规格参数 */
    @Excel(name = "设备规格参数")
    private String specifications;

    public void setEquipmentId(String equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentId() 
    {
        return equipmentId;
    }

    public void setEquipmentCode(String equipmentCode) 
    {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentCode() 
    {
        return equipmentCode;
    }

    public void setEquipmentName(String equipmentName) 
    {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName() 
    {
        return equipmentName;
    }

    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }

    public void setBrand(String brand) 
    {
        this.brand = brand;
    }

    public String getBrand() 
    {
        return brand;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setPurchaseDate(Date purchaseDate) 
    {
        this.purchaseDate = purchaseDate;
    }

    public Date getPurchaseDate() 
    {
        return purchaseDate;
    }

    public void setWarrantyExpire(Date warrantyExpire) 
    {
        this.warrantyExpire = warrantyExpire;
    }

    public Date getWarrantyExpire() 
    {
        return warrantyExpire;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSpecifications(String specifications) 
    {
        this.specifications = specifications;
    }

    public String getSpecifications() 
    {
        return specifications;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("equipmentId", getEquipmentId())
            .append("equipmentCode", getEquipmentCode())
            .append("equipmentName", getEquipmentName())
            .append("model", getModel())
            .append("brand", getBrand())
            .append("location", getLocation())
            .append("deptId", getDeptId())
            .append("purchaseDate", getPurchaseDate())
            .append("warrantyExpire", getWarrantyExpire())
            .append("status", getStatus())
            .append("specifications", getSpecifications())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
