package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.baseDO;

import java.util.Date;

/**
 * 基础访问实体
 */
@Data
@TableName("t_link_access_stats")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkAccessStatsDO extends baseDO {
    private Long id;
    private String fullShortUrl;
    private String gid;
    private Date date;
    private Integer pv;
    private Integer uv;
    private Integer uip;
    private Integer hour;
    private Integer weekday;
}
