package org.rhw.bmr.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.user.dto.req.UserLoginReqDTO;
import org.rhw.bmr.user.dto.req.UserRegisterReqDTO;
import org.rhw.bmr.user.dto.req.UserUpdateReqDTO;
import org.rhw.bmr.user.dto.resp.UserLoginRespDTO;
import org.rhw.bmr.user.dto.resp.UserRespDTO;
import org.rhw.bmr.user.dao.entity.UserDO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username  用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 查询用户名是否可用
     * @param username  用户名
     * @return  (True : 可用) (False : 不可用)
     */
    Boolean availableUsername(String username);

    /**
     * 用户注册
     * @param requestParam 用户注册参数
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 用户登录
     * @param requestParam 用户登录参数
     * @return 登录返回Token
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检查用户是否登录
     * @param token 用户登录token
     * @return  登录状态
     */
    Boolean checkLogin(String username, String token);

    /**
     * 修改用户信息
     * @param requestParam  用户信息
     */
    void update(UserUpdateReqDTO requestParam);

    /**
     * 用户退出登录
     * @param username  用户名
     * @param token     登录Token
     */
    void logout(String username, String token);
}
