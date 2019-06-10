package com.mapdecorator.service;

import com.mapdecorator.repository.MapFeatureRepository;
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
