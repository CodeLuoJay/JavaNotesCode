/*
 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : localhost:3306
 Source Schema         : springdb

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 10/05/2021 22:33:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(11) NOT NULL DEFAULT 0,
  `name` varchar(80) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (0, 'luojay', 'luojay@qq.com', 24);
INSERT INTO `student` VALUES (1, 'mysqlTest', 'mysql@qq.com', 25);
INSERT INTO `student` VALUES (2, 'bobi', 'bobi@qq.com', 25);
INSERT INTO `student` VALUES (3, 'bobi', 'bobi@qq.com', 25);

SET FOREIGN_KEY_CHECKS = 1;
