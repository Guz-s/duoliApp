package com.ruoyi.system.service.impl;

import java.util.List;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.RepairOrderMapper;
import com.ruoyi.system.domain.RepairOrder;
import com.ruoyi.system.service.IRepairOrderService;

/**
 * 报修工单Service业务层处理
 *
 * @author duoli
 * @date 2025-09-25
 */
@Service
public class RepairOrderServiceImpl implements IRepairOrderService
{
    @Autowired
    private RepairOrderMapper repairOrderMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    /**
     * 查询报修工单
     *
     * @param orderId 报修工单主键
     * @return 报修工单
     */
    @Override
    public RepairOrder selectRepairOrderByOrderId(String orderId)
    {
        return repairOrderMapper.selectRepairOrderByOrderId(orderId);
    }

    /**
     * 查询报修工单列表
     *
     * @param repairOrder 报修工单
     * @return 报修工单
     */
    @Override
    public List<RepairOrder> selectRepairOrderList(RepairOrder repairOrder)
    {
        return repairOrderMapper.selectRepairOrderList(repairOrder);
    }

    /**
     * 批量新增报修工单
     *
     * @param repairOrders 报修工单List
     * @return 结果
     */
    @Override
    public int batchInsertRepairOrder(List<RepairOrder> repairOrders)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(repairOrders)) {
            try {
                for (int i = 0; i < repairOrders.size(); i++) {
                    RepairOrder repairOrder = repairOrders.get(i);
                    // 生成主键ID（仅对String类型主键）
                    if (repairOrder.getOrderId() == null || repairOrder.getOrderId().isEmpty()) {
                        repairOrder.setOrderId(IdUtils.fastSimpleUUID());
                    }
                    repairOrder.setCreateTime(DateUtils.getNowDate());

                    repairOrderMapper.insertRepairOrder(repairOrder);
                    // 防止内存溢出，没100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%3 == 0) || i == repairOrders.size() - 1;
                    if (bool){
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            }catch (Exception e){
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            }finally {
                sqlSession.close();
            }
        }
        return count;
    }

    /**
     * 新增报修工单
     *
     * @param repairOrder 报修工单
     * @return 结果
     */
    @Override
    public int insertRepairOrder(RepairOrder repairOrder)
    {
        // 生成主键ID（仅对String类型主键）
        if (repairOrder.getOrderId() == null || repairOrder.getOrderId().isEmpty()) {
            repairOrder.setOrderId(IdUtils.fastSimpleUUID());
        }
        repairOrder.setCreateTime(DateUtils.getNowDate());
        return repairOrderMapper.insertRepairOrder(repairOrder);
    }

    /**
     * 修改报修工单
     *
     * @param repairOrder 报修工单
     * @return 结果
     */
    @Override
    public int updateRepairOrder(RepairOrder repairOrder)
    {
        repairOrder.setUpdateTime(DateUtils.getNowDate());
        return repairOrderMapper.updateRepairOrder(repairOrder);
    }

    /**
     * 批量删除报修工单
     *
     * @param orderIds 需要删除的报修工单主键
     * @return 结果
     */
    @Override
    public int deleteRepairOrderByOrderIds(String[] orderIds)
    {
        return repairOrderMapper.deleteRepairOrderByOrderIds(orderIds);
    }

    /**
     * 删除报修工单信息
     *
     * @param orderId 报修工单主键
     * @return 结果
     */
    @Override
    public int deleteRepairOrderByOrderId(String orderId)
    {
        return repairOrderMapper.deleteRepairOrderByOrderId(orderId);
    }
}
