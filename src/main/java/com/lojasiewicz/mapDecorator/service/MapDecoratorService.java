package com.lojasiewicz.mapDecorator.service;

import com.lojasiewicz.mapDecorator.service.db.MapFeature;
import com.lojasiewicz.mapDecorator.service.db.MapFeatureRepository;
import com.lojasiewicz.mapDecorator.service.db.UserObject;
import com.lojasiewicz.mapDecorator.service.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapDecoratorService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MapFeatureRepository mapFeatureRepository;

    public List<UserObject> getUserList(){
        List<UserObject> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void insertMapFeature(MapFeature feature){
        mapFeatureRepository.save(feature);
    }
}
