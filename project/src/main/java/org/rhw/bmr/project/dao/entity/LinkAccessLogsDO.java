package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.baseDO;

/**
 * 访问日志监控
 */
@Data
@TableName("t_link_access_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkAccessLogsDO extends baseDO {

    /**
     * id
     */
    private Long id;

    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 用户信息
     */
    private String user;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * ip
     */
    private String ip;

    /**
     * 访问网络
     */
    private String network;

    /**
     * 访问设备
     */
    private String device;

}