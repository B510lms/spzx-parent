package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.AssignRoleDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping(value = "/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "分页查询")
    @GetMapping(value = "/findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@RequestHeader(value = "token") String token,
                             @PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             SysUserDto sysUserDto){
        PageInfo<SysUser> pageInfo = sysUserService.findByPage(pageNum, pageSize, sysUserDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "添加")
    @PostMapping(value = "/saveSysUser")
    public Result saveSysUser(@RequestBody SysUser sysUser,@RequestHeader(value = "token") String token) {
        sysUserService.saveSysUser(sysUser) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "修改")
    @PutMapping(value = "/updateSysUser")
    public Result updateSysUser(@RequestBody SysUser sysUser,@RequestHeader(value = "token") String token) {
        sysUserService.updateSysUser(sysUser) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/deleteById/{userId}")
    public Result deleteById(@PathVariable(value = "userId") Long userId,@RequestHeader(value = "token") String token) {
        sysUserService.deleteById(userId) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignRoleDto assignRoleDto) {
        sysUserService.doAssign(assignRoleDto) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

}
