package org.rhw.bmr.user.controller;


import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.dto.req.BmrGroupReqDTO;
import org.rhw.bmr.user.dto.req.BmrGroupSortReqDTO;
import org.rhw.bmr.user.dto.req.BmrGroupUpdateDTO;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.common.convention.result.Results;
import org.rhw.bmr.user.dto.resp.BmrGroupRespDTO;
import org.rhw.bmr.user.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增分组
     * @param requestParam  参数
     * @return  实体
     */
    @PostMapping("/api/bmr/user/v1/group")
    public Result<Void> save(@RequestBody BmrGroupReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return Results.success();
    }

    /**
     * 查询用户短链接分组集合
     * @return  集合
     */
    @GetMapping("/api/bmr/user/v1/group")
    public Result<List<BmrGroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }

    /**
     * 修改短链接名字
     */
    @PutMapping("/api/bmr/user/v1/group")
    public Result<Void> updateGroup(@RequestBody BmrGroupUpdateDTO requestParam) {
        groupService.updateGroup(requestParam);
        return Results.success();
    }

    @DeleteMapping("/api/bmr/user/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid) {
        groupService.deleteGroup(gid);
        return Results.success();
    }

    @PostMapping("/api/bmr/user/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<BmrGroupSortReqDTO> requestParam) {
        groupService.sortGroup(requestParam);
        return Results.success();
    }

}
