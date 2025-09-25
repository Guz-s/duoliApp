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
import com.ruoyi.system.domain.RepairOrder;
import com.ruoyi.system.service.IRepairOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报修工单Controller
 *
 * @author duoli
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/repair/order")
public class RepairOrderController extends BaseController {
    @Autowired
    private IRepairOrderService repairOrderService;

/**
 * 查询报修工单列表
 */
@PreAuthorize("@ss.hasPermi('repair:order:list')")
@GetMapping("/list")
    public TableDataInfo list(RepairOrder repairOrder) {
        startPage();
        List<RepairOrder> list = repairOrderService.selectRepairOrderList(repairOrder);
        return getDataTable(list);
    }

    /**
    * 导入数据
    */
    @Log(title = "报修工单", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('repair:order:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<RepairOrder> util = new ExcelUtil<RepairOrder>(RepairOrder. class);
        InputStream inputStream = file.getInputStream();
        List<RepairOrder> list = util.importExcel(inputStream);
        inputStream.close();
        int count = repairOrderService.batchInsertRepairOrder(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
    * 下载模板
    */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<RepairOrder> util = new ExcelUtil<RepairOrder>(RepairOrder. class);
        util.importTemplateExcel(response, "报修工单数据");
    }

    /**
     * 导出报修工单列表
     */
    @PreAuthorize("@ss.hasPermi('repair:order:export')")
    @Log(title = "报修工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RepairOrder repairOrder) {
        List<RepairOrder> list = repairOrderService.selectRepairOrderList(repairOrder);
        ExcelUtil<RepairOrder> util = new ExcelUtil<RepairOrder>(RepairOrder. class);
        util.exportExcel(response, list, "报修工单数据");
    }

    /**
     * 获取报修工单详细信息
     */
    @PreAuthorize("@ss.hasPermi('repair:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId) {
        return success(repairOrderService.selectRepairOrderByOrderId(orderId));
    }

    /**
     * 新增报修工单
     */
    @PreAuthorize("@ss.hasPermi('repair:order:add')")
    @Log(title = "报修工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RepairOrder repairOrder) {
        return toAjax(repairOrderService.insertRepairOrder(repairOrder));
    }

    /**
     * 修改报修工单
     */
    @PreAuthorize("@ss.hasPermi('repair:order:edit')")
    @Log(title = "报修工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RepairOrder repairOrder) {
        return toAjax(repairOrderService.updateRepairOrder(repairOrder));
    }

    /**
     * 删除报修工单
     */
    @PreAuthorize("@ss.hasPermi('repair:order:remove')")
    @Log(title = "报修工单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds) {
        return toAjax(repairOrderService.deleteRepairOrderByOrderIds(orderIds));
    }
}
