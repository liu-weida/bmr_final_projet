package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.baseDO;

@Data
@TableName("t_user_preference")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDO extends baseDO {

    /**
     * Primary Key ID
     */
    private Long id;

    /**
     * user ID
     */
    private Long userId;

    /**
     * Favorite Authors
     */
    private String author;

    /**
     * Favorite Categories
     */
    private String category;

    /**
     * Number of user clicks/reads
     */
    private Integer likeCount;


}
