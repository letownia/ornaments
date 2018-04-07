package com.lojasiewicz.mapDecorator.controller;

import com.google.gson.GsonBuilder;
import com.lojasiewicz.mapDecorator.cloudstorage.CloudStorageService;
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
    private CloudStorageService cloudStorageService;

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
        String blobName = "";
        try {
            blobName = cloudStorageService.addBlob("/photos/largeSize/" + googlePlaceId, imageDataURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mapDecoratorService.insertMapFeature(description, googlePlaceId, blobName,latitude,longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseOK();
    }

    private static ResponseEntity<String> responseOK() {
        return ResponseEntity.ok(null);
    }

}
