package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO> implements UserPreferenceService {

    @Autowired
    private BookMapper bookMapper;

    // 图书相似性图
    private Graph<Long, DefaultEdge> bookGraph;

    @PostConstruct
    public void init() {
        bookGraph = new SimpleGraph<>(DefaultEdge.class);
        buildInitialGraph();
    }

    /**
     * 构建初始图书相似性图
     */
    private void buildInitialGraph() {
        List<BookDO> books = bookMapper.selectList(null);
        for (BookDO book : books) {
            bookGraph.addVertex(book.getId());
        }

        // 根据相同作者或分类添加边
        for (int i = 0; i < books.size(); i++) {
            BookDO bookA = books.get(i);
            for (int j = i + 1; j < books.size(); j++) {
                BookDO bookB = books.get(j);
                if (bookA.getAuthor() != null && bookA.getAuthor().equals(bookB.getAuthor()) ||
                        bookA.getCategory() != null && bookA.getCategory().equals(bookB.getCategory())) {
                    bookGraph.addEdge(bookA.getId(), bookB.getId());
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

        // 查询 BookDO
        LambdaQueryWrapper<BookDO> bookQuery = Wrappers.lambdaQuery(BookDO.class).eq(BookDO::getId, bookId);
        BookDO bookDO = bookMapper.selectOne(bookQuery);

        if (bookDO == null) {
            return; // 或者抛出异常，取决于业务需求
        }

        String author = bookDO.getAuthor();
        String category = bookDO.getCategory();

        if ((author == null || author.isEmpty()) &&
                (category == null || category.isEmpty())) {
            return;
        }

        // 2. 查找是否已经有这条记录
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
            // 3b. 累加 likeCount，使用 UpdateWrapper 仅更新 likeCount
            LambdaUpdateWrapper<UserPreferenceDO> updateWrapper = Wrappers.lambdaUpdate(UserPreferenceDO.class)
                    .eq(UserPreferenceDO::getId, existPref.getId())
                    .set(UserPreferenceDO::getLikeCount, existPref.getLikeCount() + 1);
            update(updateWrapper);
        }

        // 更新图书相似性图
        updateBookGraph(bookDO);
    }

    /**
     * 根据用户阅读的图书更新图书相似性图
     * 可以根据新的阅读行为动态调整图结构
     */
    private void updateBookGraph(BookDO readBook) {
        // 假设用户阅读了一本书，找到所有与这本书相同作者或分类的书，并添加边
        List<BookDO> similarBooks = bookMapper.selectList(
                Wrappers.lambdaQuery(BookDO.class)
                        .eq(BookDO::getAuthor, readBook.getAuthor())
                        .or()
                        .eq(BookDO::getCategory, readBook.getCategory())
        );

        for (BookDO book : similarBooks) {
            if (!bookGraph.containsVertex(book.getId())) {
                bookGraph.addVertex(book.getId());
            }
            if (!book.getId().equals(readBook.getId()) && !bookGraph.containsEdge(readBook.getId(), book.getId())) {
                bookGraph.addEdge(readBook.getId(), book.getId());
            }
        }
    }

    /**
     * 提供图结构以便 RecommendationService 使用
     */
    public Graph<Long, DefaultEdge> getBookGraph() {
        return bookGraph;
    }
}
