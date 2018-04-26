package com.lojasiewicz.mapDecorator.controller;

import com.lojasiewicz.mapDecorator.cloudstorage.PhotoStorageService;
import com.lojasiewicz.mapDecorator.service.MapDecoratorService;
import com.lojasiewicz.mapDecorator.service.db.MapFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AJAXRequestControllerTest {
    private AJAXRequestController ajaxRequestController;
    private MapDecoratorService mapDecoratorServiceMock;
    private PhotoStorageService photoStorageServiceMock;
//    private MapFeatureRepository mapFeatureRepositoryMock;

    @Before
    public void setup(){
        photoStorageServiceMock = Mockito.mock(PhotoStorageService.class);
        mapDecoratorServiceMock = Mockito.mock(MapDecoratorService.class);
        ajaxRequestController = new AJAXRequestController(mapDecoratorServiceMock,photoStorageServiceMock);
    }


//    @RequestMapping(value = {"/_ah/health", "/readiness_check", "/liveness_check"})
//    public ResponseEntity<String> healthCheck() {
//        return new ResponseEntity<>("Healthy", HttpStatus.OK);
//    }
    @Test
    public void testInsertNewFeature() throws IOException{
        when(photoStorageServiceMock.createAndSaveThumbnail(any(), any())).thenReturn("thumbnailName");
        when(photoStorageServiceMock.saveMedium(any(), any())).thenReturn("mediumName");
        doNothing().when(mapDecoratorServiceMock).insertMapFeature(any(), any());
        ResponseEntity<String> result = ajaxRequestController.insertFeature(MapFeature.Category.amphibians.toString(), "","","","1","2","");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void photoServiceFails() throws IOException{
        when(photoStorageServiceMock.createAndSaveThumbnail(any(), any())).thenThrow(new IOException(""));
        when(photoStorageServiceMock.saveMedium(any(), any())).thenReturn("mediumName");
        doNothing().when(mapDecoratorServiceMock).insertMapFeature(any(), any());

        ResponseEntity<String> result = ajaxRequestController.insertFeature(MapFeature.Category.amphibians.toString(),"", "", "", "1", "2", "");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(mapDecoratorServiceMock, times(0)).insertMapFeature(any(), any());
    }
}
