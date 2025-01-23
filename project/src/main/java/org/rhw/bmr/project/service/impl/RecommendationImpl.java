package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.dto.req.RecommendBookReqDTO;
import org.rhw.bmr.project.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO> implements RecommendationService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserPreferenceServiceImpl userPreferenceServiceImpl;

    @Override
    public List<BookDO> recommendBooksForUser(RecommendBookReqDTO requestParam) {


        Long userId = requestParam.getUserId();

        // 1. 计算所有图书的 PageRank
        Map<Long, Double> bookPageRank = computeBookPageRank();

        // 2. 获取用户的偏好
        List<UserPreferenceDO> preferences = getUserPreferences(userId);

        // 3. 根据用户偏好过滤图书
        Set<Long> preferredBookIds = getPreferredBookIds(preferences);

        // 4. 根据 PageRank 对偏好图书进行排序
        List<BookDO> recommendedBooks = bookMapper.selectList(
                        Wrappers.lambdaQuery(BookDO.class)
                                .in(BookDO::getId, preferredBookIds)
                ).stream()
                .sorted((b1, b2) -> Double.compare(
                        bookPageRank.getOrDefault(b2.getId(), 0.0),
                        bookPageRank.getOrDefault(b1.getId(), 0.0)
                ))
                .limit(10)
                .collect(Collectors.toList());

        // 5. 如果不足10本，补足随机书籍
        if (recommendedBooks.size() < 10) {
            int stillNeed = 10 - recommendedBooks.size();
            List<BookDO> randomBooks = queryRandomBooks(stillNeed);
            recommendedBooks.addAll(randomBooks);
        }

        return recommendedBooks;
    }

    /**
     * 计算图书的 PageRank
     */
    private Map<Long, Double> computeBookPageRank() {
        Graph<Long, org.jgrapht.graph.DefaultEdge> bookGraph = userPreferenceServiceImpl.getBookGraph();
        PageRank<Long, org.jgrapht.graph.DefaultEdge> pageRank = new PageRank<>(bookGraph, 0.85);
        Map<Long, Double> scores = new HashMap<>();
        for (Long vertex : bookGraph.vertexSet()) {
            scores.put(vertex, pageRank.getVertexScore(vertex));
        }
        return scores;
    }

    /**
     * 获取用户的偏好，按 like_count 降序，只取 top N
     */
    private List<UserPreferenceDO> getUserPreferences(Long userId) {
        LambdaQueryWrapper<UserPreferenceDO> query = Wrappers.lambdaQuery(UserPreferenceDO.class)
                .eq(UserPreferenceDO::getUserId, userId)
                .orderByDesc(UserPreferenceDO::getLikeCount)
                .last("LIMIT 6"); // 取前6个偏好

        return baseMapper.selectList(query);
    }

    /**
     * 根据用户偏好获取相关图书ID
     */
    private Set<Long> getPreferredBookIds(List<UserPreferenceDO> preferences) {
        Set<Long> bookIds = new HashSet<>();
        for (UserPreferenceDO pref : preferences) {
            List<BookDO> books = queryBooksByAuthorAndCategory(pref.getAuthor(), pref.getCategory());
            for (BookDO book : books) {
                bookIds.add(book.getId());
            }
        }
        return bookIds;
    }

    /**
     * 在 book 表中根据 author/category 来查询书籍
     */
    private List<BookDO> queryBooksByAuthorAndCategory(String author, String category) {
        LambdaQueryWrapper<BookDO> wrapper = new LambdaQueryWrapper<>();
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
     * 如果推荐书不足10本，则随机补足
     */
    private List<BookDO> queryRandomBooks(int stillNeed) {
        LambdaQueryWrapper<BookDO> wrapper = Wrappers.lambdaQuery(BookDO.class)
                .last("LIMIT " + stillNeed);
        return bookMapper.selectList(wrapper);
    }
}
