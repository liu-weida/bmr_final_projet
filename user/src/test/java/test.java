public class test {
    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf(
                    "DELETE FROM t_link_goto_%d;\n", i
            );
        }
    }
}
