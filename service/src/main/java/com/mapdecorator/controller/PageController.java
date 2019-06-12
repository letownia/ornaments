package com.mapdecorator.controller;

//import com.google.gson.Gson;
//import org.springframework.beans.factory.annotation.Autowired;

import com.mapdecorator.config.internationalization.SupportedLocale;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HTML controller for the main page 1.) Home Page
 *
 * @author Borg Lojasiewicz
 */

@Controller
public class PageController {

  private Environment env;

  @Autowired
  public PageController(Environment env){
    this.env = env;
  }
  private final List<SupportedLocale> allSupportedLocales = Arrays.asList(SupportedLocale.values());

  @ModelAttribute("allSupportedLocales")
  public List<SupportedLocale> getAllSupportedLocales() {
    return allSupportedLocales;
  }

  @ModelAttribute("currentSupportedLocale")
  public SupportedLocale getCurrentSupportedLocale() {
    return SupportedLocale.fromLocale(LocaleContextHolder.getLocale());
  }

  @RequestMapping("/")
  public String welcome(Model model) {
    return "index";
  }
}