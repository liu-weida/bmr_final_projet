package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.baseDO;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link_os_stats")
public class LinkOsStatsDO extends baseDO {
    private Long id;
    private String fullShortUrl;
    private String gid;
    private Date date;
    private Integer cnt;
    private String os;
}
