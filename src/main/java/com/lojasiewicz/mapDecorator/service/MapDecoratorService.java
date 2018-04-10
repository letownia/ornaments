package com.lojasiewicz.mapDecorator.service;

import com.lojasiewicz.mapDecorator.service.db.MapFeature;
import com.lojasiewicz.mapDecorator.service.db.MapFeaturePhoto;
import com.lojasiewicz.mapDecorator.service.db.MapFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapDecoratorService {

    @Autowired
    private MapFeatureRepository mapFeatureRepository;

    public List<MapFeature> getAllFeatures(){
        List<MapFeature> features = new ArrayList<>();
        mapFeatureRepository.findAll().forEach(features::add);
        return features;
    }

    public void insertMapFeature(String name, String description, String googlePlaceId, String latitude, String longitude, String thumbnailIdentifier, String mediumIdentifier) throws IOException {
        MapFeature newFeature = new MapFeature();
        newFeature.setName(name);
        newFeature.setDescription(description);
        newFeature.setGooglePlaceId(googlePlaceId);
        newFeature.setLatitude(new BigDecimal(latitude));
        newFeature.setLongitude(new BigDecimal(longitude));
        MapFeaturePhoto newPhoto = new MapFeaturePhoto();
        newPhoto.setMediumIdentifier(mediumIdentifier);
        newPhoto.setThumbnailIdentifier(thumbnailIdentifier);
        newPhoto.setMapFeature(newFeature);
        newFeature.getPhotoList().add(newPhoto);
        mapFeatureRepository.save(newFeature);
    }
}
