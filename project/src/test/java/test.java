public class test {

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf("CREATE TABLE t_user_bookmark_%d (\n" +
                    "    id BIGINT AUTO_INCREMENT PRIMARY KEY,            \n" +
                    "    gid VARCHAR(20) NOT NULL,                        \n" +
                    "    username VARCHAR(50) NOT NULL,\n" +
                    "    bookId BIGINT NOT NULL,   \n" +
                    "    create_time DATETIME DEFAULT NULL COMMENT 'creation time',  \n" +
                    "    update_time DATETIME DEFAULT NULL COMMENT 'modified time', \n" +
                    "    del_flag TINYINT(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted'); \n", i);
        }
    }


}
