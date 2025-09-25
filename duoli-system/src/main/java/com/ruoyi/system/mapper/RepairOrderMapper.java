package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.RepairOrder;

/**
 * 报修工单Mapper接口
 * 
 * @author duoli
 * @date 2025-09-25
 */
public interface RepairOrderMapper 
{
    /**
     * 查询报修工单
     * 
     * @param orderId 报修工单主键
     * @return 报修工单
     */
    public RepairOrder selectRepairOrderByOrderId(String orderId);

    /**
     * 查询报修工单列表
     * 
     * @param repairOrder 报修工单
     * @return 报修工单集合
     */
    public List<RepairOrder> selectRepairOrderList(RepairOrder repairOrder);

    /**
     * 新增报修工单
     * 
     * @param repairOrder 报修工单
     * @return 结果
     */
    public int insertRepairOrder(RepairOrder repairOrder);

    /**
     * 修改报修工单
     * 
     * @param repairOrder 报修工单
     * @return 结果
     */
    public int updateRepairOrder(RepairOrder repairOrder);

    /**
     * 删除报修工单
     * 
     * @param orderId 报修工单主键
     * @return 结果
     */
    public int deleteRepairOrderByOrderId(String orderId);

    /**
     * 批量删除报修工单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRepairOrderByOrderIds(String[] orderIds);
}
