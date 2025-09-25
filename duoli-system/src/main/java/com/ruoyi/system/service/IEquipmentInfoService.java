package com.ruoyi.system.service;

import com.ruoyi.system.domain.EquipmentInfo;

import java.util.List;

/**
 * 设备信息Service接口
 *
 * @author duoli
 * @date 2025-09-25
 */
public interface IEquipmentInfoService
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
     * 批量新增设备信息
     *
     * @param equipmentInfos 设备信息List
     * @return 结果
     */
    public int batchInsertEquipmentInfo(List<EquipmentInfo> equipmentInfos);

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
     * 批量删除设备信息
     *
     * @param equipmentIds 需要删除的设备信息主键集合
     * @return 结果
     */
    public int deleteEquipmentInfoByEquipmentIds(String[] equipmentIds);

    /**
     * 删除设备信息信息
     *
     * @param equipmentId 设备信息主键
     * @return 结果
     */
    public int deleteEquipmentInfoByEquipmentId(String equipmentId);
}
