package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.service.BookInsertionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BookInsertionImpl extends ServiceImpl<BookMapper, BookDO> implements BookInsertionService {

    private static final Logger logger = LoggerFactory.getLogger(BookInsertionImpl.class);

    @Value("${scheduledTaskInsertBook.url}")
    private String url;

    // 备份文件夹路径，可以在 application.properties 中配置
    @Value("${scheduledTaskInsertBook.backingUp}")
    private String backupFolder;

    // 表格模板文件路径
    @Value("${scheduledTaskInsertBook.formworkFile}")
    private String formworkFile;

    @Override
    public void insertBook() {
        logger.info("开始处理文件夹: {}", url);
        Path folderPath = Paths.get(url);

        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            logger.error("指定的路径不存在或不是文件夹: {}", url);
            return;
        }

        // 确保备份文件夹存在
        Path backupPath = Paths.get(backupFolder);
        try {
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
                logger.info("创建备份文件夹: {}", backupFolder);
            }
        } catch (IOException e) {
            logger.error("无法创建备份文件夹 {}: {}", backupFolder, e.getMessage(), e);
            return;
        }

        File folder = folderPath.toFile();
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".csv") ||
                        name.toLowerCase().endsWith(".xlsx") ||
                        name.toLowerCase().endsWith(".xls")
        );

        if (files == null || files.length == 0) {
            logger.info("文件夹中没有可处理的CSV或Excel文件。");

            // 检查并创建表格模板文件
            Path formworkPath = Paths.get(formworkFile);
            File formwork = formworkPath.toFile();

            if (!formwork.exists()) {
                createFormworkFile();
            } else {
                logger.info("表格模板文件已存在: {}", formworkFile);
            }

            // 任务正常结束
            logger.info("任务完成，无需进一步操作。");
            return;
        }

        for (File file : files) {
            logger.info("正在处理文件: {}", file.getName());
            try {
                List<BookDO> books = new ArrayList<>();

                if (file.getName().toLowerCase().endsWith(".csv")) {
                    books = parseCSV(file);
                } else if (file.getName().toLowerCase().endsWith(".xlsx") || file.getName().toLowerCase().endsWith(".xls")) {
                    books = parseExcel(file);
                }

                if (!books.isEmpty()) {
                    // 批量插入数据库
                    saveBatch(books);
                    logger.info("成功插入 {} 条记录到数据库。", books.size());

                    // 备份已处理的文件
                    backupFile(file, backupPath);
                } else {
                    logger.warn("文件 {} 中没有有效的数据。", file.getName());
                    // 备份空文件，便于后续检查
                    backupFile(file, backupPath);
                }
            } catch (Exception e) {
                logger.error("处理文件 {} 时发生错误: {}", file.getName(), e.getMessage(), e);
                // 可以选择在出错时将文件移动到一个错误文件夹，以便后续处理
            }
        }

        // 创建表格模板文件（仅在存在文件的情况下执行，不再需要）
        // createFormworkFile();

        logger.info("文件夹处理完成: {}", url);
    }

    /**
     * 解析CSV文件
     *
     * @param file CSV文件
     * @return BookDO列表
     * @throws IOException
     * @throws CsvValidationException
     */
    private List<BookDO> parseCSV(File file) throws IOException, CsvValidationException {
        List<BookDO> books = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] headers = csvReader.readNext(); // 读取标题行
            if (headers == null || headers.length < 9) {
                logger.warn("CSV文件 {} 没有足够的标题列。", file.getName());
                return books;
            }

            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if (values.length < 9) {
                    logger.warn("CSV文件 {} 的一行数据列数不足: {}", file.getName(), String.join(",", values));
                    continue;
                }

                BookDO book = mapToBookDO(values);
                if (book != null) {
                    books.add(book);
                }
            }
        }

        return books;
    }

    /**
     * 解析Excel文件
     *
     * @param file Excel文件
     * @return BookDO列表
     * @throws IOException
     */
    private List<BookDO> parseExcel(File file) throws IOException {
        List<BookDO> books = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                logger.warn("Excel文件 {} 没有数据。", file.getName());
                return books;
            }

            Row headerRow = rowIterator.next(); // 标题行
            if (headerRow.getLastCellNum() < 9) {
                logger.warn("Excel文件 {} 的标题行列数不足。", file.getName());
                return books;
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String[] values = new String[9];
                for (int i = 0; i < 9; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    values[i] = getCellValueAsString(cell);
                }

                BookDO book = mapToBookDO(values);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (Exception e) {
            logger.error("解析Excel文件 {} 时发生错误: {}", file.getName(), e.getMessage(), e);
            throw e;
        }

        return books;
    }

    /**
     * 将CSV或Excel行数据映射为BookDO对象
     *
     * @param values 行数据
     * @return BookDO对象
     */
    private BookDO mapToBookDO(String[] values) {
        try {
            BookDO book = new BookDO();
            // 假设字段顺序为：refId, title, storagePath, author, category, description, language, clickCount, img

            book.setRefId(StringUtils.hasText(values[0]) ? Long.valueOf(values[0].trim()) : null);
            book.setTitle(StringUtils.hasText(values[1]) ? values[1].trim() : null);
            book.setStoragePath(StringUtils.hasText(values[2]) ? values[2].trim() : null);
            book.setAuthor(StringUtils.hasText(values[3]) ? values[3].trim() : null);
            book.setCategory(StringUtils.hasText(values[4]) ? values[4].trim() : null);
            book.setDescription(StringUtils.hasText(values[5]) ? values[5].trim() : null);
            book.setLanguage(StringUtils.hasText(values[6]) ? values[6].trim() : null);

            if (StringUtils.hasText(values[7])) {
                try {
                    book.setClickCount((long) Integer.parseInt(values[7].trim()));
                } catch (NumberFormatException e) {
                    logger.warn("无法解析clickCount: {}, 默认设置为0", values[7]);
                    book.setClickCount(0L);
                }
            } else {
                book.setClickCount(0L); // 默认值
            }

            book.setImg(StringUtils.hasText(values[8]) ? values[8].trim() : null);
            book.setEsSyncFlag(0);

            return book;
        } catch (Exception e) {
            logger.error("映射BookDO时发生错误: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取单元格的字符串值
     *
     * @param cell 单元格
     * @return 字符串值
     */
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    // 如果是整数，去掉小数点
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // 可以根据需要解析公式
                try {
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    switch (cellValue.getCellType()) {
                        case STRING:
                            return cellValue.getStringValue();
                        case NUMERIC:
                            return String.valueOf(cellValue.getNumberValue());
                        case BOOLEAN:
                            return String.valueOf(cellValue.getBooleanValue());
                        default:
                            return "";
                    }
                } catch (Exception e) {
                    logger.warn("解析公式单元格时发生错误: {}", e.getMessage(), e);
                    return "";
                }
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }

    /**
     * 备份已处理的文件到指定的备份文件夹，并重命名
     *
     * @param file       原始文件
     * @param backupPath 备份文件夹路径
     */
    private void backupFile(File file, Path backupPath) {
        try {
            // 获取当前日期，格式为 yyyyMMdd
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String dateStr = currentDate.format(formatter);

            // 构建新的文件名
            String newFileName = "InfoBackingUp_" + dateStr + "_" + file.getName();

            // 目标路径
            Path targetPath = backupPath.resolve(newFileName);

            // 移动并重命名文件
            Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("已备份并重命名文件: {} 到 {}", file.getName(), targetPath.toString());
        } catch (IOException e) {
            logger.error("备份文件 {} 时发生错误: {}", file.getName(), e.getMessage(), e);
        }
    }

    /**
     * 创建表格模板文件 bookInfoFormwork.csv
     */
    private void createFormworkFile() {
        Path formworkPath = Paths.get(formworkFile);
        File formwork = formworkPath.toFile();

        if (formwork.exists()) {
            logger.info("表格模板文件已存在: {}", formworkFile);
            return;
        }

        String[] headers = {"refId", "title", "storagePath", "author", "category", "description", "language", "clickCount", "img"};

        try (java.io.FileWriter writer = new java.io.FileWriter(formwork)) {
            writer.append(String.join(",", headers)).append("\n");
            logger.info("已创建表格模板文件: {}", formworkFile);
        } catch (IOException e) {
            logger.error("创建表格模板文件 {} 时发生错误: {}", formworkFile, e.getMessage(), e);
        }
    }
}
