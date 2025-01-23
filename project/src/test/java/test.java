public class test {

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf(
                    "ALTER TABLE t_books_%d DROP INDEX `idx_unique_title_author`;\n" +
                    "ALTER TABLE t_books_%d ADD UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE;\n\n", i,i);
        }
    }


}
