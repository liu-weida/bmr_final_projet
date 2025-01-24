import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf(
                    "CREATE TABLE `t_books_%d`\n" +
                            "(\n" +
                            "    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',\n" +
                            "    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',\n" +
                            "    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',\n" +
                            "    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',\n" +
                            "    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',\n" +
                            "    `description`          text DEFAULT NULL COMMENT 'Book Description',\n" +
                            "    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',\n" +
                            "    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',\n" +
                            "    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',\n" +
                            "    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',\n" +
                            "    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',\n" +
                            "    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',\n" +
                            "    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',\n" +
                            "    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',\n" +
                            "    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',\n" +
                            "    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',"+
                            "    PRIMARY KEY (`id`),\n" +
                            "    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE\n" +
                            ")  CHARACTER SET utf8mb4\n" +
                            "   COLLATE utf8mb4_general_ci;\n",i);
        }
    }


}
