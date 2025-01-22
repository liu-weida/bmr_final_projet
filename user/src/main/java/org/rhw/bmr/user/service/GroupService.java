package org.rhw.bmr.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.user.dao.entity.GroupDO;
import org.rhw.bmr.user.dto.req.BmrDeleteGroupReqDTO;
import org.rhw.bmr.user.dto.req.BmrGroupUpdateDTO;
import org.rhw.bmr.user.dto.req.BmrListGroupReqDTO;
import org.rhw.bmr.user.dto.req.BmrSaveGroupReqDTO;
import org.rhw.bmr.user.dto.resp.BmrGroupRespDTO;

import java.util.List;


/**
 * 短链接分组接口
 */
public interface GroupService extends IService<GroupDO> {


    void saveGroup(BmrSaveGroupReqDTO requestParam);

    /**
     * 查找用户的短链接分组
     * @return 分组集合
     */
    List<BmrGroupRespDTO> listGroup(BmrListGroupReqDTO requestParam);

    /**
     * 修改用户短链接分组
     */
    void updateGroup(BmrGroupUpdateDTO requestParam);

    void deleteGroup(BmrDeleteGroupReqDTO requestParam);

}
