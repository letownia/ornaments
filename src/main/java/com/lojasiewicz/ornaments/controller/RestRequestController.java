package com.lojasiewicz.ornaments.controller;

import com.lojasiewicz.ornaments.service.OrnamentsService;
import com.lojasiewicz.ornaments.service.db.MapFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RestRequestController {

    @Autowired
    private OrnamentsService ornamentsService;

    @RequestMapping(method= RequestMethod.GET, value="/groups")
    public String getGroupList(){
        return "";
    }

    @RequestMapping(method= RequestMethod.PUT,  value="/feature")
    public ResponseEntity<String> insertFeature(@RequestBody MapFeature newFeature){
        ornamentsService.insertMapFeature(newFeature);
        return responseOK();
    }

    private static ResponseEntity<String> responseOK() {
        return ResponseEntity.ok(null);
    }

}
