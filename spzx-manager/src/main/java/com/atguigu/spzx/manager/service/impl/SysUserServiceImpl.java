package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.mapper.SysRoleUserMapper;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.AssignRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper ;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private RedisTemplate<String , String> redisTemplate ;

    @Override
    public LoginVo login(LoginDto loginDto) {

        // 校验验证码是否正确
        String captcha = loginDto.getCaptcha();     // 用户输入的验证码
        String codeKey = loginDto.getCodeKey();     // redis中验证码的数据key

        // 从Redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get("user:login:validatecode:" + codeKey);
        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode , captcha)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR) ;
        }

        // 验证通过删除redis中的验证码
        redisTemplate.delete("user:login:validatecode:" + codeKey) ;

        // 根据用户名查询用户
        SysUser sysUser = sysUserMapper.selectByUserName(loginDto.getUserName());
        if(sysUser == null) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
            // throw new RuntimeException("用户名或者密码错误") ;
        }

        // 验证密码是否正确
        String inputPassword = loginDto.getPassword();
        String md5InputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if(!md5InputPassword.equals(sysUser.getPassword())) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
            // throw new RuntimeException("用户名或者密码错误") ;
        }

        // 生成令牌，保存数据到Redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("user:login:" + token , JSON.toJSONString(sysUser) , 30 , TimeUnit.MINUTES);

        // 构建响应结果对象
        LoginVo loginVo = new LoginVo() ;
        loginVo.setToken(token);
        loginVo.setRefresh_token("");

        // 返回
        return loginVo;
    }

    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login:" + token);
        return JSON.parseObject(userJson , SysUser.class) ;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login:" + token) ;
    }

    // 获取用户列表
    @Override
    public PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = sysUserMapper.findByPage(sysUserDto);
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void saveSysUser(SysUser sysUser) {
        // 1 根据输入的用户名查询用户
        SysUser dbSysUser = sysUserMapper.selectByUserName(sysUser.getUserName()) ;
        if(dbSysUser != null) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS) ;
        }

        // 2 对密码进行加密
        String digestPassword = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(digestPassword);

        // 3 用户状态初始化为1
        sysUser.setStatus(1);
        sysUserMapper.saveSysUser(sysUser);
    }

    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.updateSysUser(sysUser);
    }

    @Override
    public void deleteById(Long userId) {
        sysUserMapper.deleteById(userId) ;
    }

    @Override
    public void doAssign(AssignRoleDto assignRoleDto) {
        // 删除之前的所有的用户所对应的角色数据
        sysRoleUserMapper.deleteByUserId(assignRoleDto.getUserId()) ;

        // 分配新的角色数据
        List<Long> roleIdList = assignRoleDto.getRoleIdList();
        // 遍历得到每个角色id
        for (Long roleId:roleIdList) {
            sysRoleUserMapper.doAssign(assignRoleDto.getUserId(),roleId);
        }
    }

}