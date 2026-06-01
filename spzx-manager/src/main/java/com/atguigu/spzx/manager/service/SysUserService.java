package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.AssignRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageInfo;

public interface SysUserService {

    // 根据用户名查询用户数据
    public abstract LoginVo login(LoginDto loginDto) ;

    // 获取用户信息
    SysUser getUserInfo(String token);

    // 退出登录
    void logout(String token);

    // 获取用户列表
    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    // 添加用户
    void saveSysUser(SysUser sysUser);

    // 修改用户
    void updateSysUser(SysUser sysUser);

    // 删除用户
    void deleteById(Long userId);

    // 为用户分配角色
    void doAssign(AssignRoleDto assignRoleDto);
}

