package org.rhw.bmr.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.common.convention.exception.ClientException;
import org.rhw.bmr.project.common.convention.exception.ServiceException;
import org.rhw.bmr.project.common.enums.ValidDateType;
import org.rhw.bmr.project.dao.entity.*;
import org.rhw.bmr.project.dao.mapper.*;
import org.rhw.bmr.project.dto.biz.ShortLinkStatsRecordDTO;
import org.rhw.bmr.project.dto.req.ShortLinkCreateReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkPageReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkUpdateReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkCountQueryRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkCreateRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkPageRespDTO;
import org.rhw.bmr.project.mq.producer.ShortLinkStatsSaveProducer;
import org.rhw.bmr.project.service.LinkStatsTodayService;
import org.rhw.bmr.project.service.ShortLinkService;
import org.rhw.bmr.project.toolkit.HashUtil;
import org.rhw.bmr.project.toolkit.LinkUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.rhw.bmr.project.common.constant.RedisKeyConstant.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateBloomFilter;
    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final StringRedisTemplate stringRedisTemplate;  //Redis缓存
    private final RedissonClient redissonClient;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetWorkStatsMapper linkNetWorkStatsMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final LinkStatsTodayService linkStatsTodayService;
    private final ShortLinkStatsSaveProducer shortLinkStatsSaveProducer;

    @Value("${short-link.domain.default}")

    private String createShortLinkDefaultDomain;
    /**
     * 新增短链接
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {

        String shortLinkSuffix = generateSuffix(requestParam);
        //String fullShortLinkUrl = requestParam.getDomain()+"/"+shortLinkSuffix;

        String fullShortLinkUrl = StrBuilder.create(createShortLinkDefaultDomain)
                .append("/")
                .append(shortLinkSuffix)
                .toString();

        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(createShortLinkDefaultDomain)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createType(requestParam.getCreateType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortLinkUrl)
                .favicon(getFavicon(requestParam.getOriginUrl()))
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .delTime(0L)
                .build();

        ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                .gid(requestParam.getGid())
                .fullShortUrl(fullShortLinkUrl)
                .build();

        try {
            baseMapper.insert(shortLinkDO);
            shortLinkGotoMapper.insert(shortLinkGotoDO);
        }catch(DuplicateKeyException e){
            log.warn("短链接{}重复入库", fullShortLinkUrl);
            throw new ServiceException(String.format("短链接：%s 生成重复", fullShortLinkUrl));
        }

        stringRedisTemplate.opsForValue().set(
                String.format(GOTO_SHORT_LINK_KEY, fullShortLinkUrl),
                requestParam.getOriginUrl(),
                LinkUtil.getLinkCacheValidTime(requestParam.getValidDate()),
                TimeUnit.MILLISECONDS
        );

        shortUriCreateBloomFilter.add(fullShortLinkUrl);

        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl("http://" + shortLinkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

    /**
     * 短链接分页查询
     */
    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each ->{
            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });

    }

    /**
     * 修改短链接内容
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {

        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getOriginGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl());

        ShortLinkDO hasShortLink = baseMapper.selectOne(queryWrapper);
        if (hasShortLink == null){
            throw new ClientException("找不到对应的短链接");
        }

        // 如果gid没有进行修改，那么数据库内的分组就不用改，直接修改对应数据即可。
        if (Objects.equals(hasShortLink.getGid(), requestParam.getGid())){
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, requestParam.getGid())
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .set(Objects.equals(
                            requestParam.getValidDateType(), ValidDateType.PERMANENT.getType()),
                            ShortLinkDO::getValidDate,
                            requestParam.getValidDate(),
                            null
                    );
            ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                    .domain(hasShortLink.getDomain())
                    .shortUri(hasShortLink.getShortUri())
                    .favicon(hasShortLink.getFavicon())
                    .createType(hasShortLink.getCreateType())
                    .gid(requestParam.getGid())
                    .originUrl(requestParam.getOriginUrl())
                    .describe(requestParam.getDescribe())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate())
                    .build();
            baseMapper.update(shortLinkDO, updateWrapper);
        }else{
            // 如果改了gid，就很麻烦了
            // 先加入读写锁，避免短链接在被访问时修改分组，导致数据不一致。
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, requestParam.getFullShortUrl()));
            // 拿写锁，开始阻塞其他的读写
            RLock rLock = readWriteLock.writeLock();
            if (!rLock.tryLock()) {
                throw new ServiceException("短链接正在被访问，请稍后再试...");
            }
            try {
                // 逻辑删除原短链接
                LambdaUpdateWrapper<ShortLinkDO> linkUpdateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, hasShortLink.getGid())
                        .eq(ShortLinkDO::getDelFlag, 0)
                        .eq(ShortLinkDO::getDelTime, 0L)
                        .eq(ShortLinkDO::getEnableStatus, 0);
                ShortLinkDO delShortLinkDO = ShortLinkDO.builder()
                        .delTime(System.currentTimeMillis())
                        .build();
                delShortLinkDO.setDelFlag(1);
                baseMapper.update(delShortLinkDO, linkUpdateWrapper);
                // 插入新的短链接
                ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                        .domain(createShortLinkDefaultDomain)
                        .originUrl(requestParam.getOriginUrl())
                        .gid(requestParam.getGid())
                        .createType(hasShortLink.getCreateType())
                        .validDateType(requestParam.getValidDateType())
                        .validDate(requestParam.getValidDate())
                        .describe(requestParam.getDescribe())
                        .shortUri(hasShortLink.getShortUri())
                        .enableStatus(hasShortLink.getEnableStatus())
                        .totalPv(hasShortLink.getTotalPv())
                        .totalUv(hasShortLink.getTotalUv())
                        .totalUip(hasShortLink.getTotalUip())
                        .fullShortUrl(hasShortLink.getFullShortUrl())
                        .favicon(getFavicon(requestParam.getOriginUrl()))
                        .delTime(0L)
                        .build();
                baseMapper.insert(shortLinkDO);
                // 更新数据统计方面
                LambdaQueryWrapper<LinkStatsTodayDO> statsTodayQueryWrapper = Wrappers.lambdaQuery(LinkStatsTodayDO.class)
                        .eq(LinkStatsTodayDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkStatsTodayDO::getGid, hasShortLink.getGid())
                        .eq(LinkStatsTodayDO::getDelFlag, 0);
                List<LinkStatsTodayDO> linkStatsTodayDOList = linkStatsTodayMapper.selectList(statsTodayQueryWrapper);
                if (CollUtil.isNotEmpty(linkStatsTodayDOList)) {
                    linkStatsTodayMapper.deleteBatchIds(linkStatsTodayDOList.stream()
                            .map(LinkStatsTodayDO::getId)
                            .toList()
                    );
                    linkStatsTodayDOList.forEach(each -> each.setGid(requestParam.getGid()));
                    linkStatsTodayService.saveBatch(linkStatsTodayDOList);
                }
                LambdaQueryWrapper<ShortLinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                        .eq(ShortLinkGotoDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkGotoDO::getGid, hasShortLink.getGid());
                ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(linkGotoQueryWrapper);
                shortLinkGotoMapper.deleteById(shortLinkGotoDO.getId());
                shortLinkGotoDO.setGid(requestParam.getGid());
                shortLinkGotoMapper.insert(shortLinkGotoDO);
                LambdaUpdateWrapper<LinkAccessStatsDO> linkAccessStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkAccessStatsDO.class)
                        .eq(LinkAccessStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkAccessStatsDO::getGid, hasShortLink.getGid())
                        .eq(LinkAccessStatsDO::getDelFlag, 0);
                LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkAccessStatsMapper.update(linkAccessStatsDO, linkAccessStatsUpdateWrapper);

                LambdaUpdateWrapper<LinkOsStatsDO> linkOsStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkOsStatsDO.class)
                        .eq(LinkOsStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkOsStatsDO::getGid, hasShortLink.getGid())
                        .eq(LinkOsStatsDO::getDelFlag, 0);
                LinkOsStatsDO linkOsStatsDO = LinkOsStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkOsStatsMapper.update(linkOsStatsDO, linkOsStatsUpdateWrapper);
                LambdaUpdateWrapper<LinkBrowserStatsDO> linkBrowserStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkBrowserStatsDO.class)
                        .eq(LinkBrowserStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkBrowserStatsDO::getGid, hasShortLink.getGid())
                        .eq(LinkBrowserStatsDO::getDelFlag, 0);
                LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkBrowserStatsMapper.update(linkBrowserStatsDO, linkBrowserStatsUpdateWrapper);
                LambdaUpdateWrapper<LinkDeviceStatsDO> linkDeviceStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkDeviceStatsDO.class)
                        .eq(LinkDeviceStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkDeviceStatsDO::getGid, hasShortLink.getGid())
                        .eq(LinkDeviceStatsDO::getDelFlag, 0);
                LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkDeviceStatsMapper.update(linkDeviceStatsDO, linkDeviceStatsUpdateWrapper);
                LambdaUpdateWrapper<LinkNetworkStatsDO> linkNetworkStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkNetworkStatsDO.class)
                        .eq(LinkNetworkStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkNetworkStatsDO::getGid, hasShortLink.getGid())
                        .eq(LinkNetworkStatsDO::getDelFlag, 0);
                LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkNetWorkStatsMapper.update(linkNetworkStatsDO, linkNetworkStatsUpdateWrapper);
                LambdaUpdateWrapper<LinkAccessLogsDO> linkAccessLogsUpdateWrapper = Wrappers.lambdaUpdate(LinkAccessLogsDO.class)
                        .eq(LinkAccessLogsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkAccessLogsDO::getGid, hasShortLink.getGid())
                        .eq(LinkAccessLogsDO::getDelFlag, 0);
                LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkAccessLogsMapper.update(linkAccessLogsDO, linkAccessLogsUpdateWrapper);
            } finally {
                rLock.unlock();
            }

        }

        // 修复，如果是无限期的链接转有效期如何处理缓存信息，以及恢复短链接状态
        // 首先，如果修改了短链接的有效期或者永久性
        if (!Objects.equals(hasShortLink.getValidDateType(), requestParam.getValidDateType())
                || !Objects.equals(hasShortLink.getValidDate(), requestParam.getValidDate())){

            // 首先删除原可跳转的缓存信息
            stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));

            // 如果短链接已经过期了
            if (hasShortLink.getValidDate() != null && hasShortLink.getValidDate().before(new Date())){
                // 但是，这个是个永久有效的短链接,或者更新后仍然能够有效
                if (Objects.equals(requestParam.getValidDateType(), ValidDateType.PERMANENT.getType())
                || requestParam.getValidDate().after(new Date())){
                    stringRedisTemplate.delete(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
                }
            }
        }

        // TODO: 如果修改的是原始链接，缓存内如何更新？

    }

    /**
     * 分组内短链接数量获取
     * @param requestParam  gid数组
     * @return 对应gid的数量统计
     */
    @Override
    public List<ShortLinkCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam) {
        QueryWrapper<ShortLinkDO> queryWrapper = Wrappers.query(new ShortLinkDO())
                .select("gid as gid, count(*) as shortLinkCount")
                .in("gid", requestParam)
                .eq("enable_status", 0)
                .eq("del_flag", 0)
                .eq("del_time", 0L)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkDOList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(shortLinkDOList, ShortLinkCountQueryRespDTO.class);

    }

    @SneakyThrows
    @Override
    public void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response) {
        String serverName = request.getServerName();

        String serverPort = Optional.of(request.getServerPort())
                .filter(each -> !Objects.equals(each, 80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse("");

        String fullShortUrl = serverName + serverPort + "/" + shortUri;
        String originalLink = stringRedisTemplate.opsForValue().get(
                String.format(GOTO_SHORT_LINK_KEY, fullShortUrl)
        );
        // 如果缓存内找到相关信息，说明缓存有效，不碰数据库
        if(StrUtil.isNotBlank(originalLink)){
            ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
            shortLinkStats(fullShortUrl, null, statsRecord);
            response.sendRedirect(originalLink);
            return;
        }

        boolean contains = shortUriCreateBloomFilter.contains(fullShortUrl);
        // 布隆过滤器说不存在那就一定不存在，直接告辞
        if (!contains){
            response.sendRedirect("/page/notfound");
            return;
        }

        String gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(
                String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl)
        );

        // 这里是如果已经缓存了空值，那么说明是发生误判的短链
        if (StrUtil.isNotBlank(gotoIsNullShortLink)){
            response.sendRedirect("/page/notfound");
            return;
        }


        // 否则说明缓存已经失效，需要验证数据库内是否存在此数据
        // 创建分布式锁
        RLock lock = redissonClient.getLock(String.format(LOCK_GOTO_LONG_LINK_KEY, fullShortUrl));
        // 大师兄锁住何金银
        lock.lock();

        try{
            /**
             * 由于高并发问题，大量请求可能同时都进到了这里等待锁的解放。
             * 然而，当第一个请求成功将数据加入到缓存中之后，剩余的请求就没有必要再走数据库查询了，直接从缓存获取
             * 所以，我们实行双重判定，即使拿到了锁，也要再判断一次是否有前辈铺好了路
             */

            // 那么，从<K,V>里直接用K获取对应的V，也就是原始链接
            // 这里就是双重判定实施的地方，再次确认一下有没有更新过值
            originalLink = stringRedisTemplate.opsForValue().get(
                    String.format(GOTO_SHORT_LINK_KEY, fullShortUrl)
            );

            // 如果缓存里已经有了，那么直接跳转，不碰数据库
            if (StrUtil.isNotBlank(originalLink)){
                ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
                shortLinkStats(fullShortUrl, null, statsRecord);
                response.sendRedirect(originalLink);
                return;
            }

            gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(
                    String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl)
            );
            if (StrUtil.isNotBlank(gotoIsNullShortLink)) {
                //response.sendRedirect("/page/notfound");
                //return;
                throw new ServiceException("短链接不存在");
            }

            LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                    .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
            ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(queryWrapper);
            // 这里判定为空说明误判的请求打到了数据库，需要给缓存里加入非法值，用IS_NULL作为Key值，这样后续的请求才能发现这个链接不存在，从而直接返回！
            if (shortLinkGotoDO == null){
                stringRedisTemplate.opsForValue().set(
                        String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl),
                        "-",
                        30L,
                        TimeUnit.MINUTES
                );
                response.sendRedirect("page/notfound");
                return;
            }


            if (shortUriCreateBloomFilter.contains(fullShortUrl)){
                LambdaQueryWrapper<ShortLinkDO> queryWrapper1 = Wrappers.lambdaQuery(ShortLinkDO.class)
                        .eq(ShortLinkDO::getGid, shortLinkGotoDO.getGid())
                        .eq(ShortLinkDO::getFullShortUrl, shortLinkGotoDO.getFullShortUrl())
                        .eq(ShortLinkDO::getDelFlag, 0)
                        .eq(ShortLinkDO::getEnableStatus, 0);
                ShortLinkDO shortLinkDO = baseMapper.selectOne(queryWrapper1);

                // 布隆过滤器里有但是缓存没有，误判直接走，或者是曾经有但是被删了，所以是两个情况
                if (shortLinkDO == null || (shortLinkDO.getValidDate() != null && shortLinkDO.getValidDate().before(new Date()))){
                    // 如果是个临时有效的链接，并且短链接已经过期了
                    // 那么给他缓存个空值来防止缓存穿透
                    stringRedisTemplate.opsForValue().set(
                            String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl),
                            "-",
                            30L,
                            TimeUnit.MINUTES);
                    response.sendRedirect("page/notfound");
                    return;
                }

                // 如果找到了，确实存在数据库中，那么就加入到缓存里。
                // 这里存的仍然是<K,V>键值对，V的值就是原始链接的值
                // 跳转的时候还设置有效期？应该的，因为这个情况下是，已经确认缓存不存在，但是数据库存在了。
                stringRedisTemplate.opsForValue().set(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                        shortLinkDO.getOriginUrl(),
                        LinkUtil.getLinkCacheValidTime(shortLinkDO.getValidDate()), TimeUnit.MILLISECONDS
                );

                ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
                shortLinkStats(fullShortUrl, shortLinkDO.getGid(), statsRecord);

                response.sendRedirect(shortLinkDO.getOriginUrl());

            }
        }finally{
            lock.unlock();
        }
    }

    @Override
    public void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO statsRecord) {
        /*fullShortUrl = Optional.ofNullable(fullShortUrl).orElse(statsRecord.getFullShortUrl());
        // 使用分布式读写锁，确保对同一个短链接的统计操作是线程安全的。
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, fullShortUrl));
        // 因为是统计，所以拿读锁
        RLock rLock = readWriteLock.readLock();
        // 如果无法获取锁（其他线程正在操作），将任务重新放入延迟队列，等待下一次处理。
        // 避免多个线程同时更新相同短链接的统计数据，导致并发问题。
        if (!rLock.tryLock()) {
            delayShortLinkStatsProducer.send(statsRecord);
            return;
        }
        try {
            if (StrUtil.isBlank(gid)) {
                // 如果没有提供gid，那么就去Goto表里找对应的gid
                LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                        .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
                ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(queryWrapper);
                gid = shortLinkGotoDO.getGid();
            }
            int hour = DateUtil.hour(new Date(), true);
            Week week = DateUtil.dayOfWeekEnum(new Date());
            int weekValue = week.getIso8601Value();
            LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                    .pv(1)
                    .uv(statsRecord.getUvFirstFlag() ? 1 : 0)
                    .uip(statsRecord.getUipFirstFlag() ? 1 : 0)
                    .hour(hour)
                    .weekday(weekValue)
                    .fullShortUrl(fullShortUrl)
                    .gid(gid)
                    .date(new Date())
                    .build();
            linkAccessStatsMapper.shortLinkStats(linkAccessStatsDO);

            LinkOsStatsDO linkOsStatsDO = LinkOsStatsDO.builder()
                    .os(statsRecord.getOs())
                    .cnt(1)
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkOsStatsMapper.shortLinkOsState(linkOsStatsDO);
            LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder()
                    .browser(statsRecord.getBrowser())
                    .cnt(1)
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkBrowserStatsMapper.shortLinkBrowserState(linkBrowserStatsDO);
            LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder()
                    .device(statsRecord.getDevice())
                    .cnt(1)
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkDeviceStatsMapper.shortLinkDeviceState(linkDeviceStatsDO);
            LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder()
                    .network(statsRecord.getNetwork())
                    .cnt(1)
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkNetWorkStatsMapper.shortLinkNetworkState(linkNetworkStatsDO);
            LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder()
                    .user(statsRecord.getUv())
                    .ip(statsRecord.getRemoteAddr())
                    .browser(statsRecord.getBrowser())
                    .os(statsRecord.getOs())
                    .network(statsRecord.getNetwork())
                    .device(statsRecord.getDevice())
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .build();
            linkAccessLogsMapper.insert(linkAccessLogsDO);
            baseMapper.incrementStats(gid, fullShortUrl, 1, statsRecord.getUvFirstFlag() ? 1 : 0, statsRecord.getUipFirstFlag() ? 1 : 0);
            LinkStatsTodayDO linkStatsTodayDO = LinkStatsTodayDO.builder()
                    .todayPv(1)
                    .todayUv(statsRecord.getUvFirstFlag() ? 1 : 0)
                    .todayUip(statsRecord.getUipFirstFlag() ? 1 : 0)
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkStatsTodayMapper.shortLinkTodayState(linkStatsTodayDO);
        } catch (Throwable ex) {
            log.error("短链接访问量统计异常", ex);
        } finally {
            rLock.unlock();
        }*/

        Map<String, String> producerMap = new HashMap<>();
        producerMap.put("fullShortUrl", fullShortUrl);
        producerMap.put("gid", gid);
        producerMap.put("statsRecord", JSON.toJSONString(statsRecord));
        shortLinkStatsSaveProducer.send(producerMap);
    }

    /**
     * 生成六位短链接后缀
     */
    private String generateSuffix(ShortLinkCreateReqDTO requestParam){
        int customGenerateCount = 0;
        String shortUri;
        while (true){
            if (customGenerateCount > 10){
                throw new ServiceException("短链接生成太频繁，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();

            originUrl += UUID.randomUUID().toString();

            shortUri = HashUtil.hashToBase62(originUrl);

            if ( !shortUriCreateBloomFilter.contains(createShortLinkDefaultDomain + "/" + shortUri)){
                break;
            }
            customGenerateCount++;
        }
        return shortUri;
    }

    /**
     * 获取网页的图标
     */
    @SneakyThrows
    private String getFavicon(String url){
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP){
            String redirectUrl = connection.getHeaderField("Location");

            if (redirectUrl != null){
                URL newUrl = new URL(redirectUrl);
                connection = (HttpURLConnection) newUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                responseCode = connection.getResponseCode();
            }
        }

        if (responseCode == HttpURLConnection.HTTP_OK){
            Document document = Jsoup.connect(url).get();
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null){
                return faviconLink.attr("abs:href");
            }
        }

        return null;
    }

    /**
     * 构建短链接统计记录，判断用户是否是首次访问某个短链接
     * @param fullShortUrl  短链接
     * @param request   跳转访问请求
     * @param response  跳转返回实体
     * @return 统计记录
     */
    private ShortLinkStatsRecordDTO buildLinkStatsRecordAndSetUser(String fullShortUrl, HttpServletRequest request, HttpServletResponse response) {

        // 判断是否是新用户的Flag
        AtomicBoolean uvFirstFlag = new AtomicBoolean();
        Cookie[] cookies = request.getCookies();
        AtomicReference<String> uv = new AtomicReference<>();

        // 如果短链接没有访问过
        Runnable addResponseCookieTask = () -> {
            // 生成唯一标识符
            uv.set(UUID.fastUUID().toString());
            Cookie uvCookie = new Cookie("uv", uv.get());
            // 一般短链接的有效期很少有超过30天的
            uvCookie.setMaxAge(60 * 60 * 24 * 30);
            uvCookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
            response.addCookie(uvCookie);
            uvFirstFlag.set(Boolean.TRUE);
            stringRedisTemplate.opsForSet()
                    .add("short-link:stats:uv:" + fullShortUrl, uv.get());
            // 设置过期时间，当小时更新的时候失效，因为我们需要按照小时来统计
            int expireMinutes = LinkUtil.minutesUntilNextHour();
            stringRedisTemplate.expire("short-link:stats:uv"+fullShortUrl, expireMinutes, TimeUnit.MINUTES);
        };

        // 如果之前有访问过
        if (ArrayUtil.isNotEmpty(cookies)) {
            // 在cookies里寻找uv对应的数据，如果能找到，那么说明对应的短链接之前已经访问过了
            // 在Redis里进行数据更新
            Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "uv"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresentOrElse(each -> {
                        uv.set(each);
                        Long uvAdded = stringRedisTemplate
                                .opsForSet().add("short-link:stats:uv:" + fullShortUrl, each);
                        int expireMinutes = LinkUtil.minutesUntilNextHour();
                        // 按照小时过期缓存
                        stringRedisTemplate.expire("short-link:stats:uv"+fullShortUrl, expireMinutes, TimeUnit.MINUTES);
                        uvFirstFlag.set(uvAdded != null && uvAdded > 0L);
                    }, addResponseCookieTask);
        } else {
            // 如果没有过访问记录怎么办？当然是直接加咯。
            addResponseCookieTask.run();
        }

        String remoteAddr = LinkUtil.getActualIp(request);
        String os = LinkUtil.getOs(request);
        String browser = LinkUtil.getBrowser(request);
        String device = LinkUtil.getDevice(request);
        String network = LinkUtil.getNetwork(request);
        Long uipAdded = stringRedisTemplate.opsForSet().add("short-link:stats:uip:" + fullShortUrl, remoteAddr);
        // 按照小时过期ip信息
        int expireMinutes = LinkUtil.minutesUntilNextHour();
        stringRedisTemplate.expire("short-link:stats:uip" + fullShortUrl, expireMinutes, TimeUnit.MINUTES);

        // 如果redis返回的值是1，那么说明这个IP是首次访问
        boolean uipFirstFlag = uipAdded != null && uipAdded > 0L;
        return ShortLinkStatsRecordDTO.builder()
                .fullShortUrl(fullShortUrl)
                .uv(uv.get())
                .uvFirstFlag(uvFirstFlag.get())
                .uipFirstFlag(uipFirstFlag)
                .remoteAddr(remoteAddr)
                .os(os)
                .browser(browser)
                .device(device)
                .network(network)
                .build();
    }
}
