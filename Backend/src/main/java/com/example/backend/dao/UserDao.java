package com.example.backend.dao;


import com.example.backend.entity.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<UserData, Integer> {

    List<UserData> findByName(String name);

    List<UserData> findByEmail(String email);

    List<UserData> findByMobile(String mobile);
}

