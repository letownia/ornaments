package com.lojasiewicz.ornaments.controller;

import com.lojasiewicz.ornaments.service.OrnamentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RestRequestController {

    @Autowired
    private OrnamentsService ornamentsService;
}
