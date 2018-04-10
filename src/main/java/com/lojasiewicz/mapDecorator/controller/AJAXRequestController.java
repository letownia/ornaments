package com.lojasiewicz.mapDecorator.controller;

import com.google.gson.GsonBuilder;
import com.lojasiewicz.mapDecorator.cloudstorage.PhotoStorageService;
import com.lojasiewicz.mapDecorator.service.MapDecoratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AJAXRequestController {

    @Autowired
    private MapDecoratorService mapDecoratorService;
    @Autowired
    private PhotoStorageService photoStorageService;

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
        }
        startTime = System.nanoTime();
        try {
            mapDecoratorService.insertMapFeature(description, googlePlaceId, mediumPhotoName, thumbnailPhotoName, latitude,longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
        endTime = System.nanoTime();
        System.out.println("insertMapFeature " + (endTime - startTime) / 10000000.0);
        System.out.println("Total time " + (endTime - startTotal) / 10000000.0);
        return responseOK();
    }

    private static ResponseEntity<String> responseOK() {
        return ResponseEntity.ok(null);
    }

}
