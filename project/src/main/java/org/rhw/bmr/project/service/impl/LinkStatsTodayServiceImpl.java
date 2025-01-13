package org.rhw.bmr.project.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.dao.entity.LinkStatsTodayDO;
import org.rhw.bmr.project.dao.mapper.LinkStatsTodayMapper;
import org.rhw.bmr.project.service.LinkStatsTodayService;
import org.springframework.stereotype.Service;

/**
 * 短链接今日统计接口实现层
 */
@Service
public class LinkStatsTodayServiceImpl extends ServiceImpl<LinkStatsTodayMapper, LinkStatsTodayDO> implements LinkStatsTodayService {
}