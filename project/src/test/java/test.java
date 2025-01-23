import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class test {

//    public static void main(String[] args) {
//        for (int i = 0; i < 16; i++) {
//            System.out.printf(
//                    "CREATE TABLE `t_user_preference_%d` (\n" +
//                            "                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',\n" +
//                            "                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',\n" +
//                            "                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',\n" +
//                            "                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',\n" +
//                            "                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',\n" +
//                            "                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',\n" +
//                            "                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',\n" +
//                            "                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',\n" +
//                            "                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',\n" +
//                            "                                       PRIMARY KEY (`id`),\n" +
//                            "                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)\n" +
//                            ") ENGINE=InnoDB\n" +
//                            "  DEFAULT CHARSET=utf8mb4\n" +
//                            "  COLLATE=utf8mb4_general_ci;\n",i);
//        }
//    }

    public static void main(String[] args) {
        String fileURL = "https://www.gutenberg.org/ebooks/68566.txt.utf-8";
        String factor = "a"; // 替换为要搜索的关键字

        try {
            List<String> matchingLines = KPM(factor, fileURL);
            for (String line : matchingLines) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.err.println("搜索失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> KPM(String factor, String fileURL) throws Exception {
        char[] factorChar = factor.toCharArray();
        int[] carryover = computeCarryover(factorChar);
        List<String> matchingLines = new ArrayList<>();

        while (true) {
            // 从URL读取内容
            URL url = new URL(fileURL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setInstanceFollowRedirects(false); // 禁用自动重定向以检查重定向地址
            httpConnection.setRequestMethod("GET");

            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                // 获取重定向地址
                fileURL = httpConnection.getHeaderField("Location");
                System.out.println("重定向到: " + fileURL);
                continue; // 跳转到新地址
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    int i = 0;
                    int j = 0;
                    int textLen = line.length();
                    int factorLen = factorChar.length;
                    while (i < textLen) {
                        if (j == -1 || line.charAt(i) == factorChar[j]) {
                            i++;
                            j++;
                            if (j == factorLen) {
                                matchingLines.add(lineNumber + ": " + line);
                                j = carryover[j];
                            }
                        } else {
                            j = carryover[j];
                        }
                    }
                }
            } finally {
                httpConnection.disconnect();
            }

            break; // 如果没有重定向则退出循环
        }

        return matchingLines;
    }

    public static int[] computeCarryover(char[] pattern) {
        int len = pattern.length;
        int[] next = new int[len + 1];
        next[0] = -1;
        int i = 0, j = -1;

        // getLTS
        while (i < len) {
            if (j == -1 || pattern[i] == pattern[j]) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }
        }
        // LTS to carryover
        for (i = 1; i <= len; i++) {
            if (next[i] >= 0 && i < len && pattern[i] == pattern[next[i]]) {
                next[i] = next[next[i]];
            }
        }
        return next;
    }

}
