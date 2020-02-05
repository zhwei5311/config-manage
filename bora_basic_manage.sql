/*
 Navicat Premium Data Transfer

 Source Server         : 101.132.133.191
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 101.132.133.191:3306
 Source Schema         : bora_basic_manage

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 05/02/2020 14:22:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_basic_define
-- ----------------------------
DROP TABLE IF EXISTS `t_basic_define`;
CREATE TABLE `t_basic_define`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键：自增',
  `tenant_id` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `mark` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能标识，物料、终端等',
  `field_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段标识',
  `field_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `field_status` tinyint(2) NULL DEFAULT NULL COMMENT '字段状态，1：启用；2：禁用',
  `field_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `field_attr` tinyint(2) NULL DEFAULT NULL COMMENT '字段属性，1：系统；2：可选',
  `is_show` tinyint(2) NULL DEFAULT NULL COMMENT '是否显示，1：显示；2：不显示',
  `order_no` int(11) NULL DEFAULT NULL COMMENT '字段排序号',
  `description` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_basic_template
-- ----------------------------
DROP TABLE IF EXISTS `t_basic_template`;
CREATE TABLE `t_basic_template`  (
  `id` int(11) NOT NULL COMMENT '主键：自增',
  `field_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段标识',
  `field_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `field_status` tinyint(2) NULL DEFAULT NULL COMMENT '字段状态，1：启用；2：禁用',
  `field_attr` tinyint(2) NULL DEFAULT NULL COMMENT '字段属性，1：标准；2：可选；3：自定义',
  `description` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_material_data
-- ----------------------------
DROP TABLE IF EXISTS `t_material_data`;
CREATE TABLE `t_material_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键：自增',
  `tenant_id` int(11) NULL DEFAULT NULL COMMENT '租户号',
  `com_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `com_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `specify` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物料规格',
  `picture_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图号',
  `attribute` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性',
  `measure_unit1` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位1',
  `measure_unit2` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位2',
  `supplier` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商',
  `customer` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `updater` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `ext_info` json NULL COMMENT '自定义字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
