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
import org.springframework.web.multipart.MultipartFile;

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

//    @RequestMapping(method= RequestMethod.PUT,  value="/feature")
//    public ResponseEntity<String> insertFeature(@RequestBody MapFeature newFeature){
//        mapDecoratorService.insertMapFeature(newFeature);
//        return responseOK();
//    }
//    @RequestMapping(method= RequestMethod.PUT,  value="/ornament/")
//    public ResponseEntity<String> insertFeature(@RequestBody MultipartFile multiPartFile){krk_ornaments
//        //ornamentsService.insertMapFeature(newFeature);
//        return responseOK();
//    }

    @RequestMapping(method= RequestMethod.PUT,  value="/feature/")
    public ResponseEntity<String> insertFeature(@RequestParam("photo") MultipartFile multiPartFile,
                                                @RequestParam("description") String description,
                                                @RequestParam("googlePlaceId") String googlePlaceId,
                                                @RequestParam("latitude") String latitude,
                                                @RequestParam("longitude") String longitude){
        //ornamentsservice.insertmapfeature(newfeature);
        String blobName = "";
        try {
            blobName = cloudStorageService.addBlob("/photos/largeSize/" + googlePlaceId, multiPartFile);
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

//    @RequestMapping(method= RequestMethod.PUT,  value="/ornament/{googlePlaceId}")
//    public ResponseEntity<String> insertFeature(@RequestBody MultipartFile multiPartFile,
//                                                @PathVariable("googlePlaceId") String googlePlaceId){
//        //ornamentsService.insertMapFeature(newFeature);
//        return responseOK();
//    }

    private static ResponseEntity<String> responseOK() {
        return ResponseEntity.ok(null);
    }

}
