public class test {

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf("CREATE TABLE `t_user_preference_%d` (\n" +
                    "  `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',\n" +
                    "  `user_id`           BIGINT(20) NOT NULL COMMENT 'user ID',\n" +
                    "  `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',\n" +
                    "  `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',\n" +
                    "  `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',\n" +
                    "   `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',\n" +
                    "    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',\n" +
                    "    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',\n" +
                    "    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `uk_user_author_category`(`user_id`,`author`,`category`)\n" +
                    ") ENGINE=InnoDB \n" +
                    "  DEFAULT CHARSET=utf8mb4 \n" +
                    "  COLLATE=utf8mb4_general_ci;\n", i);
        }
    }


}
