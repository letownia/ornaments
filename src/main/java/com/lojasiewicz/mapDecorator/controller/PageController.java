package com.lojasiewicz.mapDecorator.controller;

//import com.google.gson.Gson;
//import org.springframework.beans.factory.annotation.Autowired;

import com.lojasiewicz.mapDecorator.config.internationalization.SupportedLocale;
import org.springframework.context.i18n.LocaleContextHolder;
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

    public static final List<SupportedLocale> allSupportedLocales = Arrays.asList(SupportedLocale.values());

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