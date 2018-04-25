package com.lojasiewicz.mapDecorator.service;

import com.lojasiewicz.mapDecorator.service.db.MapFeature;
import com.lojasiewicz.mapDecorator.service.db.MapFeaturePhoto;
import com.lojasiewicz.mapDecorator.service.db.MapFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapDecoratorService {

    private final MapFeatureRepository mapFeatureRepository;

    @Autowired
    public MapDecoratorService(MapFeatureRepository mapFeatureRepository){
        this.mapFeatureRepository = mapFeatureRepository;
    }

    public List<MapFeature> getAllFeatures(){
        List<MapFeature> features = new ArrayList<>();
        mapFeatureRepository.findAll().forEach(features::add);
        return features;
    }

    /**
     * newPhoto will be inserted with newFeature, but we pass it anyway inorder to validate it.
     */
    @SuppressWarnings("unused")
    public void insertMapFeature(@Valid MapFeature newFeature, @Valid MapFeaturePhoto newPhoto) throws IOException {
        mapFeatureRepository.save(newFeature);
    }
}
