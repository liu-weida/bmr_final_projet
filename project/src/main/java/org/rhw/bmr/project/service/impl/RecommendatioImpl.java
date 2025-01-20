package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendatioImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO> implements RecommendationService {


    @Autowired
    private BookMapper bookMapper; // 对应 book 表
    @Override
    public List<BookDO> recommendBooksForUser(Long userId) {
        // 1. 查询用户偏好，取前 3 位作者和前 3 个分类（可根据需求调整）
        List<UserPreferenceDO> preferences = getUserPreferences(userId);

        // 2. 从书籍表里查询匹配这些作者/分类的图书
        Set<BookDO> recommendedSet = new LinkedHashSet<>();
        for (UserPreferenceDO pref : preferences) {
            // 根据作者或分类查询书籍
            List<BookDO> books = queryBooksByAuthorAndCategory(pref.getAuthor(), pref.getCategory());
            // 将结果加入去重集合（Set）防止重复
            recommendedSet.addAll(books);

            // 如果已经大于等于10本，就可以跳出
            if (recommendedSet.size() >= 10) {
                break;
            }
        }

        // 3. 如果不够10本，从所有书随机补足
        if (recommendedSet.size() < 10) {
            int stillNeed = 10 - recommendedSet.size();
            List<BookDO> randomBooks = queryRandomBooks(stillNeed);
            recommendedSet.addAll(randomBooks);
        }

        // 4. 只返回前10本
        return recommendedSet.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * 根据用户ID查询偏好列表，按 like_count 降序，只取 top N
     * 这里同时取作者和分类。可根据需求把 author/category 分开查
     */
    private List<UserPreferenceDO> getUserPreferences(Long userId) {

        LambdaQueryWrapper<UserPreferenceDO> last = Wrappers.lambdaQuery(UserPreferenceDO.class)
                .eq(UserPreferenceDO::getUserId, userId)
                .orderByDesc(UserPreferenceDO::getLikeCount)
                .last("limit 6");

        return baseMapper.selectList(last);
    }

    /**
     * 在 book 表中根据 author/category 来查询书籍
     * 可以加上其它条件或排序逻辑
     */
    private List<BookDO> queryBooksByAuthorAndCategory(String author, String category) {
        LambdaQueryWrapper<BookDO> wrapper = new LambdaQueryWrapper<>();
        if (author != null && !author.isEmpty()) {
            wrapper.eq(BookDO::getAuthor, author);
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq(BookDO::getCategory, category);
        }
        // 这里不做排序就随机按插入顺序或ID排序; 或可以按 click_count 降序
        wrapper.orderByDesc(BookDO::getClickCount);

        // 你也可以限制只查前 x 本，比如 10
        wrapper.last("limit 10");

        return bookMapper.selectList(wrapper);
    }

    /**
     * 如果推荐书不足10本，则随机补足
     * 简单实现: 按随机查询, or 先查所有ID再随机取
     * 下面演示方式是 "查所有ID, then 随机抽stillNeed个ID"
     */
    private List<BookDO> queryRandomBooks(int stillNeed) {


        LambdaQueryWrapper<BookDO> last = Wrappers.lambdaQuery(BookDO.class).last("limit " + 20);

        // 1. 查出所有book ID
        List<BookDO> allIds = bookMapper.selectList(last);

        if (allIds.size() <= stillNeed) {
            // 全部返回
            return allIds;
        }

        // 2. 随机打乱并取前 stillNeed 个ID
        Collections.shuffle(allIds);
        List<BookDO> randomPick = allIds.subList(5, stillNeed);

        // 3. 查对应记录
        return randomPick;
    }
}
