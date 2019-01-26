/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50538
Source Host           : localhost:3306
Source Database       : qiniuyun

Target Server Type    : MYSQL
Target Server Version : 50538
File Encoding         : 65001

Date: 2019-01-26 23:08:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for myfile
-- ----------------------------
DROP TABLE IF EXISTS `myfile`;
CREATE TABLE `myfile` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `md5` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `createtime` datetime NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of myfile
-- ----------------------------
INSERT INTO `myfile` VALUES ('1089177773894901760', '281657152740.jpg', '230850fc89f2785b19a631b83fcc2869', '103.14KB   ', 'http://myfile.buildworld.cn/281657152740.jpg', '2019-01-26 23:06:31', '1');
INSERT INTO `myfile` VALUES ('1089176079819710464', '360截图1700101387113126.jpg', '2ac4f73e9cd1b3eb3b0b561be5868049', '23.44KB   ', 'http://myfile.buildworld.cn/360截图1700101387113126.jpg', '2019-01-26 22:59:47', '1');
