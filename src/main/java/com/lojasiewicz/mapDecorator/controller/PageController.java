package com.lojasiewicz.mapDecorator.controller;

//import com.google.gson.Gson;
//import org.springframework.beans.factory.annotation.Autowired;

import com.lojasiewicz.mapDecorator.config.internationalization.SupportedLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

/**
 * HTML controller for the 3 basic pages
 * 1.) Home Page
 * 2.) Group info page
 * 3.) User info page
 *
 * @author Borg Lojasiewicz
 */
@Controller
public class PageController {

    //TODO No need to autowire environment - properties should be referenced via @Value("${profile.services.url}")
    @Autowired
    private Environment env;

    public static final List<SupportedLocale> allSupportedLocales = Arrays.asList(SupportedLocale.values());

    //TODO bucketNameProperty also appears in PhotoStorageService.
    private static final String bucketNameProperty = "com.lojasiewicz.mapDecorator.bucketName";

    @Value("${com.lojasiewicz.mapDecorator.bucketName}")
    private static String bucketNameBitch;


    //TODO  FIXTHIS- javascript should call this function to find out where images are hosted
    @ModelAttribute
    public String getURLForHostingBlaaBlaaaFIXTHIS(){
        //return https://storage.googleapis.com/krk_ornaments/;
        System.out.println(bucketNameBitch);
        return "https://storage.googleapis.com/" + env.getProperty(bucketNameProperty);
    }

    @ModelAttribute("allSupportedLocales")
    public static List<SupportedLocale> getAllSupportedLocales(){
        return allSupportedLocales;
    }
    @ModelAttribute("currentSupportedLocale")
    public SupportedLocale getCurrentSupportedLocale(){
        return SupportedLocale.fromLocale(LocaleContextHolder.getLocale());
    }

//    @Autowired
//    private UserGroupService userGroupService;
//
    @RequestMapping("/")
    public String welcome(Model model) {
        //model.addAttribute("indexTitle", "Głowa do góry!");
        return "index";
    }
}