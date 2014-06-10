/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50167
Source Host           : localhost:3306
Source Database       : bos

Target Server Type    : MYSQL
Target Server Version : 50167
File Encoding         : 65001

Date: 2014-06-10 23:06:17
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `bos_params`
-- ----------------------------
DROP TABLE IF EXISTS `bos_params`;
CREATE TABLE `bos_params` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `k` varchar(32) DEFAULT NULL COMMENT '对应的key',
  `v` varchar(255) DEFAULT NULL COMMENT '对应的value',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bos_params
-- ----------------------------
INSERT INTO `bos_params` VALUES ('1', '0', 'user_status', '1', '正常', 'bos_user.userStatus', '2014-06-08 10:43:57');
INSERT INTO `bos_params` VALUES ('2', '0', 'user_status', '2', '锁定', 'bos_user.userStatus', '2014-06-08 10:44:00');
INSERT INTO `bos_params` VALUES ('3', '0', 'work_log_type', '1', '生活日志', 'bos_work_log.type', '2014-06-08 10:46:12');
INSERT INTO `bos_params` VALUES ('4', '0', 'work_log_type', '2', '工作日志', 'bos_work_log.type', '2014-06-08 10:46:30');
INSERT INTO `bos_params` VALUES ('5', '0', 'work_log_type', '3', '普通日志', '中国测试', '2014-06-10 02:36:08');
INSERT INTO `bos_params` VALUES ('6', '0', 'fetion_zenglb', 'un', '13425140701', 'zenglb的账号', '2014-06-10 03:05:35');
INSERT INTO `bos_params` VALUES ('7', '0', 'fetion_zenglb', 'pwd', 'billing6814740', '密码', '2014-06-10 03:06:03');
INSERT INTO `bos_params` VALUES ('8', '0', 'szt_zenglb', 'cardNo', '290925671', 'zenglb深圳通卡号', '2014-06-10 20:47:29');

-- ----------------------------
-- Table structure for `bos_task`
-- ----------------------------
DROP TABLE IF EXISTS `bos_task`;
CREATE TABLE `bos_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bos_task
-- ----------------------------
INSERT INTO `bos_task` VALUES ('1', 'Study PlayFramework 2.0', 'http://www.playframework.org/', '2');
INSERT INTO `bos_task` VALUES ('2', 'Study Grails 2.0', 'http://www.grails.org/', '2');
INSERT INTO `bos_task` VALUES ('3', 'Try SpringFuse', 'http://www.springfuse.com/', '2');
INSERT INTO `bos_task` VALUES ('4', 'Try Spring Roo', 'http://www.springsource.org/spring-roo', '2');
INSERT INTO `bos_task` VALUES ('5', 'Release SpringSide 4.0', 'As soon as posibble.', '2');
INSERT INTO `bos_task` VALUES ('6', 'test', 'test', '1');
INSERT INTO `bos_task` VALUES ('7', 'bbb', 'bbbb', '1');

-- ----------------------------
-- Table structure for `bos_user`
-- ----------------------------
DROP TABLE IF EXISTS `bos_user`;
CREATE TABLE `bos_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loginName` varchar(64) NOT NULL COMMENT '用户名，用于登录',
  `cnName` varchar(64) NOT NULL COMMENT '中文名称',
  `password` varchar(255) NOT NULL,
  `salt` varchar(64) NOT NULL,
  `roles` varchar(255) NOT NULL,
  `registerDate` datetime NOT NULL COMMENT '注册时间',
  `userStatus` tinyint(4) DEFAULT NULL COMMENT '用户状态1正常0锁定',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`loginName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bos_user
-- ----------------------------
INSERT INTO `bos_user` VALUES ('1', 'zlb', '曾林宝', '06cb4c7fa3f65bc13dfffc3b0fa0bddc21e382b0', '3c1b1278969ad18d', 'user,admin', '2014-06-08 03:02:05', '1', '曾林宝');
INSERT INTO `bos_user` VALUES ('2', 'aaaa', 'aaaa', '760261da2d9606ad647c02fef404da6d86891dbc', '5a812d4df9436f66', 'user', '2014-06-08 03:25:26', '1', '简单测试');
INSERT INTO `bos_user` VALUES ('3', 'bbbb', 'bbbb', '20b07c784ee53dddfa1073ee74da2312bca0e8a4', 'b4d8a6ce61d4f86f', 'user', '2014-06-08 03:25:34', '1', null);
INSERT INTO `bos_user` VALUES ('4', 'aa', 'aa', '2d484d16c0af9c17dce05b767e7cd3a07126b452', '0498bbabb9b5b56d', 'user', '2014-06-09 23:35:47', '1', null);
INSERT INTO `bos_user` VALUES ('5', 'zenglb', '曾林宝', '1397ba48abb9a6301194b3beeae037193030dba5', '6914c33f5808c86f', 'user', '2014-06-10 01:05:58', '1', null);

-- ----------------------------
-- Table structure for `bos_user_bak`
-- ----------------------------
DROP TABLE IF EXISTS `bos_user_bak`;
CREATE TABLE `bos_user_bak` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `un` varchar(32) NOT NULL COMMENT '用户名，用于登录',
  `pwd` varchar(64) NOT NULL COMMENT '密码',
  `created` datetime DEFAULT NULL,
  `userStatus` tinyint(4) DEFAULT NULL COMMENT '用户状态1正常0锁定',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `cnName` varchar(32) DEFAULT NULL COMMENT '中文名称',
  `roles` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户信息';

-- ----------------------------
-- Records of bos_user_bak
-- ----------------------------
INSERT INTO `bos_user_bak` VALUES ('1', 'zlb', '5F9256478845C9DBB59DE62D06B0292C', '2014-06-07 20:01:16', '1', '备注说明', '曾林宝', null);

-- ----------------------------
-- Table structure for `bos_user_bind`
-- ----------------------------
DROP TABLE IF EXISTS `bos_user_bind`;
CREATE TABLE `bos_user_bind` (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '类型',
  `description` varchar(255) DEFAULT NULL COMMENT '绑定关系描述',
  `v1` varchar(255) DEFAULT NULL COMMENT '变量v1',
  `v2` varchar(255) DEFAULT NULL COMMENT '变量v2',
  `v3` varchar(255) DEFAULT NULL COMMENT '变量v3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户绑定信息表';

-- ----------------------------
-- Records of bos_user_bind
-- ----------------------------

-- ----------------------------
-- Table structure for `bos_work_log`
-- ----------------------------
DROP TABLE IF EXISTS `bos_work_log`;
CREATE TABLE `bos_work_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
  `type` tinyint(4) DEFAULT NULL COMMENT '日志类型',
  `content` varchar(256) DEFAULT NULL COMMENT '内容',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='工作日志表';

-- ----------------------------
-- Records of bos_work_log
-- ----------------------------
INSERT INTO `bos_work_log` VALUES ('3', '1', '1', '中国测试', '2014-06-08 15:39:38');
INSERT INTO `bos_work_log` VALUES ('5', '5', '1', '中国测试中文网', '2014-06-10 01:36:35');
INSERT INTO `bos_work_log` VALUES ('6', '5', '1', '魂牵梦萦', '2014-06-10 02:18:46');
INSERT INTO `bos_work_log` VALUES ('7', '5', '2', '魂牵梦萦', '2014-06-10 02:51:13');
