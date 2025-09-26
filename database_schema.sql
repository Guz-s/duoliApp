-- ==========================================
-- 多丽制衣衣车报修管理系统数据库设计
-- ==========================================

-- 1. 设备信息表 (equipment_info) - 设备档案管理
-- 说明：记录所有衣车设备的基本信息，便于维修管理
CREATE TABLE `equipment_info` (
  `equipment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `equipment_code` varchar(50) NOT NULL COMMENT '设备编号',
  `equipment_name` varchar(100) NOT NULL COMMENT '设备名称',
  `model` varchar(50) DEFAULT NULL COMMENT '设备型号',
  `brand` varchar(50) DEFAULT NULL COMMENT '品牌',
  `location` varchar(200) DEFAULT NULL COMMENT '设备位置',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '所属部门',
  `purchase_date` date DEFAULT NULL COMMENT '采购日期',
  `warranty_expire` date DEFAULT NULL COMMENT '保修到期日',
  `status` char(1) DEFAULT '0' COMMENT '设备状态（0正常 1维修中 2停用）',
  `specifications` text COMMENT '设备规格参数',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`equipment_id`),
  UNIQUE KEY `uk_equipment_code` (`equipment_code`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';

-- 2. 报修工单表 (repair_order) - 核心业务表
-- 说明：存储所有报修申请，是系统的核心数据表
CREATE TABLE `repair_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工单ID',
  `order_no` varchar(32) NOT NULL COMMENT '工单编号（如：R2024001）',
  `title` varchar(200) NOT NULL COMMENT '工单标题',
  `reporter_id` bigint(20) NOT NULL COMMENT '报修人ID',
  `reporter_name` varchar(50) NOT NULL COMMENT '报修人姓名',
  `reporter_phone` varchar(11) NOT NULL COMMENT '报修人电话',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '报修部门ID',
  `department` varchar(100) DEFAULT NULL COMMENT '部门/车间',
  `location` varchar(200) NOT NULL COMMENT '报修地点',
  `equipment_id` bigint(20) DEFAULT NULL COMMENT '设备ID（可选）',
  `equipment_model` varchar(100) DEFAULT NULL COMMENT '设备型号',
  `fault_level` varchar(10) NOT NULL COMMENT '故障等级（紧急/较急/一般/其他）',
  `fault_level_desc` varchar(200) DEFAULT NULL COMMENT '其他等级描述',
  `fault_description` text NOT NULL COMMENT '故障描述',
  `fault_images` text COMMENT '故障图片（JSON格式存储图片路径数组）',
  `status` char(1) DEFAULT '0' COMMENT '工单状态（0待处理 1处理中 2已完成 3已取消）',
  `priority` tinyint(1) DEFAULT 1 COMMENT '优先级（1低 2中 3高 4紧急）',
  `assigned_to` bigint(20) DEFAULT NULL COMMENT '分配给的维修人员ID',
  `repair_start_time` datetime DEFAULT NULL COMMENT '维修开始时间',
  `repair_end_time` datetime DEFAULT NULL COMMENT '维修结束时间',
  `repair_duration` int(11) DEFAULT NULL COMMENT '维修用时（分钟）',
  `repair_result` text COMMENT '维修结果描述',
  `repair_cost` decimal(10,2) DEFAULT NULL COMMENT '维修费用',
  `satisfaction_rating` tinyint(1) DEFAULT NULL COMMENT '满意度评分（1-5分）',
  `satisfaction_comment` varchar(500) DEFAULT NULL COMMENT '满意度评价',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_reporter_id` (`reporter_id`),
  KEY `idx_assigned_to` (`assigned_to`),
  KEY `idx_status` (`status`),
  KEY `idx_fault_level` (`fault_level`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修工单表';

-- 3. 工单操作记录表 (repair_order_log) - 操作日志
-- 说明：记录工单的所有状态变更和操作历史，形成时间线
CREATE TABLE `repair_order_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `order_id` bigint(20) NOT NULL COMMENT '工单ID',
  `action_type` varchar(20) NOT NULL COMMENT '操作类型（create创建/assign分配/start开始/complete完成/cancel取消/comment备注）',
  `action_desc` varchar(200) NOT NULL COMMENT '操作描述',
  `old_status` char(1) DEFAULT NULL COMMENT '原状态',
  `new_status` char(1) DEFAULT NULL COMMENT '新状态',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) NOT NULL COMMENT '操作人姓名',
  `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `images` text COMMENT '相关图片（维修过程图片等）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`log_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单操作记录表';

-- 4. 维修人员工作统计表 (repair_worker_stats) - 绩效统计
-- 说明：维修人员的工作量和绩效统计，支持报表展示
CREATE TABLE `repair_worker_stats` (
  `stats_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `worker_id` bigint(20) NOT NULL COMMENT '维修人员ID',
  `stats_date` date NOT NULL COMMENT '统计日期',
  `total_orders` int(11) DEFAULT 0 COMMENT '总工单数',
  `completed_orders` int(11) DEFAULT 0 COMMENT '已完成工单数',
  `pending_orders` int(11) DEFAULT 0 COMMENT '待处理工单数',
  `processing_orders` int(11) DEFAULT 0 COMMENT '处理中工单数',
  `avg_repair_time` int(11) DEFAULT 0 COMMENT '平均维修时间（分钟）',
  `total_repair_time` int(11) DEFAULT 0 COMMENT '总维修时间（分钟）',
  `avg_rating` decimal(3,2) DEFAULT NULL COMMENT '平均满意度评分',
  `urgent_orders` int(11) DEFAULT 0 COMMENT '紧急工单数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stats_id`),
  UNIQUE KEY `uk_worker_date` (`worker_id`, `stats_date`),
  KEY `idx_stats_date` (`stats_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修人员工作统计表';

-- 5. 设备故障统计表 (equipment_fault_stats) - 设备分析
-- 说明：统计设备故障频率，支持设备维护决策
CREATE TABLE `equipment_fault_stats` (
  `stats_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `equipment_id` bigint(20) NOT NULL COMMENT '设备ID',
  `stats_month` varchar(7) NOT NULL COMMENT '统计月份（YYYY-MM）',
  `fault_count` int(11) DEFAULT 0 COMMENT '故障次数',
  `total_downtime` int(11) DEFAULT 0 COMMENT '总停机时间（分钟）',
  `avg_repair_time` int(11) DEFAULT 0 COMMENT '平均修复时间（分钟）',
  `urgent_fault_count` int(11) DEFAULT 0 COMMENT '紧急故障次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stats_id`),
  UNIQUE KEY `uk_equipment_month` (`equipment_id`, `stats_month`),
  KEY `idx_stats_month` (`stats_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备故障统计表';

-- ==========================================
-- 索引优化建议
-- ==========================================

-- 复合索引：提高查询性能
ALTER TABLE `repair_order` ADD INDEX `idx_status_priority_create` (`status`, `priority`, `create_time`);
ALTER TABLE `repair_order` ADD INDEX `idx_assigned_status` (`assigned_to`, `status`);
ALTER TABLE `repair_order` ADD INDEX `idx_dept_status_create` (`dept_id`, `status`, `create_time`);

-- 全文索引：支持故障描述搜索
ALTER TABLE `repair_order` ADD FULLTEXT KEY `ft_fault_description` (`fault_description`);
ALTER TABLE `repair_order` ADD FULLTEXT KEY `ft_title` (`title`);

-- ==========================================
-- 视图定义
-- ==========================================

-- 工单详情视图：关联用户和部门信息
CREATE VIEW `v_repair_order_detail` AS
SELECT 
    ro.order_id,
    ro.order_no,
    ro.title,
    ro.reporter_name,
    ro.reporter_phone,
    ro.department,
    ro.location,
    ro.equipment_model,
    ro.fault_level,
    ro.fault_description,
    ro.status,
    ro.priority,
    ro.create_time,
    ro.repair_start_time,
    ro.repair_end_time,
    ro.repair_duration,
    rw.nick_name AS repair_worker_name,
    rw.phonenumber AS repair_worker_phone,
    sd.dept_name,
    CASE ro.status 
        WHEN '0' THEN '待处理'
        WHEN '1' THEN '处理中'
        WHEN '2' THEN '已完成'
        WHEN '3' THEN '已取消'
        ELSE '未知'
    END AS status_text,
    CASE ro.priority
        WHEN 1 THEN '低'
        WHEN 2 THEN '中'
        WHEN 3 THEN '高'
        WHEN 4 THEN '紧急'
        ELSE '未知'
    END AS priority_text
FROM repair_order ro
LEFT JOIN sys_user rw ON ro.assigned_to = rw.user_id
LEFT JOIN sys_dept sd ON ro.dept_id = sd.dept_id;

-- ==========================================
-- 存储过程：工单编号生成
-- ==========================================

DELIMITER //
CREATE PROCEDURE `GenerateOrderNo`(OUT orderNo VARCHAR(32))
BEGIN
    DECLARE prefix VARCHAR(10) DEFAULT 'R';
    DECLARE nextNum INT;
    DECLARE today VARCHAR(8);
    
    -- 获取今天的日期
    SET today = DATE_FORMAT(NOW(), '%Y%m%d');
    
    -- 获取今天的最大编号
    SELECT IFNULL(MAX(CAST(SUBSTRING(order_no, LENGTH(prefix) + 1) AS UNSIGNED)), 0) + 1 
    INTO nextNum 
    FROM repair_order 
    WHERE order_no LIKE CONCAT(prefix, DATE_FORMAT(NOW(), '%Y%m%d'), '%');
    
    -- 生成新的工单编号
    SET orderNo = CONCAT(prefix, today, LPAD(nextNum, 3, '0'));
END //
DELIMITER ;

-- ==========================================
-- 触发器：自动更新统计数据
-- ==========================================

-- 工单状态变更时自动更新统计
DELIMITER //
CREATE TRIGGER `tr_repair_order_stats_update` 
AFTER UPDATE ON `repair_order`
FOR EACH ROW
BEGIN
    -- 如果状态发生变化，更新维修人员统计
    IF OLD.status != NEW.status AND NEW.assigned_to IS NOT NULL THEN
        INSERT INTO repair_worker_stats (worker_id, stats_date, total_orders, completed_orders, pending_orders, processing_orders)
        VALUES (NEW.assigned_to, CURDATE(), 1, 
                CASE WHEN NEW.status = '2' THEN 1 ELSE 0 END,
                CASE WHEN NEW.status = '0' THEN 1 ELSE 0 END,
                CASE WHEN NEW.status = '1' THEN 1 ELSE 0 END)
        ON DUPLICATE KEY UPDATE
            total_orders = total_orders + 1,
            completed_orders = completed_orders + CASE WHEN NEW.status = '2' THEN 1 ELSE 0 END,
            pending_orders = pending_orders + CASE WHEN NEW.status = '0' THEN 1 ELSE 0 END,
            processing_orders = processing_orders + CASE WHEN NEW.status = '1' THEN 1 ELSE 0 END,
            update_time = NOW();
    END IF;
END //
DELIMITER ;
