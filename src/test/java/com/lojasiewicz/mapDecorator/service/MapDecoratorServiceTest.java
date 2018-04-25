package com.lojasiewicz.mapDecorator.service;

import com.lojasiewicz.mapDecorator.service.db.MapFeatureRepository;
import org.junit.Before;

import static org.mockito.Mockito.*;


public class MapDecoratorServiceTest {

    private MapDecoratorService mapDecoratorService;
    private MapFeatureRepository mockRepository;
    @Before
    public void setUp(){
        mockRepository = mock(MapFeatureRepository.class);
        mapDecoratorService = new MapDecoratorService(mockRepository);
    }
}
