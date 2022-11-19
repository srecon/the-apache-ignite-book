package com.blu.imdg.dao;

import com.blu.imdg.dto.Employee;
import com.blu.imdg.mapper.UserMapper;
import org.springframework.cache.annotation.Cacheable;

public class UserServices {
    private UserMapper userMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Employee getEmployee (String eName){return userMapper.getEmployee(eName);}
}
