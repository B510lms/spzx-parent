package com.atguigu.service.user.service;

import com.atguigu.spzx.model.entity.user.UserAddress;

import java.util.List;

public interface UserAddressService {

    List<UserAddress> findUserAddressList();

    UserAddress getById(Long id);

}
