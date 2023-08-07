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

 Date: 07/08/2023 10:24:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xy_admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `xy_admin_menu`;
CREATE TABLE `xy_admin_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单主键ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父菜单ID',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '权限标识',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NOT NULL COMMENT '修改时间',
  `creater_id` bigint(0) NULL DEFAULT NULL COMMENT '创建员 ID',
  `creater` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建员',
  `modifier_id` bigint(0) NULL DEFAULT NULL COMMENT '修改员 ID',
  `modifier` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改员',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除 （0/否 1/是）',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xy_admin_menu
-- ----------------------------
INSERT INTO `xy_admin_menu` VALUES (1, '系统菜单', 'monitor', 0, 1, '/sys', '', 'M', '', '2022-07-04 14:56:29', '2022-07-04 14:56:31', NULL, NULL, NULL, NULL, b'0', '系 统管理目录');
INSERT INTO `xy_admin_menu` VALUES (3, '用户管理', 'UserFilled', 1, 1, '/sys/user', 'sys/user/index', 'C', 'system:user:list', '2022-07-04 15:20:51', '2022-07-04 15:20:53', NULL, NULL, NULL, NULL, b'0', '用户管理菜单');
INSERT INTO `xy_admin_menu` VALUES (4, '角色管理', 'avatar', 1, 2, '/sys/role', 'sys/role/index', 'C', 'system:role:list', '2022-07-04 15:23:35', '2022-07-04 15:23:39', NULL, NULL, NULL, NULL, b'0', '角色管理菜单');
INSERT INTO `xy_admin_menu` VALUES (5, '菜单管理', 'menu', 1, 3, '/sys/menu', 'sys/menu/index', 'C', 'system:menu:list', '2022-07-04 15:23:41', '2022-07-04 15:23:43', NULL, NULL, NULL, NULL, b'0', '菜单管理菜单');
INSERT INTO `xy_admin_menu` VALUES (8, '用户新增', '#', 3, 2, '', '', 'F', 'system:user:add', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '添加用户按钮');
INSERT INTO `xy_admin_menu` VALUES (9, '用户修改', '#', 3, 3, '', '', 'F', 'system:user:edit', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '修改用户按钮');
INSERT INTO `xy_admin_menu` VALUES (10, '用户删除', '#', 3, 4, '', '', 'F', 'system:user:delete', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '删除用户按钮');
INSERT INTO `xy_admin_menu` VALUES (11, '分配角色', '#', 3, 5, '', '', 'F', 'system:user:role', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '分配角色按钮');
INSERT INTO `xy_admin_menu` VALUES (12, '重置密码', '#', 3, 6, '', '', 'F', 'system:user:resetPwd', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '重置密码按钮');
INSERT INTO `xy_admin_menu` VALUES (13, '角色新增', '#', 4, 2, '', '', 'F', 'system:role:add', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '添加用户按钮');
INSERT INTO `xy_admin_menu` VALUES (14, '角色修改', '#', 4, 3, '', '', 'F', 'system:role:edit', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '修改用户按钮');
INSERT INTO `xy_admin_menu` VALUES (15, '角色删除', '#', 4, 4, '', NULL, 'F', 'system:role:delete', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '删除用户按钮');
INSERT INTO `xy_admin_menu` VALUES (16, '分配权限', '#', 4, 5, '', '', 'F', 'system:role:menu', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '分配权限按钮');
INSERT INTO `xy_admin_menu` VALUES (17, '菜单新增', '#', 5, 2, '', NULL, 'F', 'system:menu:add', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '添加菜单按钮');
INSERT INTO `xy_admin_menu` VALUES (18, '菜单修改', '#', 5, 3, '', NULL, 'F', 'system:menu:edit', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '修改菜单按钮');
INSERT INTO `xy_admin_menu` VALUES (19, '菜单删除', '#', 5, 4, '', NULL, 'F', 'system:menu:delete', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '删除菜单按钮');
INSERT INTO `xy_admin_menu` VALUES (20, '用户查询', '#', 3, 1, '', NULL, 'F', 'system:user:query', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '用户查询按钮');
INSERT INTO `xy_admin_menu` VALUES (21, '角色查询', '#', 4, 1, '', NULL, 'F', 'system:role:query', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '角色查询按钮');
INSERT INTO `xy_admin_menu` VALUES (22, '菜单查询', '#', 5, 1, '', NULL, 'F', 'system:menu:query', '2022-07-04 15:24:42', '2022-07-04 15:24:46', NULL, NULL, NULL, NULL, b'0', '菜单查询按钮');
INSERT INTO `xy_admin_menu` VALUES (23, '配置管理', 'Operation', 0, 1, '/config', '', 'M', '', '2022-07-04 14:56:29', '2022-07-04 14:56:31', NULL, NULL, NULL, NULL, b'0', '配置管理目录');
INSERT INTO `xy_admin_menu` VALUES (24, '数据源配置', 'Orange', 23, 1, '/config/sources', 'config/sources/index', 'C', 'system:sources:list', '2022-07-04 14:56:29', '2022-07-04 14:56:31', NULL, NULL, NULL, NULL, b'0', '数据源配置');
INSERT INTO `xy_admin_menu` VALUES (34, '数据迁移配置', 'Money', 23, 2, '/config/migration', 'config/migration/index', 'C', 'system:migration:list', '2023-03-14 19:51:24', '2023-03-14 19:51:30', NULL, NULL, NULL, NULL, b'0', '数据迁移配置');
INSERT INTO `xy_admin_menu` VALUES (35, '数据迁移操作', '#', 34, 1, '/config/migration/operation', 'config/migration/operation/index', 'F', '', '2023-03-16 14:00:42', '2023-03-16 14:00:45', NULL, NULL, NULL, NULL, b'0', '数据迁移操作');
INSERT INTO `xy_admin_menu` VALUES (36, '代码管理', 'Edit', 0, 1, '/code', NULL, 'M', '', '2023-07-18 09:57:40', '2023-07-18 09:57:43', NULL, NULL, NULL, NULL, b'0', '代码配置');
INSERT INTO `xy_admin_menu` VALUES (37, '代码列表', 'Cellphone', 36, 1, '/code/list', 'code/list/index', 'C', '', '2023-07-18 15:22:06', '2023-07-18 15:22:09', NULL, NULL, NULL, NULL, b'0', '代码列表');
INSERT INTO `xy_admin_menu` VALUES (38, '运行记录', 'Calendar', 36, 1, '/code/run', 'code/run/index', 'C', '', '2023-07-21 17:15:56', '2023-07-21 17:15:59', NULL, NULL, NULL, NULL, b'0', '运行记录');
INSERT INTO `xy_admin_menu` VALUES (39, '运行记录详情', '#', 38, 1, '/code/run/details', 'code/run/details/index', 'F', '', '2023-07-23 17:28:18', '2023-07-23 17:28:30', NULL, NULL, NULL, NULL, b'0', '运行记录详情');

SET FOREIGN_KEY_CHECKS = 1;
