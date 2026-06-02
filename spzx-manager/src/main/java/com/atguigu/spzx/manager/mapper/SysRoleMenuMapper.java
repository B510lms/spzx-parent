package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.system.AssignMenuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {

    List<Long> findSysRoleMenuByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void doAssign(AssignMenuDto assignMenuDto);

    void updateSysRoleMenuIsHalf(Long id);

}
