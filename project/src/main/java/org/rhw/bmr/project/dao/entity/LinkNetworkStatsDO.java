package org.rhw.bmr.project.dao.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.baseDO;

import java.util.Date;


@Data
@TableName("t_link_network_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkNetworkStatsDO extends baseDO {

    /**
     * id
     */
    private Long id;


    /**
     * 完整短链接
     */
    private String fullShortUrl;

    private String gid;

    /**
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer cnt;

    /**
     * 访问网络
     */
    private String network;
}