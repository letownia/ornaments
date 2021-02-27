package com.mapdecorator.config.internationalization;

import java.util.Locale;

public enum SupportedLocale {
    pl("pl", "Polski"),
    es("es", "Español"),
    en("en", "English");

    public static final SupportedLocale defaultLocale = en;
    private final String localeCode;
    private final String displayName;

    SupportedLocale(String localeCode, String displayName){
        this.localeCode = localeCode;
        this.displayName = displayName;
    }

    public static SupportedLocale fromLocale(Locale locale){
        try{
            return SupportedLocale.valueOf(locale.getLanguage());
        }catch(Exception e){
            return defaultLocale;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLocaleCode() {
        return localeCode;
    }
}
