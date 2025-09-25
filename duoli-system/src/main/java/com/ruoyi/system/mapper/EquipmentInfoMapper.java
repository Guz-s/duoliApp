package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.EquipmentInfo;

import java.util.List;

/**
 * 设备信息Mapper接口
 *
 * @author duoli
 * @date 2025-09-25
 */
public interface EquipmentInfoMapper
{
    /**
     * 查询设备信息
     *
     * @param equipmentId 设备信息主键
     * @return 设备信息
     */
    public EquipmentInfo selectEquipmentInfoByEquipmentId(String equipmentId);

    /**
     * 查询设备信息列表
     *
     * @param equipmentInfo 设备信息
     * @return 设备信息集合
     */
    public List<EquipmentInfo> selectEquipmentInfoList(EquipmentInfo equipmentInfo);

    /**
     * 新增设备信息
     *
     * @param equipmentInfo 设备信息
     * @return 结果
     */
    public int insertEquipmentInfo(EquipmentInfo equipmentInfo);

    /**
     * 修改设备信息
     *
     * @param equipmentInfo 设备信息
     * @return 结果
     */
    public int updateEquipmentInfo(EquipmentInfo equipmentInfo);

    /**
     * 删除设备信息
     *
     * @param equipmentId 设备信息主键
     * @return 结果
     */
    public int deleteEquipmentInfoByEquipmentId(String equipmentId);

    /**
     * 批量删除设备信息
     *
     * @param equipmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEquipmentInfoByEquipmentIds(String[] equipmentIds);
}
