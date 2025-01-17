package org.rhw.bmr.user.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.user.common.database.baseDO;

/**
 * 短链接分组实体
 */
@Data
@TableName("t_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDO extends baseDO {
    private Long id;
    private String gid;
    private String name;
    private String username;

    private int sortOrder;
}
