package com.mapdecorator.service;

import com.mapdecorator.repository.db.MapFeature;
import com.mapdecorator.repository.db.MapFeaturePhoto;
import com.mapdecorator.repository.MapFeatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapDecoratorService {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
     * newPhoto will be inserted with newFeature, but we pass it anyway in order to validate it.
     */
    @SuppressWarnings("unused")
    public void insertMapFeature(@Valid MapFeature newFeature, @Valid MapFeaturePhoto newPhoto){
        mapFeatureRepository.save(newFeature);
    }
}
