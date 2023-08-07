/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : xy_dash

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 07/08/2023 10:23:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xy_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `xy_admin_role`;
CREATE TABLE `xy_admin_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色主键ID',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_admin_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `xy_admin_role_menu`;
CREATE TABLE `xy_admin_role_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色菜单主键ID',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `xy_admin_user`;
CREATE TABLE `xy_admin_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'default.jpg' COMMENT '用户头像',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `login_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `xy_admin_user_role`;
CREATE TABLE `xy_admin_user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户角色主键ID',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_data_sources
-- ----------------------------
DROP TABLE IF EXISTS `xy_data_sources`;
CREATE TABLE `xy_data_sources`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '连接ip',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `port` int(0) NULL DEFAULT NULL COMMENT '端口',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '类型 0 mysql/1 sqlserver',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migration_code_operation_historys
-- ----------------------------
DROP TABLE IF EXISTS `xy_migration_code_operation_historys`;
CREATE TABLE `xy_migration_code_operation_historys`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operation_id` bigint(0) NULL DEFAULT NULL COMMENT '操作id',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '0 执行失败 1 执行成功 2 开始执行 3 生成jar 4 运行中',
  `msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index1`(`operation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码运行记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migration_code_operations
-- ----------------------------
DROP TABLE IF EXISTS `xy_migration_code_operations`;
CREATE TABLE `xy_migration_code_operations`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code_id` bigint(0) NULL DEFAULT NULL COMMENT '代码id',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '0 执行失败 1 执行成功 2 开始执行 3 生成jar 4 运行中',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index1`(`code_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码运行记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migration_codes
-- ----------------------------
DROP TABLE IF EXISTS `xy_migration_codes`;
CREATE TABLE `xy_migration_codes`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `migration_id` bigint(0) NULL DEFAULT NULL COMMENT '迁移id',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `run_count` int(0) NULL DEFAULT NULL COMMENT '运行次数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index1`(`migration_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migration_data_sources
-- ----------------------------
DROP TABLE IF EXISTS `xy_migration_data_sources`;
CREATE TABLE `xy_migration_data_sources`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库名',
  `source_id` bigint(0) NULL DEFAULT NULL COMMENT '数据源id',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `migrations_id` bigint(0) NOT NULL COMMENT '迁移配置id',
  `unique_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `migrations_data_sources_indx1`(`source_id`) USING BTREE,
  INDEX `migrations_data_sources_indx2`(`migrations_id`) USING BTREE,
  INDEX `migrations_data_sources_indx3`(`unique_name`) USING BTREE,
  INDEX `1`(`source_id`, `unique_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 334 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据迁移数据源配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migration_fields
-- ----------------------------
DROP TABLE IF EXISTS `xy_migration_fields`;
CREATE TABLE `xy_migration_fields`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名称',
  `table_id` bigint(0) NULL DEFAULT NULL COMMENT '表id',
  `property_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性名称',
  `field_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段类型',
  `property_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性类型',
  `pkg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '包名',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段对应值',
  `value_map` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应值的map',
  `defaulted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '0 普通字段 1主键 2 创建时间',
  `value_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `xy_migration_fields_index1`(`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8091 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字段配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migration_join_tables
-- ----------------------------
DROP TABLE IF EXISTS `xy_migration_join_tables`;
CREATE TABLE `xy_migration_join_tables`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `table_id` bigint(0) NOT NULL COMMENT '表id',
  `field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '查询字段',
  `join_filed` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联字段',
  `migration_table_id` bigint(0) NOT NULL COMMENT '迁移表id',
  `field_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '查询字段类型',
  `join_filed_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联字段类型',
  `select_list` json NULL COMMENT '查询字段',
  `query_map` json NULL COMMENT '查询条件',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `xy_migration_join_tables_index1`(`table_id`) USING BTREE,
  INDEX `xy_migration_join_tables_index2`(`migration_table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 242 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migration_tables
-- ----------------------------
DROP TABLE IF EXISTS `xy_migration_tables`;
CREATE TABLE `xy_migration_tables`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `migration_sources_id` bigint(0) NULL DEFAULT NULL COMMENT '数据源配置id',
  `source_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源类名',
  `is_migration` bit(1) NULL DEFAULT b'0' COMMENT '是否迁移表',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名',
  `position` int(0) NULL DEFAULT NULL COMMENT '权重（数据迁移顺序）',
  `source_unique_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源唯一名称',
  `random` int(0) NULL DEFAULT NULL COMMENT '下标',
  `query_map` json NULL COMMENT '查询条件',
  `id_type` int(0) NULL DEFAULT NULL COMMENT 'id生成策略',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `xy_migration_tables_index1`(`migration_sources_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 620 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据迁移表配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xy_migrations
-- ----------------------------
DROP TABLE IF EXISTS `xy_migrations`;
CREATE TABLE `xy_migrations`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `thread_count` int(0) NULL DEFAULT NULL COMMENT '线程数量',
  `start_thread` bit(1) NULL DEFAULT b'0' COMMENT '是否开启线程',
  `port` int(0) NOT NULL DEFAULT 9835 COMMENT '启动端口号',
  `count` int(0) NULL DEFAULT 5000 COMMENT '每次查询多少条',
  `sql_spliec` bit(1) NULL DEFAULT b'1' COMMENT '是否sql拼接插入',
  `created_time` time(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `is_del` bit(1) NULL DEFAULT NULL COMMENT '是否删除 （0/否 1/是）',
  `name` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '迁移类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 182 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据迁移配置' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
