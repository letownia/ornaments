package com.lojasiewicz.mapDecorator.controller;

import com.lojasiewicz.mapDecorator.service.MapDecoratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class AJAXRequestController {

    @Autowired
    private MapDecoratorService mapDecoratorService;

    @RequestMapping(method= RequestMethod.GET, value="/groups")
    public String getGroupList(){
        return "";
    }

//    @RequestMapping(method= RequestMethod.PUT,  value="/feature")
//    public ResponseEntity<String> insertFeature(@RequestBody MapFeature newFeature){
//        mapDecoratorService.insertMapFeature(newFeature);
//        return responseOK();
//    }
//    @RequestMapping(method= RequestMethod.PUT,  value="/ornament/")
//    public ResponseEntity<String> insertFeature(@RequestBody MultipartFile multiPartFile){
//        //ornamentsService.insertMapFeature(newFeature);
//        return responseOK();
//    }

    @RequestMapping(method= RequestMethod.PUT,  value="/feature/")
    public ResponseEntity<String> insertFeature(@RequestParam("photo") MultipartFile multiPartFile,
                                                @RequestParam("description") String description,
                                                @RequestParam("googlePlaceId") String googlePlaceId){
        //ornamentsservice.insertmapfeature(newfeature);

        try {
            mapDecoratorService.insertMapFeature(description, googlePlaceId, multiPartFile);
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
