package com.lojasiewicz.mapDecorator.service;

import com.lojasiewicz.mapDecorator.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapDecoratorService {

    @Autowired
    private MapFeatureRepository mapFeatureRepository;

    public List<UserObject> getUserList(){
        List<UserObject> users = new ArrayList<>();
      //  userRepository.findAll().forEach(users::add);
        return users;
    }

    public void insertMapFeature(String description, String googlePlaceId, MultipartFile photo) throws IOException {
        MapFeature newFeature = new MapFeature();
        newFeature.setDescription(description);
        newFeature.setGooglePlaceId(googlePlaceId);
        MapFeaturePhoto newPhoto = new MapFeaturePhoto();
        newPhoto.setPhoto(photo.getBytes());
        newPhoto.setMapFeature(newFeature);
        newFeature.getPhotoList().add(newPhoto);
        mapFeatureRepository.save(newFeature);
    }
}
