package org.rhw.bmr.user.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user")
//用户--数据库持久层实体
public class UserDO {
    /**
     * 用户ID
     */
    private long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱地址
     */
    private String mail;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 注销时间戳
     */
    private long deletionTime;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除表示，0：未删除 1：删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
}
