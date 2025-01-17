public class test {

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf("ALTER TABLE `t_books_%d`\n" +
                    "ADD COLUMN `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: 未同步 1: 已同步';", i);
        }
    }


}
