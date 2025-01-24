package org.rhw.bmr.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.BookmarkDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.BookmarkMapper;
import org.rhw.bmr.project.dto.req.BookmarkReqDTO;
import org.rhw.bmr.project.dto.req.BookmarkSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookmarkSearchRespDTO;
import org.rhw.bmr.project.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookmarkImpl extends ServiceImpl<BookmarkMapper, BookmarkDO> implements BookmarkService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public void bookmark(BookmarkReqDTO requestParam) {
        String gid = requestParam.getGid();
        long bookId = requestParam.getBookId();
        String username = requestParam.getUsername();

        BookmarkDO build = BookmarkDO.builder().gid(gid).bookId(bookId).username(username).build();

        baseMapper.insert(build);
    }

    @Override
    public void deleteBookmark(BookmarkReqDTO requestParam) {

        String gid = requestParam.getGid();
        long bookId = requestParam.getBookId();
        String username = requestParam.getUsername();

        LambdaUpdateWrapper<BookmarkDO> set = Wrappers.lambdaUpdate(BookmarkDO.class)
                .eq(BookmarkDO::getGid, gid)
                .eq(BookmarkDO::getBookId, bookId)
                .eq(BookmarkDO::getUsername, username)
                .set(BookmarkDO::getDelFlag, 1);

        baseMapper.update(null, set);
    }

    @Override
    public IPage<BookmarkSearchRespDTO> bookmarkSearch(BookmarkSearchReqDTO requestParam) {

        int currentPage = requestParam.getPageNo() == null ? 1 : requestParam.getPageNo();
        int pageSize = requestParam.getPageSize() == null ? 10 : requestParam.getPageSize();

        // 无论用户传什么分页参数，都只取前10本
        Page<BookmarkDO> page = new Page<>(currentPage, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<BookmarkDO> queryWrapper = Wrappers.lambdaQuery(BookmarkDO.class)
                .eq(StringUtils.isNotBlank(requestParam.getUsername()), BookmarkDO::getUsername, requestParam.getUsername())
                .eq(requestParam.getGid() != null, BookmarkDO::getGid, requestParam.getGid())
                .eq(BookmarkDO::getDelFlag, 0);

        // 分页查询 BookmarkDO
        IPage<BookmarkDO> bookmarkDOIPage = baseMapper.selectPage(page, queryWrapper);


        if (CollUtil.isEmpty(bookmarkDOIPage.getRecords())) {
            return new Page<>();
        }

        // 提取所有 bookId
        List<Long> bookIds = bookmarkDOIPage.getRecords().stream()
                .map(BookmarkDO::getBookId)
                .collect(Collectors.toList());

        // 批量查询 BookDO
        List<BookDO> books = bookMapper.selectList(Wrappers.lambdaQuery(BookDO.class).in(BookDO::getId, bookIds));


        // 转换成 Map，便于快速查找
        Map<Long, BookDO> bookMap = books.stream()
                .collect(Collectors.toMap(BookDO::getId, Function.identity()));

        // 转换 BookmarkDO -> BookmarkSearchRespDTO
        IPage<BookmarkSearchRespDTO> respDTOPage = bookmarkDOIPage.convert(bookmarkDO -> {
            BookDO bookDO = bookMap.get(bookmarkDO.getBookId());
            if (bookDO == null) {
                return new BookmarkSearchRespDTO(); // 处理空值
            }
            return BeanUtil.toBean(bookDO, BookmarkSearchRespDTO.class);
        });

        return respDTOPage;
    }




}