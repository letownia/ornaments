package com.mapdecorator.controller;

//import com.mapdecorator.cloudstorage.GooglePhotoStorageService;
//import com.mapdecorator.cloudstorage.PhotoStorageService;
//import com.mapdecorator.GooglePhotoStorageService;

import com.mapdecorator.images.ImageStorageService;
import com.mapdecorator.repository.db.MapFeature.Category;
import com.mapdecorator.service.MapDecoratorService;
//import com.mapdecorator.service.db.MapFeature.Category;
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
    private ImageStorageService imageStorageService;
//    private MapFeatureRepository mapFeatureRepositoryMock;

    @Before
    public void setup(){
        imageStorageService = Mockito.mock(ImageStorageService.class);
        mapDecoratorServiceMock = Mockito.mock(MapDecoratorService.class);
        ajaxRequestController = new AJAXRequestController(mapDecoratorServiceMock, imageStorageService);
    }


//    @RequestMapping(value = {"/_ah/health", "/readiness_check", "/liveness_check"})
//    public ResponseEntity<String> healthCheck() {
//        return new ResponseEntity<>("Healthy", HttpStatus.OK);
//    }
    @Test
    public void testInsertNewFeature() throws IOException{
        when(imageStorageService.createAndSaveThumbnail(any(), any())).thenReturn("thumbnailName");
        when(imageStorageService.saveMedium(any(), any())).thenReturn("mediumName");
        doNothing().when(mapDecoratorServiceMock).insertMapFeature(any(), any());
        ResponseEntity<String> result = ajaxRequestController.insertFeature(Category.AMPHIBIANS.toString(), "","","","1","2","");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void photoServiceFails() throws IOException{
        when(imageStorageService.createAndSaveThumbnail(any(), any())).thenThrow(new IOException(""));
        when(imageStorageService.saveMedium(any(), any())).thenReturn("mediumName");
        doNothing().when(mapDecoratorServiceMock).insertMapFeature(any(), any());

        ResponseEntity<String> result = ajaxRequestController.insertFeature(Category.MAMMALS.toString(),"", "", "", "1", "2", "");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(mapDecoratorServiceMock, times(0)).insertMapFeature(any(), any());
    }
}
