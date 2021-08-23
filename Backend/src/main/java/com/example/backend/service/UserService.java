package com.example.backend.service;

import com.example.backend.dao.UserDao;
import com.example.backend.entity.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<UserData> getAllUsers() {
            List <UserData> userData = new ArrayList<>();
            userDao.findAll().forEach(userData1 -> userData.add(userData1));
            return userData;
    }

    public UserData addUser(UserData userData) {
        return userDao.save(userData);
    }

    public boolean getEmailorMobile(UserData userData) {
        if(!userDao.findByEmail(userData.getEmail()).isEmpty() || !userDao.findByMobile(userData.getMobile()).isEmpty()) {
            return true;
        }
        return false;
    }

    public UserData editUser(Integer id, UserData userData) {
        UserData edit = userDao.findById(id).get();
        edit = userData;
        return userDao.save(userData);
    }

    public UserData getUserByIdSetImg(Integer id, String path) {
        UserData userData =  userDao.findById(id).get();
        userData.setPhotoPath(path);
        userDao.save(userData);
        return userData;
    }

}
