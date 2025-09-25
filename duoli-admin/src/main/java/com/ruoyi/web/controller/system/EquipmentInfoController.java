package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.system.domain.EquipmentInfo;
import com.ruoyi.system.service.IEquipmentInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备信息Controller
 *
 * @author duoli
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/repair/inforepair")
public class EquipmentInfoController extends BaseController {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;

/**
 * 查询设备信息列表
 */
@PreAuthorize("@ss.hasPermi('repair:inforepair:list')")
@GetMapping("/list")
    public TableDataInfo list(EquipmentInfo equipmentInfo) {
        startPage();
        List<EquipmentInfo> list = equipmentInfoService.selectEquipmentInfoList(equipmentInfo);
        return getDataTable(list);
    }

    /**
    * 导入数据
    */
    @Log(title = "设备信息", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('repair:inforepair:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<EquipmentInfo> util = new ExcelUtil<EquipmentInfo>(EquipmentInfo. class);
        InputStream inputStream = file.getInputStream();
        List<EquipmentInfo> list = util.importExcel(inputStream);
        inputStream.close();
        int count = equipmentInfoService.batchInsertEquipmentInfo(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
    * 下载模板
    */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<EquipmentInfo> util = new ExcelUtil<EquipmentInfo>(EquipmentInfo. class);
        util.importTemplateExcel(response, "设备信息数据");
    }

    /**
     * 导出设备信息列表
     */
    @PreAuthorize("@ss.hasPermi('repair:inforepair:export')")
    @Log(title = "设备信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EquipmentInfo equipmentInfo) {
        List<EquipmentInfo> list = equipmentInfoService.selectEquipmentInfoList(equipmentInfo);
        ExcelUtil<EquipmentInfo> util = new ExcelUtil<EquipmentInfo>(EquipmentInfo. class);
        util.exportExcel(response, list, "设备信息数据");
    }

    /**
     * 获取设备信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('repair:inforepair:query')")
    @GetMapping(value = "/{equipmentId}")
    public AjaxResult getInfo(@PathVariable("equipmentId") String equipmentId) {
        return success(equipmentInfoService.selectEquipmentInfoByEquipmentId(equipmentId));
    }

    /**
     * 新增设备信息
     */
    @PreAuthorize("@ss.hasPermi('repair:inforepair:add')")
    @Log(title = "设备信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EquipmentInfo equipmentInfo) {
        return toAjax(equipmentInfoService.insertEquipmentInfo(equipmentInfo));
    }

    /**
     * 修改设备信息
     */
    @PreAuthorize("@ss.hasPermi('repair:inforepair:edit')")
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EquipmentInfo equipmentInfo) {
        return toAjax(equipmentInfoService.updateEquipmentInfo(equipmentInfo));
    }

    /**
     * 删除设备信息
     */
    @PreAuthorize("@ss.hasPermi('repair:inforepair:remove')")
    @Log(title = "设备信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{equipmentIds}")
    public AjaxResult remove(@PathVariable String[] equipmentIds) {
        return toAjax(equipmentInfoService.deleteEquipmentInfoByEquipmentIds(equipmentIds));
    }
}
