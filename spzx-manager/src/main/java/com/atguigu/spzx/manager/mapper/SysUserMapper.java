package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {

    // 根据用户名查询用户数据
    SysUser selectByUserName(String userName) ;

    // 获取用户列表
    List<SysUser> findByPage(SysUserDto sysUserDto);

    // 添加用户
    void saveSysUser(SysUser sysUser);

    // 修改用户
    void updateSysUser(SysUser sysUser);

    // 删除用户
    void deleteById(Long userId);
}