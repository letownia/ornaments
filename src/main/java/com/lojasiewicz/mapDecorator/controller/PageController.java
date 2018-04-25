package com.lojasiewicz.mapDecorator.controller;

//import com.google.gson.Gson;
//import org.springframework.beans.factory.annotation.Autowired;

import com.lojasiewicz.mapDecorator.config.internationalization.SupportedLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * HTML controller for the main page
 * 1.) Home Page
 *
 * @author Borg Lojasiewicz
 */
@Controller
public class PageController {


    /* TODO - either completely remove env or inject it via constructor injection.
     */
    @Autowired
    private Environment env;

    private final List<SupportedLocale> allSupportedLocales = Arrays.asList(SupportedLocale.values());

    @ModelAttribute("allSupportedLocales")
    public  List<SupportedLocale> getAllSupportedLocales(){
        return allSupportedLocales;
    }
    @ModelAttribute("currentSupportedLocale")
    public SupportedLocale getCurrentSupportedLocale(){
        return SupportedLocale.fromLocale(LocaleContextHolder.getLocale());
    }

    @RequestMapping("/")
    public String welcome(Model model) {
        //model.addAttribute("indexTitle", "Głowa do góry!");
        return "index";
    }

    private void populateIndexModel(Model model){
        ResourceBundle messages = ResourceBundle.getBundle("messages");
        String addressError = messages.getString("addressError");
        String namePattern = messages.getString("nameError");
        // use @Retention(RetentionPolicy.RUNTIME) to extract from hibernate annotation
        String nameError = MessageFormat.format(namePattern,env.getProperty("name"),"");
      //  model.addAttribute("nameError", messages.
    }
}