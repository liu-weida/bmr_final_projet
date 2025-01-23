package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.PostConstruct;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO>
        implements UserPreferenceService {

    @Autowired
    private BookMapper bookMapper;

    /**
     * 图书相似性图（邻接表）
     * key: bookId
     * value: 与 bookId 相似的所有其他书的 ID（双向添加，逻辑上是无向图）
     */
    private Map<Long, List<Long>> adjacencyList;

    @PostConstruct
    public void init() {
        adjacencyList = new HashMap<Long, List<Long>>();
        buildInitialGraph();
    }

    /**
     * 构建初始的图书相似性邻接表
     */
    private void buildInitialGraph() {
        List<BookDO> books = bookMapper.selectList(null);
        if (books == null || books.isEmpty()) {
            return;
        }

        // 1. 先把所有书加入 map
        for (BookDO book : books) {
            adjacencyList.putIfAbsent(book.getId(), new ArrayList<Long>());
        }

        // 2. 根据相同作者或分类添加边（无向图：需要在 A、B 两边都加）
        int size = books.size();
        for (int i = 0; i < size; i++) {
            BookDO bookA = books.get(i);
            Long idA = bookA.getId();
            for (int j = i + 1; j < size; j++) {
                BookDO bookB = books.get(j);
                Long idB = bookB.getId();

                boolean sameAuthor = false;
                if (bookA.getAuthor() != null && bookB.getAuthor() != null) {
                    sameAuthor = bookA.getAuthor().equals(bookB.getAuthor());
                }
                boolean sameCategory = false;
                if (bookA.getCategory() != null && bookB.getCategory() != null) {
                    sameCategory = bookA.getCategory().equals(bookB.getCategory());
                }

                if (sameAuthor || sameCategory) {
                    adjacencyList.get(idA).add(idB);
                    adjacencyList.get(idB).add(idA);
                }
            }
        }
    }

    @Override
    public void recordUserPreference(ReadBookReqDTO requestParam) {
        Long userId = requestParam.getUserid();
        Long bookId = requestParam.getBookId();

        // 1. 参数校验
        if (userId == null || bookId == null) {
            return;
        }

        // 2. 查询 BookDO
        LambdaQueryWrapper<BookDO> bookQuery = Wrappers.lambdaQuery(BookDO.class)
                .eq(BookDO::getId, bookId);
        BookDO bookDO = bookMapper.selectOne(bookQuery);
        if (bookDO == null) {
            return;
        }

        String author = bookDO.getAuthor();
        String category = bookDO.getCategory();
        if ((author == null || author.isEmpty()) &&
                (category == null || category.isEmpty())) {
            return;
        }

        // 3. 查找是否已经有这条记录
        LambdaQueryWrapper<UserPreferenceDO> wrapper = Wrappers.lambdaQuery(UserPreferenceDO.class)
                .eq(UserPreferenceDO::getUserId, userId)
                .eq(author != null, UserPreferenceDO::getAuthor, author)
                .eq(category != null, UserPreferenceDO::getCategory, category);

        UserPreferenceDO existPref = getOne(wrapper);
        if (existPref == null) {
            // 3a. 新建
            UserPreferenceDO newPref = new UserPreferenceDO();
            newPref.setUserId(userId);
            newPref.setAuthor(author);
            newPref.setCategory(category);
            newPref.setLikeCount(1);
            save(newPref);
        } else {
            // 3b. 累加 likeCount
            LambdaUpdateWrapper<UserPreferenceDO> updateWrapper = Wrappers.lambdaUpdate(UserPreferenceDO.class)
                    .eq(UserPreferenceDO::getId, existPref.getId())
                    .set(UserPreferenceDO::getLikeCount, existPref.getLikeCount() + 1);
            update(updateWrapper);
        }

        // 4. 动态更新图结构
        updateBookGraph(bookDO);
    }

    /**
     * 根据用户阅读的图书动态更新图：
     * 如果发现新的相似书籍，需要在邻接表中把它们连起来
     */
    private void updateBookGraph(BookDO readBook) {
        Long readBookId = readBook.getId();
        if (!adjacencyList.containsKey(readBookId)) {
            adjacencyList.put(readBookId, new ArrayList<Long>());
        }

        // 找到所有与这本书相同作者或分类的书
        List<BookDO> similarBooks = bookMapper.selectList(
                Wrappers.lambdaQuery(BookDO.class)
                        .eq(BookDO::getAuthor, readBook.getAuthor())
                        .or()
                        .eq(BookDO::getCategory, readBook.getCategory())
        );

        for (BookDO book : similarBooks) {
            Long otherId = book.getId();
            if (!otherId.equals(readBookId)) {
                // 双向添加
                if (!adjacencyList.containsKey(otherId)) {
                    adjacencyList.put(otherId, new ArrayList<Long>());
                }
                // A->B
                if (!adjacencyList.get(readBookId).contains(otherId)) {
                    adjacencyList.get(readBookId).add(otherId);
                }
                // B->A
                if (!adjacencyList.get(otherId).contains(readBookId)) {
                    adjacencyList.get(otherId).add(readBookId);
                }
            }
        }
    }

    /**
     * 提供邻接表给外部使用（例如计算 PageRank）
     */
    public Map<Long, List<Long>> getBookAdjacencyList() {
        return adjacencyList;
    }
}
