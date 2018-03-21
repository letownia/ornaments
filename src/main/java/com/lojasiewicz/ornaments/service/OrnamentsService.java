package com.lojasiewicz.ornaments.service;

import com.lojasiewicz.ornaments.service.db.UserObject;
import com.lojasiewicz.ornaments.service.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrnamentsService {
    @Autowired
    private UserRepository userRepository;

    public List<UserObject> getUserList(){
        List<UserObject> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}
