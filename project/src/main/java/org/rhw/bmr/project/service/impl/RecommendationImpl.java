package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.common.algo.CustomPageRankAlgorithm;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.dto.req.RecommendBookReqDTO;
import org.rhw.bmr.project.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Collections;
import java.util.Comparator;

@Service
public class RecommendationImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO>
        implements RecommendationService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserPreferenceServiceImpl userPreferenceServiceImpl;

    @Override
    public List<BookDO> recommendBooksForUser(RecommendBookReqDTO requestParam) {
        Long userId = requestParam.getUserId();

        // 1. 计算所有图书的 PageRank（使用我们手写的 PageRank）
        Map<Long, Double> bookPageRank = computeBookPageRank();

        // 2. 获取用户的偏好
        List<UserPreferenceDO> preferences = getUserPreferences(userId);

        // 3. 根据用户偏好过滤图书
        Set<Long> preferredBookIds = getPreferredBookIds(preferences);

        // 4. 查询出这些图书，并根据 PageRank 值排序
        List<BookDO> recommendedBooks = new ArrayList<BookDO>();
        if (!preferredBookIds.isEmpty()) {
            List<BookDO> filteredBooks = bookMapper.selectList(
                    Wrappers.lambdaQuery(BookDO.class).in(BookDO::getId, preferredBookIds)
            );
            if (filteredBooks != null && !filteredBooks.isEmpty()) {
                recommendedBooks.addAll(filteredBooks);
            }
        }

        // 排序：使用传统 Comparator，避免 Lambda
        Collections.sort(recommendedBooks, new Comparator<BookDO>() {
            @Override
            public int compare(BookDO b1, BookDO b2) {
                double score1 = bookPageRank.getOrDefault(b1.getId(), 0.0);
                double score2 = bookPageRank.getOrDefault(b2.getId(), 0.0);
                // 降序
                return Double.compare(score2, score1);
            }
        });

        // 只取前10本
        List<BookDO> finalList = new ArrayList<BookDO>();
        int limit = Math.min(10, recommendedBooks.size());
        for (int i = 0; i < limit; i++) {
            finalList.add(recommendedBooks.get(i));
        }

        // 5. 如果不足10本，则随机补足
        if (finalList.size() < 10) {
            int stillNeed = 10 - finalList.size();
            List<BookDO> randomBooks = queryRandomBooks(stillNeed);
            finalList.addAll(randomBooks);
        }

        return finalList;
    }

    /**
     * 调用自定义 PageRank 算法
     */
    private Map<Long, Double> computeBookPageRank() {
        // 从 UserPreferenceServiceImpl 获取构建好的邻接表
        Map<Long, List<Long>> bookAdjList = userPreferenceServiceImpl.getBookAdjacencyList();

        // 这里可根据需要调整参数
        double dampingFactor = 0.85;
        int maxIterations = 100;      // 最大迭代次数
        double epsilon = 1.0e-6;      // 收敛阈值

        return CustomPageRankAlgorithm.computePageRank(bookAdjList, dampingFactor, maxIterations, epsilon);
    }

    /**
     * 获取用户的偏好，按 like_count 降序，只取前 N 项
     */
    private List<UserPreferenceDO> getUserPreferences(Long userId) {
        return baseMapper.selectList(
                Wrappers.lambdaQuery(UserPreferenceDO.class)
                        .eq(UserPreferenceDO::getUserId, userId)
                        .orderByDesc(UserPreferenceDO::getLikeCount)
                        .last("LIMIT 6")
        );
    }

    /**
     * 根据用户偏好获取相关图书ID
     */
    private Set<Long> getPreferredBookIds(List<UserPreferenceDO> preferences) {
        Set<Long> bookIds = new HashSet<Long>();
        for (int i = 0; i < preferences.size(); i++) {
            UserPreferenceDO pref = preferences.get(i);
            List<BookDO> books = queryBooksByAuthorAndCategory(pref.getAuthor(), pref.getCategory());
            for (int j = 0; j < books.size(); j++) {
                bookIds.add(books.get(j).getId());
            }
        }
        return bookIds;
    }

    /**
     * 在 book 表中根据 author/category 查询书籍
     */
    private List<BookDO> queryBooksByAuthorAndCategory(String author, String category) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookDO> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookDO>();

        if (author != null && !author.isEmpty()) {
            wrapper.eq(BookDO::getAuthor, author);
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq(BookDO::getCategory, category);
        }
        // 按 click_count 降序
        wrapper.orderByDesc(BookDO::getClickCount);
        // 限制查询数量
        wrapper.last("LIMIT 20");

        return bookMapper.selectList(wrapper);
    }

    /**
     * 如果推荐书不足 10 本，则随机补足
     */
    private List<BookDO> queryRandomBooks(int stillNeed) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookDO> wrapper =
                Wrappers.lambdaQuery(BookDO.class).last("LIMIT " + stillNeed);
        return bookMapper.selectList(wrapper);
    }
}
