package com.atguigu.service.user.mapper;

import com.atguigu.spzx.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

    UserInfo getByUsername(String username);

    void save(UserInfo userInfo);

}
