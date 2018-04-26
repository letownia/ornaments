package com.lojasiewicz.mapDecorator.controller;

import com.google.gson.GsonBuilder;
import com.lojasiewicz.mapDecorator.cloudstorage.PhotoStorageService;
import com.lojasiewicz.mapDecorator.service.MapDecoratorService;
import com.lojasiewicz.mapDecorator.service.db.MapFeature;
import com.lojasiewicz.mapDecorator.service.db.MapFeaturePhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
public class AJAXRequestController {

    private final MapDecoratorService mapDecoratorService;
    private final PhotoStorageService photoStorageService;

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
     * @param photoStorageService
     */
    @Autowired
    public AJAXRequestController(MapDecoratorService mapDecoratorService, PhotoStorageService photoStorageService){
        this.mapDecoratorService = mapDecoratorService;
        this.photoStorageService = photoStorageService;
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
        String res = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(mapDecoratorService.getAllFeatures());
        return "var featuresJSONObject = " + res + ";";
        //new Gson().toJson(users.stream().map(x->x.getUserName()).collect(Collectors.toList()));
    }

    @RequestMapping(method= RequestMethod.PUT,  value="/feature", consumes = {"multipart/form-data"})
    public ResponseEntity<String> insertFeature(
                                                @RequestParam("name") String name,
                                                @RequestParam("description") String description,
                                                @RequestParam("googlePlaceId") String googlePlaceId,
                                                @RequestParam("latitude") String latitude,
                                                @RequestParam("longitude") String longitude,
                                                @RequestParam("imageDataURL") String imageDataURL
                                                ){

        String mediumPhotoName = "", thumbnailPhotoName = "";
        long startTime,endTime, startTotal;
        startTotal = System.nanoTime();
        try {
            startTime = System.nanoTime();
            byte[] photoBytes = PhotoStorageService.imagaDataURLtoBinary(imageDataURL);
            endTime = System.nanoTime();
            System.out.println("imagaDataURLtoBinary " + (endTime-startTime)/10000000.0);
            startTime = System.nanoTime();
            mediumPhotoName = photoStorageService.saveMedium("photos/medium/" + googlePlaceId, photoBytes);
            endTime = System.nanoTime();
            System.out.println("saveMedium " + (endTime - startTime) / 10000000.0);
            startTime = System.nanoTime();
            thumbnailPhotoName = photoStorageService.createAndSaveThumbnail("photos/thumbnail/" + googlePlaceId, photoBytes);
            endTime = System.nanoTime();
            System.out.println("createAndSaveThumbnail " + (endTime - startTime) / 10000000.0);
        } catch (IOException e) {
            e.printStackTrace();
            return responseError("Application photoStorageService error. Please try again or contact the system administrator");
        }
        startTime = System.nanoTime();
        try {
            MapFeature newFeature = new MapFeature();
            newFeature.setName(name);
            newFeature.setDescription(description);
            newFeature.setGooglePlaceId(googlePlaceId);
            newFeature.setLatitude(new BigDecimal(latitude));
            newFeature.setLongitude(new BigDecimal(longitude));
            MapFeaturePhoto newPhoto = new MapFeaturePhoto();
            newPhoto.setMediumIdentifier(mediumPhotoName);
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
        return responseOK();
    }

    @RequestMapping(value = {"/_ah/health", "/readiness_check", "/liveness_check"})
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Healthy", HttpStatus.OK);
    }

    private static ResponseEntity<String> responseOK() {
        return ResponseEntity.ok(null);
    }

    private static ResponseEntity<String> responseError(String message){
        ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        return errorResponse;
    }
}
