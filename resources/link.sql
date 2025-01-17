CREATE DATABASE bmr;

CREATE TABLE `t_user_0`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_1`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_2`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_3`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_4`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_5`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_6`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_7`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_8`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_9`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_10`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_11`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_12`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_13`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_14`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_15`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_group_0`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_1`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_2`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_3`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_4`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_5`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_6`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_7`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_8`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_9`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_10`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_11`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_12`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_13`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_14`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_15`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);

ALTER TABLE `t_group_0` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_1` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_2` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_3` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_4` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_5` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_6` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_7` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_8` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_9` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_10` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_11` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_12` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_13` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_14` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_15` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';


CREATE TABLE `t_books_0`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
)  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_1`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_2`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_3`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_4`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_5`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_6`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_7`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_8`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_9`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_10`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_11`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_12`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_13`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_14`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;CREATE TABLE `t_books_15`
                             (
                                 `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
                                 `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
                                 `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
                                 `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
                                 `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
                                 `description`          text DEFAULT NULL COMMENT 'Book Description',
                                 `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
                                 `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
                                 `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
                                 `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
                                 `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
                                 `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
                                 `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
                                 `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_unique_title_author` (`title`, `author`) USING BTREE
                             )  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;