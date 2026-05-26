package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.system.LoginVo;

public interface SysUserService {

    // 根据用户名查询用户数据
    public abstract LoginVo login(LoginDto loginDto) ;

    // 获取用户信息
    SysUser getUserInfo(String token);

    // 退出登录
    void logout(String token);
}

