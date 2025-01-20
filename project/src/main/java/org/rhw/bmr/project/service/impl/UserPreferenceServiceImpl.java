package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO> implements UserPreferenceService {

    @Autowired
    private BookMapper bookMapper;

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
    }
}
