/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50521
Source Host           : localhost:3306
Source Database       : spring2021

Target Server Type    : MYSQL
Target Server Version : 50521
File Encoding         : 65001

Date: 2021-05-08 19:31:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `balance` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account
-- ----------------------------
INSERT INTO `t_account` VALUES ('1', '小王', '1000');
INSERT INTO `t_account` VALUES ('4', '小强', '7000');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userLoginName` varchar(10) DEFAULT NULL,
  `userPhone` varchar(255) DEFAULT NULL,
  `userAge` int(6) DEFAULT NULL,
  `userPwd` varchar(10) DEFAULT NULL,
  `userName` varchar(15) DEFAULT NULL,
  `state` int(11) DEFAULT NULL COMMENT 'state 1启用 0冻结',
  `createTime` date DEFAULT NULL,
  `delState` int(11) DEFAULT NULL COMMENT 'delState 删除状态1 删除 0未删除',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'luoxian', '1330010101', null, '123456', '杨晓林', '1', '2018-07-09', '0');
INSERT INTO `t_user` VALUES ('2', '小王', null, null, '123456', '王二小', '1', '2018-07-14', '0');
INSERT INTO `t_user` VALUES ('3', 'qazwsx', null, null, 'qazwsx', '吕杰', '0', '2018-09-05', '1');
INSERT INTO `t_user` VALUES ('4', 'luoxianxin', null, null, '123456', '吕杰', '1', '2018-09-06', '1');
INSERT INTO `t_user` VALUES ('8', 'zhangsan', null, null, '324234', '张三', '1', '2018-09-04', '1');
INSERT INTO `t_user` VALUES ('9', 'lisi', null, null, '1234567', '小张', '0', '2018-09-26', '0');
INSERT INTO `t_user` VALUES ('10', 'lisi1', null, null, '12345671', '小张1', '1', '2018-09-26', '1');
INSERT INTO `t_user` VALUES ('11', 'lisi2', null, null, '12345672', '小张2', '1', '2018-09-26', '1');
INSERT INTO `t_user` VALUES ('12', 'lisi3', null, null, '12345673', '小张3', '1', '2018-09-26', '0');
INSERT INTO `t_user` VALUES ('13', 'lisi4', null, null, '12345674', '小张4', '1', '2018-09-26', '1');
INSERT INTO `t_user` VALUES ('14', 'lisi5', null, null, '12345675', '小张5', '0', '2018-09-26', '1');
INSERT INTO `t_user` VALUES ('15', 'lisi6', null, null, '12345676', '小张6', '1', '2018-09-26', '0');
INSERT INTO `t_user` VALUES ('16', 'lisi7', null, null, '12345677', '小张7', '1', '2018-09-26', '0');
INSERT INTO `t_user` VALUES ('17', 'lisi8', null, null, '12345678', '小张8', '0', '2018-09-26', '0');
INSERT INTO `t_user` VALUES ('18', 'admin', '1338989090', '18', 'admin123', '小白', '1', '2021-05-08', '0');
INSERT INTO `t_user` VALUES ('19', 'admin', '1338989090', '18', 'admin123', '小白', '1', '2021-05-08', '0');
