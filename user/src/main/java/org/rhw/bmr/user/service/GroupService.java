package org.rhw.bmr.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.user.dao.entity.GroupDO;
import org.rhw.bmr.user.dto.req.BmrGroupSortReqDTO;
import org.rhw.bmr.user.dto.req.BmrGroupUpdateDTO;
import org.rhw.bmr.user.dto.resp.BmrGroupRespDTO;

import java.util.List;


/**
 * 短链接分组接口
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 新增短链接分组
     * @param groupName 分组名
     */
    void saveGroup(String groupName);

    /**
     * 新增短链接分组
     * @param groupName 分组名
     */
    void saveGroup(String userName, String groupName);

    /**
     * 查找用户的短链接分组
     * @return 分组集合
     */
    List<BmrGroupRespDTO> listGroup();

    /**
     * 修改用户短链接分组
     */
    void updateGroup(BmrGroupUpdateDTO requestParam);

    void deleteGroup(String gid);

    void sortGroup(List<BmrGroupSortReqDTO> requestParam);
}
