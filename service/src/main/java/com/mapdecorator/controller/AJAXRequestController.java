package com.mapdecorator.controller;

import com.google.gson.GsonBuilder;
import com.mapdecorator.images.ImageStorageService;
import com.mapdecorator.service.MapDecoratorService;
import com.mapdecorator.repository.db.MapFeature;
import com.mapdecorator.repository.db.MapFeaturePhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
public class AJAXRequestController {

    private final MapDecoratorService mapDecoratorService;
    private final ImageStorageService imageStorageService;

    /**
     * This comment should be moved to some sort of documentation:
     *
     * We have decided to use the standards suggested in the latest spring-documentation :
     * https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/beans.html#beans-constructor-injection
     *
     * Constructor injection is the standard choice for our beans, with setter injection being permitted only in the case
     * of optional or reassignable dependencies. Field injection is to be *avoided*!
     *
     * @param mapDecoratorService
     * @param imageStorageService
     */
    @Autowired
    public AJAXRequestController(MapDecoratorService mapDecoratorService, ImageStorageService imageStorageService){
        this.mapDecoratorService = mapDecoratorService;
        this.imageStorageService = imageStorageService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allCategories")
    /**
     * Get's all the features as a single JSON object.
     * The JSON will be a list of MapFeature with only the properties that are tagged with @Expose annotation.
     */
    public String getAllCategories() {
        String res = new GsonBuilder().create().toJson(MapFeature.Category.values());
        return "var allCategories = " + res + ";";
        //new Gson().toJson(users.stream().map(x->x.getUserName()).collect(Collectors.toList()));
    }

    @RequestMapping(method= RequestMethod.GET, value="/featuresJSONObject")
    /**
     * Get's all the features as a single JSON object.
     * The JSON will be a list of MapFeature with only the properties that are tagged with @Expose annotation.
     */
    public String getFeaturesJSONObject(){
        /*WARNING : featuresJSONObject corresponds to a javascript object in indexHTML */
        return getJSONObjectForFeatures(mapDecoratorService.getAllFeatures(), "featuresJSONObject");
    }

    @PostMapping("/feature")
    public ResponseEntity<String> insertFeature(
                                                @RequestParam String category,
                                                @RequestParam String name,
                                                @RequestParam String description,
                                                @RequestParam String googlePlaceId,
                                                @RequestParam String latitude,
                                                @RequestParam String longitude,
                                                @RequestParam String imageDataURL
                                                ){

        String mediumPhotoName = "";
        String thumbnailPhotoName = "";
        long startTime,endTime, startTotal;
        startTotal = System.nanoTime();
        try {
            startTime = System.nanoTime();
            byte[] photoBytes = ImageStorageService.imagaDataURLtoBinary(imageDataURL);
            endTime = System.nanoTime();
            System.out.println("imagaDataURLtoBinary " + (endTime-startTime)/10000000.0);
            startTime = System.nanoTime();
            mediumPhotoName = imageStorageService.saveMedium("photos/medium/" + googlePlaceId, photoBytes);
            endTime = System.nanoTime();
            System.out.println("saveMedium " + (endTime - startTime) / 10000000.0);
            startTime = System.nanoTime();
            thumbnailPhotoName = imageStorageService.createAndSaveThumbnail("photos/thumbnail/" + googlePlaceId, photoBytes);
            endTime = System.nanoTime();
            System.out.println("createAndSaveThumbnail " + (endTime - startTime) / 10000000.0);
        } catch (IOException e) {
            e.printStackTrace();
            return responseError("Application imageStorageService error. Please try again or contact the system administrator");
        }
        startTime = System.nanoTime();
        MapFeature newFeature = new MapFeature();
        try {
            newFeature.setName(name);
            newFeature.setDescription(description);
            newFeature.setCategory(MapFeature.Category.valueOf(category.toUpperCase()));
            newFeature.setGooglePlaceId(googlePlaceId);
            newFeature.setLatitude(new BigDecimal(latitude));
            newFeature.setLongitude(new BigDecimal(longitude));
            MapFeaturePhoto newPhoto = new MapFeaturePhoto();
            newPhoto.setMediumPhotoIdentifier(mediumPhotoName);
            newPhoto.setThumbnailIdentifier(thumbnailPhotoName);
            newPhoto.setMapFeature(newFeature);
            newFeature.getPhotoList().add(newPhoto);
            mapDecoratorService.insertMapFeature(newFeature, newPhoto);
        } catch (Exception e) {
            System.out.println("Error inserting feature : " + e);
            System.out.println("error " + e.getMessage());
            e.printStackTrace();
            return responseError("Application database error. Please try again or contact the system administrator");
        }
        endTime = System.nanoTime();
        System.out.println("insertMapFeature " + (endTime - startTime) / 10000000.0);
        System.out.println("Total time " + (endTime - startTotal) / 10000000.0);
        /* WARNING : String "newFeatures" corresponds to javascript object in index.hmtl */
        return responseOK(getJSONObjectForFeatures(Arrays.asList(newFeature), "newFeatures"));
    }

    @RequestMapping(value = {"/_ah/health", "/readiness_check", "/liveness_check"})
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Healthy", HttpStatus.OK);
    }

    private static String getJSONObjectForFeatures(List<MapFeature> mapFeatures, String objectName) {
        String res = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(mapFeatures);
        return "var " + objectName + " = " + res + ";";
    }
    private static ResponseEntity<String> responseOK(String responseBody) {
        return ResponseEntity.ok(responseBody);
    }

    private static ResponseEntity<String> responseError(String message){
        ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        return errorResponse;
    }
}
