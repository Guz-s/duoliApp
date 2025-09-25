package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.EquipmentInfo;
import com.ruoyi.system.mapper.EquipmentInfoMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.service.IEquipmentInfoService;

/**
 * 设备信息Service业务层处理
 *
 * @author duoli
 * @date 2025-09-25
 */
@Service
public class EquipmentInfoServiceImpl implements IEquipmentInfoService
{
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    /**
     * 查询设备信息
     *
     * @param equipmentId 设备信息主键
     * @return 设备信息
     */
    @Override
    public EquipmentInfo selectEquipmentInfoByEquipmentId(String equipmentId)
    {
        return equipmentInfoMapper.selectEquipmentInfoByEquipmentId(equipmentId);
    }

    /**
     * 查询设备信息列表
     *
     * @param equipmentInfo 设备信息
     * @return 设备信息
     */
    @Override
    public List<EquipmentInfo> selectEquipmentInfoList(EquipmentInfo equipmentInfo)
    {
        return equipmentInfoMapper.selectEquipmentInfoList(equipmentInfo);
    }

    /**
     * 批量新增设备信息
     *
     * @param equipmentInfos 设备信息List
     * @return 结果
     */
    @Override
    public int batchInsertEquipmentInfo(List<EquipmentInfo> equipmentInfos)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(equipmentInfos)) {
            try {
                for (int i = 0; i < equipmentInfos.size(); i++) {
                    EquipmentInfo equipmentInfo = equipmentInfos.get(i);
                    // 生成设备ID
                    if (equipmentInfo.getEquipmentId() == null || equipmentInfo.getEquipmentId().isEmpty()) {
                        equipmentInfo.setEquipmentId(IdUtils.fastSimpleUUID());
                    }
                    equipmentInfo.setCreateTime(DateUtils.getNowDate());
                    
                    equipmentInfoMapper.insertEquipmentInfo(equipmentInfo);
                    // 防止内存溢出，没100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%3 == 0) || i == equipmentInfos.size() - 1;
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
     * 新增设备信息
     *
     * @param equipmentInfo 设备信息
     * @return 结果
     */
    @Override
    public int insertEquipmentInfo(EquipmentInfo equipmentInfo)
    {
        // 生成设备ID
        if (equipmentInfo.getEquipmentId() == null || equipmentInfo.getEquipmentId().isEmpty()) {
            equipmentInfo.setEquipmentId(IdUtils.fastSimpleUUID());
        }
        equipmentInfo.setCreateTime(DateUtils.getNowDate());
        return equipmentInfoMapper.insertEquipmentInfo(equipmentInfo);
    }

    /**
     * 修改设备信息
     *
     * @param equipmentInfo 设备信息
     * @return 结果
     */
    @Override
    public int updateEquipmentInfo(EquipmentInfo equipmentInfo)
    {
        equipmentInfo.setUpdateTime(DateUtils.getNowDate());
        return equipmentInfoMapper.updateEquipmentInfo(equipmentInfo);
    }

    /**
     * 批量删除设备信息
     *
     * @param equipmentIds 需要删除的设备信息主键
     * @return 结果
     */
    @Override
    public int deleteEquipmentInfoByEquipmentIds(String[] equipmentIds)
    {
        return equipmentInfoMapper.deleteEquipmentInfoByEquipmentIds(equipmentIds);
    }

    /**
     * 删除设备信息信息
     *
     * @param equipmentId 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteEquipmentInfoByEquipmentId(String equipmentId)
    {
        return equipmentInfoMapper.deleteEquipmentInfoByEquipmentId(equipmentId);
    }
}
